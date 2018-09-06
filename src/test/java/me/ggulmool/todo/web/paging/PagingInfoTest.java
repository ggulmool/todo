package me.ggulmool.todo.web.paging;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PagingInfoTest {

    private List<Integer> pagingList = new ArrayList<>();

    @Test
    public void 전체_1페이지_페이징_요소_확인() {
        PageRequest pageRequest = new PageRequest(0, 10); // 현재페이지(0=1페이지), 페이지당 보여줄 갯수
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 8));

        List<PageElement> pageElements = pagingInfo.getPageElements();
        assertThat(pageElements).size().isEqualTo(1);
        assertIsCurrentPageElementPage(1, pageElements.get(0));
    }

    @Test
    public void 전체_2페이지_페이징_요소_확인() {
        PageRequest pageRequest = new PageRequest(1, 10);
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 15));

        List<PageElement> pageElements = pagingInfo.getPageElements();
        assertThat(pageElements).size().isEqualTo(2);
        assertIsNotCurrentPageElementPage(1, pageElements.get(0));
        assertIsCurrentPageElementPage(2, pageElements.get(1));
    }

    @Test
    public void 전체_3페이지_페이징_요소_확인() {
        PageRequest pageRequest = new PageRequest(1, 10);
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 25));

        List<PageElement> pageElements = pagingInfo.getPageElements();
        assertThat(pageElements).size().isEqualTo(3);

        assertIsNotCurrentPageElementPage(1, pageElements.get(0));
        assertIsCurrentPageElementPage(2, pageElements.get(1));
        assertIsNotCurrentPageElementPage(3, pageElements.get(2));
    }

    @Test
    public void 전체_20페이지_페이징_요소_확인() {
        PageRequest pageRequest = new PageRequest(13, 10);
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 202));

        List<PageElement> pageElements = pagingInfo.getPageElements();
        assertThat(pageElements).size().isEqualTo(10);

        assertIsNotCurrentPageElementPage(11, pageElements.get(0));
        assertIsNotCurrentPageElementPage(12, pageElements.get(1));
        assertIsNotCurrentPageElementPage(13, pageElements.get(2));
        assertIsCurrentPageElementPage(14, pageElements.get(3));
        assertIsNotCurrentPageElementPage(15, pageElements.get(4));
        assertIsNotCurrentPageElementPage(16, pageElements.get(5));
        assertIsNotCurrentPageElementPage(17, pageElements.get(6));
        assertIsNotCurrentPageElementPage(18, pageElements.get(7));
        assertIsNotCurrentPageElementPage(19, pageElements.get(8));
        assertIsNotCurrentPageElementPage(20, pageElements.get(9));
    }

    @Test
    public void 페이징_Next_버튼_활성화_여부확인() {
        PageRequest pageRequest = new PageRequest(5, 10);
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 145));

        assertThat(pagingInfo.isNextVisible()).isTrue();
        assertThat(pagingInfo.isPreviousVisible()).isFalse();
    }

    @Test
    public void 페이징_Prev_버튼_활성화_여부확인() {
        PageRequest pageRequest = new PageRequest(13, 10);
        PagingInfo pagingInfo = new PagingInfo(new PageImpl<>(pagingList, pageRequest, 145));

        assertThat(pagingInfo.isNextVisible()).isFalse();
        assertThat(pagingInfo.isPreviousVisible()).isTrue();
    }

    private void assertIsCurrentPageElementPage(int page, PageElement element) {
        assertThat(element.getPageNumber()).isEqualTo(page);
        assertThat(element.isCurrentPage()).isTrue();
    }

    private void assertIsNotCurrentPageElementPage(int page, PageElement element) {
        assertThat(element.getPageNumber()).isEqualTo(page);
        assertThat(element.isCurrentPage()).isFalse();
    }
}