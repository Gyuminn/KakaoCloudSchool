# [Spring Boot] M:N 관계

1. **M:N 관계**

   1. M:N 관계

      테이블의 하나의 행이 다른 테이블의 여러 개의 행과 매핑이 되고 반대로 상대방 테이블에서 하나의 행이 다시 여러 개의 행과 매핑이 되는 관계

      - 쇼핑몰에서 회원 정보와 상품 정보의 관계
        한 명의 회원은 여러 종류의 상품을 구매할 수 있음
        하나의 상품은 여러 회원이 구매가 가능.
      - 영화와 회원 사이의 리뷰 관계
        하나의 영화에 여러 회원이 리뷰를 남길 수 있고 한 명의 회원이 여러 영화에 리뷰를 남길 수 있음.

   2. 관계형 데이터베이스에서 M:N 관계 표현
      - 관계형 데이터베이스는 다대다 관계를 포함하지 못함.
      - 다대다 관계의 경우는 1:M 관계 2개로 분할해서 표현 - 별도의 테이블을 만들어서 처리
      - 별도의 테이블을 만들고 이 테이블에서 2개의 테이블의 기본키를 외래키로 소유
   3. JPA에서의 M:N 관계 표현

      - JPA 에서는 ManyToMany로 다대다 관계 표현이 가능 - 실제로는 거의 사용하지 않음.

        ```java
        class Member {
        	Movie[]
        }

        class Movie{
        	Member[]
        }
        ```

        이렇게 만들면 한 명의 회원이 하나의 영화에 대해서 리뷰를 작성하게 되면 2곳에서 데이터 삽입이 이루어져야 한다.
        양쪽 데이터의 불일치가 발생할 가능성이 있다.

   4. **다대다 매핑 기준**
      - 연결 테이블의 기본키 설정
        양쪽 테이블의 기본키를 받아서 생성한 외래키로 생성하고 이 기본키의 조합을 기본키로 사용 - 식별 관계
        별도의 키를 만들어서 기본키로 사용 - 비식별 관계
      - 별도의 클래스 생성 여부
        별도의 클래스를 생성해서 사용
        별도의 클래스를 만들지 않고 @ManyToMany와 @JoinTable을 이용 - 잘 안씀.

2. **영화 리뷰 프로젝트**
   - 회원 정보
   - 영화 정보
     - 영호 제목(기본 키가 될 수 있는가? - 절대로 똑같은게 없는가.)
     - 첨부 파일(여러 개 가능)
       영화 아이디를 만들어서 영화 아이디와 영화 제목을 갖는 테이블을 생성하고, 영화 아이디와 첨부 파일을 갖는 별도의 테이블을 생성
   - 리뷰 정보
     - 리뷰 번호
       영화와 1: N
       회원과 1: N
     - 평점
     - 감상문
3. **프로젝트 기본 설정**
   1. lombok, spring web, thymeleef, jpa, 데이터베이스 드라이버
   2. application.yml 설정
   3. 실행 클래스에 데이터베이스 감시 어노테이션인 @EnableJpaAuditing
4. **Entity 작업**

   1. 여러 곳에서 공통으로 사용되는 속성을 가진 Entity 생성 - domain.BaseEntity

      ```java
      @MappedSuperclass
      @EntityListeners(value={AuditingEntityListener.class})
      @Getter
      abstract classBaseEntity {
          @CreatedDate
          @Column(name="regdate", updatable =false)
      private LocalDateTime regDate;

          @LastModifiedDate
          @Column(name="moddate")
      private LocalDateTime modDate;
      }

      ```

   2. domain.Movie

      ```java
      @Entity
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      @ToString
      public classMovieextendsBaseEntity {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
      private Long mno;

      private String title;
      }
      ```

   3. domain.MovieImage

      ```java
      package com.kakao.reviewapp0116.domain;

      import jakarta.persistence.*;
      import lombok.*;

      @Entity
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      // toString을 할 때 movie는 제외
      // 지연 생성이기 때문에 get을 하지 않은 상태에서 toString을 호출하면
      // NullPointerException이 발생
      @ToString(exclude = "movie")
      // 부모 테이블을 만들 때 이 속성의 값을 포함시켜 생성해주세요 라는 뜻.
      // 컬럼의 어노테이션에 @Embedded를 추가하면 부모 테이블이 생성될때 들어감.
      // 1대 다 관계에서는 많이 사용하지 않고 1대 1관게에서 테이블을 나누어 설계할 때 주로 사용된다.
      @Embeddable
      public class MovieImage {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          private Long inum;

          private String uuid; // 파일의 이름이 겹치지 않도록 하기 위해서 추가

          private String imgName; // 파일 이름

          // 하나의 디렉토리에 너무 많은 파일이 저장되지 않도록 업로드한 날짜 별로 파일을 기록하기 위한 디렉토리 이름
          // (aws의 경우는 안만들어도 됨.)
          private String path;

          // 하나의 Movie가 여러 개의 MovieImage를 소유
          // 데이터를 불러올 때 movie를 불러오지는 않고 사용할 때 불러옴.
          // 외래키의 이름은 안쓰면 movie_mno로 만들어진다.
          @ManyToOne(fetch = FetchType.LAZY)
          private Movie movie;
      }
      ```

   4. domain.Member

      ```java
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
      ```

   5. domian.Review

      ```java
      package com.kakao.reviewapp0116.domain;

      import jakarta.persistence.*;
      import lombok.*;

      @Entity
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @Getter
      @ToString(exclude = {"movie", "member"})
      public class Review extends BaseEntity {
          @Id
          @GeneratedValue(strategy = GenerationType.AUTO)
          private Long reviewnum;

          @ManyToOne(fetch = FetchType.LAZY)
          private Movie movie;

          @ManyToOne(fetch = FetchType.LAZY)
          private Member member;

          private int grade;

          private String text;
      }
      ```

5. **Repository 인터페이스 생성**

   1. Movie 관련 Repository - persistence.MovieRepository

      ```java
      public interface MovieRepository extends JpaRepository<Movie, Long> {
      }
      ```

   2. persistence.MovieImageRepository

      ```java
      public interface MovieRepository extends JpaRepository<Movie, Long> {
      }
      ```

   3. persistence.MemberRepository

      ```java
      public interface MemberRepository extends JpaRepository<Member, Long> {
      }
      ```

   4. persistence.ReviewRepository

      ```java
      public interface ReviewRepository extends JpaRepository<Review, Long> {
      }
      ```

6. **샘플 데이터 삽입 - Test 클래스에서 수행**

   1. Movie 데이터 삽입

      **MovieImage의 삽입은 Movie의 삽입과 동시에 이루어진다.**

      ```java
      @SpringBootTest
      public class RepositoryTests {
          @Autowired
          private MovieRepository movieRepository;

          @Autowired
          private MovieImageRepository movieImageRepository;

          @Test
          public void insertMovie() {
              // 영화 100개 생성 후 삽입
              IntStream.rangeClosed(1, 100).forEach(i -> {
                  Movie movie = Movie.builder()
                          .title("Movie..." + i)
                          .build();
                  movieRepository.save(movie);

                  int count = (int)(Math.random() * 5) + 1; // 1, 2, 3, 4
                  for(int j = 0; j<count; j++) {
                      MovieImage movieImage = MovieImage.builder()
                              .uuid(UUID.randomUUID().toString())
                              .movie(movie)
                              .imgName("test" + j + ".jpg")
                              .build();
                      movieImageRepository.save(movieImage);
                  }
              });
          }
      }
      ```

   2. Member 데이터 삽입

      ```java
      @Autowired
          private MemberRepository memberRepository;

          @Test
          public void insertMember() {
              IntStream.rangeClosed(1, 100).forEach(i -> {
                  Member member = Member.builder()
                          .email("r" + i + "@gmail.com")
                          .pw("1111")
                          .nickname("reviewer" + i)
                          .build();
                  memberRepository.save(member);
              });
          }
      ```

   3. Review 데이터 삽입

      외래키를 가지고 있으니 잘 집어넣어야 한다.

      ```java
      @Test
          public void insertMovieReview() {
              IntStream.rangeClosed(1, 200).forEach(i -> {
                  // 영화 번호
                  Long mno = (long) (Math.random() * 100) + 1;
                  // 회원 번호
                  Long mid = (long) (Math.random() * 100) + 1;

                  Movie movie = Movie.builder().mno(mno).build();
                  Member member = Member.builder().mid(mid).build();

                  Review review = Review.builder()
                          .movie(movie)
                          .member(member)
                          .grade((int) (Math.random() * 5) + 1)
                          .text("영화 느낌...." + i)
                          .build();

                  reviewRepository.save(review);
              });
          }
      ```

