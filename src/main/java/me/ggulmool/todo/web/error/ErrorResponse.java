package me.ggulmool.todo.web.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private String error;

    public ErrorResponse(final String message) {
        super();
        this.message = message;
    }

    public ErrorResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }
}
