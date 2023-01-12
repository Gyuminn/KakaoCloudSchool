package com.kakao.guestbookapp0112.persistence;

import com.kakao.guestbookapp0112.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
