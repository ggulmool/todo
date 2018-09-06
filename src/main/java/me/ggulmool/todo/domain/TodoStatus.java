package me.ggulmool.todo.domain;

public enum TodoStatus {

    DOING,
    DONE;

    public boolean isDoing() {
        return this == DOING;
    }
}
