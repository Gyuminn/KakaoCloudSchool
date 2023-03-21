package com.gyumin.redis0321;

import com.gyumin.redis0321.domain.Member;
import org.springframework.data.repository.CrudRepository;

// Redis에 JPA처럼 작업을 할 수 있도록 해주는 Repository
// Proxy 패턴을 적용해서 SpringFramework가 Bean을 생성해서 사용할 수 있도록 해준다.
public interface MemberRedisRepository extends CrudRepository<Member, String> {
}
