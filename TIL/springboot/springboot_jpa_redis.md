# [Spring Boot] JPA 복습 + Redis

1. **Entity 생성**

   개발을 할 때 자동 생성 옵션을 사용하지만 운영을 하는 경우에는 대부분 테이블을 만들고 연결만 해서 사용

   - 설정
     - 테이블 자동 생성 옵션
       `spring.jpa.hibernate.ddl-auto=update`
       운영 환경에서는 update 대신에 validate를 사용해서 제대로 만들어졌는지 확인
     - SQL 출력
       `spring.jpa.properties.hibrenate.show_sql=true`
     - 포맷 설정
       `spring.jpa.properties.hibernate.format_sql=true`
     - 바인딩 값 확인
       `logging.level.org.hibernate.type.descriptor.sql=true`

2. **Entity를 사용하기 위한 Repository**

   ```java
   public interface 이름 extends JPARepository<Entity 클래스이름, @Id가 적용된 컬럼의 자료형> {}
   ```

3. **Entity 작업**
   - JPARepository에서 제공하는 메서드 - `기본적인 CRUD`
     데이터 삽입(Entity), 삭제(Entity와 Id), 수정(Entity), 조회(전체, Id를 이용한 조회 - 페이징과 정렬)
     삽입과 수정은 upsert 형태로 동작을 하는데 Id의 값이 존재하지 않으면 삽입이고 존재하면 수정
   - 이름을 이용한 메서드 선언과 활용 - `Query Method`
     삭제나 조회의 경우는 컬럼 이름을 이용해서 삭제나 조회가 가능
   - jpql을 이용한 조회 - `join이나 group by 또는 집계가 나오는 경우, 조건을 이용한 검색`
     정적인 문자열을 이용하는 방식 - 메서드 위에 JPQL을 문자열로 작성
   - Querydsl을 이용(Spring Boot 2.5와 2.6 그리고 3.0 이상의 설정이 다름) - `동적 쿼리(예를 들어 where 절에서 컬럼의 이름이 변경되는 경우)를 이용하는 방식`
4. **데이터베이스를 변경해서 사용하는 경우 주의할 점**
   - ID를 자동 생성하는 옵션을 사용하는 경우 MySQL이나 MariaDB와 Oracle이 다르기 때문에 생성 전략에 따라 어느 한쪽에서 동작하지 않을 수 있다.
   - group by의 경우 mariaDB는 구문에 제약(group by에 작성하지 않은 컬럼도 select에서 사용이 가능 - 첫 번째 데이터를 리턴)이 없다.
     MySQL은 8.0 부터는 설정을 변경하거나 select 절에 group by에 사용한 컬럼이나 집계함수를 이용한 것만 조회 가능
   - Oracle은 group by 절에서 사용한 컬럼이나 집계 함수를 이용하는 경우만 아니면 select 절에 기재할 수 없다.
   - 회원 서비스는 여러 곳에서 사용하기 때문에 실제로 별도의 API로 만드는 경우가 많고 별도의 테이블로 만드는 경우가 많아서 foreign key를 이용한 조회를 할 수 없을 가능성이 높음.
     Member 테이블의 정보를 이용해서 다른 API 서버나 데이터베이스에 데이터를 요청하는 방식으로 구현되는 경우가 많다.
     jdk 1.8의 streamAPI를 학습해야 한다.
