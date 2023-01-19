# [Spring Boot] JPA 심화

1.  **JPA의 Query 처리**

    1. JPARepository가 기본적으로 제공하는 메서드를 사용

       save, delete, findAll, findById

    2. QueryMethod 이용
       - 이름이 Query인 메서드
       - 인터페이스를 메서드를 만들 때 규칙을 지켜주면 메서드의 구현을 자동으로 수행
    3. @Query를 이용해서 JPQL이나 SQL을 작성해서 실행
       - 쿼리를 문자열로 작성을 하기 때문에 컴파일 시점에 에러를 확인할 수 없음.
       - 동적인 쿼리를 만드는 것이 어려움 - 상황에 따라서 변하는 쿼리(예를 들어 게시판에서 게시글 + 제목만, 제목만, 글쓴이 등 상황에 따라 쿼리가 달라질 때 어려움)
    4. query dsl
       - JPQL을 코드로 작성할 수 있도록 도와주는 빌더 API
       - 쿼리를 자바 코드로 작성
       - 장점
         - 조건에 맞게 동적으로 쿼리를 생성할 수 있음.
         - 쿼리를 재사용할 수 있고 제약 조건 조립 및 가독성을 향상
         - 컴파일 시점에 오류를 잡아낼 수 있음
         - IDE의 자동 완성 기능을 사용할 수 있음
       - 단점
         Spring Boot 버전에 따라서 설정 방법이 다름.
         2.5 ↔ 2.6, 2.7 ↔ 3.0

2.  **MappedSuperclass**
    - 테이블로 생성되지 않는 Entity 클래스
    - 추상 클래스와 유사한데 여러 Entity가 공통으로 가져야 하는 속성을 정의하는 클래스
    - 등록 시간이나 수정 시간처럼 여러 Entity가 공통으로 갖는 속성을 정의
3.  **JPA Auditing**
    - Entity 객체에 어떤 변화가 생길 때 감지하는 리스너가 존재
    - `@EntityListeners(value={클래스이름.class})`을 추가하면 Entity에 변화가 생기면 클래스의 메서드가 동작
      보통은 클래스를 직접 만들지 않고 Spring JPA가 제공하는 `AuditingEntityListener.class`를 주로 설정
    - 이 기능을 사용하기 위해서는 SpringBootApplication 클래스의 상단에 `@EnableJpaAuditing`을 추가해야 함.
4.  **Entity와 DTO**
    - 두 가지 모두 속성들을 합쳐서 하나로 묶기 위해 만드는 클래스
    - **Repository에서는 Entity 객체를 이용하고 그 이외의 영역에서는 DTO를 사용한는 것을 권장**
    - 따로 만드는 이유는 사용자의 요청이나 응답과 Entity가 일치하지 않는 경우가 많고, Entity는 JPA가 관리하는 Context에 속하기 때문에 직접 관리하는 것은 일관성에 문제가 발생할 가능성이 있기 때문이다.
    - DTO는 용도 별로 생성하는 것을 기본으로 한다.
5.  **애플리케이션 개요**

    1.  요청

        - 목록 보기 - list: GET
        - 등록: register - GET, POST(목록 보기로 리다이렉트, 페이지가 있다면 1page)
        - 조회: read - GET
        - 수정: modify - GET, POST(조회로 리다이렉트)
        - 삭제: remove - POST(조회로 갈 수 없으니 목록 보기로 리다이렉트)

        (로그인의 경우도 리다이렉트를 해줘야 한다는 점.)

    2.  구조

        Controller(Rest Controller도 생성) ↔ Service(ServiceImpl) ↔ Repository

             DTO                                                                                               Entity

6.  **Spring Boot Project 생성**
    - 프로젝트 이름
      guestbook
    - 빌드 도구
      gradle-groovy
    - packaging
      jar
    - 버전
      2.7.7(3.x. 사용 가능)
    - 의존성
      lombok, spring web, thymeleaf, spring data jpa, 사용하곶하는 데이터베이스
