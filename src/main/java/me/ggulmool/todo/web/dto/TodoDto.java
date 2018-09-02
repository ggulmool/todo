package me.ggulmool.todo.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ggulmool.todo.domain.Todo;
import me.ggulmool.todo.domain.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
@Scope("prototype")
public class TodoDto {

    private Long id;
    private String contents;
    private List<Long> parentIds;

    @Autowired
    private TodoRepository todoRepository;

    public void setDotoRequest(TodoRequest todoRequest) {
        this.id = todoRequest.getId();
        this.contents = todoRequest.getContents();
        this.parentIds = todoRequest.getParentIds();
    }

    public Todo convertTodo() {
        return new Todo(contents);
    }

    public List<Todo> getParentTodos() {
        return todoRepository.findByIdIn(parentIds);
    }
}
