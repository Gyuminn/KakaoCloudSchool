package com.kakao.springbootjpa0110.persistence;

import com.kakao.springbootjpa0110.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // mno의 값이 from부터 to 사이인 데이터 조회하는 메서드
    List<Memo> findByMnoBetween(Long from, Long to);

    // mno의 값이 from부터 to 사이인 데이터를 Mno의 내림차순 정려해서 조회하는 메서드
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
}
