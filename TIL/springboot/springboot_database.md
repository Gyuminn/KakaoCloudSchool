# [Spring Boot] 데이터 베이스 연동

1. **Test**
   1. 테스트 코드 작성 이유
      - 개발 과정에서 문제를 미리 발견할 수 있음.
      - 리팩토링 리스크가 줄어듬.
      - 하나의 명세 문서가 됨.
      - 불필요한 내용이 추가되는 것을 방지
   2. 테스트 종류
      - 단위 테스트(Unit Test)
        메서드나 클래스 테스트 - Repository, Service, Controller를 테스트
      - 통합 테스트(Integration Tests)
        하나의 서비스(모듈 결합)를 테스트 - 3개 모듈을 합쳐서 테스트
      - 시스템 테스트(System Tests)
      - 인수 테스트(Acceptance Tests)
        최근에는 클라우드 환경에서 개발을 하게 되면 이 과정은 생략되기도 함.
   3. 코드 작성 방법 - Given When Then 패턴
      - 단계를 설정해서 코드를 작성
      - 단계
        - Given
          테스트를 하기 전에 필요한 환경을 설정
          변수 설정이나 Mock 객체(가짜 객체) 생성
        - When
          테스트의 목적을 보여주는 단계로 테스트 코드를 작성하고 결과값을 가져오는 과정
        - Then
          결과 검증
   4. 좋은 테스트를 위한 5가지 속성
      - Fast
      - Elated(고립, 독립) - 외부 요인으로부터 독립적
      - Repeatable(반복)
      - Self Validating(자가 검증)
      - Timely(적시에) - 실제 구현되기 전에 테스트
   5. Spring Boot에서 Test
      - spring-boot-starter-test의 의존성을 설정하면 됨
      - JUnit5와 Mock이 포함됨.
   6. JUnit5에서 어노테이션
      - @Test
        테스트를 위한 메서드
      - @BeforeAll
        테스트를 시작하기 전에 한 번 호출
      - @BeforeEach
        모든 테스트마다 시작하기 전에 호출
      - @AfterAll
        테스트를 수행하고 난 후 한 번 호출
      - @AfterEach
        모든 테스트마다 수행하고 난 후 호출
   7. 가짜 객체
      - 외부로 주입받아야하는 경우 실제 객체를 만들어서 테스트를 해도 되지만 있다고 가정하고 테스트 가능
        @MockBean 클래스 변수이름;
2. **연동 방법**
   - Java JDBC 코드 이용 방법
   - Framework 이용 방법
     - Spring JDBC 이용
     - **JPA(ORM) 이용(사용 빈도가 높음)**
     - SQL Mapper Framework(MyBatis) 이용
3. **ORM(Object Relational Mapping)**

   1. ORM
      - 객체 지향 패러다임을 관계형 데이터베이스에 보존하는 기술
      - 객체와 관계형 데이터베이스의 테이블을 매핑해서 사용
      - 관계형 데이터베이스에서 테이블을 설계하는 것과 Class를 만드는 것을 템플릿을 만든다는 점에서 유사
      - 인스턴스와 Row가 유사
   2. 장점
      - 특정 관계형 데이터베이스에 종속되지 않음.
      - 객체 지향 프로그래밍
      - 생산성 향상
   3. 단점
      - 쿼리 처리가 복잡
      - 성능 저하 위험
      - 학습 시간
   4. JPA(Java Persistence API)
      - Java ORM 기술에 대한 표준 API
      - JPA는 인터페이스고 구현체로는 Hibernate, EclipseLink, OpenJpa 등이 있음
      - 대부분의 경우 구현체는 Hibernate를 사용
        데이터베이스 ↔ JDBC ↔ Hibernate ↔ JPA
      - Persistence Context
        - 애플리케이션과 데이터베이스 사이의 중재자 역할
          Entity ↔ Persistence Context ↔ DataBase
        - 직접 연결하지 않고 Persistence Context를 두는 이유는 중간 매개체를 두면 버퍼링이나 캐싱 등을 활용할 수 있음.
        - 쓰기 지연을 수행: 트랜잭션 처리를 commit하기 전까지는 데이터에비으세 반영하지 않음.
   5. Entity 클래스를 만드는 방법

      **VO 클래스의 상단에 @Entity를 추가**

