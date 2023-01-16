package com.kakao.reviewapp0116.persistence;

import com.kakao.reviewapp0116.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
