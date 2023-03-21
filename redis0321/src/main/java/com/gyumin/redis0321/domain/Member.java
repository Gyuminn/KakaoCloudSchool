package com.gyumin.redis0321.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("member")
public class Member {
    @Id
    private String id;

    private String name;

    private int age;

    // 선택적인 Builder 패턴이나, 모두 넣어야 하는 AllargsConstructor는 Id가 자동생성되는 것에는 맞지 않다.
    // 그래서 알맞게 생성자를 만들어 준 것.
    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
