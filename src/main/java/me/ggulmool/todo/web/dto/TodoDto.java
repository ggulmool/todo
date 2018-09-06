package me.ggulmool.todo.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.domain.support.TodoRepository;
import me.ggulmool.todo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
@Scope("prototype")
public class TodoDto {

    private String contents;
    private User user;
    private List<Long> parentIds = new ArrayList<>();

    @Autowired
    private TodoRepository todoRepository;

    public void setTodoRequest(TodoRequest todoRequest, User user) {
        this.contents = todoRequest.getContents();
        this.user = user;
        this.parentIds = todoRequest.getParentIds();
    }

    public Todo convertTodo() {
        return new Todo(contents, user);
    }

    public List<Todo> getParentTodos() {
        return todoRepository.findTodoByIdInAndUser(parentIds, user);
    }
}
