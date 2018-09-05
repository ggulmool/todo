package me.ggulmool.todo.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class TodoTest {

    private Todo todo1;
    private Todo todo2;
    private Todo todo3;
    private Todo todo4;
    private User user = new User("user1", "사용자1");

    @Before
    public void setUp() throws Exception {
        // 1 - x
        // 2 - 1 참조
        // 3 - 1 참조
        // 4 - 1, 3 참조
        todo1 = new Todo(1L, "집안일", user);
        todo2 = new Todo(2L, "빨래", user);
        todo3 = new Todo(3L, "청소", user);
        todo4 = new Todo(4L, "방청소", user);

        todo2.addParentTodo(todo1);
        todo3.addParentTodo(todo1);
        todo4.addParentTodos(Arrays.asList(todo1, todo3));
    }

    @Test
    public void ID1_할일의_참조된할일과_참조하고있는_할일_확인() {
        assertThat(todo1.getRefTodos()).hasSize(3);
        assertThat(todo1.getRefTodos()).contains(todo2, todo3, todo4);
        assertThat(todo1.getParentTodos()).hasSize(0);
    }

    @Test
    public void ID2_할일의_참조된할일과_참조하고있는_할일_확인() {
        assertThat(todo2.getRefTodos()).hasSize(0);
        assertThat(todo2.getParentTodos()).hasSize(1);
        assertThat(todo2.getParentTodos()).contains(todo1);
    }

    @Test
    public void ID3_할일의_참조된할일과_참조하고있는_할일_확인() {
        assertThat(todo3.getRefTodos()).hasSize(1);
        assertThat(todo3.getRefTodos()).contains(todo4);
        assertThat(todo3.getParentTodos()).hasSize(1);
        assertThat(todo3.getParentTodos()).contains(todo1);
    }

    @Test
    public void ID4_할일의_참조된할일과_참조하고있는_할일_확인() {
        assertThat(todo4.getRefTodos()).hasSize(0);
        assertThat(todo4.getParentTodos()).hasSize(2);
        assertThat(todo4.getParentTodos()).contains(todo1, todo3);
    }

    @Test
    public void 할일_등록() {
        Todo todo = new Todo(5L, "거실청소", user);

        assertThat(todo.getId()).isEqualTo(5L);
        assertThat(todo.getContents()).isEqualTo("거실청소");
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.DOING);
    }

    @Test
    public void 할일_등록시_참조할일_등록() {
        Todo todo = new Todo(5L, "거실청소", user);
        todo.addParentTodos(Arrays.asList(todo1, todo3));

        assertThat(todo.getParentTodos()).hasSize(2);
        assertThat(todo.getParentTodos()).contains(todo1, todo3);
    }

    @Test
    public void 할일_수정() {
        todo1.updateContents("집안일수정");

        assertThat(todo1.getContents()).isEqualTo("집안일수정");
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DOING);
    }

    @Test
    public void 할일_수정시_참조할일_수정() {
        assertThat(todo4.getParentTodos()).hasSize(2);
        assertThat(todo4.getParentTodos()).contains(todo1, todo3);

        todo4.updateContents("손빨래");
        todo4.addParentTodos(Arrays.asList(todo1, todo2));

        assertThat(todo4.getContents()).isEqualTo("손빨래");
        assertThat(todo4.getParentTodos()).hasSize(2);
        assertThat(todo4.getParentTodos()).contains(todo1, todo2);
    }

    @Test
    public void ID2_할일의_완료_처리_참조하고있는_할일이_없는경우() {
        todo2.done();
        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test
    public void ID1_할일의_완료_처리_참조하고_있는_할일이_있는경우() {
        todo2.done();
        todo4.done();
        todo3.done();
        todo1.done();

        assertThat(todo2.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo4.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo3.getStatus()).isEqualTo(TodoStatus.DONE);
        assertThat(todo1.getStatus()).isEqualTo(TodoStatus.DONE);
    }

    @Test(expected = TodoCannotDoneException.class)
    public void ID1_할일_완료_처리시_참조된_할일_ID2_3_4_미완료인경우() {
        todo1.done();
        fail("TodoNotCompleteException이 발생해야 한다.");
    }

    @Test(expected = TodoCannotDoneException.class)
    public void ID1_할일_완료_처리시_참조된_할일_ID2_4_처리_ID3_미완료인경우() {
        todo2.done();
        todo4.done();
        todo1.done();
        fail("TodoNotCompleteException이 발생해야 한다.");
    }

    @Test
    public void 참조할일이_포함된_할일의_표시내용_확인() {
        assertThat(todo1.getDisplayContents()).isEqualTo("집안일");
        assertThat(todo2.getDisplayContents()).isEqualTo("빨래 @1");
        assertThat(todo3.getDisplayContents()).isEqualTo("청소 @1");
        assertThat(todo4.getDisplayContents()).isEqualTo("방청소 @1 @3");
    }
}