5. **Redis**

   1. 데이터 저장 위치에 따른 데이터베이스 분류
      - DISK 기반
      - In Memory 기반
        메모리에 데이터를 저장해 두었다가 동일한 요청이 있을 떄 메모리에 저장된 데이터를 빠르게 돌려주는 방식의 데이터베이스로 고속으로 컨텐츠를 제공하는 장점이 있고 웹 서버의 부담을 줄일 수 있는 데이터베이스
   2. redis
      - 오픈 소스인 메모리 키 값 데이터 구조 저장소
      - 주 사용 용도는 캐싱, 세션 관리, pub/sub 및 순위표 등
      - C 언어로 만들어져 있으며 많은 언어를 지원
      - 리눅스나 Mac에 설치해서 사용하는 것이 가능하고 AWS에서는 Amazon ElasticCache라는 최적화된 완전 관리형 데이터베이스 서비스를 제공하고 있고 EC2 인스턴스에 직접 설치해서 사용 가능
      - 성능이 우수 - 작업 수행에 걸리는 시간은 대부분 1ms 이내
      - 다양한 데이터 타입을 지원
      - 메시지를 채널에 게시해서 채널에서 구독자에게 전달이 가능 - 채팅이나 메시징 시스템에 활용 가능
      - 마스터-슬레이브 아키텍쳐를 이용해서 비동기식 복제를 지원
   3. 사용 사례
      - 캐싱
      - 세션 관리
      - 실시간 순위
        Redis에는 Sorted Set 이라는 데이터 구조를 지원하기 때문에 쉽게 순위표를 작성
      - 속도 제한
        이벤트 속도를 측정하고 필요한 경우 제한할 수 있음, Redis 카운터를 이용해서 특정 기간 동안 엑세스 횟수를 세고 한도를 초과하는 경우 제한을 할 수 있음, 특정 그룹의 게시물 수를 제한하고 스패머의 영향을 억제하는데 샤용 가능
   4. 설치
      - docker에 설치
        ```bash
        docker run --name redis -d -p 6379:6379 redis
        ```
      - docker로 설치한 redis에 접속
        ```bash
        docker run -it --link redis:redis --rm redis redis-cli -h redis -p 6379
        ```
   5. CURD

      - Strings: key와 value 모두 String
        - set (key) (value)
        - get (key)
        - del (key)
        - mset (key1) (value1) (key2) (value2) …
        - mget (key1) (key2) …
        - keys (패턴)
          key의 조회
        - scan 시작인덱스 패턴 개수
          개수를 설정해서 조회하는데 결과의 첫 번째는 다음 페이지 존재 여부(없으면 0)이고 기본적으로 10개씩 조회
      - List: 하나의 key에 여러 개의 데이터를 저장하는 자료 구조 - deque
        - lpush
          lpush key 데이터나열
        - lrange
          lrange key 시작인덱스-종료인덱스(전체를 조회할 때는 `0 -1`)
        - rpush
          rpush key 데이터나열
        - lpop
          lpop key
        - rpop
          rpop key
        - llen
          llen key
        - ltrim
          ltrim key 시작인덱스 종료인덱스
          시작인덱스부터 종료인덱스까지 삭제
          주의할 점은 없는 인덱스 사용하면 모두 제거한다는 것.
        - set
          하나의 key에 순서 상관없이 여러 개의 value를 저장하는데 중복된 데이터가 있으면 저장하지 않음
          sadd key 데이터나열: 데이터 추가
          smembers key: 모든 데이터 조회
          sismember key value: 데이터 존재 여부 확인
          srem key value: 데이터 삭제
          spop key count: 무작위로 count 만큼 pop
          srandmember key count: 무작위로 count만큼 리턴
        - sorted set
          piority queue와 비슷하게 score와 value를 가지는 구조로 score로 정렬, score가 같으면 value로 sort
          zadd key score1 member1 score2 member2 …
          zrange key 시작인덱스 종료인덱스 score
        - hash
          하나의 key에 filed와 value 여러 개를 저장할 수 있는 자료구조
      - application.yaml 파일에 접속 정보 설정
        ```yaml
        spring:
        	reids:
        		lettuce:
        			pool:
        				max-acitve: 5 # pool에 할당될 수 있는 커넥션 최대수
        				max-idle: 5 # pool의 'idle' 커넥션 최대 수
        				min-idle: 2
        		host: localhost
        		port: 6379
        ```
      - Redis 설정 클래스 생성 및 작성 - config.RedisConfig
        이거까지 해야 쓸 수 있음

        ```java
        @Configuration
        @EnableRedisRepositories
        @RequiredArgsConstructor
        public class RedisConfig {

            private final RedisProperties redisProperties;

            @Bean
            public RedisConnectionFactory redisConnectionFactory() {
                return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
            }

            @Bean
            public RedisTemplate<String, Object> redisTemplate() {
                RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                redisTemplate.setConnectionFactory(redisConnectionFactory());
                redisTemplate.setKeySerializer(new StringRedisSerializer());
                redisTemplate.setValueSerializer(new StringRedisSerializer());
                return redisTemplate;
            }
        }
        ```

      - Controller 클래스를 만들고 테스트

        ```java
        @RestController
        @RequiredArgsConstructor
        public class RedisController {
            private final RedisTemplate<String, String> redisTemplate;

            @PostMapping("/redisTest")
            public ResponseEntity<?> addRedisKey() {
                ValueOperations<String, String> vop = redisTemplate.opsForValue();

        				// 토큰을 저장할 때는 key로 유저를 식별할 수 있는 문자열을 만들고
        				// token의 값은 jwt 형식에 맞춰서 생성해주는 것이 좋다.
        				// key는 UUID + userid
                vop.set("yellow", "banana");
                vop.set("red", "apple");
                vop.set("green", "watermelon");
                return new ResponseEntity<>(HttpStatus.CREATED);
            }

            @GetMapping("/redisTest/{key}")
            public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        				// opsForValue - Strings
        				// opsForList, opsForSet, opsForZSet, opsForHash - 자료구조들!!
                ValueOperations<String, String> vop = redisTemplate.opsForValue();
                String value = vop.get(key);
                return new ResponseEntity<>(value, HttpStatus.OK);
            }
        }
        ```

      - Spring Boot 에서는 Redis를 JPA처럼 사용하도록 `CrudRepository` 인터페이스를 제공 - Hash를 이용한다. (DTO로 간주?)
      - Redis를 위한 Repository 인터페이스를 생성
      - 객체 저장 - DTO 클래스 생성 - dto.Member

        ```java
        @Getter
        @Setter
        // RedisHash 어노테이션을 통해 설정한 값을 Redis의 key 값 prefix르ㅗ 사용
        @RedisHash("member")
        public class Member {
        		// @Id는 JPA와 동일한 역할을 수행하는데 member:{id}의 위치에 자동으로 genrate됨.
            @Id
            private String id;
            private String name;
            private int age;

            public Member(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
        ```

      - Repository 인터페이스 생성 - persistence.MemberRedisRepository
        ```java
        public interface MemberRedisRepository extends CrudRepository<Member, String> {
        }
        ```
      - Service 클래스 생성 - Service.MemberService

        ```java
        @RequiredArgsConstructor
        @Service
        public class MemberService {

            private final MemberRedisRepository memberRedisRepository;

            public void addMember() {
                Member member = new Member("jan", 99);
                memberRedisRepository.save(member);
            }
        }
        ```

      - Controller 클래스에 처리 코드 추가

        ```java
        @RestController
        @RequiredArgsConstructor
        public class RedisController {
        		...
        		private final MemberService memberService;

        		@GetMapping("/addmember")
        		public ResponseEntity<?> addMember() {
        		    memberService.addMember();
                return new ResponseEntity<>("success", HttpStatus.OK);
        		}
        }
        ```
