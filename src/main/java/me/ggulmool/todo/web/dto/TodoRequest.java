package me.ggulmool.todo.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TodoRequest {

    private String contents;
    private List<Long> parentIds = new ArrayList<>();
}
