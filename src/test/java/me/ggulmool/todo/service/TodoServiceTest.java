package me.ggulmool.todo.service;

import me.ggulmool.todo.domain.*;
import me.ggulmool.todo.domain.exception.TodoCannotDoneException;
import me.ggulmool.todo.domain.support.TodoRepository;
import me.ggulmool.todo.web.dto.TodoDto;
import me.ggulmool.todo.web.dto.TodoRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @InjectMocks
    private TodoDto todoDto;

    private PageRequest page = new PageRequest(0, 10);

    private Todo todo1;
    private Todo todo2;
    private Todo todo3;
    private Todo todo4;
    private User user = new User("user1", "사용자1");

    @Before
    public void setUp() throws Exception {
        todo1 = new Todo(1L, "집안일", user);
        todo2 = new Todo(2L, "빨래", user);
        todo3 = new Todo(3L, "청소", user);
        todo4 = new Todo(4L, "방청소", user);

        todo2.addParentTodo(todo1);
        todo3.addParentTodo(todo1);
        todo4.addParentTodos(Arrays.asList(todo1, todo3));
    }

    @Test
    public void 할일_목록_조회() {
        // given
        List<Todo> todos = Arrays.asList(todo1, todo2, todo3, todo4);
        when(todoRepository.findTodoByUser(page, user)).thenReturn(new PageImpl<Todo>(todos, page, todos.size()));

        // when
        Page<Todo> results = todoService.getTodos(page, user);

        // then
        assertThat(results.getContent()).hasSize(4);
        assertThat(results.getNumberOfElements()).isEqualTo(4);
    }

    @Test
    public void ID4의_할일_참조_목록_조회() {
        // given
        when(todoRepository.findTodoByIdAndUser(4L, user)).thenReturn(Optional.of(todo4));

        // when
        List<Todo> parentTodos = todoService.getParentTodos(4L, user);

        // then
        assertThat(parentTodos).contains(todo1, todo3);
        assertThat(parentTodos.get(0).getId()).isEqualTo(1L);
        assertThat(parentTodos.get(0).getContents()).isEqualTo("집안일");
        assertThat(parentTodos.get(1).getId()).isEqualTo(3L);
        assertThat(parentTodos.get(1).getContents()).isEqualTo("청소");
    }

    @Test
    public void 할일_등록() {
        // given
        TodoRequest addTodoRequest = TodoRequest.builder().contents("집안일").build();
        todoDto.setTodoRequest(addTodoRequest, user);
        when(todoRepository.save(todoDto.convertTodo())).thenReturn(todo1);

        // when
        Todo addedTodo = todoService.addTodo(todoDto);

        // then
        assertThat(addedTodo).isEqualTo(todo1);
    }

    @Test
    public void 할일_등록시_참조할일_등록() {
        // given
        TodoRequest addTodoRequest = TodoRequest.builder().contents("방청소").parentIds(Arrays.asList(1L, 3L)).build();
        todoDto.setTodoRequest(addTodoRequest, user);
        when(todoRepository.findTodoByIdInAndUser(todoDto.getParentIds(), user)).thenReturn(Arrays.asList(todo1, todo3));
        when(todoRepository.save(todoDto.convertTodo())).thenReturn(todo4);

        // when
        Todo addedTodo = todoService.addTodo(todoDto);

        // then
        assertThat(addedTodo).isEqualTo(todo4);
        assertThat(addedTodo.getParentTodos()).contains(todo1, todo3);
    }

    @Test
    public void 할일_수정() {
        // given
        TodoRequest updateTodoRequest = TodoRequest.builder().contents("집안일 수정").build();
        todoDto.setTodoRequest(updateTodoRequest, user);
        when(todoRepository.findTodoByIdAndUser(1L, user)).thenReturn(Optional.of(todo1));

        // when
        Todo updatedTodo = todoService.update(1L, todoDto);

        // then
        assertThat(updatedTodo.getContents()).isEqualTo("집안일 수정");
    }

    @Test
    public void 할일_수정시_참조할일_수정() {
        // given
        TodoRequest updateTodoRequest = TodoRequest.builder().contents("손빨래").parentIds(Arrays.asList(1L, 2L)).build();
        todoDto.setTodoRequest(updateTodoRequest, user);
        when(todoRepository.findTodoByIdInAndUser(todoDto.getParentIds(), user)).thenReturn(Arrays.asList(todo1, todo2));
        when(todoRepository.findTodoByIdAndUser(4L, user)).thenReturn(Optional.of(todo4));

        // when
        Todo updatedTodo = todoService.update(4L, todoDto);

        // then
        assertThat(updatedTodo.getContents()).isEqualTo("손빨래");
        assertThat(updatedTodo.getParentTodos()).contains(todo1, todo2);
    }

    @Test
    public void ID2_할일_완료_처리_참조하고있는_할일이_없는경우() throws Exception {
        // given
        Long todoId = 2L;
        when(todoRepository.findTodoByIdAndUser(todoId, user)).thenReturn(Optional.of(todo2));

        // when
        todoService.done(todoId, user);

        // then
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    public void ID1의_할일_완료_처리_ID2_3_4가_참조하고_있는경우() throws Exception {
        // given
        when(todoRepository.findTodoByIdAndUser(2L, user)).thenReturn(Optional.of(todo2));
        when(todoRepository.findTodoByIdAndUser(4L, user)).thenReturn(Optional.of(todo4));
        when(todoRepository.findTodoByIdAndUser(3L, user)).thenReturn(Optional.of(todo3));
        when(todoRepository.findTodoByIdAndUser(1L, user)).thenReturn(Optional.of(todo1));

        // when
        todoService.done(2L, user);
        todoService.done(4L, user);
        todoService.done(3L, user);
        todoService.done(1L, user);

        // then
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo4.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo3.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test(expected = TodoCannotDoneException.class)
    public void ID1의_할일_완료_처리시_익센션발생_ID2_3_4가_참조하고_있는경우() throws Exception {
        // given
        Long todoId = 1L;
        when(todoRepository.findTodoByIdAndUser(todoId, user)).thenReturn(Optional.of(todo1));

        // when
        todoService.done(todoId, user);
        fail("TodoNotCompleteException이 발생해야 한다.");
    }
}
