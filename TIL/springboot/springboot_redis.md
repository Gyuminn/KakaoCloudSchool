# [Spring Boot] Redis

1. **Redis(REmote Dictionary Server)**
   1. 개요
      - Memory 기반의 데이터베이스
      - AWS에서 사용을 할 때는 최적화된 완전 관리형 데이터베이스 형태로 ElasticCache라는 서비스로 제공하고 EC2에서 자체 관리하는 Redis를 설치해서 사용하는 것이 가능
        ElasticCache는 동일한 VPC 내에서만 접근 가능
      - 메모리 데이터베이스는 저장 공간에 제약이 있기 때문에 보조 저장소로 사용이 되지만 클러스터 기능을 이용하면 주 저장소로 사용이 가능
      - **메모리 데이터베이스이고 싱글 스레드 기반**
        저장된 데이터는 사라질 가능성이 있기 때문에 Redis 에서는 자체적으로 백업하는 기능을 제공하고 있는데 RDB 방식이나 AOF 방식을 이용
        **RDB 방식은 데이터 전체에 대한 스냅샷을 작성하고 이를 디스크에 저장하는 방식 - 완전한 복구가 어려움**
        **AOF(Append Only File): 이벤트를 로그로 저장 - 로딩 속도가 느릴 수 있고 시간이 지나면 로그 파일의 크기가 너무 커지게 된다.**
        실제 사용하는 방식은 매일 RDB 방식으로 스냅샷을 만들고 스냅샷 이후의 변경은 AOF를 사용하는데 AOF는 일별로 별도로 만들고 일정 기간 동안의 데이터만 보관
        싱글 스레드 방식이라는 것은 여러 클라이언트의 요청이 한꺼번에 오면 Event Queue를 만들어서 Queue에 저장하고 하나씩 꺼내서 Redis Core Thread가 작업을 수행
        Multi Thread를 이용하게 되면 여러 개의 요청을 동시에 처리하는 것처럼 해서 효율이 높아지지만 일정 개수 이상의 스레드가 생성되면 **Context Switching** 때문에 효율이 떨어질 수 있다.
        Multi Thread의 또 다른 문제점 중의 하나는 **데이터의 불일치**이다.
        **Dead Lock** 발생 가능성이 있음.
        - A 요청 - C와 D 테이블에 데이터를 삽입(트랜잭션)
        - B 요청 - D와 C 테이블에 데이터를 삽입(트랜잭션)
        - 읽기는 SharedLock이 걸리는데, 나머지 작업은 ExclusiveLock이 걸린다. 이 때, A 요청과 B 요청이 서로 락걸린 D와 C를 원하기 때문에, 무한 대기 상태에 걸린다.
          싱글 스레드의 단점은 전체 데이터 스캔을 하게 되면 시간이 너무 오래 걸린다.
      - **redis를 production 환경에서 구축할 때는 한 대의 Master와 하나 이상의 replica 서버로 구성**
        Read를 제외한 작업은 Master에서 수행하고 Read 작업은 replica에서 수행하는데 Master 노드에서는 replica 서버에 데이터를 복제해서 동기화하는 작업을 수행(Replication)
        Master에 장애가 발생하면 Replica중 하나가 Master의 역할을 대신 수행
      - redis를 모니터링해서 상태를 관리하는 솔루션으로는 Redis Centinel과 Redis Cluster
      - **분산 Lock(로드밸런싱에서 사용하는 개념이다) → 예약 대기열 시스템같은거 만들 때 좋다.**
        요청1을 처리할 수 있는 처리1, 처리2, 처리3이 있다고 생각해보자. 이 세개가 같은 요청1을 처리하는 것은 중복이다. 한 곳에서만 처리하도록 하기 위해 로드밸런싱같은 기능을 이용한다. 근데 어떤 애가 작업을 할 것이냐는 레디스가 Priority Queue에 저장해서 가장 먼저 수행되는 애한테 전달.
      - **구독(PUB)과 게시(SUB)가 가능**
        관계형 데이터베이스는 트리거를 이용해서 하나의 작업이 완료되거나 실패했을 떄 다른 작업을 수행하는 구조로 구독과 게시를 만드는데 이런 형태는 코드가 복잡해질 가능성이 높다.
      - 요약
        **메모리기반 데이터베이스, 키밸류 구조, 싱글 스레드 3가지 키워드는 꼭 기억하자.**
   2. **Redis Sentinel Architecture**
      - 센티널, 마스터, 레플리카로 구성
      - 센티널이 레디스 서버(마스터, 레플리카)들을 관리 - **인그레스와 비슷!!**
      - 센티널은 레디스 서버들의 상태를 주기적으로 모니터링하고 마스터 서버가 서비스할 수 없는 상태가 되면 다른 레플리카를 마스터로 변경
      - 마스터가 장애가 발생해서 레플리카 중 하나가 마스터가 되면 이제 다른 레플리카들은 새로 선출된 마스터의 데이터를 복제하도록 설정을 변경하는데 이를 장애 복구(failover)라고 한다.
      - 애플리케이션은 센티널과 연결을 해야 한다.
      - 실제 구현에서는 센티널도 여러 개로 구성 - 만들 때는 일반적으로 홀수로 구성
        다수결(quorum) 알고리즘으로 결정하기 때문
   3. **Redis Cluster Architecture**
      - redis 3.0부터 지원
      - 클러스터에 포함된 모든 노드들이 서로 통신하면서 고가용성을 유지하며 **샤딩** 기능을 가지고 있음.
      - **Mesh** 구조(전부 다 연결되어 통신이 가능하도록)로 연결되고 **gossip protocol**(노드 간에 주기적으로 데이터를 주고 받아서 모든 노드에게 전파하는 방식)을 사용해서 모니터링
      - **샤딩**은 데이터를 분배해서 저장하는 것을 의미하는데 해시 함수를 이용해서 해시 함수로 실행한 함수값을 사용해서 어떤 노드에 저장할 지 결정
        노드가 추가되거나 제거되면 클러스터 명령어를 사용해서 해시 함수 값 범위를 조정할 수 있는데 이 작업을 rebalancing 또는 reshard라고 한다.
        reshard 중에는 데이터의 위치를 실시간으로 추적할 수 없음 - Moved Redirection 에러가 발생할 수 있음.
