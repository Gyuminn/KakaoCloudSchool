package com.kakao.guestbookapp0112.service;

import com.kakao.guestbookapp0112.domain.Board;
import com.kakao.guestbookapp0112.domain.Member;
import com.kakao.guestbookapp0112.dto.BoardDTO;
import com.kakao.guestbookapp0112.dto.PageRequestDTO;
import com.kakao.guestbookapp0112.dto.PageResponseDTO;
import com.kakao.guestbookapp0112.persistence.BoardRepository;
import com.kakao.guestbookapp0112.persistence.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;
    // 2가지가 연쇄적으로 삭제되기 때문에 Transactional을 적용해야 한다.
    @Transactional
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno); // 댓글 삭제
        boardRepository.deleteById(bno); // 게시글 삭제
    }

    public Long register(BoardDTO dto) {
        log.info("Service:" + dto);
        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
    }

    public PageResponseDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        // Entity를 DTO로 변경하는 람다 인스턴스 생성
        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));

        // 목록 보기 요청 처리
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        return new PageResponseDTO<>(result, fn);
    }

    public BoardDTO get(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);
        Object [] ar  = (Object[]) result;
        return entityToDTO((Board) ar[0], (Member) ar[1], (Long) ar[2]);
    }
}
