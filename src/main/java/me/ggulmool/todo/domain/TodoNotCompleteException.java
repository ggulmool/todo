package me.ggulmool.todo.domain;

public class TodoNotCompleteException extends RuntimeException {

    public TodoNotCompleteException(String message) {
        super(message);
    }

    public TodoNotCompleteException(String message, Throwable ex) {
        super(message, ex);
    }
}
