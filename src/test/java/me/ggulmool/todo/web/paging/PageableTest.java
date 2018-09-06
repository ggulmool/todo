package me.ggulmool.todo.web.paging;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PageableTest {

    private List<Integer> pagingList = new ArrayList<>();

    private Page<Integer> pageImpl1;
    private Page<Integer> pageImpl2;
    private Page<Integer> pageImpl3;

    @Before
    public void setUp() {
        // new PageRequest(0, 6) 현재페이지(0=1페이지), 페이지당 보여줄 갯수
        pageImpl1 = new PageImpl<>(pagingList, new PageRequest(0, 6), 18); // 1페이지
        pageImpl2 = new PageImpl<>(pagingList, new PageRequest(1, 6), 18); // 2페이지
        pageImpl3 = new PageImpl<>(pagingList, new PageRequest(2, 6), 18); // 3페이지
    }

    @Test
    public void 처음페이지_여부_확인() {
        assertThat(pageImpl1.isFirst()).isTrue();
        assertThat(pageImpl2.isFirst()).isFalse();
        assertThat(pageImpl3.isFirst()).isFalse();
    }

    @Test
    public void 마지막페이지_여부_확인() {
        assertThat(pageImpl1.isLast()).isFalse();
        assertThat(pageImpl2.isLast()).isFalse();
        assertThat(pageImpl3.isLast()).isTrue();
    }

    @Test
    public void 다음페이지_정보_확인() {
        assertThat(pageImpl1.hasNext()).isTrue();
        assertThat(pageImpl1.nextPageable()).isNotNull();

        assertThat(pageImpl2.hasNext()).isTrue();
        assertThat(pageImpl2.nextPageable()).isNotNull();

        assertThat(pageImpl3.hasNext()).isFalse();
        assertThat(pageImpl3.nextPageable()).isNull();
    }

    @Test
    public void 이전페이지_정보_확인() {
        assertThat(pageImpl1.hasPrevious()).isFalse();
        assertThat(pageImpl1.previousPageable()).isNull();

        assertThat(pageImpl2.hasPrevious()).isTrue();
        assertThat(pageImpl2.previousPageable()).isNotNull();

        assertThat(pageImpl3.hasPrevious()).isTrue();
        assertThat(pageImpl3.previousPageable()).isNotNull();
    }

    @Test
    public void 현재페이지_번호_확인() {
        assertThat(pageImpl1.getNumber()).isEqualTo(0);
        assertThat(pageImpl2.getNumber()).isEqualTo(1);
        assertThat(pageImpl3.getNumber()).isEqualTo(2);
    }

    @Test
    public void 페이지당_보여줄갯수_확인() {
        assertThat(pageImpl1.getSize()).isEqualTo(6);
        assertThat(pageImpl2.getSize()).isEqualTo(6);
        assertThat(pageImpl3.getSize()).isEqualTo(6);
    }

    @Test
    public void 전체_페이지_갯수_확인() {
        assertThat(pageImpl1.getTotalPages()).isEqualTo(3);
        assertThat(pageImpl2.getTotalPages()).isEqualTo(3);
        assertThat(pageImpl3.getTotalPages()).isEqualTo(3);
    }
}