2. **Spirng과 Redis**

   자바의 경우는 redis를 사용할 수 있는 라이브러리가 여러 종류 - Jedis, Lettuce, Redisson 등

   애플리케이션을 개발하면서 쓰기 동작보다 읽기 동작이 많은 데이터가 있다면 캐싱을 고민

   **Spring은 AOP 기반의 어노테이션을 제공하기 때문에 캐시 기능을 운영 중인 애플리케이션에 도입하는 것이 쉬움.**

   비즈니스 로직과 공통 관심 사항이 하나의 블럭에 작성되지 않도록 프로그래밍

   공통 관심 사항을 별도로 작성하고 컴파일할 떄나 런타임 시에 비즈니스 로직에 끼워넣어서 실행

   비즈니스 로직에서도 이와 유사한 기능인 구독과 게시 패턴을 사용하기도 한다.

   비즈니스 로직이 작성된 후 새로운 기능을 추가해야 하는 경우 비즈니스 로직이므로 기존 비즈니스 로직 코드에 추가하는 형태로 많이 구현하는데 이렇게 하지 말고 새로운 기능을 별도의 영역에 구현을 하고 기존 비즈니스 로직을 구독하는 형태로 만들고 기존 비즈니스 로직에 이벤트가 발생하면 구독하고 있는 로직에 이벤트를 발생시켜 수행하도록 만들기도 한다.

   회원가입을 하면 쿠폰을 전송 - 회원가입 코드에 쿠폰 전송 코드를 추가했다가 지웠다가 하는 것이 아니다. `회원가입 + 이벤트 발생` 코드로 작성하고 이벤트가 발생하면 쿠폰 전송을 수행하는 것! 이벤트가 끝나면 이벤트를 해제하면 됨. 그리고 트랜잭션을 하나가 아니라 나눌 수 있기 때문에 더 효율적이게 됨.

   1. 의존성

      starter-data-redis

   2. 사용법
      - RedisRepository 이용 - JPA 형태로 사용
      - **RedisTemplate 이용(권장)**
   3. 커넥션 설정
      - 스프링 부트는 Lettuce 라이브러리를 기본으로 사용
      - Lettuce는 Netty 프레임워크를 포함하고 있어서 비동기 논블로킹 형태로 동작
        멀티 스레드를 사용하더라도 멀티 스레드에 안전한 프로그래밍이 가능
      - 연결은 Connection Pool 없이 사용하는 것을 권장하지만 트랜잭션을 사용하는 경우라면 Connection Pool을 만들어서 사용
      - 연결을 만들 떄 구조가 Sentinel 방식이면 Sentinel 노드와 연결을 해야 하고 Cluster 방식이면 모든 노드와 전부 연결을 해야 한다.
   4. Spring Boot에서 Redis를 사용하기 위한 설정

      - lombok, springweb, devtools, redis 의존성 설정
      - Redis 설정을 위한 클래스 작성 - config.RedisConfig

        ```java
        package com.gyumin.redis0321.config;

        import io.lettuce.core.ClientOptions;
        import io.lettuce.core.SocketOptions;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.redis.connection.*;
        import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
        import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
        import org.springframework.data.redis.serializer.StringRedisSerializer;

        import java.time.Duration;

        // RedisRepository 사용 가능하도록 해주는 어노테이션
        @EnableRedisRepositories
        @Configuration
        public class RedisConfig {
            // 레디스 설정 팩토리 클래스
            @Bean
            public RedisConnectionFactory basicCacheRedisConnectionFactory() {
                /// 센티널 방식 - 관리자가 따로 존재하는 방식
                /*
                RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
                configuration.setMaster("마스터");
                configuration.addSentinel("레플리카");
                 */

                // 클러스터 방식 - 모든 노드가 Mesh로 연결
                /*
                RedisClusterConfiguration configuration = new RedisClusterConfiguration();
                configuration.setMaxRedirects(3); // 최대 연결 개수
                configuration.setClusterNodes(List.of(
                        new RedisNode("IP", 1),
                        new RedisNode("IP", 2),
                        new RedisNode("IP", 3)
                ));
                 */

                // 레디스 구성에 따라 달라짐 - 단독 서버
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);

                // 옵션 설정
                final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
                final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

                // 연결 팩토리
                LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                        .clientOptions(clientOptions)
                        .commandTimeout(Duration.ofSeconds(5))
                        .shutdownTimeout(Duration.ZERO)
                        .build();
                return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);
            }

            // 실제 사용될 Template을 생성
            @Bean
            public RedisTemplate<String, Object> redisTemplate() {
                RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                redisTemplate.setConnectionFactory(basicCacheRedisConnectionFactory());
                // redis는 데이터를 보관할 때 byte 배열로 보관
                // 데이터를 읽고 쓸 때 byte가 아닌 실제 자료형을 설정해주어야 데이터를 올바르게 읽고 쓸 수 있다.
                // Serializer(직렬화) - 객체 단위로 읽고 쓸 수 있도록 해주는 기능
                // DTO를 만들 떄 implements Serializable을 해주는 경우가 있는데
                // DTO 객체 자체를 파일에 읽고 쓰기를 하거나 다른 시스템에 전송할 떄
                // Java <-> Java JSON
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setValueSerializer(new StringRedisSerializer());
                return redisTemplate;
            }
        }
        ```