4. **Entity Class와 JpaRepository**
   1. 개발에 필요한 코드
      - Entity Class와 Entity Object
      - Entity를 처리하는 Repository
        인터페이스를 설계하는데 JPA에서 제공하는 인터페이스를 상속만 하면 Spring Boot 내부에서 자동으로 객체를 생성하고 실행한는 구조
        어느 정도의 메서드는 이미 구현되어 있고 메서드를 선언만 해도 구현해주기도 하기 떄문에 단순 CRUD나 페이지 처리 등을 개발할 때 직접 코드를 작성하지 않아도 된다.
   2. Entity 관련 Annotation
      - `@Entity`
        Entity 클래스 생성
      - `@Table`
        Entity와 매핑할 테이블을 설정하는데 생략하면 클래스 이름과 동일한 이름의 테이블과 매핑되는데 mysql이나 mariadb는 첫글자를 제외하고 대문자가 있으면 \_를 추가하고 소문자로 변경해서 테이블 이름을 생성한다.
        - Member
          oracle-Member, mraidb-member
        - TblMember
          oracle-TblMember, mariadb-tbl_member
          - name 속성
            테이블 이름 설정
          - uniqueConstraints 속성
            여러 개의 컬럼이 합쳐져서 유일성을 갖는 경우 사용
            uniqueConstraints={@UniqueConstraint(columnNames={컬럼이름 나열})}
      - `@Id`
        컬럼 위에 기재해서 Primary Key를 설정, 필수
      - `@GeneratedValue`
        키 생성 전략을 기재하는데 @Id와 같이 사용
      - `@Column`
        컬럼 위에 기재해서 테이블의 열과 매핑
        - name
          컬럼 이름 설정, 생략하면 속성 이름과 동일한 컬럼으로 매핑
        - unique
        - nullable
        - insertable, updateable
        - precision
          숫자의 전체 자릿수
        - scale
          소숫점 자릿수
        - length
          문자열 길이
      - `@Lob`
        BLOB나 CLOB 타입
        lob는 바이트배열, 보통은 파일의 내용을 저장할 목적으로 사용
        파일을 저장할 때는 대부분의 경우 파일의 경로를 문자열로 저장하고 파일의 내용은 파일 스토리지(Amazon의 S3가 대표적)에 별도로 둔다.
      - `@Enumerated`
        enum 타입 - check 제약 조건
      - `@Transient`
        테이블에서 제외, 파생 속성 - 다른 속성들을 가지고 만들어내는 속성
      - `@Temporal`
        날짜 타입 매핑
      - `@CreatedDate`
        생성 날짜 자동 삽입
      - `@LastModifiedDate`
        수정 날짜 자동 삽입
      - `@Access`
        사용자가 값을 사용할 때 바인딩하는 방식
        - @Access(AccessType.FIELD)
          속성을 직접 사용
        - @Access(AccessType.PROPERTY)
          getter와 setter 사용
   3. 기본키 생성 전략
      - AUTO
        default로 JPA가 생성 전략을 선택
        MariaDB는 AutoIncrement, Oracle이면 Sequence
      - IDENTITY
        AutoIncrement - oracle에서는 이 전략을 사용하면 테이블 생성을 못함
      - SEQUENCE
        oracle의 sequence 이용
      - TABLE
        직접 생성 전략을 결정
      - 기본키를 복합키로 생성
        기본키를 사용할 속성들을 별도의 VO 클래스로 만들고 Serializable 인터페이스를 구현하고 기본 생성자와 모든 속성을 매개변수로 갖는 생성자를 만들고 Getter만 추가해서 생성
        Entity 클래스에 위 클래스의 속성을 추가하고 위에 `@Id`와 `@Embeddable`을 추가
