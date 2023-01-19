# [Spring Boot] 1:N 관계

1. **관계**

   관계형 데이터베이스는 테이블 간에 관계 설정 가능

   1. 관계형 데이터베이스의 관계

      1:1

      1:N

      N:N(다대다 관계) - 1:N 관계 2개로 분할

   2. JPA에서의 관계
      - 종류
        - OneToOne
        - OneToMany
        - ManyToOne
        - ManyToMany
      - 방향성
        - 단방향성
        - 양방향성
   3. 회원과 게시글의 관계
      - 한 명의 회원은 0개 이상의 게시글을 작성할 수 있음
      - 1개의 게시글은 반드시 한 명의 회원이 작성해야 함
      - 회원과 게시글의 관계는 1:N 관계
        관계형 데이터베이스에서는 이 경우 회원 테이블의 기본키를 게시글 테이블의 외래키로 설정
        JPA에서는 양방향성을 가질 수 있고 반대 방향으로 관계 설정이 가능
        - JPA에서는 이 경우 회원 정보를 가져올 때 게시글을 참조하는 경우가 많은지?
        - 게시글을 가져올 때 회원 정보를 참조하는 경우가 많은지?(이게 보통 맞다.)
        - 양쪽의 경우가 모두 많은지?
   4. 데이터 베이스 구조

      회원 테이블: 게시글 테이블 = 1: N

      게시글 테이블: 댓글 테이블 = 1: N

      회원 테이블: 댓글 테이블 = 1: N

