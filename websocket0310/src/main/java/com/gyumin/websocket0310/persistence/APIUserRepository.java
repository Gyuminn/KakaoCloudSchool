package com.gyumin.websocket0310.persistence;

import com.gyumin.websocket0310.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}