7.  **환경 설정**

    - [application.properties](http://application.properties) 파일을 application.yml 파일로 변경하고 작성
      yml 파일을 작성할 때 주의할 점은 들여쓰기와 값을 설정할 때 공백하나 추가해야 하는 것이다.

      ```yaml
      #서버 포트 설정
      server:
        port: 80

      #데이터베이스 접속 정보
      spring:
        datasource:
          url: jdbc:mariadb://localhost:3306/gyumin
          driver-class-name: org.mariadb.jdbc.Driver
          username: root
          password: root
        jpa:
          hibernate:
            ddl-auto: update
          properties:
            hibernate:
              format_sql: true #hibernate query 포맷을 정리
              show_sql: true #hibernate query를 콘솔에 출력

        thymeleaf:
          cache: false #뷰를 캐싱하지 않도록 설정

      #sql을 작성할 때 ?로 작성한 것을 추적
      logging:
        level:
          org.hibernate.type.descriptor.sql: trace
      ```

    - 애플리케이션을 실행해서 오류가 있는지 확인하고 데이터베이스 연결 pool(미리 만들어두고 빌려서 사용할 수 있도록 해주는 것)이 만들어지는지 확인

8.  **화면 출력 준비**

    - bootstrap sidebar 압축 해제해서 static 디렉토리에 복사
    - templates 디렉토리에 layout 디렉토리를 만들고 기본 레이아웃 파일인 basic.html을 복사
    - templates 디렉토리에 view를 저장할 guestbook 디렉토리를 생성
    - 사용자의 요청을 받아서 뷰로 출력하도록 해주는 ViewController를 만들어서 기본 요청을 처리하는 메서드를 작성 - controller.ViewController
      ```java
      @Log4j2
      @Controller
      public class ViewController {
          @GetMapping({"/", "/guestbook/list"})
          public String list() {
              log.info("메인 화면....");
              return "/guestbook/list";
          }
      }
      ```
    - guestbook 디렉토리에 list.html 파일을 만들고 작성
      ```html
      <!DOCTYPE html>
      <html lang="en">
        <th:block th:replace="~{/layout/basic:: setContent(~{this::content})}">
          <th:block th:fragment="content">
            <h1>방명록</h1>
          </th:block>
        </th:block>
      </html>
      ```
    - 애플리케이션을 실행하고 브라우저에 [localhost](http://localhost) 입력
    - 실행 클래스에 JPA 감시를 위한 어노테이션을 추가

      ```java
      @SpringBootApplication
      // JPA의 변화를 감시하겠다는 어노테이션
      @EnableJpaAuditing
      public class Guestbookapp0111Application {

          public static void main(String[] args) {
              SpringApplication.run(Guestbookapp0111Application.class, args);
          }

      }
      ```

9.  **Entity 작업**

    1. 생성 날짜와 수정 날짜를 모든 Entity가 소유하도록 하고자 함.
       - 하나의 상위 Entity를 만들어서 상속을 하면 되는데 이 상위 Entity는 테이블로 만들어지면 안된다. 이럴 때는 `@MappedSuperclass`를 추가해주면 된다.
       - 생성 날짜는 속성 위에 `@CreatedDate`를 추가해주면 된다.
       - 수정 날짜는 속성 위에 `@LastModifiedDate`를 추가하면 된다.
    2. 공통된 속성을 가진 Entity 생성 - domain.BaseEntity

       ```java
       @MappedSuperclass
       // Entity 변화 감시
       @EntityListeners(value = {AuditingEntityListener.class})
       @Getter
       public class BaseEntity {
           @CreatedDate
           @Column(name = "regdate", updatable = false)
           private LocalDateTime regDate;

           @LastModifiedDate
           @Column(name = "moddate")
           private LocalDateTime modDate;
       }
       ```

    3. application에서 사용할 Entity 생성 - domain.GuestBook

       - 컬럼
         기본키로 사용할 번호 - 자동생성
         제목이 될 문자열 - 100자 정도에 필수 입력
         내용이 될 문자열 - 1500자 정도에 필수 입력
         작성자 - 50자 정도에 필수 입력
         작성 날짜
         수정 날짜

         ```java
         @Entity
         @Getter
         @Builder
         @AllArgsConstructor
         @NoArgsConstructor
         @ToString
         public class GuestBook extends BaseEntity {
             @Id
             @GeneratedValue(strategy = GenerationType.AUTO)
             private Long gno;

             @Column(length = 100, nullable = false)
             private String title;

             @Column(length = 1500, nullable = false)
             private String content;

             @Column(length = 50, nullable = false)
             private String writer;
         }
         ```

       - 실행을 하고 SQL 로그가 찍히는지 확인하고 데이터베이스에 접속해서 테이블을 확인
         테이블이 생성되는데 SQL이 출력되지 않으면 application.yml 파일의 설정을 확인
         테이블이 생성되지 않는 경우에 에러가 없다면 application.yml 파일의 ddl-auto 부분을 확인

         ```java
         Hibernate:

             create table guest_book (
                gno bigint not null,
                 moddate datetime(6),
                 regdate datetime(6),
                 content varchar(1500) not null,
                 title varchar(100) not null,
                 writer varchar(50) not null,
                 primary key (gno)
             ) engine=InnoDB
         Hibernate: create sequence hibernate_sequence start with 1 increment by 1
         ```

10. **GuestBook에 대한 데이터베이스 작업을 위한 Repository**

    1. GuestBook Entity에 대한 작업을 위한 인터페이스 생성 - persistence.GuestBookRepository

       ```java
       public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {
       }
       ```

    2. query dsl 사용을 위한 설정 - build.gradle

       - 2.x 버전 - 별도로 추가

         ```java
         buildscript {
             ext {
                 queryDslVersion = "5.0.0"
             }
         }

         plugins {
         		...
         		id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
         }

         ...
         dependencies {
         		// QueryDSL
             implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
             implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
         		...
         }

         def querydslDir = "$buildDir/generated/querydsl"

         querydsl {
             jpa = true
             querydslSourcesDir = querydslDir
         }
         sourceSets {
             main.java.srcDir querydslDir
         }
         configurations {
             compileOnly {
                 extendsFrom annotationProcessor
             }
             querydsl.extendsFrom compileClasspath
         }
         compileQuerydsl {
             options.annotationProcessorPath = configurations.querydsl
         }

         ...
         ```

         변경 내용을 적용

       - 오른쪽에 gradle 탭을 클릭하고 Task 안에서 build를 선택한 후 Tasks 탭에서 build/build를 더블클릭. 그러면 build/generated/querydsl/{package이름}/에 클래스가 생성됨.
       - 3.x 버전

         ```
         dependencies에 추가
         // ==스프링 부트 3.0이상 ==
         implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
         annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
         annotationProcessor "jakarta.annotation:jakarta.annotation-api"
         annotationProcessor "jakarta.persistence:jakarta.persistence-api"

         ```

    3. QueryDsl 사용을 위해서 Repository 인터페이스의 모양을 변경

       ```java
       public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, QuerydslPredicateExecutor<GuestBook> {
       }
       ```

    4. test 디렉토리에 데이터 삽입 메서드를 작성하고 테스트

       ```java
       @SpringBootTest
       public class RepositoryTest {
           @Autowired
           private GuestBookRepository guestBookRepository;

           // 데이터 삽입 메서드 테스트
           @Test
           public void insertDumies() {
               IntStream.rangeClosed(1, 300).forEach(i -> {
                   GuestBook guestBook = GuestBook.builder()
                           .title("제목..." + i)
                           .content("내용..." + i)
                           .writer("user..." + i)
                           .build();
                   System.out.println(guestBookRepository.save(guestBook));
               });
           }
       }
       ```

       수정 - 삽입과 동일하게 save 메서드를 이용하는데 기본키의 값을 설정하지 않거나 존재하지 않는 값이면 삽입이고 존재하는 값이면 수정(리턴값이 null일 수 없다.)

       ```java
       ...
       @Test
           public void updateDummies() {
               GuestBook guestBook = GuestBook.builder()
                       .gno(1L)
                       .title("제목 변경")
                       .content("내용 변경")
                       .writer("규민")
                       .build();
               System.out.println(guestBookRepository.save(guestBook));
           }
       ```

    5. querydsl 테스트(일반 Entity를 이용하지 않고 Q로 시작하는 것을 이용한다.)

       - 사용법
         BooleanBuilder를 생성
         구문은 Predicate 타입의 함수를 생성
         BooleanBuilder에 작성된 Predicate을 추가하고 실행
       - title에 1이라는 글자가 포함된 Entity 조회

         ```java
         @Test
             // title에 1이라는 글자가 포함된 Entity 조회
             public void Query1() {
                 // 10개씩 첫 번째 페이지의 데이터를 조회
                 // modDate의 내림차순 정렬
                 Pageable pageable = PageRequest.of(0, 10, Sort.by("modDate").descending());

                 // Querydsl 수행
                 QGuestBook qGuestBook = QGuestBook.guestBook;

                 // title에 1이 포함된 조건을 생성
                 String keyword = "1";
                 BooleanBuilder builder = new BooleanBuilder();
                 BooleanExpression expression = qGuestBook.title.contains(keyword);
                 builder.and(expression);

                 Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);
                 for (GuestBook guestBook : result) {
                     System.out.println(guestBook);
                 }
             }
         ```

       - title 또는 content에 1이 포함되는 데이터 조회

         ```java
         ...
         @Test
             public void testQuery2() {
                 Pageable pageable = PageRequest.of(0, 10, Sort.by("modDate").descending());

                 // querydsl 수행을 위해서 Q 클래스 가져오기
                 QGuestBook qGuestBook = QGuestBook.guestBook;

                 String keyword = "1";
                 BooleanBuilder builder = new BooleanBuilder();

                 // title에 포함된 조건
                 BooleanExpression exTitle = qGuestBook.title.contains(keyword);
                 // contest에 포함된 조건
                 BooleanExpression exContent = qGuestBook.content.contains(keyword);
                 // 2개의 조건을 or로 결합
                 BooleanExpression exAll = exTitle.or(exContent);

         				// 우선 순위가 있기 때문에 합치지 않고 builder 2개 써서 진행
                 builder.and(exAll);
                 builder.and(qGuestBook.gno.lt(100L));

                 Page<GuestBook> result = guestBookRepository.findAll(builder, pageable);
                 for (GuestBook guestBook : result.getContent()) {
                     System.out.println(guestBook);
                 }
             }
         ```

11. **Service Layer**

    1. Service와 Controller 그리고 View가 사용할 GuestBook 관련 DTO 클래스 생성 - dto.GuestBookDTO(이전과는 다르게 이제 Entity가 없어지고 Getter Setter모두 만든다)

       ```java
       @Builder
       @NoArgsConstructor
       @AllArgsConstructor
       @Data
       public class GuestBookDTO {
           private Long gno;
           private String title;
           private String content;
           private String writer;
           private LocalDateTime regDate;
           private LocalDateTime modDate;
       }
       ```

    2. Service 인터페이스 생성 - service.GuestBookService

       **Service에서 가장 많이 하는 것 중 하나가 DTO와 Entity 사이의 변환**

       인터페이스에 default method로 추가해주는 것이 좋다.

       ```java
       public interface GuestBookService {
           // DTO를 Entity로 변환해주는 메서드
           default GuestBook dtoToEntity(GuestBookDTO dto) {
       				// 삽입 날짜와 수정 날짜는 entity가 삽입되거나 수정될 때 생성되므로 옮겨줄 필요가 없음.
               GuestBook entity = GuestBook.builder()
                       .gno(dto.getGno())
                       .title(dto.getTitle())
                       .content(dto.getContent())
                       .writer(dto.getWriter())
                       .build();
               return entity;
           }

           // entity를 dto로 변환해주는 메서드
           // 전부 옮겨주어야 한다.
           default GuestBookDTO entityToDTO(GuestBook entity) {
               GuestBookDTO dto = GuestBookDTO.builder()
                       .gno(entity.getGno())
                       .title(entity.getTitle())
                       .content(entity.getContent())
                       .writer(entity.getWriter())
                       .regDate(entity.getRegDate())
                       .modDate(entity.getModDate())
                       .build();
               return dto;
           }
       }
       ```

    3. ServiceImpl 클래스 생성 - service.GuestBookServiceImpl

       ```java
       @Service
       @Log4j2
       @RequiredArgsConstructor
       public class GuestBookServiceImpl implements GuestBookService{
           private final GuestBookRepository guestBookRepository;
       }
       ```

    4. 데이터 삽입

       - Service 인터페이스에 데이터 삽입을 위한 메서드를 선언
         이제 전적으로 개발자가, 어떤 것을 매개변수로 주고 어떤 것을 리턴할지 작성해 나가야 한다.
         삽입을 할 때 일반적으로 dto를 매개변수로 주고, 결과는 여러가지로 리턴할 수 있다.

         ```java
         public interface GuestBookService {
             // 데이터 삽입을 위한 메서드
             // 매개변수는 대부분의 경우 dto
             // 리턴 타입은 삽입된 데이터를 그대로 리턴하기도 하고
             // 성공과 실패 여부를 위해서 boolean을 리턴하기도 하고
             // 영향받은 행의 개수를 의미하는 int를 리턴하기도 하고
             // 기본키의 값을 리턴하는 경우도 있다.
             // 일단 여기서는 기본키의 값을 리턴하겠다.
             public Long register(GuestBookDTO dto);

         		...
         ```

       - ServiceImpl에 추가

         ```java
         @Service
         @Log4j2
         @RequiredArgsConstructor
         public class GuestBookServiceImpl implements GuestBookService{
             private final GuestBookRepository guestBookRepository;

             public Long register(GuestBookDTO dto) {
                 // 파라미터가 제대로 넘어오는지 확인
                 log.info("삽입 데이터:" + dto.toString());
                 // repository의 메서드 매개변수로 변경
                 GuestBook entity = dtoToEntity(dto);
                 // 이 시점에는 entity에 gno와 regDate, modDate가 없다.
                 // save를 할 때 설정한 내역을 가지고 필요한 데이터를 설정한다.
                 // gno, regDate, modDate가 설정된다.
                 guestBookRepository.save(entity);
                 return entity.getGno();
             }
         }
         ```

       - 서비스 메서드를 테스트해본다.
         ```java
         @Test
             public void testRegister() {
                 GuestBookDTO dto = GuestBookDTO.builder()
                         .title("삽입 제목")
                         .content("삽입 내용")
                         .writer("삽입 작성자").build();
                 System.out.println(guestBookService.register(dto));
             }
         ```

    5. 목록 보기

       - 게시판 형태에서 목록 보기 요청
         페이지 번호, 페이지 당 데이터 개수, 검색 항목, 검색 값 - 이런 형태의 요청 DTO가 필요
       - 게시판 형태에서 목록 보기 응답
         Pageable 형태의 DTO 목록
         페이지 번호 리스트
         이전 페이지 여부
         다음 페이지 여부
         현재 페이지 번호
       - 요청을 위한 DTO 클래스 생성 - dto.PageRequestDTO

         ```java
         @Builder
         @AllArgsConstructor
         @Data
         public class PageRequestDTO {
             // 현재 페이지 번호
             private int page;
             // 페이지 당 데이터 개수
             private int size;

             public PageRequestDTO() {
                 // 기본값 설정 - NoArgsConstructor Annotation은 필요가 없어짐.
                 this.page = 1;
                 this.size = 10;
             }

             // Repository에게 전달할 Pageable 객체를 만들어주는 메서드
             public Pageable getPageable(Sort sort) {
                 return PageRequest.of(page - 1, size, sort);
             }
         }
         ```

       - 응답을 위한 DTO 클래스 생성 - dto.PageResponseDTO

         ```java
         // 다른 종류의 Entity에서도 사용할 수 있도록 하기 위해서 Generic 사용
         @Data
         // DTO는 결과를 출력할 때 데이터 클래스
         // EN은 변환에 사용할 메서드를 소유한 클래스
         public class PageResponseDTO<DTO, EN> {
             // 데이터 목록
             private List<DTO> dtoList;

             // Page 단위의 Entity를 받아서 DTO의 List로 변환
             // 첫 번째 매개변수가 Page 단위의 Entity이고
             // 두 번째는 데이터 변환을 위한 메서드
             public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn) {
                 // En(Entity)과 DTO(클래스 타입)을 변환해주는 함수를 매개변수로 받아서 DTO 타입의 List로 변환해주는 것.
                 dtoList = result.stream().map(fn).collect(Collectors.toList());
             }
         }
         ```

       - Service 인터페이스에 목록 보기를 위한 메서드 추가
         ```java
         public interface GuestBookService {
         		...
         		// 목록 보기를 위한 메서드
             PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO);
         }
         ```
       - ServiceImpl 클래스에 에 목록 보기를 위한 메서드 추가

         ```java
         public class GuestBookServiceImpl implements GuestBookService{
         		...
         		// 목록 보기를 위한 메서드
             public PageResponseDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO pageRequestDTO) {
                 // Repository에게 넘겨줄 Pageable 객체를 생성
                 // gno의 내림차순으로 정렬
                 Pageable pageable = pageRequestDTO.getPageable(
                         Sort.by("gno").descending()
                 );

                 // 데이터 찾아오기
                 Page<GuestBook> result = guestBookRepository.findAll(pageable);

                 // 데이터 목록을 받아서 데이터 목록을 순회하면서 제공된 메서드가 리턴하는 목록으로 변경해주는 람다
                 Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDTO(entity));

                 return new PageResponseDTO<>(result, fn);
             }
         ```

       - Test
         ```java
         ...
         @Test
             public void testList() {
                 // 1페이지 10개
                 PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                         .page(1)
                         .size(10)
                         .build();
                 PageResponseDTO<GuestBookDTO, GuestBook> result = guestBookService.getList(pageRequestDTO);
                 for(GuestBookDTO dto: result.getDtoList()) {
                     System.out.println(dto);
                 }
             }
         ```
       - 페이지 번호 목록을 출력할 수 있도록 PageResponseDTO를 수정

         ```java
         // 다른 종류의 Entity에서도 사용할 수 있도록 하기 위해서 Generic 사용
         @Data
         // DTO는 결과를 출력할 때 데이터 클래스
         // EN은 변환에 사용할 메서드를 소유한 클래스
         public class PageResponseDTO<DTO, EN> {
             // 데이터 목록
             private List<DTO> dtoList;

             // 전체 페이지 개수
             private int totalPage;

             // 현재 페이지 번호
             private int page;

             // 하나의 페이지에 출력할 데이터 개수
             private int size;

             // 페이지 번호에서 시작하는 페이지 번호, 끝나는 페이지 번호
             private int startPage;
             private int endPage;

             // 이전과 다음 여부
             private boolean prev;
             private boolean next;

             // 페이지 번호 목록
             private List<Integer> pageList;

             // Paging 결과를 가지고 추가한 항목들을 계산해주는 메서드
             private void makePageList(Pageable pageable) {
                 // 현재 페이지 번호
                 // JPA는 페이지 번호가 0부터 시작하므로 + 1
                 this.page = pageable.getPageNumber() + 1;
                 // 페이지 당 데이터 개수
                 this.size = pageable.getPageSize();

                 // 임시로 마지막 페이지 번호를 생성
                 // 페이지 번호를 10개를 출력
                 // 현재 페이지 1~10까지는 10, 11~20은 20...
                 // 10으로 나누어서 소수가 있으면 올림을 하고 곱하기 10
                 int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
                 // 시작하는 번호
                 startPage = tempEnd - 9;
                 // 이전 여부
                 prev = startPage > 1;
                 // 마지막 페이지 번호
                 endPage = totalPage > tempEnd ? tempEnd : totalPage;
                 // 다음 여부
                 next = totalPage > tempEnd;
                 // 페이지 번호 목록
                 // startPage부터 endPage까지를 스트림으로 만들어서 List로 변환
                 pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
             }

             // Page 단위의 Entity를 받아서 DTO의 List로 변환
             // 첫 번째 매개변수가 Page 단위의 Entity이고
             // 두 번째는 데이터 변환을 위한 메서드
             public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn) {
                 // En(Entity)과 DTO(클래스 타입)을 변환해주는 함수를 매개변수로 받아서 DTO 타입의 List로 변환해주는 것.
                 dtoList = result.stream().map(fn).collect(Collectors.toList());

                 // 전체 페이지 개수
                 totalPage = result.getTotalPages();
                 // 페이지 목록 메서드 호출
                 makePageList(result.getPageable());
             }
         }
         ```

       - Test
         ```java
         ...
         @Test
             public void testListInformation() {
                 PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                         .page(1)
                         .size(10)
                         .build();
                 PageResponseDTO<GuestBookDTO, GuestBook> result = guestBookService.getList(pageRequestDTO);
                 // 데이터 확인
                 // 데이터 목록
                 System.out.println(result.getDtoList());
                 // 페이지 목록
                 System.out.println(result.getPageList());
                 // 전체 페이지 개수
                 System.out.println(result.getTotalPage());
                 // 이전 여부 - boolean은 get메서드가 아닌 is메서드로 생성됨.
                 System.out.println(result.isPrev());
                 // 다음 여부
                 System.out.println(result.isNext());
             }
         ```

12. **Controller와 View**

    1. 목록 보기

       - View Controller를 수정

         ```java
         @Log4j2
         @Controller
         @RequiredArgsConstructor
         public class ViewController {
             private final GuestBookService guestBookService;

             @GetMapping("/")
             public String list() {
                 return "redirect:/guestbook/list";
             }

             @GetMapping("/guestbook/list")
             public void list(PageRequestDTO pageRequestDTO, Model model) {
                 model.addAttribute("result", guestBookService.getList(pageRequestDTO));
             }
         }
         ```

       - list.html 수정
         ```java
         <!DOCTYPE html>
         <html lang="en">
         <th:block th:replace="~{/layout/basic:: setContent(~{this::content})}">
           <th:block th:fragment="content">
             <h1>방명록</h1>
             <table class ="table table-striped">
               <tr>
                 <th scope = "col">번호</th>
                 <th scope = "col">제목</th>
                 <th scope = "col">작성자</th>
                 <th scope = "col">작성일</th>
               </tr>
               <tr th:each = "dto: ${result.dtoList}">
                 <td>[[${dto.gno}]]</td>
                 <td>[[${dto.title}]]</td>
                 <td>[[${dto.writer}]]</td>
                 <td>[[${#temporals.format(dto.regData, 'yyyy-MM-dd'}]]</td>
               </tr>
             </table>
           </th:block>
         </th:block>
         ```
       - 목록 보기를 JSON 데이터 형식으로 보내기 위한 Controller를 생성하고 요청 처리 메서드 작성

         ```java
         @Log4j2
         @RestController
         @RequiredArgsConstructor
         public class JSONController {
             private final GuestBookService guestBookService;

             @GetMapping("/guestbook/list.json")
             public PageResponseDTO<GuestBookDTO, GuestBook> list(PageRequestDTO pageRequestDTO) {
                 return guestBookService.getList(pageRequestDTO);
             }
         }
         ```

13. **데이터 삽입 구현**

    1. ViewController에 데이터 삽입을 위한 메서드 추가

       ```java
       @PostMapping("/guestbook/register")
           public String register(GuestBookDTO dto, RedirectAttributes rattr) {
               log.info(dto); // 파라미터 확인
               Long gno = guestBookService.register(dto);
               // RedirectAttributes는 세션에 저장하는데 한 번 사용하고 자동 소멸
               // 데이터 전송
               // session.addFlashAttribute를 쓰는 경우가 있는데 종료가 따로 안됨.
               // rattr과 같이 쓰면 알아서 종료된다.
               rattr.addFlashAttribute("msg", gno + "등록");
               // 데이터베이스에 변경 작업을 수행하고 나면 반드시 redirect
               return "redirect:/guestbook/list";
           }
       ```

    2. 데이터 삽입 화면 생성 - register.html

       ```html
       <!DOCTYPE html>
       <html lang="en">
         <th:block th:replace="~{/layout/basic:: setContent(~{this::content})}">
           <th:block th:fragment="content">
             <h1>방명록 작성</h1>
             <form th:method="post">
               <div class="form-group">
                 <label>제목</label>
                 <input
                   type="text"
                   class="form-control"
                   name="title"
                   placeholder="제목 입력"
                 />
               </div>
               <div class="form-group">
                 <label>내용</label>
                 <textarea
                   class="form-control"
                   rows="5"
                   name="content"
                   placeholder="제목 입력"
                 ></textarea>
               </div>
               <div class="form-group">
                 <label>작성자</label>
                 <input
                   type="text"
                   class="form-control"
                   name="writer"
                   placeholder="작성자"
                 />
               </div>
               <button type="submit" class="btn btn-primary">등록</button>
             </form>
           </th:block>
         </th:block>
       </html>
       ```

    3. list.html 파일에 작성 링크를 만들고 메시지 출력 영역을 생성

       ```html
       <!DOCTYPE html>
       <html lang="en">
         <th:block th:replace="~{/layout/basic:: setContent(~{this::content})}">
           <th:block th:fragment="content">
             <h1>방명록</h1>
             <span>
               <a th:href="@{/guestbook/register}">
                 <button type="button" class="btn btn-danger">작성</button>
               </a>
             </span>
             ...
           </th:block>
         </th:block>
       </html>
       ```