3. **Redis를 JPA처럼 사용**

   1. Entity 클래스

      ```java
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
      ```

   2. CrudRepository를 생성

      ```java
      package com.gyumin.redis0321;

      import com.gyumin.redis0321.domain.Member;
      import org.springframework.data.repository.CrudRepository;

      // Redis에 JPA처럼 작업을 할 수 있도록 해주는 Repository
      // Proxy 패턴을 적용해서 SpringFramework가 Bean을 생성해서 사용할 수 있도록 해준다.
      public interface MemberRedisRepository extends CrudRepository<Member, String> {
      }
      ```

   3. Service 클래스를 생성 - Template Method Pattern을 사용

      - 인터페이스를 생성하고 메서드를 구현
      - Service는 여러 Layer로 구성이 가능 - 나누어지는 Layer는 Template Method Pattern을 적용하지 않을 수도 있음.
      - Service를 Template Method 패턴으로 만드는 이유는 Service Layer가 다른 사람과 통신하기 위한 수단
      - 다른 사람과 대화를 할 때는 구현된 내용을 가지고 하는 것이 아니고 필요한 부분이 구현되었는지를 확인한다.
      - 인터페이스 생성 - service.MemberRedisService

        ```java
        package com.gyumin.redis0321.service;

        public interface MemberRedisService {
            // 회원 추가
            void addMember();
        }
        ```

      - 클래스를 생성 - service.MemberRedisServiceImpl

        ```java
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
        ```

   4. Controller 클래스를 만들어서 요청과 연결 - controller.RedisController

      ```java
      package com.gyumin.redis0321.controller;

      import com.gyumin.redis0321.service.MemberRedisService;
      import lombok.RequiredArgsConstructor;
      import org.springframework.http.HttpStatus;
      import org.springframework.http.ResponseEntity;
      import org.springframework.web.bind.annotation.PostMapping;
      import org.springframework.web.bind.annotation.RestController;

      @RestController
      @RequiredArgsConstructor
      public class RedisController {
          private final MemberRedisService memberRedisService;

          @PostMapping
          public ResponseEntity<String> add() {
              memberRedisService.addMember();
              return new ResponseEntity<>("success", HttpStatus.OK);
          }
      }
      ```

   5. 터미널에서 확인

      ```bash
      docker run --name myredis -d -p 6379:6379 redis
      redis-cli -h 127.0.0.1
      docker exec -it myredis /bin/bash
      keys *
      hgetall member:{아이디}
      ```

