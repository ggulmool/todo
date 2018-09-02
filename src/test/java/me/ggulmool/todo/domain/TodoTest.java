package me.ggulmool.todo.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TodoTest {

    @Test
    public void 할일_등록() {
        Todo todo1 = new Todo(1L, "집안일");
        assertThat(todo1.getId()).isEqualTo(todo1.getId());
        assertThat(todo1.getContents()).isEqualTo(todo1.getContents());
        assertThat(todo1.getStatus()).isEqualTo(todo1.getStatus());
    }

    @Test
    public void 할일_수정() {
        Todo todo1 = new Todo(1L, "집안일");
        todo1.updateContents("집안일수정");
        assertThat(todo1.getContents()).isEqualTo("집안일수정");
    }

    @Test
    public void 다른할일_참조() {
        Todo todo1 = new Todo(1L, "집안일");
        Todo todo2 = new Todo(2L, "빨래");
        Todo todo3 = new Todo(3L, "청소");
        Todo todo4 = new Todo(4L, "방청소");

        todo2.addParentTodo(todo1);
        todo3.addParentTodo(todo1);
        todo4.addParentTodo(todo1);
        todo4.addParentTodo(todo3);

        assertThat(todo1.getRefTodos()).hasSize(3);
        assertThat(todo1.getParentTodos()).hasSize(0);

        assertThat(todo2.getRefTodos()).hasSize(0);
        assertThat(todo2.getParentTodos()).hasSize(1);

        assertThat(todo3.getRefTodos()).hasSize(1);
        assertThat(todo3.getParentTodos()).hasSize(1);

        assertThat(todo4.getRefTodos()).hasSize(0);
        assertThat(todo4.getParentTodos()).hasSize(2);
    }

    @Test
    public void 할일_완료_처리_케이스1() throws Exception {
        Todo todo1 = new Todo(1L, "집안일");
        todo1.done();
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    public void 할일_완료_처리_케이스2() throws Exception {
        Todo todo1 = new Todo(1L, "집안일");
        Todo todo2 = new Todo(2L, "빨래");
        Todo todo3 = new Todo(3L, "청소");
        Todo todo4 = new Todo(4L, "방청소");

        // 1 - x
        // 2 - 1 참조
        // 3 - 1 참조
        // 4 - 1, 3 참조
        todo2.addParentTodo(todo1);
        todo3.addParentTodo(todo1);
        todo4.addParentTodo(todo1);
        todo4.addParentTodo(todo3);

        todo2.done();
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.DONE);

        todo4.done();
        assertThat(todo4.getStatus()).isEqualTo(TodoStatus.DONE);

        todo3.done();
        assertThat(todo3.getStatus()).isEqualTo(TodoStatus.DONE);

        todo1.done();
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test(expected = TodoCannotDoneException.class)
    public void 할일_완료_처리시_참조할일이_완료되지_않은경우_익센션_케이스1() throws Exception {
        Todo todo1 = new Todo(1L, "집안일");
        Todo todo2 = new Todo(2L, "빨래");

        todo2.addParentTodo(todo1);
        todo1.done();
        fail("TodoNotCompleteException이 발생해야 한다.");
    }

    @Test(expected = TodoCannotDoneException.class)
    public void 할일_완료_처리시_참조할일이_완료되지_않은경우_익센션_케이스2() throws Exception {
        Todo todo1 = new Todo(1L, "집안일");
        Todo todo3 = new Todo(3L, "청소");
        Todo todo4 = new Todo(4L, "방청소");

        todo4.addParentTodo(todo1);
        todo4.addParentTodo(todo3);

        todo1.done();
        todo4.done();
        fail("TodoNotCompleteException이 발생해야 한다.");
    }

    @Test
    public void 참조할일이_포함된_할일의_표시내용_확인() throws Exception {
        Todo todo1 = new Todo(1L, "집안일");
        Todo todo2 = new Todo(2L, "빨래");
        Todo todo3 = new Todo(3L, "청소");
        Todo todo4 = new Todo(4L, "방청소");

        todo2.addParentTodo(todo1);
        todo3.addParentTodo(todo1);
        todo4.addParentTodo(todo1);
        todo4.addParentTodo(todo3);

        assertThat(todo1.getDisplayContents()).isEqualTo("집안일");
        assertThat(todo2.getDisplayContents()).isEqualTo("빨래 @1");
        assertThat(todo3.getDisplayContents()).isEqualTo("청소 @1");
        assertThat(todo4.getDisplayContents()).isEqualTo("방청소 @1 @3");
    }
}