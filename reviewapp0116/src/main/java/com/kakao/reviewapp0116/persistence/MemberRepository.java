package com.kakao.reviewapp0116.persistence;

import com.kakao.reviewapp0116.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
