package me.ggulmool.todo.web.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PagingInfo<T> {

    @Getter
    @JsonProperty("start_page")
    private int startPage;

    @Getter
    @JsonProperty("end_page")
    private int endPage;
    private Page<T> page;

    public PagingInfo(Page<T> page) {
        this.page = page;
        this.endPage = endPage();
        this.startPage =  (tempEndPage() - page.getSize()) + 1;
    }

    @JsonProperty("is_previous")
    public boolean isPreviousVisible() {
        return startPage != 1;
    }

    @JsonProperty("is_next")
    public boolean isNextVisible() {
        return endPage * page.getSize() < page.getTotalElements();
    }

    @JsonProperty("page_elements")
    public List<PageElement> getPageElements() {
        List<PageElement> elements = new ArrayList<>();

        int currentPage = page.getNumber() + 1;
        for (int i = startPage; i <= endPage; i++) {
            elements.add(new PageElement(i, i == currentPage));
        }

        return elements;
    }

    @JsonProperty("total_elements")
    public int getTotalElements() {
        return (int) page.getTotalElements();
    }

    @JsonProperty("todos")
    public List<T> getTodos() {
        return page.getContent();
    }

    private int endPage() {
        int endPage = tempEndPage();
        return endPage > page.getTotalPages() ? page.getTotalPages() : endPage;
    }

    private int tempEndPage() {
        int currentPage = page.getNumber() + 1;
        return (int) (Math.ceil(currentPage / (double) page.getSize()) * page.getSize());
    }
}
