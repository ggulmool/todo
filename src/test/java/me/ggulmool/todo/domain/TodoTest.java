package me.ggulmool.todo.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TodoTest {

    private Todo todo1, todo2, todo3, todo4;

    @Before
    public void setUp() throws Exception {
        todo1 = new Todo(1L, "집안일");
        todo2 = new Todo(2L, "빨래");
        todo3 = new Todo(3L, "청소");
        todo4 = new Todo(4L, "방청소");
    }

    @Test
    public void 할일_등록() {
        Todo todo5 = new Todo(5L, "설거지");
        assertThat(todo5.getId()).isEqualTo(5L);
        assertThat(todo5.getContents()).isEqualTo("설거지");
        assertThat(todo5.getStatus()).isEqualTo(TodoStatus.DOING);
    }

    @Test
    public void 할일_수정() {
        todo1.setContents("집안일수정");
        assertThat(todo1.getContents()).isEqualTo("집안일수정");
    }

    @Test
    public void 다른할일_참조() {
        todo2.addReferenceTodo(todo1);
        todo3.addReferenceTodo(todo1);
        todo4.addReferenceTodo(todo1);
        todo4.addReferenceTodo(todo3);

        assertThat(todo1.getReferenceTodos()).hasSize(0);
        assertThat(todo1.getReferencedTodos()).hasSize(3);

        assertThat(todo2.getReferenceTodos()).hasSize(1);
        assertThat(todo2.getReferencedTodos()).hasSize(0);

        assertThat(todo3.getReferenceTodos()).hasSize(1);
        assertThat(todo3.getReferencedTodos()).hasSize(1);

        assertThat(todo4.getReferenceTodos()).hasSize(2);
        assertThat(todo4.getReferencedTodos()).hasSize(0);
    }
}