2. **게시판 프로젝트 설정**

   1. 프로젝트 생성
      - 의존성
        Lombok, Spring Web, Thymeleaf, Spring Data JPA, MariaDB
   2. [application.properties](http://application.properties) 파일을 application.yml 파일로 이름을 변경하고 기본 설정 작성

      ```yaml
      server:
        port: 80

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
              format_sql: true
              show_sql: true

        thymeleaf:
          cache: false

      logging:
        level:
          org.hibernate.type.descriptor.sql: trace
      ```

   3. 실행 클래스 상단에 데이터베이스 감시 어노테이션 추가

      ```java
      @EnableJpaAuditing
      @SpringBootApplication
      public class Guestbookapp0112Application {

          public static void main(String[] args) {
              SpringApplication.run(Guestbookapp0112Application.class, args);
          }

      }
      ```

3. **domain 클래스 작업**

   1. 공통으로 사용할 수정 날짜와 생성 날짜를 갖는 Entity - domain.BaseEntity

      ```java
      @MappedSuperclass // 테이블 생성을 하지 않는 Entity
      @EntityListeners(value={AuditingEntityListener.class}) // JPA의 동작을 감시
      @Getter
      public class BaseEntity {
          @CreatedDate
          @Column(name="regdate", updatable = false)
          private LocalDateTime regDate;

          @LastModifiedDate
          @Column(name="moddate", updatable = false)
          private LocalDateTime modDate;
      }
      ```

   2. 회원 정보를 저장할 Entity 생성 - domain.Member

      ```java
      @Entity
      @Table(name="tbl_member")
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      @ToString
      public class Member extends BaseEntity{
          @Id
          private String email;

          private String password;

          private String name;
      }
      ```

   3. 게시글 정보를 저장할 Entity 생성 - domain.Board

      ```java
      @Entity
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      @ToString
      public class Board extends BaseEntity{
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          private Long bno;

          private String title;

          private String content;
      }
      ```

   4. 댓글 정보를 저장할 Entity 생성 - domain.Reply

      ```java
      @Entity
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      @ToString
      public class Reply extends BaseEntity {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          private Long rno;

          private String text;

          private String replyer;
      }
      ```

4. **관계 어노테이션의 세부 속성**

   1. optional
      - 필수 여부를 설정하는 것으로 기본값은 true
      - 반드시 존재해야 하는 경우라면 false로 설정하면 된다.
      - 이 값이 true이면 데이터를 가져올 때 outer join을 하게 되고 false이면 inner join을 수행한다.
        inner join: 양쪽 테이블에 모두 존재하는 데이터만 join에 참여(교집합)
        outer join: 한쪽 테이블에만 존재하는 데이터도 join에 참여(합집함)
   2. mappedBy

      양방향 관계 설정 시 다른 테이블에 매핑되는 Entity 필드를 설정

   3. cascade

      상태 변화를 전파시키는 옵션

      - PERSIST
        부모 Entity가 영속화될 때(저장) 자식 Entity도 같이 영속화
      - MERGE
        부모 Entity가 병합될 때 자식 Entity도 같이 병합
      - REMOVE
        부모 Entity가 삭제될 때 자식 Entity도 같이 삭제
      - REFRESH
        부모 Entity가 refresh될 때 자식 Entity도 같이 refresh될 때
      - DETACH
        부모 Entity가 detach될 때 자식 Entity도 같이 detach - 더 이상 Context의 관리받지 않도록 하는 것, 트랜잭션이 종료되더라도 이 객체는 변화가 생기지 않음.
      - ALL
        모든 상태를 전이

   4. opphanRemoval

      JoinColumn의 값이 NULL인 자식 객체를 삭제하는 옵션

   5. fetch

      외래키로 설정된 데이터를 가져오는 시점에 대한 옵션으로 Eager와 Lazy 설정

5. **ManyToOne**
   - 외래키를 소유해야 하는 테이블을 위한 Entity에 설정하는데 `@ManyToOne`이라고 컬럼 위에 작성
   - 테이블의 방향을 반대로 설정할 수 있는데 `@OneToMany`
   - `@JoinColumn` 이용해서 join 하는 컬럼을 명시적으로 작성할 수 있는데 생략하면 `참조변수이름_참조하는 테이블의기본키컬럼이름` 으로 생성
   - 외래키 값은 부모 Entity에서만 삽입, 수정, 삭제가 가능하다.
     자식 Entity에서는 읽기만 가능하다.
   - Board 테이블에 Member 테이블을 참조할 수 있는 관계를 설정
     ```java
     ...
     @ManyToOne
         private Member writer;
     ```
   - Reply 테이블에 Board 테이블을 참조할 수 있는 관계를 설정
     ```java
     ...
     @ManyToOne
         private Board board;
     ```
6. **Repository 인터페이스 생성**

   1. MemberRepository

      ```java
      public interface MemberRepository extends JpaRepository<Member, String> {

      }
      ```

   2. BoardRepository

      ```java
      public interface BoardRepository extends JpaRepository<Board, Long> {
      }
      ```

   3. ReployRepository

      ```java
      public interface ReplyRepository extends JpaRepository<Reply, Long> {
      }
      ```

7. **샘플 데이터 삽입**

   Repository Test

   ```java
   @SpringBootTest
   public class RepositoryTest {
       // Autowired로 주입받기
       @Autowired
       private MemberRepository memberRepository;

       @Autowired
       private BoardRepository boardRepository;

       @Autowired
       private ReplyRepository replyRepository;

       @Test
       // 회원 데이터 삽입
       public void insertMember() {
           for(int i = 1; i <= 100; i++) {
               Member member = Member.builder()
                       .email("user" + i + "@kakao.com")
                       .password("1111")
                       .name("USER" + i)
                       .build();
               memberRepository.save(member);
           }
       }

       @Test
       // 게시글 데이터 삽입
       public void insertBoard() {
           for(int i = 1; i <= 100; i++) {
               // Member를 만들고 Board를 만들어가야한다.
               Member member = Member.builder()
                       .email("user" + i + "@kakao.com")
                       .build();
               Board board = Board.builder()
                       .title("제목..." + i)
                       .content("내용..." + i)
                       .writer(member)
                       .build();
               boardRepository.save(board);
           }
       }

       @Test
       // 댓글 데이터 삽입
       public void insertReply() {
           for (int i = 1; i <= 300; i++) {
               long bno = (long) (Math.random() * 100) + 1;
               Board board = Board.builder()
                       .bno(bno)
                       .build();

               Reply reply = Reply.builder()
                       .text("댓글..." + i)
                       .board(board)
                       .replyer("guest")
                       .build();
               replyRepository.save(reply);
           }
       }
   }
   ```

8. **Eager/Lazy Loading**

   Entity에 관계를 설정하면 데이터를 가져올 때 참조하는 데이터도 같이 가져온다.

   1. 1개의 게시글을 가져오는 테스트 메서드

      ```java
      @Test
          // 게시글 1개를 가져오는 메서드
          public void readBoard() {
              Optional<Board> result = boardRepository.findById(100L);
              Board board = result.get();
              System.out.println(board);
              System.out.println(board.getWriter());
          }
      ```

      실제 실행되는 쿼리 - join을 수행해서 데이터를 가져온다.

      ```sql
      select
              b1_0.bno,
              b1_0.content,
              b1_0.moddate,
              b1_0.regdate,
              b1_0.title,
              w1_0.email,
              w1_0.moddate,
              w1_0.name,
              w1_0.password,
              w1_0.regdate
          from
              board b1_0
          left join
              tbl_member w1_0
                  on w1_0.email=b1_0.writer_email
          where
              b1_0.bno=?
      ```

   2. reply 정보 1개를 가져오는 메서드

      ```java
      @Test
          // 댓글 1개를 가져오는 메서드
          public void readReply() {
              Optional<Reply> result = replyRepository.findById(100L);
              Reply reply = result.get();
              System.out.println(reply);
              System.out.println(reply.getBoard());
          }
      ```

      근데 이 때 readBoard와 readReply에서 외래키를 설정해놔서 댓글에 따라 Join을 수행하는데 이는 **자원의 낭비**이다. 필요할 때 가져오도록 해보자. (생성자에서 주입받는 개념에서 setter를 통해 주입받는 개념으로 생각하는 것으로 생각해보자.)

   3. **Lazy Loading**

      - 지연 로딩
      - 사용할 때 가져오는 것
      - 관계를 설정할 때 `fetch = FetchType.LAZY` 을 추가하면 지연 로딩이 된다.
      - 지연 로딩을 사용할 때 주의할 사항
        - toString
          내부 모든 속성의 toString을 호출해서 그 결과를 가지고 하나의 문자열을 생성
          지연로딩을 하게되면 참조하는 속성의 값이 처음에는 null이다.
          null.toString이 호출되서 NullPointerException이 발생하게 된다.
          이 문제를 해결하기 위해서 지연로딩되는 속성은 toString에서 제외를 해야 한다.
          `@ToString(exclude="제외할 속성이름")`
        - 지연 로딩을 사용하게 되면 2개의 select 구문이 수행되어야 한다.
          데이터를 찾아오고 그 후 그 데이터의 외래키를 이용해서 참조하는 테이블에서 데이터를 찾아오게 된다.
          JPA는 하나의 메서드에서 하나의 Context 연결만을 사용하는데 하나의 SQL 구문이 끝나면 Context와의 연결이 해제된다.
          따라서 2개의 SQL을 실행할 수 없다.
          이런 경우에는 메서드에 `@Transactional`을 붙여서 메서드의 수행이 종료될 때까지 연결을 해제하지 말라고 해야 한다.
      - Board Entity를 수정

        ```java
        @Entity
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString(exclude = "writer") // toString을 만들 때 writer의 toString은 호출 안함.
        public class Board extends BaseEntity{
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private Long bno;

            private String title;

            private String content;

            @ManyToOne(fetch = FetchType.LAZY)
            // 처음에는 가져오지 않고 사용을 할 때 가져온다.
            private Member writer;
        }
        ```

      - Reply Entity를 수정

        ```java
        @ToString(exclude = "board")
        public class Reply extends BaseEntity {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private Long rno;

            private String text;

            private String replyer;

            @ManyToOne(fetch = FetchType.LAZY)
            private Board board;
        }
        ```

      - readBoard와 readReply 테스트 메서드에 @Transcational 추가
        Join이 아닌 select 구문 2번 실행하므로 데이터베이스 연결을 끊지 않고 유지하기 위해 쓴다. 그렇지 않으면 마지막 board.getWriter()나 reply.getBoard() 에서 이미 끊어진 데이터베이스 결과에서 찾으려고 하기 때문에 에러가 난다.

        ```java
        @Test
            @Transactional
            // 게시글 1개를 가져오는 메서드
            public void readBoard() {
                Optional<Board> result = boardRepository.findById(100L);
                Board board = result.get();
                System.out.println(board);
                System.out.println(board.getWriter());
            }

        @Test
            // 댓글 1개를 가져오는 메서드
            @Transactional
            public void readReply() {
                Optional<Reply> result = replyRepository.findById(100L);
                Reply reply = result.get();
                System.out.println(reply);
                System.out.println(reply.getBoard());
            }
        ```

9. **JOIN**

   1. Join
      - 2개 이상의 테이블(동일한 테이블 2개도 가능)을 가로 방향으로 합치는 작업
      - 종류
        - Cross Join(Cartesian Product)
          2개 테이블의 모든 조합을 만들어내는 것, 외래키를 설정하지 않았을 때 수행
        - INNER JOIN - EQUI JOIN
          양쪽 테이블에 동일한 의미를 갖는 컬럼의 값이 동일한 경우에만 결합
        - NON EQUI JOIN
          양쪽 테이블에 동일한 의미를 갖는 컬럼의 값이 동일한 경우를 제외한 방식(>, >=, <, <= 또는 between)으로 겨합
        - OUTER JOIN
          한 쪽 테이블에만 존재하는 데이터도 JOIN에 참여하는 방식
          방향에 따라서 LEFT OUTER JOIN, RIGHT OUTER JOIN, FULL OUTER JOIN이 있음.
        - SELF JOIN
          동일한 테이블을 가지고 JOIN, 하나의 테이블에 동일한 의미를 갖는 컬럼이 2개 이상 존재할 때 수행한다.
          ex) 테이블에 회원 아이디와 친구 아이디가 같이 존재하는 경우 - 내 친구의 친구를 조회하고자 할 때
   2. join query

      `@Query(”select 찾을것 from 엔티티이름 left outer join 엔티티안의참조속성 참조하는테이블의별칭”)`

      이렇게 만들어진 쿼리의 결과는 Object 타입이다.

      결과를 Object 타입의 배열로 형 변환해서 사용해야 한다.

      join을 수행한 경우는 Arrays.toString을 이용해서 내용을 출력해서 확인해보고 사용을 해야 한다.

   3. join 테스트

      - Board 데이터를 가져올 때 Writer의 데이터도 가져오기 - BoardRepository 인터페이스에 메서드 선언
        ```java
        public interface BoardRepository extends JpaRepository<Board, Long> {
            // Board 데이터를 가져올 때 Writer 정보도 가져오는 메서드
            @Query("select b, w from Board b left join b.writer w where b.bno=:bno")
            Object getBoardWithWriter(@Param("bno") Long bno);
        }
        ```
      - 테스트

        ```java
        // Board 데이터를 가져올 때 Writer의 데이터도 가져오기
            @Test
            public void joinTest1() {
                Object result = boardRepository.getBoardWithWriter(100L);
                System.out.println(result);
                Object[] ar = (Object[]) result;
                System.out.println(Arrays.toString(ar));

                Board board = (Board)ar[0];
                Member member = (Member)ar[1];
            }
        ```

      - 관계가 설정되지 않은 경우에는 on 절을 이용해서 Join이 가능
      - 게시글을 가져올 때 연관된 모든 댓글을 가져오기 - BoardRepository 인터페이스에 메서드를 선언
        근데, Board Entity 쪽에서 Reply쪽에 적어준 게 없다.(Reply Entity쪽에서 Board 쪽에 ManyToOne으로 설정했기 때문).

        ```java
        public interface BoardRepository extends JpaRepository<Board, Long> {
            // Board 데이터를 가져올 때 Writer 정보도 가져오는 메서드
            @Query("select b, w from Board b left join b.writer w where b.bno=:bno")
            Object getBoardWithWriter(@Param("bno") Long bno);

            // 글번호를 받아서 Board와 그리고 Board와 관련된 Reply 정보 찾아오기
            // Board 1개에 여러 개의 Reply가 존재
            // Board와 Reply를 결합한 형태의 목록으로 리턴하기 때문에 List로 쓴다
            @Query("select b, r from Board b left join Reply r on r.board = b where b.bno=:bno")
            List<Object[]> getBoardWithReply(@Param("bno") Long bno);
        }

        // 두 메서드의 차이점을 비교해보자!
        ```

      - 게시글 목록(페이징 필요)을 가져올 때 게시글과 작성자 정보, 그리고 댓글의 개수를 가져오기 - 메인 화면을 만들 쿼리 - BoardRepository 인터페이스에 메서드를 선언
        ```java
        // 게시글 목록과 작성자 정보, 그리고 댓글의 개수를 가져오는 메서드
            // 페이징 처리 필요 - 리턴 타입은 Page이다.
            @Query("select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board = b group by b")
            Page<Object[]> getBoardWithReplyCount(Pageable pageable);
        ```
      - 테스트

        ```java
        @Test
            public void testJoin3() {
                Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
                Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

                result.get().forEach(row -> {
                    Object[] ar = (Object[]) row;
                    System.out.println(Arrays.toString(ar));

                    Board b = (Board) ar[0];
                    Member m = (Member) ar[1];
                    Long c = (Long) ar[2];
                    System.out.println(c);
                });
            }
        ```

      - 상세 보기를 위한 메서드 - BoardRepository 인터페이스에 메서드 선언
        게시글 번호 1개를 가지고 찾아오면 된다.
        ```java
        // 글번호를 가지고 데이터를 찾아오는 메서드 - 상세보기
            @Query("select b, w, count(r) from Board b left join b.writer w left outer join Reply r on r.board = b where b.bno=:bno")
            Object getBoardByBno(@Param("bno") Long bno);
        ```