5. **JPA를 활용하는 애플리케이션을 생성**

   1. 프로젝트 생성
      - 의존성
        Lombok, Spring Web, Spring Data JPA, 사용하고자 하는 데이터베이스
   2. 실행하면 에러

      JPA를 사용하는 프로젝트는 실행하면 데이터베이스에 바로 접속을 시도하는데 접속 정보가 없어서 에러

   3. application.properties를 yaml로 변경

      ```java
      server:
        port: 80

      #데이터베이스 접속 정보
      spring:
        datasource:
          driver-class-name: org.mariadb.jdbc.Driver
          url: jdbc:mariadb://localhost:3306/gyumin
          username: root
          password: root
      ```

   4. 다시 실행

      HikariDataSource 라이브러리를 이용해서 데이터베이스 접속을 하는 것을 로그에서 확인 가능

   5. Entity 클래스 생성 - domain.Memo

      ```java
      package com.kakao.springbootjpa0110.domain;

      import jakarta.persistence.*;
      import lombok.AllArgsConstructor;
      import lombok.Builder;
      import lombok.Data;
      import lombok.NoArgsConstructor;

      @Entity // Entity 클래스 생성
      @Table(name="tbl_memo") // 테이블 이름 설정
      @Data
      @Builder
      @NoArgsConstructor
      @AllArgsConstructor
      public class Memo {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          private Long mno;

          @Column(length = 200, nullable = false)
          private String memoText;
      }
      ```

   6. application.yml에 jpa 설정 추가

      #Entity를 가지고 테이블을 만들 것인지 여부를 설정

      - create
        시작될 때 무조건 새로 생성
      - create-drop,
        시작할 때 생성하고 종료할 때 삭제
      - update
        변경되면 적용
      - validate
        매핑만 확인
      - none
        생성하지 않음

      ```yaml
      server:
        port: 80

      #데이터베이스 접속 정보
      spring:
        datasource:
          driver-class-name: org.mariadb.jdbc.Driver
          url: jdbc:mariadb://localhost:3306/gyumin
          username: root
          password: root

        jpa:
          hibernate:
            ddl-auto: update
          #show_sql은 sql 구문을 출력하는 것이고 format_sql은 포맷을 설정해서 출력
          properties:
            hibernate:
              format-sql: true
              show_sql: true

      #?에 값을 바인딩할 때 그 값을 확인
      logging:
        level:
          org.hibernate.type.descriptor.sql: trace
      ```

   7. 실행하고 SQL 구문과 데이터베이스에 테이블이 생성되었는지 확인
   8. Entity에 CRUD 작업을 수행할 수 있는 Repository 생성 - persistence.MemoRepository

      ```java
      public interface MemoRepository extends JpaRepository<Memo, Long> {
      }
      ```

      - 2개의 템플릿 자료형을 설정해야 하는데 첫 번째는 사용할 Entity 클래스이고 두 번째는 Entity 클래스의 `@Id` 속성의 자료형이다.
      - 자동 생성되는 메서드
        - insert, update ⇒ save(Entity 객체)
        - select ⇒ findById(기본키값), findAll()
        - count ⇒ count()
        - delete ⇒ deleteById(기본키값), delete(Entity 객체)

   9. Repository의 기본 메서드 테스트

      - test 디렉토리에 RepositoryTest 클래스를 작성하고 실행

        ```java
        package com.kakao.springbootjpa0110;

        import com.kakao.springbootjpa0110.domain.Memo;
        import com.kakao.springbootjpa0110.persistence.MemoRepository;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;

        import java.util.stream.IntStream;

        @SpringBootTest
        public class RepositoryTest {
            @Autowired
            MemoRepository memoRepository;

            // 삽입 테스트
            @Test
            public void testInsert() {
                // 데이터 100개 삽입
                IntStream.rangeClosed(1, 100).forEach(i -> {
                    Memo memo = Memo.builder()
                            .memoText("Sample..." + i)
                            .build();
                    memoRepository.save(memo);
                });
            }
        }
        ```

      - 테이블의 전체 데이터를 가져오는 메서드를 테스트 - 테스트 클래스에 메서드 생성 후 테스트
        ```java
        ...
        @Test
            public void testAll() {
                List<Memo> list = memoRepository.findAll();
                for(Memo memo: list) {
                    System.out.println(memo);
                }
            }
        ```
      - 하나의 키를 가지고 데이터를 찾아오는 메서드를 테스트

        ```java
        ...
        @Test
            public void getById() {
                // 기본키를 가지고 조회하면 없거나 1개의 데이터 리턴
                // 그럴 때는 Optional을 사용해야 한다.
                Optional<Memo> result = memoRepository.findById(1000L);

                if(result.isPresent()) {
                    System.out.println(result.get());
                } else {
                    System.out.println("데이터가 존재하지 않습니다.");
                }
            }
        ```

      - 수정하는 메서드를 테스트
        ```java
        ...
        @Test
            public void updateTest() {
                // 기본 값이 존재하면 수정이지만 존재하지 않으면 삽입이다.
                Memo memo = Memo.builder()
                        .mno(1000L)
                        .memoText("데이터 수정")
                        .build();
                memoRepository.save(memo);
            }
        ```
      - 삭제하는 메서드를 테스트

        ```java
        @Test
            public void deleteTest() {
                // 기본키를 가지고 삭제
                memoRepository.deleteById(100L);
                // Entity를 이용해서 삭제
                memoRepository.delete(Memo.builder().mno(99L).build());

                // 없는 데이터를 삭제하고자 하면 에러
                // 삭제를 할 때는 존재 여부를 확인해야 한다.
                memoRepository.deleteById(1000L);
            }
        ```

   10. 페이징 & 정렬

       - 관계형 데이터베이스에서 paging(페이지 단위로 데이터를 가져오는 것 - Top N)을 처리하기 위해서는 데이터베이스마다 다른 방식으로 구현해야 한다.
         Oracle의 경우는 Inline View나 FETCH & OFFSET을 이용하고 MySQL이나 MariaDB는 limit을 이용
       - JPA에는 연결한 데이터베이스에 따라 SQL을 자동으로 변환한다.
       - 페이징과 정렬은 findAll 메서드를 이용
         Pageable이라는 인터페이스의 객체를 대입하면 Paging 처리를 해서 Page<T> 타입으로 리턴
       - Pageable 인터페이스 생성
         - `PageRequest.of(int page, int size)`
           0부터 시작하는 페이지 번호와 페이지당 데이터 개수를 설정
         - `PageRequest.of(int page, int size Sort.Direction, String …props)`
           정렬 방향과 정렬 속성을 추가
         - `PageRequest.of(int page, int size, Sort sort)`
           정렬 관련 정보를 Sort 객체로 생성해서 대입
       - 페이징만 수행해서 데이터를 조회

         ```java
         ...
         @Test
             public void testPaging() {
                 Pageable pageable = PageRequest.of(0, 10);
                 Page<Memo> result = memoRepository.findAll(pageable);
                 // 전체 페이지 개수
                 System.out.println(result.getTotalPages());

                 // 조회된 데이터 순회
                 for(Memo memo: result.getContent()) {
                     System.out.println(memo);
                 }
             }
         ```

       - 정렬을 수행해서 페이징
         ```java
         ...
         @Test
             public void testSort() {
                 // mno의 내림차순
                 Sort sort = Sort.by("mno").descending();
                 Pageable pageable = PageRequest.of(0, 10, sort);
                 Page<Memo> result = memoRepository.findAll(pageable);
                 for(Memo memo : result.getContent()){
                     System.out.println(memo);
                 }
             }
         ```
         그렇지만 mno가 같다면!?
       - 정렬 조건 2개를 결합해서 페이징 - 3개 이상은 동일

         ```java
         ...
         @Test
             public void testSortConcate() {
                 Sort sort1 = Sort.by("mno").descending();
                 Sort sort2 = Sort.by("memoText").ascending();
                 // 2개 결합 - sort1의 값이 같으면 sort2로 정렬
                 Sort sortAll = sort1.and(sort2);

                 Pageable pageable = PageRequest.of(0, 10, sortAll);
                 Page<Memo> result = memoRepository.findAll(pageable);
                 for(Memo memo: result.getContent()) {
                     System.out.println(memo);
                 }
             }
         ```

   11. query methods

       - 인터페이스에 메서드 이름만 생성하면 select와 delete 구문을 만들어주는 것
       - https://docs.spring.io/spring-data/jpa/docs/current/refrence/html/#jpa.query-methods
       - 이름 자체가 query
         - `find + (Entity 이름) + By + 컬럼이름`
           Item Entity에서 name을 가지고 조회하는 메서드: findItemByName(String name)
         - Or나 And 사용 가능
       - 매개변수는 자신이 설정하는 Select 형태에 따라서 설정을 하고 Pageable과 Sort를 추가할 수 있다.
       - Pageable이 없으면 리턴 타입은 List이고 존재하면 Page이다.
       - mno의 값이 a 부터 b 사이인 데이터를 조회하는 메서드를 MemoRepository에 작성

         ```java
         public interface MemoRepository extends JpaRepository<Memo, Long> {
             // mno의 값이 from부터 to 사이인 데이터 조회하는 메서드
             List<Memo> findByMnoBetween(Long from, Long to);

             // mno의 값이 from부터 to 사이인 데이터를 Mno의 내림차순 정려해서 조회하는 메서드
             List<Memo> finddByMnoBetweenOrderByMnoDesc(Long from, Long to);
         }
         ```

       - Test 클래스에 만들어서 확인

         ```java
         ...
         @Test
             public void queryMethod1() {
                 List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(30L, 40L);

                 for(Memo memo: list) {
                     System.out.println(memo);
                 }
             }
         ```

       - 동일한 메서드에 Paging 적용하기 위한 메서드를 MemoRepository에 추가
         ```java
         ...
         // mno의 값이 from부터 to 사이인 데이터를 Mno의 내림차순 정려해서 조회하는 메서드
             Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
         }
         ```
       - Test 클래스에 만들어서 확인
         ```java
         @Test
             public void queryMethod2() {
                 Pageable pageable = PageRequest.of(1, 5);
                 Page<Memo> result = memoRepository.findByMnoBetween(0L, 50L, pageable);
                 for (Memo memo : result.getContent()) {
                     System.out.println(memo);
                 }
             }
         ```
       - 특정 Mno 보다 작은 값을 가진 속성 삭제
         ```java
         public interface MemoRepository extends JpaRepository<Memo, Long> {
         		...
             void deleteByMnoLessThan(Long num);
         }
         ```
       - 테스트 클래스 작성 - Transcational과 Commit 필요

         ```java
         ...
         @Test
             // 특정한 작업에서는 트랜잭션을 적용하지 않으면 예외가 발생
             @Transactional
             // 트랜잭션이 적용되면 자동 Commit이 되지 않으므로 Commit을 호출해야 실제 작업이 완성된다.
             @Commit
             public void deleteMnoTest() {
                 memoRepository.deleteByMnoLessThan(10L);

                 List<Memo> list = memoRepository.findAll();
                 for(Memo memo: list) {
                     System.out.println(memo);
                 }
             }
         ```

   12. SQL 실행 ⇒ `@Query`

       - JPQL이라고 해서 JPA에서 제공하는 쿼리 문법을 이용할 수 있고 Native SQL(데이터베이스에서 실제 사용하는 SQL)을 이용하는 것이 가능.
       - Select가 아닌 경우는 `@Modifying`과 같이 사용해야 한다.
       - JPQL은 SQL과 거의 유사하기 때문에 별도의 학습이 필요없이 사용이 가능
       - MemoRepository 인터페이스에 수정하는 메서드를 선언

         ```java
         		@Transactional
             @Modifying
             // Native SQL이 아니기 때문에 Table 이름을 적는 것이 아니고 Entity 클래스의 이름을 사용해야 한다.
             @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
             public int updateMemoText(@Param("mno") Long mno,
                                       @Param("memoText") String memoText);

             // Memo를 매개변수로 받아서 수정하는 메서드 - JPQL 사용
             @Transactional
             @Modifying
             // Native SQL이 아니기 때문에 Table 이름을 적는 것이 아니고 Entity 클래스의 이름을 사용해야 한다.
             @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
             public int updateMemoText(@Param("param") Memo memo);
         ```

       - Test 클래스에서 테스트하는 메서드를 만들어서 확인

         ```java
         ...
         @Test
             public void testUpdateQuery() {
                 System.out.println(memoRepository.updateMemoText(11L, "문자열 수정"));

                 System.out.println(memoRepository.updateMemoText(
                         Memo.builder()
                                 .mno(12L)
                                 .memoText("문자열 수정2").build()
                 ));
             }
         ```

       - JPQL을 이용해서 페이징 처리하는 메서드를 MemoRepository 인터페이스 생성
         ```java
         ...
         @Query("select m from Memo m where m.mno > :mno")
             Page<Memo> getListWithQuery(@Param("mno") Long mno, Pageable pageable);
         ```
       - Test
         ```java
         ...
         @Test
             public void testSelectQuery() {
                 // mno의 내림차순으로 정렬해서 0번 페이지 10개의 데이터를 가져오는 Pageable 객체
                 Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
                 Page<Memo> result = memoRepository.getListWithQuery(50L, pageable);
                 for(Memo memo: result.getContent()){
                     System.out.println(memo);
                 }
             }
         ```
       - Object[] 리턴하는 메서드
         - select 구문을 보낼 때 제공되는 기본 메서드나 Query 메서드를 이용하면 Entity 타입으로 리턴하여 원하는 데이터만 추출할 수 없다.
         - JOIN이나 GROUP BY 같은 SQL을 실행하면 이에 맞는 Entity가 존재하지 않을 가능성이 높다. 이런 경우 원하는 내용만 추출하고자 할 때 Object[] 을 리턴하는 메서드를 사용할 수 있다.
       - mno와 memoText와 그리고 현재 시간(CURRENT_DATE)를 조회하고자 하는 경우에 적당한 Entity가 존재하지 않음. 그럴 땐 이렇게.
         ```java
         ...
         @Query("select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno")
             Page<Object[]> getObjectWithQuery(@Param("mno") Long mno, Pageable pageable);
         ```
       - Test
         ```java
         ...
         @Test
             public void testObjectQuery() {
                 // mno의 내림차순으로 정렬해서 0번 페이지의 10개의 데이터를 가져오는 Pageable 객체
                 Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
                 Page<Object[]> result = memoRepository.getObjectWithQuery(50L, pageable);
                 for (Object[] memo : result.getContent()) {
                     System.out.println(Arrays.toString(memo));
                 }
             }
         ```
       - nativeSQL 실행
         `@Query(value=”sql 작성”, nativeQuery=true)`