4. **RedisTemplate**

   Redis를 사용할 수 있도록 만들어진 Template 클래스

   1. 문자열(String) 사용

      - ValueOperation<K,V>
        문자열 자료 구조를 사용할 수 있는 기능을 제공
        RestTemplate의 opsForValue() 메서드를 이용해서 생성
      - 메서드
        - set
          데이터만 저장할 수 있고 유효 기간을 설정
        - setIfAbsent
          매칭되는 데이터가 없을 때 저장
        - setIfPresent
          매칭되는 데이터가 있으면 덮어씌움
        - getAndDelete
          Stack의 POP
        - getAndSet
          데이터를 조회하고 Value를 새로 저장
        - getAndExpire
          가져오면서 유효 기간을 설정
      - 일반적으로 세션을 직접 생성하는 경우에는 session의 만료 시간을 별도로 개발자 설정하지만 redis를 이용하면 삽입할 때 바로 설정 가능
      - Controller에 key 생성하고 받아오는 것 확인하기

        ```java
        package com.gyumin.redis0321.controller;

        import com.gyumin.redis0321.service.MemberRedisService;
        import lombok.RequiredArgsConstructor;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.core.ValueOperations;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.PathVariable;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RestController;

        import java.time.Duration;

        @RestController
        @RequiredArgsConstructor
        public class RedisController {
            private final MemberRedisService memberRedisService;

            @PostMapping("/addmember")
            public ResponseEntity<String> add() {
                memberRedisService.addMember();
                return new ResponseEntity<>("success", HttpStatus.OK);
            }

            // Redis를 사용할 수 있는 bean 주입
            private final RedisTemplate<String, String> redisTemplate;

            @PostMapping("/redistest")
            public ResponseEntity<?> addRedisString() {
                // 문자열을 사용할 수 있는 객체 생성
                ValueOperations<String, String> vop = redisTemplate.opsForValue();
                // 반 영구적 저장
                vop.set("yellow", "banana");
                // 30초 동안만 유효한 저장
                vop.set("blue", "블루베리", Duration.ofSeconds(10));
                return new ResponseEntity<>(HttpStatus.CREATED);
            }

            @GetMapping("/redistest/{key}")
            public ResponseEntity<?> getRedisKey(@PathVariable String key) {
                ValueOperations<String, String> vop = redisTemplate.opsForValue();
                String value = vop.get(key);
                return new ResponseEntity<>(value, HttpStatus.OK);
            }
        }
        ```

      - 테스트 도구에서 삽입을 요청
      - 데이터 확인 - blue는 30초 후에 삭제됨.

   2. 다른 자료 구조 사용
      - opsForValue: String
      - opsForList: List
      - opsForSet: Set
      - opsForZSet: 정렬된 Set
      - opSForHash: Hash
   3. **분산 락**
      - Scale Out
        고갸용성 확보를 위해서 컴퓨터(하드웨어)를 추가해서 해결
        **동시성 문제가 발생 - 요청이 왔을 때 모든 컴퓨터에서 처리하게 되면 작업의 중복**
        Load Balancer가 필요
      - Load Balancer
        여러 개의 객체가 있을 떄 부하를 나누어주는 소프트웨어
        **Load Balancer를 구현하는 원리는 분산 락인데 전부 락을 건 상태에서 락 해제를 먼저 한 하나의 서비스만 동작하도록 하는 것**
      - NoSQL과 Nodesms Single Thread 기반
   4. 분산 락 실습

      - 분산 락 실습을 위해서 별도의 RedisTemplate을 생성하는 Config 파일 추가

        ```java
        package com.gyumin.redis0321.config;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.redis.connection.RedisConnectionFactory;
        import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
        import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.serializer.GenericToStringSerializer;

        @Configuration
        public class LockConfig {
            public RedisConnectionFactory lockedRedisConnectionFactory() {
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
                return new LettuceConnectionFactory(configuration);
            }

            @Bean
            public RedisTemplate<String, Long> lockedRedisTemplate() {
                RedisTemplate<String, Long> lockedRedisTemplate = new RedisTemplate<>();
                lockedRedisTemplate.setConnectionFactory(lockedRedisConnectionFactory());
                lockedRedisTemplate.setKeySerializer(new GenericToStringSerializer<>(Long.class));
                lockedRedisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
                return lockedRedisTemplate;
            }
        }
        ```

      - Spring에서 동일한 자료형의 bean을 2개 이상 만드는 경우 에러가 발생할 수 있음.
        @Autowired나 @RequiredArgsConstructor를 사용했을 떄 주의를 해야 하는데 2가지 주입 방식은 기본적으로 자료형을 가지고 주입을 받는다.
        동일한 자료형의 bean이 2개 이상 존재하면 에러가 발생할 수 있음.
        **이런 경우에는 Bean을 만들 떄 name 속성을 이용해서 Bean의 이름을 만들고 @Qualifer나 @Resource를 이용해서 일치하는 name에 해당하는 Bean을 찾아서 주입받아 달라고 해야 한다.**
        사용자의 요청을 처리하는 클래스를 Service라고 칭하지만 업문에서는 **Adpator**라고 하기도 한다.
        여기서는 Controller ↔ RedisTemplate 이렇게 하고 있는데 보통 중간에 Service를 둔다. 근데 Adaptor라고 하기도 함. Controller ↔ Adapter ↔ RedisTemplate
      - 그래서 요즘은 어떻게 하느냐
        **Service 계층에 복잡한 비즈니스 로직 - 게시**
        **Adpater - 데이터 베이스 연동 로직 = 구독**
      - DTO나 Map을 사용하는 이유
        한꺼번에 사용되어야 하는 데이터를 묶어서 사용
        login(String id, String pw)
        register(String id, String pw, int age, String address, String phonenumber)
        **이렇게 하면 register는 코드를 잘못 짠거다! 객체지향이 무너짐(변수 순서 다르면 어칼래)**
        register(Map<String, Object> member) → 이렇게 만들면 key를 알아야되는데
        register(DTO member) → 이렇게 만들면 순서를 기억할 필요가 없음. 온점 찍으면 그냥 나오잖아
        **객체 지향 언어의 가장 큰 특징 - 순서가 없다. 그러니까 매개변수가 3개 이상 넘어가면 DTO를 만드는 것을 고려하자!!!!!!!!!**
      - 분산 서비스를 위한 LockAdapter 생성 - service.LockAdapter

        ```java
        package com.gyumin.redis0321.service;

        import lombok.extern.log4j.Log4j2;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.core.ValueOperations;
        import org.springframework.stereotype.Component;

        import java.time.Duration;

        // Component, Configuration, Controller(RestController), Service, Repository
        // Bean 아래 있는 메서드가 리턴하는 데이터
        // Bean 자동 객체 - 클래스의 객체를 생성해서 Spring Bean으로 등록
        // Configuration은 여러 Bean을 생성하고자 하는 경우 자동 생성을 위해서 사용
        // Component는 명확하게 목적을 정하기 어려운 경우 사용
        @Component
        @Log4j2
        public class LockAdpater {
            private final RedisTemplate<String, Long> lockRedisTemplate;
            private ValueOperations<String, Long> lockOperation;

            public LockAdpater(RedisTemplate<String, Long> lockRedisTemplate) {
                this.lockRedisTemplate = lockRedisTemplate;
                lockOperation = lockRedisTemplate.opsForValue();
            }

            // 락 획득 메서드
            public Boolean holdLock(String hotelId, Long userId) {
                String lockKey = "key";
                // 매칭되는 데이터가 없을 때 저장
                return lockOperation.setIfAbsent(lockKey, userId, Duration.ofSeconds(10));
            }

            // 락 확인하는 메서드
            public Long checkLock(String hotelId) {
                String lockKey = "key";
                return lockOperation.get(lockKey);
            }

            // 락을 헤제하는 메서드
            public void clearLock(String hotelId) {
                lockRedisTemplate.delete("key");
            }
        }
        ```

      - 분산 락을 테스트하기 위한 클래스를 생성하고 작성

        ```java
        package com.gyumin.redis0321;

        import com.gyumin.redis0321.service.LockAdpater;
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.DisplayName;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;

        @SpringBootTest
        public class LockAdapterTest {
            @Autowired
            private LockAdpater lockAdpater;

            private final Long firstUserId = 1L;
            private final Long secondUserId = 2L;
            private final Long thirdUserId = 3L;

            @Test
            @DisplayName("분산 락을 테스트합니다.")
            public void testLock() {
                final String hotelId = "마라무쌍";

                // clearLock은 처음 수행할 떄는 주석처리하고
                // 두 번쨰 테스트 할 때는 지우고 확인해야 하므로 주석을 해제
                // lockAdpater.clearLock(hotelId);

                Boolean isSuccess = lockAdpater.holdLock(hotelId, firstUserId);
                // 단순이 찍어보는 것이 아님.
                // 결과가 맞는지 확인.
                Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);

                isSuccess = lockAdpater.holdLock(hotelId, secondUserId);
                // 결과가 맞는지 확인.
                Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);

                isSuccess = lockAdpater.holdLock(hotelId, thirdUserId);
                // 결과가 맞는지 확인.
                Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);

                // true가 하나고 false는 2개면 된다. (RedisController 에서 @Controller 주석처리하고 진행해야 에러가 안남)
            }
        }
        ```

        실행을 해보면 첫 번쨰 하나만 true가 나오고 나머지는 false
        이 형태로 만들게 되면 첫 번째만 true가 나오게 된다.
        실제 로드밸런서처럼 동작하려면 Thread를 이용해서 실행해야 한다.

      - **스레드 생성 방법 3가지**
        - Thread 클래스를 상속
        - Runnable 인터페이스를 구현
        - Callable 인터페이스를 구현 - 리턴받는 것이 가능(Future<T>)
      - 스레드를 이용해서 Redis에 접근
        이 방법은 안됨 - 레디스에 접속할 수 없다는 에러
        레디스는 싱글 스레드 기반이어서 동시에 접근하려고 하면 에러

        ```java

            @Test
            @DisplayName("분산 락을 테스트합니다.")
            public void testLock() {
                final String hotelId = "마라무쌍";

                // clearLock은 처음 수행할 떄는 주석처리하고
                // 두 번쨰 테스트 할 때는 지우고 확인해야 하므로 주석을 해제
                // lockAdpater.clearLock(hotelId);

                new Thread() {
                    public void run() {
                        Boolean isSuccess = lockAdpater.holdLock(hotelId, firstUserId);
                        // 결과가 맞는지 확인.
                        // Assertions.assertTrue(isSuccess);
                        System.out.println(isSuccess);
                    }
                }.start();

                new Thread() {
                    public void run() {
                        Boolean isSuccess = lockAdpater.holdLock(hotelId, secondUserId);
                        // 결과가 맞는지 확인.
                        // Assertions.assertTrue(isSuccess);
                        System.out.println(isSuccess);
                    }
                }.start();

                new Thread() {
                    public void run() {
                        Boolean isSuccess = lockAdpater.holdLock(hotelId, thirdUserId);
                        // 결과가 맞는지 확인.
                        // Assertions.assertTrue(isSuccess);
                        System.out.println(isSuccess);
                    }
                }.start();

                // true가 하나고 false는 2개면 된다. (RedisController 에서 @Controller 주석처리하고 진행해야 에러가 안남)
            }
        }
        ```

        따라서 여러 개의 스레드를 사용하고자 하면 CycliBarrier를 이용해서 생성
        하나의 스레드가 동작 중이면 다른 스레드는 대기하라고 해야 한다.

   5. 순위

      - Sorted Set(ZSet)
        Key와 Value 그리고 Value와 연관된 Score를 같이 저장해서 Score를 기준으로 정렬을 수행
        offset과 count를 제공
        실시간 인기 검색어나 게임의 사용자 랭킹 같은 기능에 활용
        RedisTemplate의 opsForZSet 이라는 메서드를 이용해서 ZSetOperations 라는 타입을 만들어서 생성
        Value와 Score라는 값을 사용하기 위해서 TypedTuple이라는 클래스를 사용
      - BiddingAdapter 클래스 - service.BiddingAdapter

        ```java
        package com.gyumin.redis0321.service;

        import lombok.RequiredArgsConstructor;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.stereotype.Component;

        import java.util.List;
        import java.util.stream.Collectors;

        @Component
        @RequiredArgsConstructor
        public class BiddingAdapter {
            private final RedisTemplate<String, Long> redisTemplate;

            public Boolean createBidding(Long hotelId, Long userId, Double amount) {
                String key = hotelId + "";
                return redisTemplate.opsForZSet().add(key, userId, amount);
            }

            public List<Long> getTopBidders(Long hotelId, Integer fetchCount) {
                String key = hotelId + "";
                return redisTemplate
                        .opsForZSet()
                        .reverseRangeByScore(key, 0D, Double.MAX_VALUE, 0, fetchCount)
                        .stream()
                        .collect(Collectors.toList());
            }

            public Double getBidAmount(Long hotelId, Long userId) {
                String key = hotelId + "";
                return redisTemplate.opsForZSet().score(key, userId);
            }

            public void clear(Long hotelId) {
                String key = hotelId + "";
                redisTemplate.delete(key);
            }
        }
        ```

      - 테스트

        ```java
        package com.gyumin.redis0321;

        import com.gyumin.redis0321.service.BiddingAdapter;
        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;

        import java.util.List;

        @SpringBootTest
        public class BiddingAdpaterTests {
            private final Long firstUserId = 1L;
            private final Long secondUserId = 2L;
            private final Long thirdUserId = 3L;
            private final Long fourthUserId = 4L;
            private final Long fifthUserId = 5L;

            @Autowired
            private BiddingAdapter biddingAdapter;

            @Test
            public void simulate() {
                // biddingAdapter.clear(1000L);

                biddingAdapter.createBidding(1000L, firstUserId, 100d);
                biddingAdapter.createBidding(1000L, secondUserId, 110d);
                biddingAdapter.createBidding(1000L, thirdUserId, 120d);
                biddingAdapter.createBidding(1000L, fourthUserId, 130d);
                biddingAdapter.createBidding(1000L, fifthUserId, 140d);

                biddingAdapter.createBidding(1000L, firstUserId, 150d);
                biddingAdapter.createBidding(1000L, thirdUserId, 190d);

                List<Long> topBidders = biddingAdapter.getTopBidders(1000L, 3);

                // 보통은 이렇게 결과를 확인하지만 실제 테스트 기법에서는 결과가 예상한 것과 일치하는지 확인한다.
                System.out.println(topBidders);

                Assertions.assertEquals(thirdUserId, topBidders.get(0));
                Assertions.assertEquals(firstUserId, topBidders.get(1));
                Assertions.assertEquals(fifthUserId, topBidders.get(2));
            }
        }
        ```

