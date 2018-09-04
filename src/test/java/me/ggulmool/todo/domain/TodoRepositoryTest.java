package me.ggulmool.todo.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Slf4j
public class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    private List<Todo> todos = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
         todos = todoRepository.findAll();
    }

    @Test
    public void 할일_등록() throws Exception {
        Todo todo = new Todo("todolist개발");
        Todo savedTodo = todoRepository.save(todo);
        assertThat(savedTodo).isEqualTo(todo);
        assertThat(savedTodo.getId()).isEqualTo(todo.getId());
        assertThat(savedTodo.getContents()).isEqualTo(todo.getContents());
        assertThat(savedTodo.getStatus()).isEqualTo(todo.getStatus());
    }

    @Test
    public void 할일_참조() {
        Optional<Todo> todoOpt = todoRepository.findById(1L);
        Todo savedTodo1 = todoOpt.get();
        assertThat(savedTodo1.getRefTodos()).hasSize(3);
        assertThat(savedTodo1.getParentTodos()).hasSize(0);
    }

    @Test
    public void 할일_목록조회() {
        List<Object[]> result = todoRepository.findAllTodos(new PageRequest(0, 10));
        result.forEach(arr -> System.out.println(Arrays.toString(arr)));
    }

    @Test
    public void 참조할일_목록조회() {
        Optional<Todo> todoOpt = todoRepository.findById(4L);
        Todo todo = todoOpt.get();
        List<Todo> parentTodos = todo.getParentTodos();
        assertThat(parentTodos).hasSize(2);
    }
}