10. **Service 계층**

    1. 데이터 전송에 사용하는 게시글의 DTO를 생성 - dto.BoardDTO

       ```java
       @Data
       @Builder
       @AllArgsConstructor
       @NoArgsConstructor
       public class boardDTO {
           private Long bno;

           private String title;

           private String content;

           private String writerEmail;

           private String writerName;

           private LocalDateTime regDate;

           private LocalDateTime modDate;

           private int replyCount;
       }
       ```

    2. 게시글 관련 서비스 인터페이스를 생성하고 기본 메서드를 작성 - service.BoardService

       ```java
       public interface BoardService {
           // DTO -> Entity로 변환해주는 메서드
           default Board dtoToEntity(BoardDTO dto) {
               Member member = Member.builder()
                       .email(dto.getWriterEmail()).build();

               Board board = Board.builder()
                       .bno(dto.getBno())
                       .title(dto.getTitle())
                       .content(dto.getContent())
                       .writer(member)
                       .build();
               return board;
           }

           // Entity -> DTO로 변환해주는 메서드
           default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
               BoardDTO dto = BoardDTO.builder()
                       .bno(board.getBno())
                       .title(board.getTitle())
                       .content(board.getContent())
                       .regDate(board.getRegDate())
                       .modDate(board.getModDate())
                       .writerEmail(member.getEmail())
                       .writerName(member.getName())
                       .replyCount(replyCount.intValue())
                       .build();
               return dto;
           }
       }
       ```

    3. 게시글 관련 서비스 메서드를 구현할 클래스를 생성 - service.BoardServiceImpl

       ```java
       @Service
       @Log4j2
       @RequiredArgsConstructor
       public class BoardServiceImpl implements BoardService{
           private final BoardRepository boardRepository;
       }
       ```

    4. 게시글 등록 요청을 생성

       - BoardService 인터페이스에 게시글 등록 요청을 처리할 메서드를 선언
         ```java
         public interface BoardService {
             // 게시글 등록
             Long register(BoardDTO dto);
         		...
         }
         ```
       - BoardServiceImpl 클래스에 게시글 등록 요청을 처리할 메서드를 구현

         ```java
         @Service
         @Log4j2
         @RequiredArgsConstructor
         public class BoardServiceImpl implements BoardService{
             private final BoardRepository boardRepository;

             public Long register(BoardDTO dto) {
                 log.info("Service:" + dto);
                 Board board = dtoToEntity(dto);
                 boardRepository.save(board);
                 return board.getBno();
             }
         }
         ```

       - Service Test 클래스를 만들어서 확인

         ```java
         // 등록 테스트
             @Test
             public void registerTest() {
                 BoardDTO dto = BoardDTO.builder()
                         .title("등록 테스트")
                         .content("등록을 테스트합니다.")
                         .writerEmail("user33@kakao.com")
                         .build();

                 Long bno = boardService.register(dto);
                 System.out.println(bno);
             }
         ```

    5. 게시글 목록 보기를 처리

       - 게시글 목록 보기 요청을 저장할 DTO 클래스를 생성 - dto.PageRequestDTO

         ```java
         @Builder
         @AllArgsConstructor
         @Data
         public class PageRequestDTO {
             // 페이징 처리를 위한 속성
             private int page;
             private int size;

             // 검색 관련 속성
             private String type;

             private String keywords;

             // page와 size 값이 없을 떄 사용할 기본값 설정을 위한 생성자
             public PageRequestDTO() {
                 this.page = 1;
                 this.size = 10;
             }

             // page와 size를 가지고 Pageable 객체를 생성해주는 메서드
             public Pageable getPageable(Sort sort) {
                 return PageRequest.of(page - 1, size, sort);
             }
         }
         ```

       - 게시글 목록 결과를 출력하기 위한 DTO 클래스 생성 - dto.PageResponseDTO

         ```java
         @Data
         public class PageResponseDTO<DTO, EN> {
             // 데이터 목록
             private List<DTO> dtoList;

             // 페이지 번호 관련 속성
             private int totalPage; // 전체 페이지 개수

             private int page; // 현재 페이지 번호

             private int size; // 페이지 당 데이터 출력 개수

             private int startPage, endPage; // 페이지 시작 번호와 끝 번호

             private boolean prev, next; // 이전과 다음 출력 여부

             private List<Integer> pageList; // 페이지 번호 목록
         }
         ```

       - 검색 결과를 가지고 데이터를 출력하기 위해 메소드 추가 - dto.PageResponseDTO

         ```java
         // 검색 결과(Page<Board>)를 가지고 데이터를 생성해주는 메서드
             public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn ) {
                 // 검색 결과 목록을 DTO의 List로 변환
                 dtoList = result.stream().map(fn).collect(Collectors.toList());

                 // 전체 페이지 개수 구하기
                 totalPage = result.getTotalPages();
                 // 페이지 번호 목록 관련 속성을 결정하는 메서드
                 makePageList(result.getPageable());
             }

             // 페이지 번호 목록 관련 속성을 결정하는 메서드
             private void makePageList(Pageable pageable) {
                 // 현재 페이지 번호
                 this.page = pageable.getPageNumber() + 1;
                 // 데이터 개수
                 this.size = pageable.getPageSize();

                 // 임시로 마지막 페이지 번호 계산
                 int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;
                 // 임시로 마지막 페이지 번호 계산
                 startPage = tempEnd - 9;
                 prev = startPage > 1;
                 // 마지막 페이지 번호 수정
                 endPage = totalPage > tempEnd ? tempEnd : totalPage;
                 // 다음 여부
                 next = totalPage > endPage;
                 // 페이지 번호 목록 만들기
                 pageList = IntStream.rangeClosed(startPage, endPage).boxed().collect(Collectors.toList());
             }
         ```

       - 게시글 목록보기 요청을 처리하기 위한 메서드를 BoardService 인터페이스에 선언
         ```java
         // 게시글 목록 보기
         PageResponseDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
         ```
       - 게시글 목록보기 요청을 처리하기 위한 구현체를 BoardServiceImpl에 작성

         ```java
         public PageResponseDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
                 log.info(pageRequestDTO);

                 // Entity를 DTO로 변경하는 람다 인스턴스 생성
                 Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0], (Member) en[1], (Long) en[2]));

                 // 목록 보기 요청 처리
                 Page<Object[]> result = boardRepository.getBoardWithReplyCount(
                         pageRequestDTO.getPageable(Sort.by("bno").descending())
                 );

                 return new PageResponseDTO<>(result, fn);
             }
         ```

       - ServiceTest 클래스에서 목록보기 메서드 테스트
         ```java
         // 목록 보기 테스트
             @Test
             public void testList() {
                 PageRequestDTO pageRequestDTO = new PageRequestDTO();
                 PageResponseDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
                 System.out.println(result);
             }
         ```

    6. 상세보기
       - BoardService 인터페이스 상세보기를 위한 메서드를 선언
         ```java
         // 게시글 상세 보기
         BoardDTO get(Long bno);
         ```
       - BoardServiceImpl 클래스에 상세보기를 위한 메서드를 구현
         ```java
         public BoardDTO get(Long bno) {
                 Object result = boardRepository.getBoardByBno(bno);
                 Object [] ar  = (Object[]) result;
                 return entityToDTO((Board) ar[0], (Member) ar[1], (Long) ar[2]);
             }
         ```
       - ServiceTest 클래스에 테스트 메서드를 구현
         ```java
         // 게시글 상세보기 테스트
             @Test
             public void testGet() {
                 Long bno = 100L;
                 BoardDTO boardDTO = boardService.get(bno);
                 System.out.println(boardDTO);
             }
         ```
    7. 게시물 삭제

       **부모 Entity에 이는 데이터를 삭제할 때는 자식 Entity의 데이터를 어떻게 할 것인지 고민해봐야 한다.**

       삭제를 할 것인지 아니면 삭제된 효과만 나타낼 것인지(외래키의 값을 null로 하거나 삭제 여부를 나타내는 필드를 추가해서 필드의 값을 변경하는 등)

       - ReplyRepository 클래스에 글 번호를 가지고 댓글을 삭제하는 메서드를 생성

         ```java
         public interface ReplyRepository extends JpaRepository<Reply, Long> {

             @Modifying
             @Query("delete from Reply r where r.board.bno = :bno")
             void deleteByBno(@Param("bno") Long bno);
         }
         ```

       - BoardService 인터페이스에 글번호를 가지고 댓글을 삭제하는 메서드를 선언
         ```java
         // 게시글 번호를 가지고 댓글을 삭제
             void removeWithReplies(Long bno);
         ```
       - BoardServiceImpl에 글번호를 가지고 댓글을 삭제하는 메서드를 구현
         ```java
         private final ReplyRepository replyRepository;
             // 2가지가 연쇄적으로 삭제되기 때문에 Transactional을 적용해야 한다.
             @Transactional
             public void removeWithReplies(Long bno) {
                 replyRepository.deleteByBno(bno); // 댓글 삭제
                 boardRepository.deleteById(bno); // 게시글 삭제
             }
         ```
         **@Transactional은 웬만헤서는 꼭 Service 계층!! Repository에 하면 안된다. Controller에서 해도 되지만 의미가 없다.**
       - Test
         ```java
         @Test
             public void testDelete() {
                 boardService.removeWithReplies(100L);
             }
         ```

    8. 게시물 수정

       Entity에는 setter를 만들지 않기 때문에 그냥은 수정이 안된다. 근데 Setter를 만들게 되면 bno나 writer에 불필요한 수정이 발생할 수 있기 때문에 그러면 안된다.

       따라서 Annotaion을 사용하지 말고 따로 메서드를 만들어준다.

       - Board Entity에 수정 가능한 항목의 setter메서드를 생성

         ```java
         // title을 수정하는 메서드
             public void changeTitle(String title) {
                 if(title == null || title.trim().length() == 0) {
                     this.title = "무제";
                     return;
                 }
                 this.title = title;
             }

             // content를 수정하는 메서드
             public void changeContent(String content) {
                 this.content = content;
             }
         ```

       - BoardService 인터페이스에 게시글 수정을 위한 메서드를 생성
         ```java
         // 게시글 수정
             Long modify(BoardDTO dto);
         ```
       - BoardServiceImpl 클래스에 게시글 수정을 위한 메서드를 생성

         ```java
         @Transactional
             public Long modify(BoardDTO dto) {
                 // JPA에서는 삽입과 수정 메서드가 동일
                 // upsert(있으면 수정 없으면 삽입)를 하고자 하는 경우는 save를 호출하면 되지만
                 // update를 하고자 하면 데이터가 있는지 확인한 후 수행하는 것이 좋다.

                 if (dto.getBno() == null) {
                     return 0L;
                 }
                 // 데이터 존재 여부를 확인
                 Optional<Board> board = boardRepository.findById(dto.getBno());
                 // 존재하는 경우
                 if (board.isPresent()) {
                     board.get().changeTitle(dto.getTitle());
                     board.get().changeContent(dto.getContent());
                     boardRepository.save(board.get());
                     return board.get().getBno();
                 } else {
                     return 0L;
                 }
             }
         ```

       - Test
         ```java
         @Test
             public void testUpdate() {
                 BoardDTO dto = BoardDTO.builder()
                         .bno(99L)
                         .title("제목 업데이트")
                         .content("내용 업데이트")
                         .build();
                 System.out.println(boardService.modify(dto));
             }
         ```

