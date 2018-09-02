package me.ggulmool.todo.web;

import lombok.extern.slf4j.Slf4j;
import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.service.TodoService;
import me.ggulmool.todo.web.dto.TodoDto;
import me.ggulmool.todo.web.dto.TodoRequest;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/todos")
@Slf4j
public class ApiTodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private ObjectFactory<TodoDto> todoDtoFactory;

    @GetMapping
    public Page<Todo> todos() {
        PageRequest pageRequest = new PageRequest(0, 10);
        return todoService.getTodos(pageRequest);
    }

    @PostMapping
    public ResponseEntity addTodo(@RequestBody TodoRequest todoRequest) {
        TodoDto todoDto = todoDtoFactory.getObject();
        log.info("tododto - {}", todoDto);
        todoDto.setDotoRequest(todoRequest);
        Todo todo = todoService.addTodo(todoDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(todo.generateRestUrl()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity updateTodo(@PathVariable Long todoId, @RequestBody TodoRequest todoRequest) {
        TodoDto todoDto = todoDtoFactory.getObject();
        log.info("tododto - {}", todoDto);
        todoDto.setDotoRequest(todoRequest);
        Todo updatedTodo = todoService.update(todoId, todoDto);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }
}
