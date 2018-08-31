package me.ggulmool.todo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id", "contents"})
@Entity
@Table(name = "TODO")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TODO_ID", nullable = false)
    private Long id;

    @Column(name = "CONTENTS", nullable = false, length = 100)
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 10)
    private TodoStatus status;

    @ManyToMany(mappedBy = "parentTodos")
    private List<Todo> refTodos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "TODO_PARENT_REF",
            joinColumns = {@JoinColumn(name = "TODO_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PARENT_ID", nullable = false)}
    )
    private List<Todo> parentTodos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public Todo() {
    }

    public Todo(String contents) {
        this.contents = contents;
        this.status = TodoStatus.DOING;
    }

    public Todo(Long id, String contents) {
        this.id = id;
        this.contents = contents;
        this.status = TodoStatus.DOING;
    }

    public void addParentTodo(Todo todo) {
        parentTodos.add(todo);
        todo.addRefTodo(this);
    }

    public void addRefTodo(Todo todo) {
        refTodos.add(todo);
    }

    public void updateContents(String contents) {
        this.contents = contents;
    }

    public void done() {
        if (isExistDoingRefTodos()) {
            throw new TodoNotCompleteException("reftodos not complete");
        }
        this.status = TodoStatus.DONE;
    }

    private boolean isExistDoingRefTodos() {
        return refTodos.stream()
                .anyMatch(t -> t.status.isDoing());
    }

    public String displayContents() {
        if (isExistParentTodos()) {
            return contents + " " + parentTodosDisplayContents();
        }
        return contents;
    }

    private String parentTodosDisplayContents() {
        return parentTodos.stream()
                .map(t -> "@" + t.id)
                .collect(Collectors.joining(" "));
    }

    private boolean isExistParentTodos() {
        return parentTodos.size() > 0;
    }
}