11. **Controller 계층**

    1. 공통된 디자인을 적용하는 작업
       - bootstrap sidebar의 파일을 static 디렉토리에 복사
       - templates 디렉토리에 layout 디렉토리를 생성하고 기본 레이아웃 파일인 basic.html을 복사
    2. 클라이언트의 요청을 받아서 필요한 서비스 로직을 호출하고 그 결과를 View에게 전달하는 PageController 클래스를 생성 - controller.BoardController

       ```java
       @Controller
       @Log4j2
       @RequiredArgsConstructor
       public class BoardController {
           private final BoardService boardService;

           // 기본 요청 생성
           @GetMapping({"/", "/board/list"})
           public String list(PageRequestDTO pageRequestDTO, Model model) {
               log.info("기본 목록 보기 요청");
               model.addAttribute("result", boardService.getList(pageRequestDTO));
               return "board/list";
           }
       }
       ```

    3. templates 디렉토리에 board 디렉토리를 생성하고 그 안에 기본 화면으로 사용할 list.html 생성

       ```java
       <!DOCTYPE html>
       <th:block th:replace="~{/layout/basic :: setContent(~{this::content})}">
           <th:block th:fragment="content">
               <h1 class="mt-4">
                   게시판
                   <span>
               <a th:href="@{/board/register}">
                 <button type="button" class="btn btn-primary">게시물 작성</button>
               </a>
             </span>
               </h1>
               <table class="table table-striped">
                   <thead>
                   <tr>
                       <th>글번호</th>
                       <th>제목</th>
                       <th>작성자</th>
                       <th>작성일</th>
                   </tr>
                   </thead>
                   <tbody>
                   <tr th:each="dto:${result.dtoList}">
                       <td>[[${dto.bno}]]</td>
                       <td>[[${dto.title}]]...
                           <span class="badge" th:text="${dto.replyCount}">
                           </span>
                       </td>
                       <td>[[${dto.writerName}]]</td>
                       <td>[[${#temporals.format(dto.regDate, 'yyyy-MM-dd')}]]</td>
                   </tr>
                   </tbody>
               </table>
               <ul class="pagination h-100 justify-content-center align-items-center">
                   <li class="page-item" th:if="${result.prev}">
                       <a class="page-link" th:href="@{/board/list(page=${result.startPage - 1})}">이전</a>
                   </li>
                   <li th:class="'page-item ' + ${result.page== page ? 'active': ''}" th:each="page:${result.pageList}">
                       <a class="page-link" th:href="@{/board/list(page=${page})}">
                           [[${page}]]
                       </a>
                   </li>
                   <li class="page-item" th:if="${result.next}">
                       <a class="page-link" th:href="@{/board/list(page=${result.endPage + 1})}">다음</a>
                   </li>
               </ul>
           </th:block>
       </th:block>
       ```

    4. 게시글 등록

       - 게시물 등록하기 위해 BoardController에 요청 처리 메서드 추가

         ```java
         // 게시물 등록 화면으로 이동하는 요청 - Fowarding
             @GetMapping("/board/register")
             public void register(Model model) {
                 log.info("등록 화면으로 포워딩");
             }

             // 게시물을 등록하는 요청 - Redirect
             // RedirectAttributes - 1회용 ㅔ션
             @PostMapping("/board/register")
             public String register(BoardDTO dto, RedirectAttributes rattr) {
                 // 파라미터 확인
                 log.info("dto: " + dto.toString());
                 // 데이터 삽입
                 Long bno = boardService.register(dto);
                 rattr.addFlashAttribute("msg", bno + "등록");

                 return "redirect:/board/list";
             }
         ```

       - templates/board 디렉토리에 게시물 등록을 위한 register.html 파일을 만들고 등록화면을 생성

         ```java
         <!DOCTYPE html>
         <html lang="en" xmlns:th="http://www.thymeleaf.org">

         <th:block th:replace="~{/layout/basic :: setContent(~{this::content} )}">
             <th:block th:fragment="content">
                 <h1 class="mt-4">Board Register Page</h1>

                 <form th:action="@{/board/register}" th:method="post">
                     <div class="form-group">
                         <label>Title</label>
                         <input type="text" class="form-control" name="title" placeholder="Enter Title">
                     </div>
                     <div class="form-group">
                         <label>Content</label>
                         <textarea class="form-control" rows="5" name="content"></textarea>
                     </div>
                     <div class="form-group">
                         <label>Writer Email</label>
                         <input type="email" class="form-control" name="writerEmail" placeholder="Writer Email ">
                     </div>
                     <button type="submit" class="btn btn-primary">Submit</button>
                 </form>

             </th:block>

         </th:block>
         ```

           <aside>
           💻 Foreign Key
           다른 테이블의 데이터를 참조하기 위해서 설정
           다른 테이블에서는 Primary Key이거나 Unique한 속성이어야 한다.
           
           참조 무결성 제약 조건
           FK는 null이거나 참조할 수 있는 값만 가져야 한다.
           다른 테이블에 없는 값을 가질 수 없다.
           JPA에서는 설정을 변경하지 않으면 null 불가능
           
           </aside>

    5. 상세 보기

       - list.html 파일에서 제목 부분에 상세보기 링크를 설정
         ```java
         ...
         <a th:href="@{/board/read(bno=${dto.bno}, page=${result.page})}">
         	[[${dto.title}]]...
         </a>
         ...
         ```
       - BoardController 클래스에 상세보기 요청을 처리하는 메서드를 구현
         ```java
         // ModelAttribute는 파라미터로 사용하면 넘겨받은 데이터를 결과에 그대로 전달할 목적으로 사용
             // 모델에 저장하지 않고 넘길 수 있다.
             @GetMapping("/board/read")
             public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
                 log.info("글 번호: " + bno);
                 BoardDTO dto = boardService.get(bno);
                 model.addAttribute("dto", dto);
             }
         ```
       - tempalates/board 디렉토리에 read.html 파일을 생성하고 작성

         ```java
         <!DOCTYPE html>
         <html lang="en" xmlns:th="http://www.thymeleaf.org">
         <th:block th:replace="~{/layout/basic :: setContent(~{this::content} )}">
             <th:block th:fragment="content">
                 <h1 class="mt-4">Board Read Page</h1>

                 <div class="form-group">
                     <label>Bno</label>
                     <input type="text" class="form-control" name="bno" th:value="${dto.bno}" readonly>
                 </div>
                 <div class="form-group">
                     <label>Title</label>
                     <input type="text" class="form-control" name="title" th:value="${dto.title}" readonly>
                 </div>
                 <div class="form-group">
                     <label>Content</label>
                     <textarea class="form-control" rows="5" name="content" readonly>[[${dto.content}]]</textarea>
                 </div>
                 <div class="form-group">
                     <label>Writer</label>
                     <input type="text" class="form-control" name="writer" th:value="${dto.writerName}" readonly>
                 </div>
                 <div class="form-group">
                     <label>RegDate</label>
                     <input type="text" class="form-control" name="regDate"
                            th:value="${#temporals.format(dto.regDate, 'yyyy/MM/dd HH:mm:ss')}" readonly>
                 </div>
                 <div class="form-group">
                     <label>ModDate</label>
                     <input type="text" class="form-control" name="modDate"
                            th:value="${#temporals.format(dto.modDate, 'yyyy/MM/dd HH:mm:ss')}" readonly>
                 </div>
                 <a th:href="@{/board/modify(bno = ${dto.bno}, page=${requestDTO.page}, type=${requestDTO.type})}">
                     <button type="button" class="btn btn-primary">수정</button>
                 </a>

                 <a th:href="@{/board/list(page=${requestDTO.page} , type=${requestDTO.type})}">
                     <button type="button" class="btn btn-info">목록</button>
                 </a>
             </th:block>
         </th:block>
         ```

    6. 수정과 삭제

       - 수정과 삭제는 상세보기에 작성
         로그인이 있다면 로그인한 유저와 작성한 유저의 기본키의 값이 같을 때 링크가 보이도록 설정
       - 수정 화면으로 이동하도록 하기 위한 처리를 BoardController에 추가 - 상세보기 요청을 수정
         ```java
         @GetMapping({"/board/read", "/board/modify"})
             public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
                 log.info("글 번호: " + bno);
                 BoardDTO dto = boardService.get(bno);
                 model.addAttribute("dto", dto);
             }
         ```
       - templates/board 디렉토리에 modify.html 추가

         ```java
         <!DOCTYPE html>
         <html lang="en" xmlns:th="http://www.thymeleaf.org">
         <th:block th:replace="~{/layout/basic :: setContent(~{this::content} )}">
             <th:block th:fragment="content">
                 <h1 class="mt-4">Board Read Page</h1>

                 <form action="/board/modify" method="post" id="form">
                     <!-- 화면에 보이지는 않지만 form의 데이터를 전송할 때 서버에게 전달은 된다. -->
                     <input type="hidden" name="page" th:value="${requestDTO.page}"/>
                     <div class="form-group">
                         <label>Bno</label>
                         <input type="text" class="form-control" name="bno" th:value="${dto.bno}" readonly>
                     </div>
                     <div class="form-group">
                         <label>Title</label>
                         <input type="text" class="form-control" name="title" th:value="${dto.title}">
                     </div>
                     <div class="form-group">
                         <label>Content</label>
                         <textarea class="form-control" rows="5" name="content">[[${dto.content}]]</textarea>
                     </div>
                     <div class="form-group">
                         <label>Writer</label>
                         <input type="text" class="form-control" name="writer" th:value="${dto.writerName}" readonly>
                     </div>
                     <div class="form-group">
                         <label>RegDate</label>
                         <input type="text" class="form-control"
                                th:value="${#temporals.format(dto.regDate, 'yyyy/MM/dd HH:mm:ss')}" readonly>
                     </div>
                     <div class="form-group">
                         <label>ModDate</label>
                         <input type="text" class="form-control"
                                th:value="${#temporals.format(dto.modDate, 'yyyy/MM/dd HH:mm:ss')}" readonly>
                     </div>
                 </form>
                 <button type="button" class="btn btn-primary" id="modifyBtn">완료</button>

                 <a th:href="@{/board/list(page=${requestDTO.page} , type=${requestDTO.type})}">
                     <button type="button" class="btn btn-info">목록</button>
                 </a>
                 <script th:inline="javascript">
                     // form 찾아오기
                     let actionForm = document.getElementById("form");

                     // 완료 버튼 누를 때
                     document.getElementById("modifyBtn").addEventListener("click", (e) => {
                         if (!confirm("수정하시겠습니까?")) {
                             return;
                         }
                         // form의 데이터 전송
                         actionForm.action="/board/modify";
                         actionForm.method="post";
                         actionForm.submit();
                     })
                 </script>
             </th:block>
         </th:block>
         ```

       - 수정을 위한 Controller - **bno와 page 정보를 같이 가져가야 한다**

         ```java
         @PostMapping("/board/modify")
             public String modify(
                     BoardDTO dto,
                     @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                     RedirectAttributes redirectAttributes
             ) {
                 log.info("dto: " + dto.toString());
                 boardService.modify(dto);
                 redirectAttributes.addFlashAttribute("bno", dto.getBno());
                 redirectAttributes.addFlashAttribute("page", requestDTO.getPage());

                 return "redirect:/board/read?bno=" + dto.getBno() + "&page=" + requestDTO.getPage();
             }
         ```

       - modify.html 파일에 삭제를 위한 버튼 및 수행을 위한 스크립트 코드를 추가

         ```java
         ...
         <button type="button" class="btn btn-warning" id="removeBtn">삭제</button>
         ...
         <script>
         ...
         document.getElementById("removeBtn").addEventListener("click", (e) => {
                         if (!confirm("삭제하시겠습니까?")) {
                             return;
                         }

                         actionForm.action="/board/remove";
                         actionForm.method="post";
                         actionForm.submit();
                     })
         </script>
         ```

       - BoardCongtroller 클래스에 삭제 요청을 처리

         ```java
         @PostMapping("/board/remove")
             public String remove(
                     BoardDTO dto,
                     RedirectAttributes redirectAttributes
             ) {
                 log.info("dto: " + dto.toString());

                 // 삭제
                 boardService.removeWithReplies(dto.getBno());
                 redirectAttributes.addFlashAttribute("msg", dto.getBno() + " 삭제");
                 return "redirect:/board/list";
             }
         ```

