package com.kakao.reviewapp0116.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDTO <DTO,EN> {
    private List<DTO> dtoList;
    private int totalPage;
    private int page;
    private int size;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;
    private List<Integer> pageList;

    public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1;
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
        startPage = tempEnd - 9;
        prev = startPage > 1;
        endPage = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(startPage,endPage).boxed().collect(Collectors.toList());
    }
}
