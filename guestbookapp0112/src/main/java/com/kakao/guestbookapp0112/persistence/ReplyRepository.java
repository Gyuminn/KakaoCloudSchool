package com.kakao.guestbookapp0112.persistence;

import com.kakao.guestbookapp0112.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
