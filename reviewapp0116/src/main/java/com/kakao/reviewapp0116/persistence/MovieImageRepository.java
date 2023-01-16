package com.kakao.reviewapp0116.persistence;

import com.kakao.reviewapp0116.domain.MovieImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieImageRepository extends JpaRepository<MovieImage, Long> {
}
