package com.kakao.guestbookapp0112.persistence;

import com.kakao.guestbookapp0112.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

interface SearchBoardRepository {
    Board search1();

    // 검색을 위한 메서드
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
