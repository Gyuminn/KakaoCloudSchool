package com.kakao.securityapp0119;

import com.kakao.securityapp0119.model.ClubMember;
import com.kakao.securityapp0119.model.ClubMemberRole;
import com.kakao.securityapp0119.persistence.ClubMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTests {
    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 샘플 데이터 삽입
    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .mid("member" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("user" + i + "@gmail.com")
                    .name("사용자" + i)
                    .social(false)
                    .roleSet(new HashSet<ClubMemberRole>())
                    .build();

            clubMember.addRole(ClubMemberRole.USER);
            if(i > 90) {
                clubMember.addRole(ClubMemberRole.ADMIN);
            }
            clubMemberRepository.save(clubMember);
        });
    }

    // mid를 이용해서 조회하는 메서드
    @Test
    public void testRead() {
        Optional<ClubMember> result = clubMemberRepository.getWithRoles("member101");
        if (result.isPresent()) {
            System.out.println(result);
            System.out.println(result.get());
        } else {
            System.out.println("존재하지 않는 아이디");
        }
    }

    @Test
    public void testReadEmail() {
        Optional<ClubMember> clubMember = clubMemberRepository.findByEmail("user95@email.com");
        System.out.println(clubMember.get().getRoleSet());
    }
}
