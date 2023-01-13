package com.kakao.guestbookapp0112.controller;

import com.kakao.guestbookapp0112.dto.BoardDTO;
import com.kakao.guestbookapp0112.dto.PageRequestDTO;
import com.kakao.guestbookapp0112.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 기본 요청 생성
    @GetMapping({"/", "/board/list"})
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        log.info("기본 목록 보기 요청");
        model.addAttribute("result", boardService.getList(pageRequestDTO));
        return "board/list";
    }

    // 게시물 등록 화면으로 이동하는 요청 - Fowarding
    @GetMapping("/board/register")
    public void register(Model model) {
        log.info("등록 화면으로 포워딩");
    }

    // 게시물을 등록하는 요청 - Redirect
    // RedirectAttributes - 1회용 ㅔ션
    @PostMapping("/board/register")
    public String register(BoardDTO dto, RedirectAttributes rattr) {
        // 파라미터 확인
        log.info("dto: " + dto.toString());
        // 데이터 삽입
        Long bno = boardService.register(dto);
        rattr.addFlashAttribute("msg", bno + "등록");

        return "redirect:/board/list";
    }
}
