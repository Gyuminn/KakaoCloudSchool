package com.gyumin.websocket0310.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class APIUserDTO extends User {
    private String mid;
    private String mpw;

    // 생성자 직접 생성
    // security의 User를 상속받으면 super로 세가지를 전달해줘야 함.
    public APIUserDTO(String username, String password, Collection<GrantedAuthority> authorities) {
        // Spring Security는 아이디, 비번, 권한의 모임이 기본 정보
        super(username, password, authorities);
        this.mid = username;
        this.mpw = password;
    }
}
