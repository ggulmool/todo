package me.ggulmool.todo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Todo {

    private Long id;
    private String contents;
    private List<Todo> referencedTodos = new ArrayList<>();
    private List<Todo> referenceTodos = new ArrayList<>();
    private TodoStatus status;

    public Todo(Long id, String contents) {
        this.id = id;
        this.contents = contents;
        this.status = TodoStatus.DOING;
    }

    public void addReferenceTodo(Todo todo) {
        referenceTodos.add(todo);
        todo.addReferencedTodo(this);
    }

    public void addReferencedTodo(Todo todo) {
        referencedTodos.add(todo);
    }

}
