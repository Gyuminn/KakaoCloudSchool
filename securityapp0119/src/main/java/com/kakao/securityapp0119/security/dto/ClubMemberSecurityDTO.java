package com.kakao.securityapp0119.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


@Getter
@Setter
@ToString
public class ClubMemberSecurityDTO extends User {
    private String mid;
    private String mpw;
    private String email;
    private String name;
    private boolean del;
    private boolean social;

    public ClubMemberSecurityDTO(String username, String password, String email, String name, boolean del, boolean social,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.name = name;
        this.del = del;
        this.social = social;
    }
}