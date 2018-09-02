package me.ggulmool.todo.service;

import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.domain.TodoNotFoundException;
import me.ggulmool.todo.domain.TodoRepository;
import me.ggulmool.todo.web.dto.TodoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public Page<Todo> getTodos(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }

    @Transactional
    public Todo addTodo(TodoDto todoDto) {
        Todo todo = todoDto.convertTodo();
        todo.addParentTodos(todoDto.getParentTodos());
        todoRepository.save(todo);
        return todo;
    }

    @Transactional
    public Todo update(Long todoId, TodoDto todoDto) {
        Todo todo = getTodo(todoId);
        todo.update(todoDto, todoDto.getParentTodos());
        return todo;
    }

    private Todo getTodo(Long todoId) {
        Optional<Todo> todoOpt = todoRepository.findById(todoId);
        return todoOpt.orElseThrow(() -> new TodoNotFoundException("할일이 존재하지 않습니다."));
    }

    private List<Todo> getParentTodos(List<Long> todoIds) {
        return todoRepository.findByIdIn(todoIds);
    }
}
