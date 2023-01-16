package com.kakao.guestbookapp0112;

import com.kakao.guestbookapp0112.dto.BoardDTO;
import com.kakao.guestbookapp0112.dto.PageRequestDTO;
import com.kakao.guestbookapp0112.dto.PageResponseDTO;
import com.kakao.guestbookapp0112.dto.ReplyDTO;
import com.kakao.guestbookapp0112.service.BoardService;
import com.kakao.guestbookapp0112.service.ReplyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Test
    public void testDelete() {
        boardService.removeWithReplies(100L);
    }

    @Test
    public void testUpdate() {
        BoardDTO dto = BoardDTO.builder()
                .bno(99L)
                .title("제목 업데이트")
                .content("내용 업데이트")
                .build();
        System.out.println(boardService.modify(dto));
    }

    @Autowired
    ReplyService replyService;

    @Test
    public void testGetList() {
        // 게시글 번호를 이용해서 댓글 가져오기
        List<ReplyDTO> list = replyService.getList(27L);
        list.forEach(dto -> System.out.println(dto));
    }

    @Test
    public void insertReply() {
        ReplyDTO dto = ReplyDTO.builder()
                .text("댓글 삽입 테스트")
                .replyer("user1@kakao.com")
                .bno(27L)
                .build();
        System.out.println(replyService.register(dto));
    }
}
