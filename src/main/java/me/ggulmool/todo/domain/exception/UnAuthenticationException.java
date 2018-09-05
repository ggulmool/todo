package me.ggulmool.todo.domain.exception;

public class UnAuthenticationException extends RuntimeException {

    public UnAuthenticationException(String message) {
        super(message);
    }

    public UnAuthenticationException(String message, Throwable ex) {
        super(message, ex);
    }
}