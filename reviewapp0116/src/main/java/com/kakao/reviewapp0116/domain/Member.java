package com.kakao.reviewapp0116.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name="m_member")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mid;

    private String email;

    private String pw;

    private String nickname;
}