7. **메인 화면 출력을 위한 쿼리 연습**

   1. 영화 정보를 가지고 영화 이미지 정보와 리뷰 개수 및 grade의 평균을 구해주는 메서드 - 페이지 단위로 구현
      - **SQL을 먼저 생각해보자!**
        ```sql
        # 영화 정보, 영화에 대한 이미지 정보, 리뷰 개수, 평점 평균
        # 평균, 합계 등을 구할 때 조심해야 할 것은 join할 때 review.grade의 평균이 0인 영화는 안나와버린다.
        # 따라서 LEFT OUTER JOIN을 해줘야 하는데, 이때 coalesce 를 써주면 null일 경우 0을 출력해준다!!
        select m.mno, avg(coalesce(review.grade,0)), count(review.reviewnum) from movie m, movie_image mi, review where m.mno = mi.movie_mno and m.mno = review.movie_mno group by m.mno;
        ```
      - MovieRepository
        Movie 정보를 가지고 연관된 MovieImage와 Review의 개수, Review에서 grade의 평균을 구해주는쿼리
        ```java
        public interface MovieRepository extends JpaRepository<Movie, Long> {
            @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r.reviewnum) " +
                    "from Movie m " +
                    "left outer join MovieImage mi on mi.movie = m " +
                    "left outer join Review r on r.movie = m " +
                    "group by m")
            public Page<Object[]> getList(Pageable pageable);
        }
        ```
      - 테스트
        ```java
        @Test
            // JOIN 연습
            public void joinTest() {
                Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
                Page<Object[]> result = movieRepository.getList(pageable);
                for (Object[] objects : result.getContent()) {
                    System.out.println(Arrays.toString(objects));
                }
            }
        ```
   2. 영화 상세보기를 위해서 특정 영화 아이디를 이용해서 위와 동일한 데이터를 찾아오기
      - 쿼리를 MovieRepository에 선언
        ```java
        // 틀정 영화 정보를 가지고 영화 이미지 정보와 리뷰 개수 및 grade의 평균을 구해주는 메서드
            @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
                    "from Movie m " +
                    "left outer join MovieImage mi on mi.movie = m " +
                    "left outer join Review r on r.movie = m " +
                    "where m.mno = :mno " +
                    "group by m")
            List<Object[]> getMovieWithAll(@Param("mno") Long mno);
        ```
      - 테스트
        ```java
        @Test
            public void detailTest() {
                List<Object[]> list = movieRepository.getMovieWithAll(3L);
                for(Object [] ar: list) {
                    System.out.println(Arrays.toString(ar));
                }
            }
        ```
   3. 특정 영화의 리뷰 가져오기

      - ReviewRepository

        ```java
        public interface ReviewRepository extends JpaRepository<Review, Long> {
            // 테이블의 전체 데이터 가져오기 - Paging 기능
            // 기본키를 가지고 데이터 1개 가져오기
            // 데이터 삽입과 수정(기본키를 조건으로 하는)에 사용되는 메서드 제공
            // 기본키를 가지고 삭제하는 메서드와 entity를 가지고 삭제

            // 이름을 기반으로 하는 메서드 생성이 가능
            List<Review> findByMovie(Movie movie);

            // JPQL을 이용한 쿼리 작성 가능 - JOIN

            // Querydsl을 이용한 쿼리 작성 가능 - 동적 쿼리
        }
        ```

      - 테스트1 - 성공.

        ```java
        @Test
            public void getReviews() {
                Movie movie = Movie.builder().mno(96L).build();

                List<Review> result = reviewRepository.findByMovie(movie);
                result.forEach(review -> {
                    System.out.println(review);
                });
            }
        ```

      - **테스트 2 - 에러.**

        ```java
        @Test
            // @Transactional
            public void getReviews() {
                Movie movie = Movie.builder().mno(96L).build();

                List<Review> result = reviewRepository.findByMovie(movie);
                result.forEach(review -> {
                    System.out.println(review.getReviewnum());
                    System.out.println(review.getMember().getEmail());
                });
            }
        ```

        review를 출력하면 문제가 없지만 review 안에서 Member나 Movie를 가져와서 세부 데이터를 출력하면 에러 - Fetch 모드를 LAZY로 설정했기 때문인데 @Transcational을 적용해서 전체 코드가 수행이 종료되기 전에 데이터베이스의 트랜잭션을 종료하지 않도록 해서 해결 가능.
        또한 EntityGraph로 해결 가능.

   4. @EntityGraph
      - Entity의 특정 속성을 같이 로딩하도록 표시하는 어노테이션.(LAZY 설정인 것도 로딩하겠다는 뜻)
      - 2개의 속성을 설정할 수 있는데 로딩 설정을 변경하고자 하는 속성의 이름을 배열로 명시하는 attributePaths와 어떻게 적용할 것인지를 결정하는 type(FETCH - 명시한 속성만 EAGER가 되고 나머지는 LAZY로 처리, LOAD - FETCH의 반대)
      - ReviewRepository에서 수정
        ```java
        ...
        // 이름을 기반으로 하는 메서드 생성이 가능
            @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
            List<Review> findByMovie(Movie movie);
        ...
        ```
      - 테스트 재실행 - @Transcational을 추가하지 않아도 성공
   5. 회원 탈퇴 처리(영화 정보 삭제 시의 MovieImage와 Review 삭제 문제도 동일)

      회원 정보를 삭제하는 경우 외래키로 연결된 Review 데이터를 어떻게 할 것이냐는 문제.

      같이 삭제하는 경우가 있고 외래키의 값을 null로 설정하는 방법이 있음.

      회원 정보가 없더라도 리뷰 데이터 자체가 의미를 갖는 경우라면 null로 설정하는 것이 바람직하고 리뷰 데이터 자체가 의미가 없다면 삭제를 한다.

      삭제를 할 때도 실제 삭제를 하는 경우도 있고 별도의 컬럼을 제공해서 삭제를 한 것처럼 나들ㄷ기도 한다.

      회원 정보를 삭제할 때 회원 정보를 삭제하고 리뷰 정보를 삭제하거나 수정하면 에러.

      외래키로 참조하는 테이블의 데이터를 먼저 삭제하거나 수정하고 참조되는 테이블의 데이터를 삭제해야 한다.

      - ReviewRepository 인터페이스에 회원 정보가 삭제될 때 처리할 메서드를 선언
        ```java
        // 회원 정보를 가지고 데이터를 삭제하는 메서드
            void deleteByMember(Member member);
        ```
      - 테스트
        ```java
        @Test
            @Transactional
            @Commit
            public void deleteByMember() {
                Member member = Member.builder().mid(13L).build();
                reviewRepository.deleteByMember(member);
            }
        ```
      - null로 만드는 방법
        ```java
        @Modifying
            @Query("update Review r set r.member.mid=null where r.member.mid=:mid")
            void updateByMember(@Param("mid") Long mid);
        ```
      - 테스트
        ```java
        @Test
            @Transactional
            @Commit
            public void updateByMember() {
                reviewRepository.updateByMember(42L);
            }
        ```