6. **Oracle로 변경**

   1. Doker에 오라클 설치 및 실행
      - 도커 이미지 검색
        `docker search 이름`
      - Oracle 11g 이미지 다운로드
        `docker pull jaspeen/oracle-xe-11g`
      - Oracle 이미지를 실행
        `docker run —name oracle11g -d -p 8080:8080 -p 1521:1521 jaspeen/oracle-xe-11g`
        oracle의 sid는 xe(혹은 orcl)
        관리자 계정은 system이고 비번은 oracle
   2. build.gradle에서 오라클의 의존성 확인

      `runtimeOnly "com.oracle.database.jdbc:ojdbc8"`

   3. application.yml 파일 수정

      ```java
      spring:
      	datasource:
      		driver-class-name: oracle.jdbc.driver.OracleDriver
      		url: jdbc:oracle:thin:@192.168.0.7:1521:xe
      		username: system
      		password: oracle
      ...
      ```

7. **Spring Boot에서 MyBatis 사용**

   1. build.gradle 파일에 MyBatis 의존성을 설정

      implementaion ‘org.mybatis.spring.boot:mybatis-spring-boot-starter:2.3.0’

   2. 테이블과 연동할 DTO 클래스 생성(Entitiy 클래스가 아님. 없음.) - dto.MemoDTO

      ```java
      @Data
      @Builder
      @NoArgsConstructor
      @AllArgsConstructor
      public class MemoDTO {
      	private Long mno;
      	private String memoText;
      }
      ```

   3. SQL을 실행할 인터페이스를 생성 - persistence.MemoMapper

      ```java
      public interface MemoMapper {
      	@Select("select * from tbl_memo")
      	public List<MemoDTO> listMemo();
      }
      ```

   4. MyBatis 설정을 위한 클래스를 생성 - config.MybatisConfig

      ```java
      @Configuration
      @EnableTransactionManagement
      @MapperScan(basePackages={"com.kakao.springbootjpa0110.persistence"}
      public class MyBatisConfig {
      	@Bean
      	public DataSource batisDataSource() {
      		HikariConfig hikariConfig = new HikariConfig();

      }
      ```
