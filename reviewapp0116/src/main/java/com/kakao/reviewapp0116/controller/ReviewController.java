package com.kakao.reviewapp0116.controller;

import com.kakao.reviewapp0116.dto.ReviewDTO;
import com.kakao.reviewapp0116.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    // 영화번호에 해당하는 댓글 목록을 처리
    @GetMapping("/{mno}/list")
    public ResponseEntity<List<ReviewDTO>> list(@PathVariable("mno") Long mno) {
        List<ReviewDTO> result = reviewService.getList(mno);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 댓글 추가
    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@PathVariable("mno") Long mno,
                                          @RequestBody ReviewDTO reviewDTO) {
        Long result = reviewService.register(reviewDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    // 댓글 수정
    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> updateReview(@PathVariable("mno") Long mno,
                                             @PathVariable("reviewnum") Long reviewnum,
                                             @RequestBody ReviewDTO reviewDTO) {
        Long result = reviewService.modify(reviewDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> deleteReview(@PathVariable("mno") Long mno,
                                             @PathVariable("reviewnum") Long reviewnum) {
        Long result = reviewService.remove(reviewnum);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
