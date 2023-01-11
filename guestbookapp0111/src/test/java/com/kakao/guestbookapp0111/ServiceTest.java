package com.kakao.guestbookapp0111;

import com.kakao.guestbookapp0111.domain.GuestBook;
import com.kakao.guestbookapp0111.dto.GuestBookDTO;
import com.kakao.guestbookapp0111.dto.PageRequestDTO;
import com.kakao.guestbookapp0111.dto.PageResponseDTO;
import com.kakao.guestbookapp0111.service.GuestBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private GuestBookService guestBookService;

    @Test
    public void testRegister() {
        GuestBookDTO dto = GuestBookDTO.builder()
                .title("삽입 제목")
                .content("삽입 내용")
                .writer("삽입 작성자").build();
        System.out.println(guestBookService.register(dto));
    }

    @Test
    public void testList() {
        // 1페이지 10개
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<GuestBookDTO, GuestBook> result = guestBookService.getList(pageRequestDTO);
        for(GuestBookDTO dto: result.getDtoList()) {
            System.out.println(dto);
        }
    }

    @Test
    public void testListInformation() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(19)
                .size(10)
                .build();
        PageResponseDTO<GuestBookDTO, GuestBook> result = guestBookService.getList(pageRequestDTO);
        // 데이터 확인
        // 데이터 목록
        System.out.println(result.getDtoList());
        // 페이지 목록
        System.out.println(result.getPageList());
        // 전체 페이지 개수
        System.out.println(result.getTotalPage());
        // 이전 여부 - boolean은 get메서드가 아닌 is메서드로 생성됨.
        System.out.println(result.isPrev());
        // 다음 여부
        System.out.println(result.isNext());
    }
}
