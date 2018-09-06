package me.ggulmool.todo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.ggulmool.todo.domain.exception.TodoCannotDoneException;
import me.ggulmool.todo.domain.support.BaseEntity;
import me.ggulmool.todo.web.dto.TodoDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id", "contents"})
@Entity
@Table(name = "todo")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false)
    private Long id;

    @Column(name = "contents", nullable = false, length = 50)
    private String contents;

    @Column(name = "display_contents", nullable = false, length = 100)
    @JsonProperty("display_contents")
    private String displayContents;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private TodoStatus status;

    @ManyToMany(mappedBy = "parentTodos")
    @JsonIgnore
    private List<Todo> refTodos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "todo_parent_ref",
            joinColumns = {@JoinColumn(name = "todo_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "parent_id", nullable = false)}
    )
    @JsonIgnore
    private List<Todo> parentTodos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Todo() {
    }

    public Todo(String contents, User user) {
        this.contents = contents;
        this.displayContents = contents;
        this.user = user;
        this.status = TodoStatus.DOING;
    }

    public Todo(Long id, String contents, User user) {
        this.id = id;
        this.contents = contents;
        this.displayContents = contents;
        this.user = user;
        this.status = TodoStatus.DOING;
    }

    public void addParentTodos(List<Todo> todos) {
        parentTodos.clear();
        refTodos.clear();
        if (todos.size() > 0) {
            for (Todo todo : todos) {
                addParentTodo(todo);
            }
        }
    }

    public void addParentTodo(Todo todo) {
        parentTodos.add(todo);
        todo.addRefTodo(this);
        updateDisplayContents();
    }

    public void addRefTodo(Todo todo) {
        refTodos.add(todo);
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void done() {
        if (isExistDoingRefTodos()) {
            throw new TodoCannotDoneException("완료하지 않은 참조된 할일이 존재합니다.");
        }
        this.status = TodoStatus.DONE;
    }

    private boolean isExistDoingRefTodos() {
        return refTodos.stream()
                .anyMatch(t -> t.status.isDoing());
    }

    public void updateDisplayContents() {
        displayContents =  contents + " " + parentTodosDisplayContents();
    }

    public void update(TodoDto todoDto, List<Todo> parentTodos) {
        this.contents = todoDto.getContents();
        this.displayContents = todoDto.getContents();
        addParentTodos(parentTodos);
    }

    public String generateRestUrl() {
        return String.format("/api/todos/%d", id);
    }

    public void assignUser(User user) {
        this.user = user;
    }

    private String parentTodosDisplayContents() {
        return parentTodos.stream()
                .map(t -> "@" + t.id)
                .collect(Collectors.joining(" "));
    }
}
