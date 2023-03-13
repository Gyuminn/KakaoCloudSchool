package com.gyumin.websocket0310;

import com.gyumin.websocket0310.domain.APIUser;
import com.gyumin.websocket0310.persistence.APIUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

@SpringBootTest
class APIRepositoryTests {

    @Autowired
    private APIUserRepository apiUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testInserts() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            APIUser apiUser = APIUser.builder()
                    .mid("apiuser" + i)
                    .mpw(passwordEncoder.encode("1111"))
                    .build();
            apiUserRepository.save(apiUser);
        });
    }
}
