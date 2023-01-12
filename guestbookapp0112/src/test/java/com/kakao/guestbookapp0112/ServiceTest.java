package com.kakao.guestbookapp0112;

import com.kakao.guestbookapp0112.dto.BoardDTO;
import com.kakao.guestbookapp0112.dto.PageRequestDTO;
import com.kakao.guestbookapp0112.dto.PageResponseDTO;
import com.kakao.guestbookapp0112.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private BoardService boardService;

    // 등록 테스트
    @Test
    public void registerTest() {
        BoardDTO dto = BoardDTO.builder()
                .title("등록 테스트")
                .content("등록을 테스트합니다.")
                .writerEmail("user33@kakao.com")
                .build();

        Long bno = boardService.register(dto);
        System.out.println(bno);
    }

    // 목록 보기 테스트
    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResponseDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        System.out.println(result);
    }

    // 게시글 상세보기 테스트
    @Test
    public void testGet() {
        Long bno = 100L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);
    }
}