5. **Redis를 이용한 구독과 게시**

   1. 구독과 게시에 필요한 클래스
      - 메시지
      - 메시지를 생성하는 게시자(Publisher)
      - 메시지를 수신하는 구독자(Subscriber)
      - 이들 사이에서 메시지를 큐잉하고 구독자가 메시지를 수신할 수 있는 토픽
   2. 작업

      - 메시지 클래스를 생성 - service.EventMessage

        ```java
        package com.gyumin.redis0321.service;

        import com.fasterxml.jackson.annotation.JsonCreator;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import lombok.Getter;
        import lombok.ToString;

        @Getter
        @ToString
        public class EventMessage {
            private Long timestamp;
            private String message;

            // 게시지가 메시지를 만들 때 사용할 메서드
            public EventMessage(String message) {
                this.timestamp = System.currentTimeMillis();
                this.message = message;
            }

            // 구독자가 읽을 메서드
            @JsonCreator
            public EventMessage(@JsonProperty("timestamp") Long timestamp, @JsonProperty("message") String message) {
                this.timestamp = timestamp;
                this.message = message;
            }
        }
        ```

      - 설정 클래스 생성 - config.EventConfig

        ```java
        package com.gyumin.redis0321.config;

        import com.gyumin.redis0321.service.EventMessage;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.redis.connection.RedisConnectionFactory;
        import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
        import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.listener.ChannelTopic;
        import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
        import org.springframework.data.redis.serializer.StringRedisSerializer;

        @Configuration
        public class EventConfig {
            @Bean
            public RedisConnectionFactory eventRedisConnectionFactory() {
                RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
                return new LettuceConnectionFactory(configuration);
            }

            @Bean
            public ChannelTopic eventTopic() {
                return new ChannelTopic("Dummy Topic");
            }

            @Bean
            public RedisTemplate<String, String> eventRedisTemplate() {
                RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
                redisTemplate.setConnectionFactory(eventRedisConnectionFactory());
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<EventMessage>(EventMessage.class));
                return redisTemplate;
            }
        }
        ```

      - 게시자 클래스 - service.EventPublisher

        ```java
        package com.gyumin.redis0321.service;

        import lombok.extern.log4j.Log4j2;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.listener.ChannelTopic;
        import org.springframework.stereotype.Component;

        @Component
        @Log4j2
        public class EventPublisher {
            private final RedisTemplate<String, String> redisTemplate;
            private final ChannelTopic topic;

            public EventPublisher(RedisTemplate<String, String> redisTemplate, ChannelTopic eventTopic) {
                this.redisTemplate = redisTemplate;
                this.topic = eventTopic;
            }

            // 메시지를 전송하는 메서드
            public void sendMessage(EventMessage eventMessage) {
                redisTemplate.convertAndSend(topic.getTopic(), eventMessage);
            }
        }
        ```

      - 구독자 클래스 - service.EventListener

        ```java
        package com.gyumin.redis0321.service;

        import org.springframework.data.redis.connection.Message;
        import org.springframework.data.redis.connection.MessageListener;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.serializer.RedisSerializer;

        // 구독자 클래스
        public class EventListener implements MessageListener {
            private RedisTemplate<String, String> redisTemplate;
            private RedisSerializer<EventMessage> valueSerializer;

            public EventListener(RedisTemplate<String, String> redisTemplate) {
                this.redisTemplate = redisTemplate;
                this.valueSerializer = (RedisSerializer<EventMessage>) redisTemplate.getValueSerializer();
            }

            // 게시자가 게시했을 때 호출될 메서드
            @Override
            public void onMessage(Message message, byte[] pattern) {
                EventMessage eventMessage = valueSerializer.deserialize(message.getBody());
                System.out.println("시간:" + eventMessage.getTimestamp());
                System.out.println(eventMessage.toString());
            }
        }
        ```

      - RedisConfig 파일에 구독자와 게시자를 연결하는 코드를 작성

        ```java
        package com.gyumin.redis0321.config;

        import com.gyumin.redis0321.service.EventListener;
        import com.gyumin.redis0321.service.EventMessage;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.data.redis.connection.MessageListener;
        import org.springframework.data.redis.connection.RedisConnectionFactory;
        import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
        import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
        import org.springframework.data.redis.core.RedisTemplate;
        import org.springframework.data.redis.listener.ChannelTopic;
        import org.springframework.data.redis.listener.RedisMessageListenerContainer;
        import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
        import org.springframework.data.redis.serializer.StringRedisSerializer;

        @Configuration
        public class EventConfig {
            ...

            // 구독자 생성 메서드
            @Bean
            public MessageListener eventListener() {
                return new EventListener(eventRedisTemplate());
            }

            // 구독자와 게시자 연결
            @Bean
            public RedisMessageListenerContainer redisMessageListenerContainer() {
                RedisMessageListenerContainer container = new RedisMessageListenerContainer();
                container.setConnectionFactory(eventRedisConnectionFactory());
                container.addMessageListener(eventListener(), eventTopic());
                return container;
            }
        }
        ```
