package com.kakao.reviewapp0116.persistence;

import com.kakao.reviewapp0116.domain.Movie;
import com.kakao.reviewapp0116.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
