package me.ggulmool.todo.service;

import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.domain.exception.TodoNotFoundException;
import me.ggulmool.todo.domain.support.TodoRepository;
import me.ggulmool.todo.domain.User;
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

    @Transactional(readOnly = true)
    public Page<Todo> getTodos(Pageable pageable, User user) {
        return todoRepository.findTodoByUser(pageable, user);
    }

    @Transactional(readOnly = true)
    public List<Todo> getParentTodos(Long todoId, User user) {
        Todo todo = getTodo(todoId, user);
        return todo.getParentTodos();
    }

    @Transactional
    public Todo addTodo(TodoDto todoDto) {
        Todo todo = todoDto.convertTodo();
        todo.addParentTodos(todoDto.getParentTodos());
        return todoRepository.save(todo);
    }

    @Transactional
    public Todo update(Long todoId, TodoDto todoDto) {
        Todo todo = getTodo(todoId, todoDto.getUser());
        todo.update(todoDto, todoDto.getParentTodos());
        return todo;
    }

    @Transactional
    public void done(Long todoId, User user) {
        Todo todo = getTodo(todoId, user);
        todo.done();
    }

    private Todo getTodo(Long todoId, User user) {
        Optional<Todo> todoOpt = todoRepository.findTodoByIdAndUser(todoId, user);
        return todoOpt.orElseThrow(() -> new TodoNotFoundException("할일이 존재하지 않습니다."));
    }
}
