package me.ggulmool.todo.web.error;

import lombok.extern.slf4j.Slf4j;
import me.ggulmool.todo.domain.TodoCannotDoneException;
import me.ggulmool.todo.domain.TodoNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ TodoNotFoundException.class })
    public ResponseEntity<Object> handleTodoFound(final RuntimeException ex, final WebRequest request) {
        log.error("500 Status Code", ex);
        final ErrorResponse bodyOfResponse = new ErrorResponse(ex.getMessage(), "todo.not.found.exception");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({ TodoCannotDoneException.class })
    public ResponseEntity<Object> handleTodoDone(final RuntimeException ex, final WebRequest request) {
        log.error("500 Status Code", ex);
        final ErrorResponse bodyOfResponse = new ErrorResponse(ex.getMessage(), "todo.cannot.done.exception");
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
