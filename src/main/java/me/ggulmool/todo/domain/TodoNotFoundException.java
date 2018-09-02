package me.ggulmool.todo.domain;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(String message) {
        super(message);
    }

    public TodoNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
