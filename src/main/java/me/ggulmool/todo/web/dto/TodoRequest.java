package me.ggulmool.todo.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodoRequest {

    private Long id;
    private String contents;
    private List<Long> parentIds;
}
