package me.ggulmool.todo.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    private User user1 = new User("user1", "사용자1", "test1234");
    private User user2 = new User("user2", "사용자2", "test1234");

    @Test
    public void 사용자별_할일_목록_조회() {
        Page<Todo> page = todoRepository.findTodoByUser(new PageRequest(0, 10), user1);
        assertThat(page.getContent()).hasSize(4);

        page = todoRepository.findTodoByUser(new PageRequest(0, 10), user2);
        assertThat(page.getContent()).hasSize(5);
    }

    @Test
    public void 할일_등록() throws Exception {
        Todo todo = new Todo("거실청소", user1);
        Todo savedTodo = todoRepository.save(todo);
        assertThat(savedTodo).isEqualTo(todo);
        assertThat(savedTodo.getId()).isEqualTo(todo.getId());
        assertThat(savedTodo.getContents()).isEqualTo(todo.getContents());
        assertThat(savedTodo.getStatus()).isEqualTo(todo.getStatus());

        Page<Todo> page = todoRepository.findTodoByUser(new PageRequest(0, 10), user1);
        assertThat(page.getContent()).hasSize(5);
    }

    @Test
    public void 할일_등록_참조할일_등록() throws Exception {
        Todo todo = new Todo("거실청소", user1);
        List<Todo> parentTodos = todoRepository.findTodoByIdInAndUser(Arrays.asList(1L, 3L), user1);
        todo.addParentTodos(parentTodos);
        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo.getParentTodos()).hasSize(2);
        assertThat(savedTodo.getRefTodos()).hasSize(0);
    }
}
