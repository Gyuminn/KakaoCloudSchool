package com.kakao.guestbookapp0112.controller;

import com.kakao.guestbookapp0112.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
}
