package me.ggulmool.todo.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchParam {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_MIN_PAGE = 1;
    private static final int DEFAULT_MAX_PAGE = 50;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_MIN_SIZE = 1;
    private static final int DEFAULT_MAX_SIZE = 50;

    private int page;
    private int size;
    private String query;
    private String target;
    private String sort;

    public SearchParam() {
        this.page = DEFAULT_PAGE;
        this.size = DEFAULT_SIZE;
    }
}
