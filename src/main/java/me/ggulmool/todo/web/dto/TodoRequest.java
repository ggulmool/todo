package me.ggulmool.todo.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequest {

    private String contents;
    private List<Long> parentIds = new ArrayList<>();

    @Builder
    public TodoRequest(String contents, List<Long> parentIds) {
        this.contents = contents;
        this.parentIds = parentIds;
    }
}
