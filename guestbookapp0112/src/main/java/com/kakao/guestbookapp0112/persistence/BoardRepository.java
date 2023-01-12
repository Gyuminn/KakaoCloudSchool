package com.kakao.guestbookapp0112.persistence;

import com.kakao.guestbookapp0112.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