12. **게시글 검색 구현**

    게시글 + 댓글, 제목만, 글작성자, 댓글내용, 댓글 작성자 등은 파라미터로 처리가 불가하기 때문에 동적 쿼리를 사용해야 한다.

    1. 동적 쿼리 생성을 위해서 **Querydsl** 사용을 위한 설정

       querydsl을 설정할 떄는 springboot 버전을 확인

       - 2.5
       - 2.6, 2.7
       - 3.0 이상
       - 3.0 이상인 경우 build.gradle 파일의 dependencies에 추가
         ```java
         ...
         dependencies {
             // == 스프링 부트 3.0 이상 ==
             implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
             annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
             annotationProcessor "jakarta.annotation:jakarta.annotation-api"
             annotationProcessor "jakarta.persistence:jakarta.persistence-api"
         ```
       - 검색을 위한 메서드를 소유할 Repository 인터페이스 생성 - SearchBoardRepository
         ```java
         public interface SearchBoardRepository {
             Board search1();
         }
         ```
       - SearchBoardRepository 인터페이스를 implements한 클래스를 만들어서 메서드를 구현 - SearchBoardRepositoryImpl

         ```java
         public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

             // QuerydslRepositorySupport 클래스에 Default Contructor가 없기 때문에
             // Contructor를 직접 생성해서 필요한 Contructor를 호출해줘야 한다. 그렇지 않으면 에러
             // 검색에 사용할 Entity 클래스를 대입해주어야 한다.
             public SearchBoardRepositoryImpl() {
                 super(Board.class);
             }
             @Override
             public Board search1() {
                 // JPQL을 동적으로 생성해서 실행
                 QBoard board = QBoard.board;

                 // 쿼리 작성
                 JPQLQuery<Board> jpqlQuery = from(board);
                 // bno가 1인 데이터를 조회
                 jpqlQuery.select(board).where(board.bno.eq(1L));
                 // jpql을 실행시킨 결과 가져오기
                 List<Board> result = jpqlQuery.fetch();

                 return null;
             }
         }
         ```

       - **JPA에서 쿼리를 실행하는 방법**
         - JPARepository가 제공하는 기본 메서드 사용
           테이블 전체 조회, 테이블에서 기본키를 가지고 하나의 데이터 조회, 삽입, 기본키를 이용해서 조건을 만들어 수정, 기본키를 이용해서 삭제, Entity를 이용한 삭제
         - Query Method
           Repository 인터페이스에 하나의 테이블에 대한 검색 및 삭제 메서드를 이름으로 생성
         - @Query를 이용한 JPQL이나 Native SQL을 작성해서 사용
           위에 두개의 방법은 하나의 테이블에서만 가능하기 때문에 거기서 못하는 것들을 가능
         - Querydsl
           세 번쨰 방법에서 무엇이 다르냐.
           JPQL을 문자열로 작성하지 않고 Java 코드로 작성
           문자열을 사용하지 않으므로 컴파일할 때 오류를 확인하는 것이 가능하다.
           자바 코드를 이용하기때문에 조건문을 이용할 수 있어서 동적 쿼리를 생성하는 것이 편리함.
           동적 쿼리는 상황에 따라 컬럼 이름이나 테이블이름이 변경되는 쿼리
       - BoardRepository 인터페이스의 선언 부분을 변경
         **이 부분이 중요한데, Proxy 패턴이다. 구현체는 SearchBoardRepositoryImpl에서 만들었는데 interface인 SearchBoardRepository를 상속받으면 search1 메서드를 사용할 수 있다. Spring에서 객체 지향적인 구조를 깨버린 그런 것인데 사용할 줄 아는 것에 익숙해지자.**
         ```java
         public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
         ...
         }
         ```
       - RepositoryTest에 테스트 추가
         ```java
         @Test
             public void testSearch1() {
                 boardRepository.search1();
             }
         ```
       - join을 수행해주는 메서드로는 leftJoin이 있다.
         on 메서드에 조인 조건을 작성한다.
       - Board와 Reply를 Join(동일한 board를 가진 애들만 찾아오는 방법) - 예시
         ```java
         Qboard board = Qboard.board;
         QReply reply = Qreply.reply;
         JPQLQuery<Board> jpqlQuery = from(board);
         jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
         ```
       - 목록 보기에 사용할 쿼리를 생성하고 테스트 - SearchBoardRepositoryImpl 클래스의 search1 메서드를 수정하고 테스트 수행

         ```java
         public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

             // QuerydslRepositorySupport 클래스에 Default Contructor가 없기 때문에
             // Contructor를 직접 생성해서 필요한 Contructor를 호출해줘야 한다. 그렇지 않으면 에러
             // 검색에 사용할 Entity 클래스를 대입해주어야 한다.
             public SearchBoardRepositoryImpl() {
                 super(Board.class);
             }
             @Override
             public Board search1() {
                 // JPQL을 동적으로 생성해서 실행
                 QBoard board = QBoard.board;
                 QReply reply = QReply.reply;
                 QMember member = QMember.member;

                 // 쿼리 작성
                 JPQLQuery<Board> jpqlQuery = from(board);
                 // 외래키가 writer이기 때문.
                 jpqlQuery.leftJoin(member).on(board.writer.eq(member));
                 // reply와 join
                 // 외래키는 reply의 board
                 jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

                 // 게시글 번호 별로 묶어서 board와 member의 email, 그리고 reply의 개수를 가져오기
                 jpqlQuery.select(board, member.email, reply.count()).groupBy(board);

                 // jpql을 실행시킨 결과 가져오기
         				// 실행 자체는 fetch가 하는 것임.
                 List<Board> result = jpqlQuery.fetch();
                 System.out.println(result);

                 return null;
             }
         }
         ```

       - 실행한 구 결과를 확인해보면 List 타입의 List
         안쪽 List의 첫 번쨰 요소가 board 객체이고, 두 번째는 [member.email](http://member.email) 에 해당하는 문자열이고 세 번째는 reply..count()에 해당하는 데이터이다.
       - Tuple
         관계형 데이터베이스에서 하나의 행(row)를 Tuple 이이라고 한다.
         특정 언어들에서는 Tuple을 자료형으로 제공하는데 여러 개의 데이터를 묶어서 하나의 데이터를 표현하기 위한 자료형
         가자 유사한 형태가 Structer(구조체)이다.
         Java에서는 Tuple이라는 자료형이 제공되지 않지만 Spring 에서 제공을 한다.
       - SearchBoardRepositoryImpl 에서 Tuple 타입으로 변경하고 테스트에서 확인

         ```java
         public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

             // QuerydslRepositorySupport 클래스에 Default Contructor가 없기 때문에
             // Contructor를 직접 생성해서 필요한 Contructor를 호출해줘야 한다. 그렇지 않으면 에러
             // 검색에 사용할 Entity 클래스를 대입해주어야 한다.
             public SearchBoardRepositoryImpl() {
                 super(Board.class);
             }
             @Override
             public Board search1() {
         				...
                 /*
                 // 게시글 번호 별로 묶어서 board와 member의 email, 그리고 reply의 개수를 가져오기
                 jpqlQuery.select(board, member.email, reply.count()).groupBy(board);
                 // jpql을 실행시킨 결과 가져오기
                 // 실행 자체는 fetch가 한다.
                 List<Board> result = jpqlQuery.fetch();
                  */
                 JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
                 tuple.groupBy(board);
                 List<Tuple> result = tuple.fetch();

                 System.out.println(result);
                 return null;
             }
         }
         ```

    2. 이제 진짜 검색을 구현

       - SearchBoardRepository 인터페이스에 Pageable과 검색 항복을 매개변수로 해서 페이지 단위로

         ```java
         interface SearchBoardRepository {
             Board search1();

             // 검색을 위한 메서드
             Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
         }
         ```

       - 검색 조건
         - 제목 검색 - t
         - 작성자 검색 - w
         - 내용 - c
         - 제목 + 내용: tc
         - 제목 + 작성자: tw
       - SearchBoardRepositoryImpl 클래스에 키워드에 따라 검색 결과를 반환하는 메서드를 구현

         ```java
         public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
                 QBoard board = QBoard.board;
                 QMember member = QMember.member;
                 QReply reply = QReply.reply;

                 JPQLQuery<Board> jpqlQuery = from(board);
                 jpqlQuery.leftJoin(member).on(board.writer.eq(member));
                 jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

                 JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
                 // 조건 생성
                 BooleanBuilder booleanBuilder = new BooleanBuilder();
                 BooleanExpression expression = board.bno.gt(0L); // bno가 0보다 큰
                 booleanBuilder.and(expression);

                 // 타입에 따른 조건 생성
                 if (type != null) {
                     // 글자 단위로 쪼개기
                     String[] typeArr = type.split("");
                     BooleanBuilder conditionBuilder = new BooleanBuilder();
                     for(String t : typeArr) {
                        switch(t) {
                            case "t":
                                conditionBuilder.or(board.title.contains(keyword));
                                break;
                            case "c":
                                conditionBuilder.or(board.content.contains(keyword));
                                break;
                            case "w":
                                conditionBuilder.or(member.email.contains(keyword));
                                break;
                        }
                     }
                     booleanBuilder.and(conditionBuilder);
                 }
                 // 조건을 tuple에 적용
                 tuple.where(booleanBuilder);

                 // 정렬 방법 설정
                 tuple.orderBy(board.bno.desc());

                 // 그룹화
                 tuple.groupBy(board);

                 // page 처리
                 tuple.offset(pageable.getOffset());
                 tuple.limit(pageable.getPageSize());

                 // 데이터 가져오기
                 List<Tuple> result = tuple.fetch();

                 // return
                 return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, tuple.fetchCount());
             }
         ```

       - 검색 테스트

         ```java
         @Test
             public void testSearch() {
                 Pageable pageable = PageRequest.of(0, 10, Sort.by("bno")
                         .descending()
                         .and(Sort.by("title").ascending()));

                 Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);

                 for(Object[] row: result.getContent()) {
                     System.out.println(Arrays.toString(row));
                 }
             }
         ```

       - PageRequestDTO 클래스에 type과 keyword가 만들어졌는지(?) 확인
       - list.html 파일에 검색 폼을 추가 - 코드 참고.
       - read.html 파일의 링크 수정 - 코드 참고
       - modify.html 파일의 폼에 hidden 추가하고 스크립트 코드 수정 - 코드 참고
       - BoardController에서 데이터 수정을 처리하는 메서드를 수정
         ```java
         return "redirect:/board/read?bno=" + dto.getBno() + "&page=" + requestDTO.getPage() + "$type=" + requestDTO.getType() + "$keyword=" + requestDTO.getKeyword();
         ```
       - BoardServiceImpl 에서 목록 보기

         ```java
         // 목록 보기 요청 처리
                 /*
                 Page<Object[]> result = boardRepository.getBoardWithReplyCount(
                         pageRequestDTO.getPageable(Sort.by("bno").descending())
                 );
                  */

                 Page<Object[]> result = boardRepository.searchPage(
                         pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending())
                 );
         ```

13. **댓글 처리**

    댓글 작업은 RestController 이용

    - 조회
      GET, /replies/board/{bno}, 댓글 배열을 포함한 객체를 반환
    - 작성
      POST, /replies, JSON 문자열, 추가된 댓글 번호 - 객체
    - 삭제
      DELETE, /replies/{rno}, 댓글번호, 삭제된 댓글 번호 - 객체
    - 수정
      PUT, /replies/{rno}, 댓글번호 + 내용, 수정된 댓글 번호 - 객체

    1. ReplyRepository 인터페이스에 게시글 번호를 가지고 댓글을 찾아오는 메서드를 선언
    2. 테스트

       ```java
       @Test
       public voidtestListReply() {
       List<Reply> replyList = replyRepository.findByBoardOrderByRno(
                   Board.builder().bno(27L).build()
           );

           replyList.forEach(reply -> {
               System.out.println(reply);
           });

       ```

    3. 댓글 서비스와 Controller 그리고 View 사이에서 데이터를 전달하기 위한 DTO 클래스를 생성 - dto.ReplyDTO

       ```java
       @Data
       @AllArgsConstructor
       @NoArgsConstructor
       @Builder
       public class ReplyDTO {
           private Long rno;
           private String text;
           private String replyer;
           private Long bno;
           private LocalDateTime regDate;
           private LocalDateTime modDate;
       }
       ```

    4. 댓글을 처리하기 위한 메서드를 소유한 ReplyService 인터페이스

       ```java
       public interfaceReplyService{
       //댓글 등록
       Long register(ReplyDTO replyDTO);

       //댓글 목록
       List<ReplyDTO> getList(Long bno);

       //댓글 수정
       Long modify(ReplyDTO replyDTO);

       //댓글 삭제
       Long remove(Long rno);

       // ReplyDTO를 Reply Entity로 변환해주는 메서드
       defaultReply dtoToEntity(ReplyDTO dto) {
               Board board = Board.builder().bno(dto.getBno()).build();
               Reply reply = Reply.builder()
                       .text(dto.getText())
                       .replyer(dto.getReplyer())
                       .board(board)
                       .build();
       returnreply;
           }

       // Reply Entity를 ReplyDTO로 변환해주는 메서드
       defaultReplyDTO entityToDTO(Reply reply) {
               ReplyDTO dto = ReplyDTO.builder()
                       .rno(reply.getRno())
                       .text(reply.getText())
                       .replyer(reply.getReplyer())
                       .regDate(reply.getRegDate())
                       .modDate(reply.getModDate())
                       .build();
       returndto;
           }
       }

       ```

    5. 댓글 요청을 처리하기 위한 메서도를 소유한 ReplyServiceImpl 클래스

       ```java
       @Service
       @RequiredArgsConstructor
       public class ReplyServiceImpl implements ReplyService {
           private final ReplyRepository replyRepository;

           @Override
           public Long register(ReplyDTO replyDTO) {
               Reply reply = dtoToEntity(replyDTO);
               replyRepository.save(reply);
               return reply.getRno();
           }

           @Override
           public List<ReplyDTO> getList(Long bno) {
               // 글 번호를 받아야 한다.
               List<Reply> result = replyRepository.findByBoardOrderByRno(Board.builder()
                       .bno(bno).build());

               // result의 내용을 정렬하기 - 수정한 시간의 내림차순 (modDate descending으로 Sorting에서 DB에서 가져와도 됨.)
               result.sort(new Comparator<Reply>() {
                   @Override
                   public int compare(Reply o1, Reply o2) {
                       return o2.getModDate().compareTo(o1.getModDate());
                   }
               });

               // Reply의 List를 ReplyDTO의 List로 변경
               return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
           }

           @Override
           public Long modify(ReplyDTO replyDTO) {
               Reply reply = dtoToEntity(replyDTO);
               replyRepository.save(reply);
               return reply.getRno();
           }

           @Override
           public Long remove(Long rno) {
               replyRepository.deleteById(rno);
               return rno;
           }
       }
       ```

    6. 테스트
       - 글 번호를 가지고 댓글 목록 가져오기
         ```java
         @Test
         public voidtestGetList() {
         //게시글 번호를 이용해서 댓글 가져오기
         List<ReplyDTO> list = replyService.getList(27L);
             list.forEach(dto -> System.out.println(dto));
         }
         ```
       - 댓글 삽입 테스트
         ```java
         @Test
             public void insertReply() {
                 ReplyDTO dto = ReplyDTO.builder()
                         .text("댓글 삽입 테스트")
                         .replyer("user1@kakao.com")
                         .bno(27L)
                         .build();
                 System.out.println(replyService.register(dto));
             }
         ```
    7. 게시글 화면에 댓글 출력

       - 게시글 처리를 위한 Controller를 생성하고 게시글 번호에 해당하는 댓글을 리턴해주는 메서드를 생성

         ```java
         @RestController
         @Log4j2
         @RequiredArgsConstructor
         @RequestMapping("/replies")//공통 URL설정
         public classReplyController {
         private finalReplyServicereplyService;

         //게시글 번호를 가지고 댓글을 리턴해주는 메서드
         @GetMapping(value="/board/{bno}")
         publicResponseEntity<List<ReplyDTO>> getByBoard(@PathVariable("bno") Long bno) {
         log.info("bno: " + bno);
         // JSON으로 주기
         return newResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
             }
         }
         ```

       - read.html 파일에 댓글을 출력하는 영역을 생성
         ```html
         <div>
           <div class="mt-4">
             <h5 id="replyCount">
               댓글
               <span class="badge"> [[${dto.replyCount}]] </span>
             </h5>
           </div>
           <div class="list-group replyList" id="replyList"></div>
         </div>
         ```
       - read.html 파일에 댓글 개수를 출력하는 영역을 클릭하면 댓글을 replyList 순으로 출력하는 영역 생성

         ```jsx
         <script th:inline="javascript">
                     window.addEventListener("load", (e) => {
                         // 게시글 번호 찾아오기
                         let bno = [[${dto.bno}]];

                         // 댓글이 추가딜 영역
                         let listGroup = document.getElementById("replyList");

                         // 날짜 출력 함수
                         let formatTime = (str) => {
                             let date = new Date(str);

                             return date.getFullYear()
                                 + '/' + (date.getMonth() + 1)
                                 + '/' + date.getDate()
                                 + ' ' + date.getHours()
                                 + ':' + date.getMinutes();
                         }

                         // 글번호에 해당하는 데이터를 가져와서 replyList에 출력하는 함수
                         let loadJSONData = () => {
                             fetch('/replies/board/' + bno).then((response) => response.json())
                                 .then((arr) => {
                                     // 출력할 내요을 저장할 변수
                                     let str = "";
                                     for (const reply of arr) {
                                         str += '<div class="card-body" data-rno="' + reply.rno + '"><b>' + reply.rno + '</b>';
                                         str += '<h5 class="card-title">' + reply.text + '</h5>';
                                         str += '<h5 class="card-title">' + reply.replyer + '</h5>';
                                         str += '<p class="card-text">' + formatTime(reply.regDate) + '</p>';
                                         str += '</div>';
                                     }

                                     listGroup.innerHTML = str;
                                 })
                         }

                         // 댓글 개수 출력 영역을 클릭하면
                         document.getElementById("replyCount")
                             .addEventListener('click', (e) => {
                                 // 댓글 출력 함수를 호출
                                 // loadJSONData();
                             })
                         // 게시글이 출력되자마자 댓글도 출력하고자 하는 경우
                         loadJSONData();
                     })
                 </script>
         ```

    8. 댓글 추가
       - 댓글 추가 및 삭제 그리고 수정을 위한 모달(Modal - 대화 상자를 제거하기 전에는 원래 영역으로 돌아갈 수 없는 창, HTML에서는 팝업 창같은 대화상자) 창을
         ```html
         <div class="modal" tabindex="-1" role="dialog">
           <div class="modal-dialog" role="document">
             <div class="modal-conent">
               <div class="modal-header">
                 <h5 class="modal-title">댓글 작업</h5>
               </div>
               <div class="modal-body">
                 <div class="form-group">
                   <input
                     class="form-control"
                     type="text"
                     name="replyText"
                     placeholder="댓글 작성"
                   />
                 </div>
                 <div class="form-group">
                   <input
                     class="form-control"
                     type="text"
                     name="replyer"
                     placeholder="작성자..."
                   />
                   <input type="hidden" name="rno" />
                 </div>
               </div>
               <div class="modal-footer">
                 <button type="button" class="btn btn-danger replyRemove">
                   삭제
                 </button>
                 <button type="button" class="btn btn-warning replyModify">
                   수정
                 </button>
                 <button type="button" class="btn btn-primary replySave">
                   추가
                 </button>
                 <button
                   type="button"
                   class="btn btn-secondary replyClose"
                   data-dismiss="modal"
                 >
                   닫기
                 </button>
               </div>
             </div>
           </div>
         </div>
         ```
       - ReplyController 클래스에 댓글 추가를 위한 메서드를 생성
         ```java
         //댓글 추가 요청 처리
         @PostMapping("")
         public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
         log.info(replyDTO);
             Long rno = replyService.register(replyDTO);
         return newResponseEntity<>(rno, HttpStatus.OK);
         }
         ```
       - read.html 파일에 댓글 작성 버튼을 추가 - 코드참고
       - 이후 jQuery를 이용해 모달창에서 댓글 추가, 수정 코드 추가 - 코드 참고
    9. 댓글 삭제
       - ReplyController 클래스에 댓글 삭제 요청 처리 메서드를 작성
         ```java
         //댓글 삭제 요청 처리
         @DeleteMapping("/{rno}")
         public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
         log.info("Rno:" + rno);
             replyService.remove(rno);
         return newResponseEntity<>(rno + " 삭제", HttpStatus.OK);
         }
         ```
       - read.html 파일에 대화상자의 삭제 버튼을 눌렀을 때 수행할 스크립트 코드를 추가
    10. 댓글 수정
        - ReplyController 클래스에 댓글 수정 요청을 처리하는 메서드 추가
          ```java
          //댓글 수정 요청 처리
          @PutMapping("/{rno}")
          public ResponseEntity<Long> modify(@RequestBody ReplyDTO replyDTO){
          log.info(replyDTO);
              Long rno = replyService.modify(replyDTO);
          return newResponseEntity<>(rno, HttpStatus.OK);
          }
          ```
        - html 추가
