package com.kakao.guestbookapp0112.service;

import com.kakao.guestbookapp0112.domain.Board;
import com.kakao.guestbookapp0112.domain.Reply;
import com.kakao.guestbookapp0112.dto.ReplyDTO;
import com.kakao.guestbookapp0112.persistence.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {
        // 글 번호를 받아야 한다.
        List<Reply> result = replyRepository.findByBoardOrderByRno(Board.builder()
                .bno(bno).build());

        // result의 내용을 정렬하기 - 수정한 시간의 내림차순 (modDate descending으로 Sorting에서 DB에서 가져와도 됨.)
        result.sort(new Comparator<Reply>() {
            @Override
            public int compare(Reply o1, Reply o2) {
                return o2.getModDate().compareTo(o1.getModDate());
            }
        });

        // Reply의 List를 ReplyDTO의 List로 변경
        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public Long modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
        return reply.getRno();
    }

    @Override
    public Long remove(Long rno) {
        replyRepository.deleteById(rno);
        return rno;
    }
}
