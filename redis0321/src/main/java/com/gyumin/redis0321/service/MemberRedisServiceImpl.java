package com.gyumin.redis0321.service;

import com.gyumin.redis0321.MemberRedisRepository;
import com.gyumin.redis0321.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRedisServiceImpl implements MemberRedisService{
    private final MemberRedisRepository memberRedisRepository;

    // 데이터 삽입의 리턴 타입
    // DTO(정보를 리턴 - 드문 케이스)
    // 기본키(성공과 실패 여부와 어떤 데이터가 추가되었는지 확인)
    // void(모든 예외 처리가 서버에 구성이 되어 있어서 실패할 가능성이 없는 경우)
    public void addMember() {
        Member member = new Member("규민", 26);
        memberRedisRepository.save(member);
    }
}
