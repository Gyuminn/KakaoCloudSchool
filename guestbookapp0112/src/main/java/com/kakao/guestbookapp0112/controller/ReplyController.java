package com.kakao.guestbookapp0112.controller;

import com.kakao.guestbookapp0112.dto.ReplyDTO;
import com.kakao.guestbookapp0112.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies") // 공통 URL 설정
public class ReplyController {
    private final ReplyService replyService;

    // 게시글 번호를 가지고 댓글을 리턴해주는 메서드
    @GetMapping(value="/board/{bno}")
    public ResponseEntity<List<ReplyDTO>> getByBoard(@PathVariable("bno") Long bno) {
        log.info("bno: " + bno);
        // JSON 으로 주기
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }
}
