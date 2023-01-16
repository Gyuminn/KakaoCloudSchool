package com.kakao.reviewapp0116;

import com.kakao.reviewapp0116.domain.Member;
import com.kakao.reviewapp0116.domain.Movie;
import com.kakao.reviewapp0116.domain.MovieImage;
import com.kakao.reviewapp0116.domain.Review;
import com.kakao.reviewapp0116.persistence.MemberRepository;
import com.kakao.reviewapp0116.persistence.MovieImageRepository;
import com.kakao.reviewapp0116.persistence.MovieRepository;
import com.kakao.reviewapp0116.persistence.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTests {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository movieImageRepository;

    @Test
    public void insertMovie() {
        // 영화 100개 생성 후 삽입
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Movie movie = Movie.builder()
                    .title("Movie..." + i)
                    .build();
            movieRepository.save(movie);

            int count = (int) (Math.random() * 5) + 1; // 1, 2, 3, 4
            for (int j = 0; j < count; j++) {
                MovieImage movieImage = MovieImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .movie(movie)
                        .imgName("test" + j + ".jpg")
                        .build();
                movieImageRepository.save(movieImage);
            }
        });
    }

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMember() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("r" + i + "@gmail.com")
                    .pw("1111")
                    .nickname("reviewer" + i)
                    .build();
            memberRepository.save(member);
        });
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMovieReview() {
        IntStream.rangeClosed(1, 200).forEach(i -> {
            // 영화 번호
            Long mno = (long) (Math.random() * 100) + 1;
            // 회원 번호
            Long mid = (long) (Math.random() * 100) + 1;

            Movie movie = Movie.builder().mno(mno).build();
            Member member = Member.builder().mid(mid).build();

            Review review = Review.builder()
                    .movie(movie)
                    .member(member)
                    .grade((int) (Math.random() * 5) + 1)
                    .text("영화 느낌...." + i)
                    .build();

            reviewRepository.save(review);
        });
    }
}
