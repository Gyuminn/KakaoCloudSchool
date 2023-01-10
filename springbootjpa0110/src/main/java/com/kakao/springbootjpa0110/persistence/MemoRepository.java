package com.kakao.springbootjpa0110.persistence;

import com.kakao.springbootjpa0110.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
