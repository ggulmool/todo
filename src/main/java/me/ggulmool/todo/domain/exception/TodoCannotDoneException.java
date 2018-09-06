package me.ggulmool.todo.domain.exception;

public class TodoCannotDoneException extends RuntimeException {

    public TodoCannotDoneException(String message) {
        super(message);
    }

    public TodoCannotDoneException(String message, Throwable ex) {
        super(message, ex);
    }
}
