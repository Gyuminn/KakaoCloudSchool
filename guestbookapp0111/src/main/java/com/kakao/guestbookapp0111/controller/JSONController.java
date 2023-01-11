package com.kakao.guestbookapp0111.controller;

import com.kakao.guestbookapp0111.domain.GuestBook;
import com.kakao.guestbookapp0111.dto.GuestBookDTO;
import com.kakao.guestbookapp0111.dto.PageRequestDTO;
import com.kakao.guestbookapp0111.dto.PageResponseDTO;
import com.kakao.guestbookapp0111.service.GuestBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class JSONController {
    private final GuestBookService guestBookService;

    @GetMapping("/guestbook/list.json")
    public PageResponseDTO<GuestBookDTO, GuestBook> list(PageRequestDTO pageRequestDTO) {
        return guestBookService.getList(pageRequestDTO);
    }
}
