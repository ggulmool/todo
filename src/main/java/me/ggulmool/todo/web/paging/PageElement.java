package me.ggulmool.todo.web.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PageElement {

    @JsonProperty("page")
    private int pageNumber;

    private boolean isCurrentPage;

    public PageElement(int pageNumber, boolean isCurrentPage) {
        this.pageNumber = pageNumber;
        this.isCurrentPage = isCurrentPage;
    }

    @JsonProperty("is_current")
    public boolean isCurrentPage() {
        return isCurrentPage;
    }
}
