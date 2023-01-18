package com.kakao.reviewapp0116.service;

import com.kakao.reviewapp0116.domain.Movie;
import com.kakao.reviewapp0116.domain.Review;
import com.kakao.reviewapp0116.dto.ReviewDTO;
import com.kakao.reviewapp0116.persistence.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getList(Long mno) {
        // mno가지고 바로 하지 못한다.
        // Entity 만들때 외래키가 아닌 movie로 만들기 때문
        Movie movie = Movie.builder().mno(mno).build();
        List<Review> result = reviewRepository.findByMovie(movie);
        return result.stream().map(review -> entityToDto(review)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO reviewDTO) {
        // DTO니까 Entity로 바꿔야겠네!
        Review review = dtoToEntity(reviewDTO);
        reviewRepository.save(review);
        return review.getReviewnum();
    }

    @Override
    public Long modify(ReviewDTO reviewDTO) {
        // reviewDTO와 다른 점은 글 번호가 있냐 없냐이다.
        // 확인 로직을 추가해도 됨.
        Review review = dtoToEntity(reviewDTO);
        reviewRepository.save(review);
        return review.getReviewnum();
    }

    @Override
    public Long remove(Long rnum) {
        reviewRepository.deleteById(rnum);
        return rnum;
    }
}