8. **파일 업로드**

   1. 파일 업로드 방법
      - Servelt 3 버전부터 추가된 자체적인 파일 업로드 라이브러리 이용
      - 별도의 파일 업로드 라이브러리를 이용
   2. 이미지 파일 출력
      - 원본 이미지를 출력
      - 썸네일 이미지를 만들어서 출력하고 썸네일 이미지를 클릭하면 원본 이미지를 출력
   3. 이미지 미리보기
      - 자바스크립트를 이용해서 업로드 하기전에 미리 보기
      - 서버에 업로드한 후 미리보기
   4. application.yml에 파일 업로드를 위한 설정 추가

      ```yaml
      spring:
        servlet:
          multipart:
            enabled: true
            location: /Users/gyumin/Documents/data
            max-request-size: 30MB
            max-file-size: 10MB
      ```

      location은 현재 컴퓨터의 디렉토리를 설정

   5. Upload 처리

      - Spring에서는 파일을 매개변수로 받을 수 있는 MultipartFile이라는 자료형을 제공
      - 파일 업로드 처리를 위한 Controller 클래스를 만들고 작성 - UploadController
        ```java
        @RestController
        @Log4j2
        public class UploadController {
            @PostMapping("/uploadajax")
            public void uploadFile(MultipartFile[] uploadFiles) {
                for(MultipartFile uploadFile: uploadFiles) {
                    // 업로드된 파일의 파일 이름
                    String originalName = uploadFile.getOriginalFilename();
                    // IE는 파일 이름이 아니고 전체 경로를 전송하기 때문에
                    // 마지막 \ 뒤 부분만 추출
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
                    log.warn("fileName:" + fileName);
                }
            }
        }
        ```
      - 파일 업로드 화면 이동을 위한 Controller 클래스 작성 - UploadTestController
        ```java
        @Controller
        public class UploadTestController {
            @GetMapping("/uploadajax")
            public void uploadAjax(){}
        }
        ```
      - templates 디렉토리에 uploadajax.html 파일을 만들고 화면 구성

        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>파일 업로드 테스트</title>
          </head>
          <body>
            <input
              name="uploadFiles"
              type="file"
              accept="image/*"
              multiple="multiple"
            />
            <button id="uploadBtn">Upload</button>
          </body>
          <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
          <script>
            $("#uploadBtn").click(function () {
              let formData = new FormData();

              // 선택된 파일 가져오기
              let inputFile = $("input[type='file']");
              let files = inputFile[0].files;

              if (files.length < 1) {
                alert("선택된 파일이 없습니다.");
                return;
              }

              // 선택한 파일을 formData에 추가
              for (let i = 0; i < files.length; i++) {
                console.log(files[i]);
                formData.append("uploadFiles", files[i]);
              }

              // 서버에 전송
              $.ajax({
                url: "/uploadajax",
                processData: false,
                contentType: false,
                data: formData,
                type: "POST",
                dataType: "json",
                success: function (result) {
                  console.log("업로드 성공");
                },
                error: function (jqXHR, textstatus, errorThrown) {
                  console.log(textstatus);
                },
              });
            });
          </script>
        </html>
        ```

      - [localhost/uploadajax](http://localhost/uploadajax) 에서 파일을 선택해서 업로드를 진행한 후 브라우저의 콘솔과 IDE의 콘솔을 확인해서 선택한 파일이 제대로 출력되는지 확인

   6. 서버에 파일 저장

      - Spring에서 제공하는 FileCopyUtils 클래스를 이용해도 되고 MultipartFile 클래스에 transferTo라는 메서드를 이용해도 byte 배열을 직접 읽어서 쓰기 작업을 해도 가능
      - 업로드된 파일의 확장자 검사 수행: 클라이언트와 서버 모두에서 수행
        exe나 zip, tar 등의 확장자에 제한을 가하는 경우가 있다.
      - 파일 이름의 중복 문제 고려: UUID나 회원 아이디 또는 날짜 같은 것을 추가해서 해결
      - 파일 업로드가 많은 경우 하나의 디렉토리에 저장이 안되는 문제 고려: 날짜 별로 디렉토리를 생성해서 파일을 업로드하는 방식으로 해결
      - application.yml 파일에 파일의 업로드 경로를 변수로 생성
        ```yaml
        com:
          gyuminsoft:
            upload:
              path: /Users/gimgyumin/Documents/data
        ```
      - UploadController에 application.yml 파일에서 만든 속성 가져온 후 작업

        ```java
        @RestController
        @Log4j2
        public class UploadController {
            @Value("${com.gyuminsoft.upload.path}")
            private String uploadPath;

            // 날짜 별로 디렉토리를 생성해주는 메서드
            private String makeFolder() {
                String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String realUploadPath = str.replace("//", File.separator);
                File uploadPathDir = new File(uploadPath, realUploadPath);

                // 디렉토리가 없으면 생성
                if (uploadPathDir.exists() == false) {
                    uploadPathDir.mkdirs();
                }
                return realUploadPath;
            }

            @PostMapping("/uploadajax")
            public void uploadFile(MultipartFile[] uploadFiles) {
                makeFolder();
                for(MultipartFile uploadFile: uploadFiles) {
                    // 업로드된 파일의 파일 이름
                    String originalName = uploadFile.getOriginalFilename();
                    // IE는 파일 이름이 아니고 전체 경로를 전송하기 때문에
                    // 마지막 \ 뒤 부분만 추출
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
                    log.warn("fileName:" + fileName);
                }
            }
        }
        ```

      - 테스트 - 디렉토리 생성완료.
      - 파일 업로드 처리

        ```java
        @RestController
        @Log4j2
        public class UploadController {
            @Value("${com.gyuminsoft.upload.path}")
            private String uploadPath;

            // 날짜 별로 디렉토리를 생성해주는 메서드
            private String makeFolder() {
                String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String realUploadPath = str.replace("//", File.separator);
                File uploadPathDir = new File(uploadPath, realUploadPath);

                // 디렉토리가 없으면 생성
                if (uploadPathDir.exists() == false) {
                    uploadPathDir.mkdirs();
                }
                return realUploadPath;
            }

            @PostMapping("/uploadajax")
            public void uploadFile(MultipartFile[] uploadFiles) {
                makeFolder();
                for(MultipartFile uploadFile: uploadFiles) {
                    // 이미지 파일이 아니면 이미지 업로드 수행하지 않음.
                    if(uploadFile.getContentType().startsWith("image") == false) {
                        log.warn("이미지 파일이 아님");
                        return;
                    }

                    // 업로드된 파일의 파일 이름
                    String originalName = uploadFile.getOriginalFilename();
                    // IE는 파일 이름이 아니고 전체 경로를 전송하기 때문에
                    // 마지막 \ 뒤 부분만 추출
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
                    log.warn("fileName:" + fileName);

                    // 디렉토리를 생성
                    String realUploadPath = makeFolder();

                    // UUID
                    String uuid = UUID.randomUUID().toString();
                    // 실제 파일이 저장될 경로 생성
                    String saveName = uploadPath + File.separator + realUploadPath + File.separator + uuid + fileName;
                    Path savePath = Paths.get(saveName);
                    try {
                        // 파일 업로드
                        uploadFile.transferTo(savePath);
                    } catch(IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
            }
        }
        ```

   7. 파일 업로드 결과 반환과 화면에서의 처리

      - 파일 업로드 결과를 위한 DTO 클래스 생성 - dto.UploadResultDTO

        ```java
        @Data
        @AllArgsConstructor
        // Serializable(직렬화): 데이터를 전송할 때 객체 단위로 전송할 수 있도록 해주는 인터페이스
        public class UploadResultDTO implements Serializable {

            private String fileName;
            private String uuid;
            private String uploadPath;

            // 실제 이미지 경로를 리턴해주는 메서드
            public String getImageUrl() {
                try {
                    return URLEncoder.encode(uploadPath + "/" + uuid + fileName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
                return "";
            }
        }
        ```

      - UploadController 클래스의 파일 업로드 처리 메서드를 수정

        ```java
        @RestController
        @Log4j2
        public class UploadController {
            @Value("${com.gyuminsoft.upload.path}")
            private String uploadPath;

            // 날짜 별로 디렉토리를 생성해주는 메서드
            private String makeFolder() {
                String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String realUploadPath = str.replace("//", File.separator);
                File uploadPathDir = new File(uploadPath, realUploadPath);

                // 디렉토리가 없으면 생성
                if (uploadPathDir.exists() == false) {
                    uploadPathDir.mkdirs();
                }
                return realUploadPath;
            }

            @PostMapping("/uploadajax")
            public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
                makeFolder();
                // 결과를 전달할 객체 생성
                List<UploadResultDTO> resultDTOList = new ArrayList<>();

                for(MultipartFile uploadFile: uploadFiles) {
                    // 이미지 파일이 아니면 이미지 업로드 수행하지 않음.
                    if(uploadFile.getContentType().startsWith("image") == false) {
                        log.warn("이미지 파일이 아님");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }

                    // 업로드된 파일의 파일 이름
                    String originalName = uploadFile.getOriginalFilename();
                    // IE는 파일 이름이 아니고 전체 경로를 전송하기 때문에
                    // 마지막 \ 뒤 부분만 추출
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
                    log.warn("fileName:" + fileName);

                    // 디렉토리를 생성
                    String realUploadPath = makeFolder();

                    // UUID
                    String uuid = UUID.randomUUID().toString();
                    // 실제 파일이 저장될 경로 생성
                    String saveName = uploadPath + File.separator + realUploadPath + File.separator + uuid + fileName;
                    Path savePath = Paths.get(saveName);
                    try {
                        // 파일 업로드
                        uploadFile.transferTo(savePath);
                        // 결과를 List에 추가
                        resultDTOList.add(new UploadResultDTO(fileName, uuid, realUploadPath));
                    } catch(IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
                return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
            }
        }
        ```

      - 콘솔을 찍어 테스트해보면 이제 저장경로(uuid)를 얻을 수 있다. 이제 출력 가능해진 상태이다.

   8. 업로드 된 이미지 출력
      - UploadController 클래스에 파일 이름을 받아서 파일의 내용을 전송해주는 요청 처리 메서드 작성
        ```java
        @GetMapping("/display")
            // 파일의 내용을 결과로 전송 - 바이트 배열을 타입으로 사용
            public ResponseEntity<byte[]> getFile(String filename) {
                ResponseEntity<byte []> result = null;
                try {
                    log.warn("파일 이름:" + filename);
                    // 전송할 파일 생성 - 디코딩 필요
                    File file = new File(uploadPath + File.separator + URLDecoder.decode(filename, "UTF-8"));
                    // 헤더에 파일이라는 것을 설정
                    HttpHeaders header = new HttpHeaders();
                    header.add("Content-Type", Files.probeContentType(file.toPath()));
                    // 파일의 내용을 응답의 결과로 생성
                    result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
                } catch(Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return result;
            }
        ```
      - uploadajax.html 에서 출력 영역 설정, 업로드 버튼을 눌렀을 때 실행되는 메서드 수정
        ```html
        // 서버에 전송 $.ajax({ url:'/uploadajax', processData: false,
        contentType: false, data: formData, type:'POST', dataType:'json',
        success:function(result) { console.log(result) let divArea =
        $(".uploadResult"); for(let i = 0; i<result.length; i++) {
        divArea.append("<img
          src='/display?filename=" + result[i].imageUrl + "'
        />"); } }, error:function(jqXHR, textstatus, errorThrown) {
        console.log(textstatus); } })
        ```
      - 실행한 후 이미지 파일을 업로드했을 때 이미지 미리보기가 되는지 확인
      - 실행한 후 이미지를 선택했을 때 이미지 미리보기 구현
        ```html
        <script>
            // 파일을 선택할 때 미리보기
            let imginp = document.getElementById("imginp");
            imginp.addEventListener("change", (e) => {
                if(imginp.files && imginp.files[0]) {
                    let filename = imginp.files[0].name;
                    let reader = new FileReader();
                    reader.addEventListener("load", (e) => {
                        document.getElementById("img").src = e.target.result;
                    });
                    reader.readAsDataURL(imginp.files[0]);
                }
            })
        	...
        ```
   9. Thumbnail 이미지 출력

      - 목록보기에서 이미지를 원본 크기 그대로 출력하는 것은 바람직하지 않은데 목록 보기에서는 이미지를 여러 개 출력해야하기 때문
      - 이런 경우에는 Thumbnail 이미지를 출력한 후 필요한 경우 원본 이미지를 출력하는 것이 효율적
        Thumbnail을 생성해주는 라이브러리를 이용해서 Thumbnail 이미지를 생성하는데 이 때 Thumbnial 이미지 이름 옆에 s\_ 를 추가
      - UploadResultDTO 클래스에 getThumbnailURL을 추가해서 Thumbnail 이미지의 경루로르 클라이언트에 전달, 클라이언트에서는 Thumbnail 이미지를 출력
      - build.gradle 파일에 dependencies에 thumbnail 이미지를 생성해주는 라이브러리의 의존성 추가
        ```java
        implementation group: 'net.coobird', name:'thumbnailator', version:'0,4,12'
        ```
      - UploadResultDTO 클래스에 Thumbnail 이미지 경로를 리턴하는 메서드를 추가
        ```java
        // Thumbnail 이미지 경로를 리턴해주는 메서드
            public String getThumbnailUrl() {
                try {
                    return URLEncoder.encode(uploadPath + "/s_" + uuid + fileName, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
                return "";
            }
        ```
      - UploadController의 파일 업로드 요청 처리 메서드를 수정

        ```java
        @RestController
        @Log4j2
        public class UploadController {
            @Value("${com.gyuminsoft.upload.path}")
            private String uploadPath;

            // 날짜 별로 디렉토리를 생성해주는 메서드
            private String makeFolder() {
                String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                String realUploadPath = str.replace("//", File.separator);
                File uploadPathDir = new File(uploadPath, realUploadPath);

                // 디렉토리가 없으면 생성
                if (uploadPathDir.exists() == false) {
                    uploadPathDir.mkdirs();
                }
                return realUploadPath;
            }

            @PostMapping("/uploadajax")
            public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
                makeFolder();
                // 결과를 전달할 객체 생성
                List<UploadResultDTO> resultDTOList = new ArrayList<>();

                for (MultipartFile uploadFile : uploadFiles) {
                    // 이미지 파일이 아니면 이미지 업로드 수행하지 않음.
                    if (uploadFile.getContentType().startsWith("image") == false) {
                        log.warn("이미지 파일이 아님");
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }

                    // 업로드된 파일의 파일 이름
                    String originalName = uploadFile.getOriginalFilename();
                    // IE는 파일 이름이 아니고 전체 경로를 전송하기 때문에
                    // 마지막 \ 뒤 부분만 추출
                    String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
                    log.warn("fileName:" + fileName);

                    // 디렉토리를 생성
                    String realUploadPath = makeFolder();

                    // UUID
                    String uuid = UUID.randomUUID().toString();
                    // 실제 파일이 저장될 경로 생성
                    String saveName = uploadPath + File.separator + realUploadPath + File.separator + uuid + fileName;
                    File saveFile = new File(saveName);
                    try {
                        // 파일 업로드
                        uploadFile.transferTo(saveFile);

                        // 썸네일 파일 이름 생성
                        File thumbnailFile = new File(uploadPath + File.separator + realUploadPath + File.separator + "s_" + uuid + fileName);

                        // 썸네일 생성
                        Thumbnailator.createThumbnail(saveFile, thumbnailFile, 100, 100);

                        // 결과를 List에 추가
                        resultDTOList.add(new UploadResultDTO(fileName, uuid, realUploadPath));
                    } catch (IOException e) {
                        System.out.println(e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
                return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
            }

            @GetMapping("/display")
            // 파일의 내용을 결과로 전송 - 바이트 배열을 타입으로 사용
            public ResponseEntity<byte[]> getFile(String filename) {
                ResponseEntity<byte []> result = null;
                try {
                    log.warn("파일 이름:" + filename);
                    // 전송할 파일 생성 - 디코딩 필요
                    File file = new File(uploadPath + File.separator + URLDecoder.decode(filename, "UTF-8"));
                    // 헤더에 파일이라는 것을 설정
                    HttpHeaders header = new HttpHeaders();
                    header.add("Content-Type", Files.probeContentType(file.toPath()));
                    // 파일의 내용을 응답의 결과로 생성
                    result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
                } catch(Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return result;
            }
        }
        ```

      - uploadajax.html에서 썸네일 이미지를 출력하도록 변경
        ```java
        ...
        divArea.append("<img src='/display?filename=" + result[i].thumbnailUrl + "'>");
        ...
        ```

   10. 업로드된 이미지 삭제

       - 삭제 아이콘을 추가하기 위해서 업로드 성공 후 호출되는 코드를 수정 - uploadajax.html

         ```java
         // 서버에 전송
                 $.ajax({
                     url:'/uploadajax',
                     processData: false,
                     contentType: false,
                     data: formData,
                     type:'POST',
                     dataType:'json',
                     success:function(result) {
                         console.log(result)

                         let divArea = $(".uploadResult");
                         let str = "";
                         for(let i = 0; i<result.length; i++) {
                             str += "<div>";
                             str += "<img src='/display?filename=" + result[i].thumbnailUrl + "'>";
                             str += "<button class='removeBtn' data-name='" + result[i].imageUrl + "'>삭제</button>";
                             str += "</div>";
                         }
                         divArea.append(str);
                     },
                     error:function(jqXHR, textstatus, errorThrown) {
                         console.log(textstatus);
                     }
                 })
         ```

       - uploadajax.html 파일에 삭제 버튼을 눌렀을 떄 호출되는 코드를 추가
         ```java
         // 삭제 버튼을 눌렀을 때 처리
             // 삭제 버튼은 동적으로 생성되었기 때문에 바로 적용이 불가능
             $('.uploadResult').on('click', ".removeBtn", function(e) {
                 // 이벤트가 발생한 버튼 찾기
                 let target = $(this);
                 // 이벤트가 발생한 버튼의 data-name 속성 찾아오기
                 let fileName = target.data("name");
                 let targetDiv = $(this).closest("div");
                 $.post('/removefile', {fileName: fileName}, function(result) {
                     console.log(result);
                     // 서버로부터의 응답이 true라면 이미지 출력 영역 삭제
                     if(result === true) {
                         targetDiv.remove();
                     }
                 });
             })
         ```
       - UploadController에 삭제 요청을 처리하는 메서드 추가

         ```java
         @PostMapping("/removefile")
             public ResponseEntity<Boolean> removeFile(String fileName) {
                 try {
                     String srcFileName = URLDecoder.decode(fileName, "UTF-8");
                     // 원본 이미지 파일 생성
                     File file = new File(uploadPath + File.separator + srcFileName);

                     // 원본 이미지 삭제
                     file.delete();

                     // 썸네일 이미지 파일 객체 생성
                     File thumbnail = new File(file.getParent() + "s_" + file.getName());
                     thumbnail.delete();

                     return new ResponseEntity<>(true, HttpStatus.OK);
                 }catch(Exception e) {
                     System.out.println(e.getLocalizedMessage());
                     return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
                 }
             }
         ```

9. **화면 출력을 위한 설정**
   1. static 디렉토리에 bootstrap 관련 파일을 복사
   2. templates 디렉토리에 기본 레이아웃으로 사용할 파일을 생성 - layout/basic.html
10. **영화 정보 등록**

    1. 개요

       영화 제목과 영화에 대한 이미지만 선택해서 업로드

    2. 영화에 대한 요청을 처리할 Controller를 생성하고 등록 요청을 처리할 메서드를 생성

       ```java
       @Controller
       @RequestMapping("/movie")
       @Log4j2
       @RequiredArgsConstructor
       public class MovieController {
           @GetMapping("/register")
           public void register() {
           }
       }
       ```

    3. 영화 등록을 위한 화면을 templates/movie/register.html 파일로 생성

       ```html
       <!DOCTYPE html>
       <html lang="en">
         <th:block
           th:replace="~{/layout/basic :: setContent(~{this::content})}"
         >
           <th:block th:fragment="content">
             <h1 class="mt-4">영화 등록</h1>
             <form th:action="@{/movie/register}" th:method="post">
               <div class="form-group">
                 <label>영화 제목</label>
                 <input
                   type="text"
                   class="form-control"
                   name="title"
                   placeholder="영화 제목을 입력하세요"
                 />
               </div>

               <div class="form-group fileForm">
                 <label>영화 포스터 목록</label>
                 <input
                   type="file"
                   class="custom-file-input files"
                   id="fileInput"
                   multiple="multiple"
                 />
                 <label class="custom-file-label" data-browse="Browse"></label>
               </div>
               <button type="submit" class="btn btn-primary">등록</button>
             </form>
           </th:block>
         </th:block>
       </html>
       ```

    4. 영화 이미지 데이터를 위한 DTO 클래스 생성 - dto.MovieImageDTO

       - **MovieDTO를 먼저 만들면 안됨.**
       - **테이블간의 관계가 완전 독립적인지, 포함(영화 이미지는 영화에 포함되는데 어쩔수 없이 나눈 테이블) 관계인지 확인해야 한다.**
       - **왜냐면 Movie (객체, DTO)안에 넣어서 Movie Image를 출력할 것이기 때문이다.**

       ```java
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       @Builder
       public class MovieImageDTO {
           // movie_mno는 movie에 있으니까 안가져와도 된다.
           private String uuid;
           private String imgName;
           private String path;

           // 실제 이미지 경로를 리턴해주는 메서드
           public String getImageUrl() {
               try {
                   return URLEncoder.encode(path + "/" + uuid + imgName, "UTF-8");
               } catch (UnsupportedEncodingException e) {
                   System.out.println(e.getLocalizedMessage());
                   e.printStackTrace();
               }
               return "";
           }

           // Thumbnail 이미지 경로를 리턴해주는 메서드
           public String getThumbnailUrl() {
               try {
                   return URLEncoder.encode(path + "/s_" + uuid + imgName, "UTF-8");
               } catch (UnsupportedEncodingException e) {
                   System.out.println(e.getLocalizedMessage());
                   e.printStackTrace();
               }
               return "";
           }
       }
       ```

    5. 영화 데이터를 위한 dto.MovieDTO

       ```java
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       @Builder
       public class MovieDTO {
           private Long mno;
           private String title;

           // 영화 이미지도 같이 등록
           // Scala 데이터는 초기화될 때 없으면 null인데 null이어도 된다.
           // 그런데 Vector 데이터는 초기화될 때 데이터가 없더라도 인스턴스 생성을 해줘야 한다. 안 그러면 null pointer exception 에러가 난다.
           // 따라서 아래와 같이 초기화해준다.
           // builder()라는 메서드를 이용해서 생성할 때 기본으로 사용하겠다는 뜻.
           @Builder.Default
           private List<MovieImageDTO> imageDTOList = new ArrayList<>();
       }
       ```

    6. 영화 데이터에 대한 처리를 위한 서비스 인터페이스를 생성하고 삽입 메서드를 선언 - service.MovieService

       ```java
       package com.kakao.reviewapp0116.service;

       import com.kakao.reviewapp0116.domain.Movie;
       import com.kakao.reviewapp0116.domain.MovieImage;
       import com.kakao.reviewapp0116.dto.MovieDTO;
       import com.kakao.reviewapp0116.dto.MovieImageDTO;

       import java.util.HashMap;
       import java.util.List;
       import java.util.Map;
       import java.util.stream.Collectors;

       public interface MovieService {
           // 데이터 삽입을 위한 메서드
           // 매개변수는 항상 DTO
           // 리턴 타입과 이름 기재
           // 리턴타입은 기본 키를 주던지 DTO를 그대로 주던지 한다.
           Long register(MovieDTO movieDTO);

           // DTO를 Entity로 변환
           // 하나의 Entity가 아니라 Movie와 MovieImage로 변환이 되어야 해서
           // Map으로 리턴
           default Map<String, Object> dtoToEntity(MovieDTO movieDTO) {
               Map<String, Object> entityMap = new HashMap<>();

               Movie movie = Movie.builder()
                       .mno(movieDTO.getMno())
                       .title(movieDTO.getTitle())
                       .build();
               entityMap.put("movie", movie);

               // MovieImageDTO의 List
               List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

               // MovieImageDTO의 List를 MovieImage Entity의 List로 변환
               if (imageDTOList != null && imageDTOList.size() > 0) {
                   List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {
                       MovieImage movieImage = MovieImage.builder()
                               .path(movieImageDTO.getPath())
                               .imgName(movieImageDTO.getImgName())
                               .uuid(movieImageDTO.getUuid())
                               .movie(movie)
                               .build();
                       return movieImage;
                   }).collect(Collectors.toList());
                   entityMap.put("imgList", movieImageList);
               }
               return entityMap;
           }
       }
       ```

    7. 영화 데이터에 대한 처리를 위한 서비스 클래스를 생성하고 삽입 메서드를 구현 - service.MovieServiceImpl

       ```java
       @Service
       @Log4j2
       @RequiredArgsConstructor
       public class MovieServiceImpl implements MovieService {
           // 이전과는 다르게 두 개의 레포지토리가 필요하다.
           private final MovieRepository movieRepository;
           private final MovieImageRepository movieImageRepository;

           @Override
           // 영화와, 영화이미지 정보를 두 번 넣기 때문에 꼭 트랜잭션 처리를 해줘야 한다.
           @Transactional
           public Long register(MovieDTO movieDTO) {
               log.warn("movieDTO:" + movieDTO);

               Map<String, Object> entityMap = dtoToEntity(movieDTO);
               // 영화와 영화 이미지 정보 찾아오기
               Movie movie = (Movie) entityMap.get("movie");
               List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

               // movie는 그냥 삽입하면 되는데
               movieRepository.save(movie);
               // imgList는 반복문을 통해 삽입한다.
               movieImageList.forEach(movieImage -> {
                   movieImageRepository.save(movieImage);
               });
               return movie.getMno();
           }
       }
       ```

    8. MovieController 클래스에 등록 요청을 처리하는 메서드 구현

       ```java
       @PostMapping("/register")
           public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {
               log.info("movieDTO:" + movieDTO);
               Long mno = movieService.register(movieDTO);
               redirectAttributes.addFlashAttribute("msg", mno + "삽입 성공");

               return "redirect:/movie/list";
           }
       ```

    9. register.html 파일에 파일 업로드와 이미지 미리보기 코드를 작성

       - 파일을 선택했을 때 파일을 업로드하기 위한 스크립트 코드를 작성

         ```html
         <!DOCTYPE html>
         <html lang="en">
           <th:block
             th:replace="~{/layout/basic :: setContent(~{this::content})}"
           >
             <th:block th:fragment="content">
               <h1 class="mt-4">영화 등록</h1>
               <form th:action="@{/movie/register}" th:method="post">
                 <div class="form-group">
                   <label>영화 제목</label>
                   <input
                     type="text"
                     class="form-control"
                     name="title"
                     placeholder="영화 제목을 입력하세요"
                   />
                 </div>

                 <div class="form-group fileForm">
                   <label>영화 포스터 목록</label>
                   <input
                     type="file"
                     class="custom-file-input files"
                     id="fileInput"
                     multiple="multiple"
                   />
                   <label
                     class="custom-file-label"
                     data-browse="Browse"
                   ></label>
                 </div>
                 <button type="submit" class="btn btn-primary">등록</button>
               </form>
               <script>
                 $(document).ready(function (e) {
                   // 제외할 확장자를 위한 정규식
                   // . 다음에 exe, sh, zip, alz, tiff로 끝나는 정규식
                   let regex = new RegExp("(.*?)\.(exe|sh|zip|alz|tiff)$");

                   // 파일의 최대 사이즈
                   let maxSize = 1024 * 1024 * 10;

                   // 파일의 확장자를 조사해주는 함수
                   function checkExtension(fileName, fileSize) {
                     if (fileSize >= maxSize) {
                       alert("파일 사이즈 초과");
                       return false;
                     }
                     // 여기서 fileName이 그냥 들어가는데 lowerCase로 해야하는 것을 생각해야한다.
                     // 예를 들어 .zip 파일은 안들어가지만 .ZIP 파일은 들어간다.
                     if (regex.test(fileName)) {
                       alert("해당 종류의 파일은 업로드 할 수 없습니다.");
                       return false;
                     }
                     return true;
                   }

                   // 파일의 선택이 변경되면
                   $(".custom-file-input").on("change", function () {
                     // 파일 이름 추출
                     let fileName = $(this).val().split("\\").pop();
                     console.log(fileName);
                     // 파일 이름을 출력
                     $(this)
                       .siblings(".custom-file-label")
                       .addClass("selected")
                       .html(fileName);

                     // 파일을 전송할 때 사용할 데이터
                     let formData = new FormData();

                     // 선택한 파일 목록 가져오기
                     inputFile = $(this);
                     files = inputFile[0].files;
                     // 추가 모드 설정
                     appended = false;

                     for (let i = 0; i < files.length; i++) {
                       // 파일 유효성 검사
                       if (
                         !checkExtension(files[i].name, fileName, files[i].size)
                       ) {
                         return false;
                       }
                       formData.append("uploadFiles", files[i]);
                       appended = true;
                     }

                     // 유효성 검사에 실패한 경우 업로드하지 않음.
                     if (!appended) {
                       return;
                     }

                     // 파일을 post 방식으로 전송
                     $.ajax({
                       url: "/uploadajax",
                       processData: false,
                       contentType: false,
                       data: formData,
                       type: "POST",
                       dataType: "json",
                       success: function (result) {
                         console.log(result);
                       },
                       error: function (jqXHR, textStatus, errorThrown) {
                         console.log(textStatus);
                       },
                     });
                   });
                 });
               </script>
             </th:block>
           </th:block>
         </html>
         ```

       - register.html에 이미지를 출력할 영역 생성하고 스타일적용 - 코드참고
       - 이미지 업로드 결과를 받아서 uploadResult 영역에 업로드한 이미지를 미리보기 해주는 함수를 스크립트에 추가 - 코드 참고
       - 이미지 업로드에 성공했을 때 핸들러에 이미지 출력 함수를 호출하는 코드를 추가
         showResult(result) 함수
       - 이미지 위의 삭제 버튼을 누르면 업로드된 이미지를 삭제하는 코드 추가 - 코드 추가

    10. registe.html파일에 등록 누를 시 처리 - DB 삽입

        ```html
        // 등록 버튼을 누를 때 이벤트 처리 $('.btn-primary').click(function(e) {
        // 기본 기능 제거 e.preventDefault(); let str = ""; if($(".uploadResult
        li").length > 3) { alert("이미지는 3개까지만 등록해야 합니다."); return;
        } $(".uploadResult li").each(function(i, obj) { let target = $(obj); let
        imsi = 'imageDTOList[' + i + '].imgName'; str += "<input type='hidden' "
        + "name=\'" + imsi + "\' " + "value=\'" + target.data('name') + "\'>";
        imsi = 'imageDTOList[' + i + '].path'; str += "<input type='hidden' " +
        "name=\'" + imsi + "\' " + "value=\'" + target.data('path') + "\'>";
        imsi = 'imageDTOList[' + i + '].uuid'; str += "<input type='hidden' " +
        "name=\'" + imsi + "\'" + " value=\'" + target.data('uuid') + "\'>"; })
        $('form').append(str).submit(); })
        ```

11. **영화 목록 보기**

    영화 번호, 영화 대표 이미지 1개, 내용, review의 grade 평균, 등록일을 출력

    1. 목룍 요청을 위한 DTO 클래스를 생성 - dto.PageRequestDTO

       ```java
       @Builder
       @AllArgsConstructor
       @Data
       public class PageRequestDTO {
           // 페이지 번호
           private int page;
           // 페이지당 데이터 개수
           private int size;
           // 검색 항목
           private String type;
           // 검색 내용
           private String keyword;

           public PageRequestDTO() {
               page = 1;
               size = 10;
           }

           // page와 size를 가지고 Pageable 객체를 생성해주는 메서드
           public Pageable getPageable(Sort sort) {
               return PageRequest.of(-1, size, sort);
           }
       }
       ```

    2. 목록 보기 요청에 응답을 하기 위한 DTO 생성 - dto.PageponseDTO

       ```java
       @Data
       public class PageResponseDTO <DTO,EN> {
           private List<DTO> dtoList;
           private int totalPage;
           private int page;
           private int size;
           private int startPage;
           private int endPage;
           private boolean prev;
           private boolean next;
           private List<Integer> pageList;

           public PageResponseDTO(Page<EN> result, Function<EN, DTO> fn) {
               dtoList = result.stream().map(fn).collect(Collectors.toList());
               totalPage = result.getTotalPages();
               makePageList(result.getPageable());
           }

           private void makePageList(Pageable pageable) {
               this.page = pageable.getPageNumber() + 1;
               this.size = pageable.getPageSize();

               int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
               startPage = tempEnd - 9;
               prev = startPage > 1;
               endPage = totalPage > tempEnd ? tempEnd : totalPage;
               next = totalPage > tempEnd;
               pageList = IntStream.rangeClosed(startPage,endPage).boxed().collect(Collectors.toList());
           }
       }
       ```

    3. MovieDTO 수정 - 화면 출력을 위한 속성 추가

       ```java
       @Data
       @NoArgsConstructor
       @AllArgsConstructor
       @Builder
       public class MovieDTO {
           private Long mno;
           private String title;

           // review의 grade 평균
           private double avg;

           // 리뷰 개수
           private Long reviewCnt;

           // 등록일과 수정
           private LocalDateTime regDate;
           private LocalDateTime modDate;

           // 영화 이미지도 같이 등록
           // Scala 데이터는 초기화될 때 없으면 null인데 null이어도 된다.
           // 그런데 Vector 데이터는 초기화될 때 데이터가 없더라도 인스턴스 생성을 해줘야 한다. 안 그러면 null pointer exception 에러가 난다.
           // 따라서 아래와 같이 초기화해준다.
           // builder()라는 메서드를 이용해서 생성할 때 기본으로 사용하겠다는 뜻.
           @Builder.Default
           private List<MovieImageDTO> imageDTOList = new ArrayList<>();
       }
       ```

    4. MovieService 인터페이스에 Entity와 DTO 사이의 변환을 수행해주는 메서드를 추가

       목록을 가져오면 Movie, List<MovieImageList>, 리뷰 평균, 리뷰개수 4가지

       ```java
       // 검색 결과를 DTO로 변환해주는 메서드
           default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages, double avg, Long reviewCnt) {
               MovieDTO movieDTO = MovieDTO.builder()
                       .mno(movie.getMno())
                       .title(movie.getTitle())
                       .regDate(movie.getRegDate())
                       .modDate(movie.getModDate())
                       .build();
               List<MovieImageDTO> movieImageDTOList = movieImages.stream().map(movieImage -> {
                   return MovieImageDTO.builder()
                           .imgName(movieImage.getImgName())
                           .path(movieImage.getPath())
                           .uuid(movieImage.getUuid())
                           .build();
               }).collect(Collectors.toList());

               movieDTO.setImageDTOList(movieImageDTOList);
               movieDTO.setAvg(avg);
               movieDTO.setReviewCnt(reviewCnt);

               return movieDTO;
       	}
       ```

    5. MovieService 인터페이스에 데이터 목록을 위한 메서드 추가

       ```java
       // 데이터 목록을 위한 메서드
           PageResponseDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO);
       ```

    6. MovieServiceImpl 클래스에 목록보기를 위한 메서드를 구현

       ```java
       @Override
           public PageResponseDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {
               // 정렬 조건을 추가해서 Pageable 객체를 생성
               Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());
               // 데이터베이스에 요청
               Page<Object[]> result = movieRepository.getList(pageable);
               // Object 배열을 MovieDTO 타입으로 변경하기 위해서
               // 함수를 생성
               // 첫 번쨰 데이터가 Movie
               // 두 번째 데이터가 List<MovieImage>
               // 세 번쨰 데이터가 평점의 평균
               // 네 번쨰 데이터가 리뷰의 개수
               Function<Object[], MovieDTO> fn = (
                       (arr -> entitiesToDTO((Movie) arr[0],
                               (List<MovieImage>) (Arrays.asList((MovieImage) arr[1])),
                               (Double) arr[2],
                               (Long) arr[3])));
               return new PageResponseDTO<>(result, fn);
           }
       ```

    7. MovieController 클래스에 목록 보기 요청을 위한 메서드를 생성

       ```java
       @GetMapping("/list")
           public void list(PageRequestDTO pageRequestDTO, Model model) {
               PageResponseDTO pageResponseDTO = movieService.getList(pageRequestDTO);
               model.addAttribute("result", pageResponseDTO);
           }
       ```

    8. movie 디렉토리에 화면 출력을 위한 list.html 생성

       ```java
       <!DOCTYPE html>
       <html lang="en">
       <th:block th:replace="~{/layout/basic :: setContent(~{this::content})}">
           <th:block th:fragment="content">
               <h1 class="mt-4">영화 목록</h1>
               <span>
                   <a th:href="@{movie/register}">
                       <button type="button" class="btn btn-primary">영화 등록</button>
                   </a>
               </span>
               <br/>
               <table class="table table-striped">
                   <thead>
                   <tr>
                       <th>영화번호</th>
                       <th>영화제목</th>
                       <th>평점</th>
                       <th>등록</th>
                   </tr>
                   </thead>
                   <tbody>
                   <tr th:each="dto:${result.dtoList}">
                       <td>
                           [[${dto.mno}]]
                       </td>
                       <td>
                           <img th:if="${dto.imageDTOList.size()>0 && dto.imageDTOList[0].path != null}"
                                th:src="|/display?filename=${dto.imageDTOList[0].thumbnailUrl}|">
                           [[${dto.title}]]
                       </td>
                       <td>[[${dto.avg}]]</td>
                       <td>[[${#temporals.format(dto.regDate, 'yyyy/MM/dd')}]]</td>
                   </tr>
                   </tbody>
               </table>
               <ul class="pagination h-100 justify-content-center align-items-center">
                   <li class="page-item " th:if="${result.prev}">
                       <a class="page-link" th:href="@{/movie/list(page=${result.startPage-1})}"
                          tabindex="-1">이전</a>
                   </li>

                   <li th:class=" 'page-item ' + ${result.page == page?'active':''} "
                       th:each="page:${result.pageList}">
                       <a class="page-link" th:href="@{/movie/list(page=${page})}">
                           [[${page}]]
                       </a>
                   </li>

                   <li class="page-item " th:if="${result.next}">
                       <a class="page-link" th:href="@{/movie/list(page=${result.endPage+1})}"
                          tabindex="-1">다음</a>
                   </li>
               </ul>
           </th:block>
       </th:block>
       ```

12. **상세 보기**

    1. MovieService 클래스에 상세보기를 위한 메서드를 선언

       ```java
       // 상세보기를 위한 메서드
           MovieDTO getMovie(Long mno);
       ```

    2. MovieServiceImpl 클래스에 상세보기를 위한 메서드를 구현

       ```java
       @Override
           public MovieDTO getMovie(Long mno) {
               // 데이터베이스에서 결과 가져오기
               List<Object[]> result = movieRepository.getMovieWithAll(mno);

               Movie movie = (Movie) result.get(0)[0];
               List<MovieImage> movieImageList = new ArrayList<>();
               result.forEach(arr -> {
                   MovieImage movieImage = (MovieImage) arr[1];
                   movieImageList.add(movieImage);
               });

               double avg = (Double) result.get(0)[2];
               Long reviewCnt = (Long) result.get(0)[3];

               return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
           }
       ```

    3. Test 디렉토리에 ServiceTest 클래스를 만들고 상세보기 테스트

       ```java
       @SpringBootTest
       public class ServiceTests {
           @Autowired
           private MovieService movieService;

           @Test
           public void getMovie() {
               System.out.println(movieService.getMovie(106L));
           }
       }
       ```

    4. MovieController 클래스에 상세보기 요청을 처리할 메서드를 생성

       ```java
       @GetMapping("/read")
           // PageRequestDTO - page 정보가 담겨있는 것을 넘겨줘야 원래 페이지로 넘어올 수 있다.
           public void read(Long mno, @ModelAttribute("requestDTO") PageRequestDTO, Model model) {
               MovieDTO movieDTO = movieService.getMovie(mno);
               model.addAttribute("dto", movieDTO);
           }
       ```

    5. list.html에서 페이지 정보가 담긴 링크 추가

       ```html
       <a th:href="@{/movie/read(mno=${dto.mno}, page=${result.page})}"
         >[[${dto.title}]]</a
       >
       ```

    6. 상세보기 화면을 movie 디렉토리의 read.html 파일을 생성하고 작성

       ```html
       <!DOCTYPE html>
       <html lang="en">
       <th:block th:replace="~{/layout/basic :: setContent(~{this::content})}">
           <th:block th:fragment="content">
               <h1 class="mt-4">영화 정보</h1>
               <div class="form-group">
                   <label>제목</label>
                   <input type="text" class="form-control" name="title" th:value="${dto.title}" readonly="readonly"/>
               </div>
               <div class="form-group">
                   <label>리뷰 개수</label>
                   <input type="text" class="form-control" name="title" th:value="${dto.reviewCnt}" readonly="readonly"/>
               </div>
               <div class="form-group">
                   <label>평점</label>
                   <input type="text" class="form-control" name="title" th:value="${dto.avg}" readonly="readonly"/>
               </div>
               <div class="uploadResult">
                   <ul>
                       <li th:each="movieImage:${dto.imageDTOList}">
                           <img th:if="${movieImage.path != null}"
                                th:src="|/display?filename=${movieImage.getThumbnailUrl()}|">
                       </li>
                   </ul>
               </div>
               <style>
                   .uploadResult {
                       width: 100%;
                       background-color: gray;
                       margin-top: 10px;
                   }

                   .uploadResult ul {
                       display: flex;
                       flex-flow: row;
                       justify-content: center;
                       vertical-align: top;
                   }

                   .uploadResult ul li {
                       list-style: none;
                       padding: 10px;
                       margin-left: 2em;
                   }

                   .uploadResult ul li img {
                       width: 100px;
                   }
               </style>
           </th:block>
       </th:block>
       ```

13. **리뷰 작업**

    1. Review Entity 클래스에 수정이 가능하도록 메서드 추가

       ```java
       public void changeGrade(int grade) {
               this.grade = grade;
           }

           public void changeText(String tex) {
               this.text = text;
           }
       ```

    2. 리뷰 작업을 위한 DTO 클래스 추가 - dto.ReviewDTO

       ```java
       @Data
       @ToString
       @Builder
       @NoArgsConstructor
       @AllArgsConstructor
       public class ReviewDTO {
           private Long reviewNum;
           private Long mno;
           private Long mid;
           private String nickname;
           private String email;
           private int grade;
           private String text;
           private LocalDateTime regDate;
           private LocalDateTime modDate;
       }
       ```

    3. 리뷰 작업을 위한 메서드를 선언할 인터페이스 생성 - service.ReviewService

       ```java
       public interface ReviewService {
           // 영화 번호에 해당하는 리뷰를 전부 가져오기
           List<ReviewDTO> getList(Long mno);

           // 리뷰 등록
           Long register(ReviewDTO reviewDTO);

           // 리뷰 수정
           Long modify(ReviewDTO reviewDTO);

           // 리뷰 삭제
           Long remove(Long rnum);

           // DTO를 Entity로 변환해주는 메서드
           default Review dtoToEntity(ReviewDTO reviewDTO) {
               Review review = Review.builder()
                       .reviewnum(reviewDTO.getReviewNum())
                       .grade(reviewDTO.getGrade())
                       .text(reviewDTO.getText())
                       .movie(Movie.builder()
                               .mno(reviewDTO.getMno()).build())
                       .member(Member.builder().mid(reviewDTO.getMid()).build())
                       .build();
               return review;
           }

           // Entity를 DTO로 변환해주는 메서드
           default ReviewDTO entityToDto(Review review) {
               ReviewDTO reviewDTO = ReviewDTO.builder()
                       .reviewNum(review.getReviewnum())
                       .mno(review.getMovie().getMno())
                       .mid(review.getMember().getMid())
                       .email(review.getMember().getEmail())
                       .nickname(review.getMember().getNickname())
                       .grade(review.getGrade())
                       .text(review.getText())
                       .regDate(review.getRegDate())
                       .modDate(review.getModDate())
                       .build();
               return reviewDTO;
           }
       }
       ```

    4. Review 작업을 위한 ReviewServiceImpl 구현

       ```java
       @Service
       @RequiredArgsConstructor
       @Log4j2
       public class ReviewServiceImpl implements ReviewService {
           private final ReviewRepository reviewRepository;

           @Override
           public List<ReviewDTO> getList(Long mno) {
               // mno가지고 바로 하지 못한다.
               // Entity 만들때 외래키가 아닌 movie로 만들기 때문
               Movie movie = Movie.builder().mno(mno).build();
               List<Review> result = reviewRepository.findByMovie(movie);
               return result.stream().map(review -> entityToDto(review)).collect(Collectors.toList());
           }

           @Override
           public Long register(ReviewDTO reviewDTO) {
               // DTO니까 Entity로 바꿔야겠네!
               Review review = dtoToEntity(reviewDTO);
               reviewRepository.save(review);
               return review.getReviewnum();
           }

           @Override
           public Long modify(ReviewDTO reviewDTO) {
               // reviewDTO와 다른 점은 글 번호가 있냐 없냐이다.
               // 확인 로직을 추가해도 됨.
               Review review = dtoToEntity(reviewDTO);
               reviewRepository.save(review);
               return review.getReviewnum();
           }

           @Override
           public Long remove(Long rnum) {
               reviewRepository.deleteById(rnum);
               return rnum;
           }
       }
       ```

    5. Review 작업을 수행할 Controller 클래스를 생성

       review의 작업은 화면 전환을 하지 않는 경우가 대부분이라서 화면을 보여주는 Controller 대신에 데이터를 넘겨주는 REST Controller를 주로 이용한다.

       화면에서도 요청을 ajax 형태로 요청을 전송한다.

       ```java
       @RestController
       @RequiredArgsConstructor
       @Log4j2
       @RequestMapping("/reviews")
       public class ReviewController {
           private final ReviewService reviewService;

           // 영화번호에 해당하는 댓글 목록을 처리
           @GetMapping("/{mno}/list")
           public ResponseEntity<List<ReviewDTO>> list(@PathVariable("mno") Long mno) {
               List<ReviewDTO> result = reviewService.getList(mno);
               return new ResponseEntity<>(result, HttpStatus.OK);
           }

           // 댓글 추가
           @PostMapping("/{mno}")
           public ResponseEntity<Long> addReview(@PathVariable("mno") Long mno,
                                                 @RequestBody ReviewDTO reviewDTO) {
               Long result = reviewService.register(reviewDTO);
               return new ResponseEntity<>(result, HttpStatus.OK);

           }

           // 댓글 수정
           @PutMapping("/{mno}/{reviewnum}")
           public ResponseEntity<Long> updateReview(@PathVariable("mno") Long mno,
                                                    @PathVariable("reviewnum") Long reviewnum,
                                                    @RequestBody ReviewDTO reviewDTO) {
               Long result = reviewService.modify(reviewDTO);
               return new ResponseEntity<>(result, HttpStatus.OK);
           }

           // 댓글 삭제
           @DeleteMapping("/{mno}/{reviewnum}")
           public ResponseEntity<Long> deleteReview(@PathVariable("mno") Long mno,
                                                    @PathVariable("reviewnum") Long reviewnum) {
               Long result = reviewService.remove(reviewnum);
               return new ResponseEntity<>(result, HttpStatus.OK);
           }
       }
       ```

    6. 별점 처리

       [https://github.com/dobtco/starrr](https://github.com/dobtco/starrr) 라이브러리 이용

       jQuery plugin

       - read.html 파일에 2개의 모달 창을 추가
         하나의 모달 창은 리뷰 작업을 위한 모달 창이고 다른 하나는 이미지 원본 보기 모달 창이다.

         ```html
         <div class="reviewModal modal" tabindex="-1" role="dialog">
           <div class="modal-content">
             <div class="modal-dialog" role="document">
               <div class="modal-header">
                 <h5 class="modal-title">영화 리뷰</h5>
                 <button
                   type="button"
                   class="close"
                   data-dismiss="modal"
                   aria-label="Close"
                 >
                   <span aria-hidden="true">&times</span>
                 </button>
               </div>
               <div class="modal-body">
                 <div class="form-group">
                   <label>작성자</label>
                   <input type="text" class="form-control" name="mid" />
                 </div>
                 <div class="form-group">
                   <label>평점</label>
                   <div class="starrr"></div>
                 </div>
                 <div class="form-group">
                   <label>리뷰</label>
                   <input
                     type="text"
                     class="form-control"
                     name="text"
                     placeholder="영화에 대한 감상"
                   />
                 </div>
               </div>
               <div class="modal-footer">
                 <button
                   type="button"
                   class="btn btn-secondary closeBtn"
                   data-dismiss="modal"
                 >
                   닫기
                 </button>
                 <button
                   type="button"
                   class="btn btn-primary reviewSaveBtn"
                   data-dismiss="modal"
                 >
                   저장
                 </button>
                 <button
                   type="button"
                   class="btn btn-warning modifyBtn"
                   data-dismiss="modal"
                 >
                   수정
                 </button>
                 <button
                   type="button"
                   class="btn btn-danger removeBtn"
                   data-dismiss="modal"
                 >
                   삭제
                 </button>
               </div>
             </div>
           </div>
         </div>

         <!-- 원본 이미지 출력을 위한 모달 창 -->
         <div class="imageModal modal" tabindex="-1" role="dialog">
           <div class="modal-dialog modal-lg" role="document">
             <div class="modal-content">
               <div class="modal-header">
                 <h5 class="modal-title">이미지</h5>
                 <button
                   type="button"
                   class="close"
                   data-dismiss="modal"
                   aria-label="Close"
                 >
                   <span aria-hidden="true">&times;</span>
                 </button>
               </div>
               <div class="modal-body"></div>
               <div class="modal-footer">
                 <button
                   type="button"
                   class="btn btn-secondary closeImage"
                   data-dismiss="modal"
                 >
                   닫기
                 </button>
               </div>
             </div>
           </div>
         </div>
         ```

       - starr 라이브러리를 깃헙에서 다운받고 압축해제하여 starrr.css와 starrr.js 복사
       - 스크립트 코드를 추가해서 에러가 없는지 확인
         ```html
         <script th:src="@{/js/starrr.js}"></script>
         <link th:href="@{/css/starrr.css}" rel="stylesheet" />
         <link
           rel="stylesheet"
           href="http://cdnjs.cloudflare.com/ajax/libs/fontawesome/4.2.0/css/font-awesome.min.js"
         />
         ```

    7. 리뷰 등록

       - read.html 파일에 리뷰 등록 버튼과 리뷰 출력 영역을 생성
         ```html
         <button type="button" class="btn btn-info addReviewBtn">
           리뷰 작성
         </button>
         <div class="list-group reviewList"></div>
         ```
       - 스크립트 코드를 추가

         ```jsx
         <script>
                     $(document).ready(function (e) {
                         let grade = 0;
                         let mno = [[${dto.mno}]];
                         $('.starrr').starrr({
                             rating: grade,
                             change: function (e, value) {
                                 if (value) {
                                     console.log(value);
                                     grade = value;
                                 }
                             }
                         })

                         let reviewModal = $(".reviewModal");
                         let inputMid = $("input[name='mid']");
                         let inputText = $("input[name='text']");

                         $('.closeBtn').click(function() {
                             reviewModal.modal('hide');
                         })

                         $('.addReviewBtn').click(function() {
                             inputMid.val('');
                             inputText.val('');
                             $('.removeBtn, .modifyBtn').hide();
                             $('.reviewSaveBtn').show();
                             reviewModal.modal('show');
                         })
                     })
                 </script>
         ```

       - 대화 상자에서 추가 버튼을 눌렀을 때 처리하는 스크립트 코드를 추가
         ```jsx
         $(".reviewSaveBtn").click(function () {
           let data = {
             mno: mno,
             grade: grade,
             text: inputText.val(),
             mid: inputMid.val(),
           };
           console.log(data);
           $.ajax({
             url: "/reviews/" + mno,
             type: "POST",
             data: JSON.stringify(data),
             dataType: "text",
             contentType: "application/json; charset=utf-8",
             success: function (result) {
               self.location.reload();
             },
           });
           reviewModal.modal("hide");
         });
         ```
