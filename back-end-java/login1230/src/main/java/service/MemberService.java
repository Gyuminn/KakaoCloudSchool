package service;

import dto.MemberDTO;

public interface MemberService {
    // 로그인 처리를 위한 메서드
    public MemberDTO login(String mid, String mpw);
}
