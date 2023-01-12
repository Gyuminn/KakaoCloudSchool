package com.kakao.guestbookapp0112.service;

import com.kakao.guestbookapp0112.domain.Board;
import com.kakao.guestbookapp0112.domain.Member;
import com.kakao.guestbookapp0112.dto.BoardDTO;

public interface BoardService {
    // DTO -> Entity로 변환해주는 메서드
    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member.builder()
                .email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    }

    // Entity -> DTO로 변환해주는 메서드
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();
        return dto;
    }
}
