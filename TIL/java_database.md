# [Java] Database 연동

1. **연동 방법**
   1. JDBC 코드 만으로 연동
      - 사용하고자 하는 데이터베이스 드라이버만 있으면 된다.
      - 드라이버가 데이터베이스와 자바 사이의 인터페이스
   2. Framework 이용
      - 드라이버와 프레임워크 라이브러리 모두 준비되어야 함.
      - 관계형 데이터베이스의 경우는 2가지로 사용 가능
        - SQL Mapper
          SQL을 Mapper File이나 Mapper Interface에 작성해서 SQL과 Java 코드를 분리하는 방식으로 사용하기는 쉬우나 성능이 떨어짐 - **MyBatis**가 대표적인 프레임워크로 SI 업계에서 주로 이용 \*\*\*\*
        - ORM
          테이블의 하나의 행과 프로그래밍 언어의 인스턴스를 1:1로 매핑시키는 방식으로 학습이 어렵지만 성능이 우수 - **JPA**(구현체는 **Hibernate**)가 대표적인 프레임워크로 솔루션 업계에서 주로 이용
          최근에는 **NoSQL**도 이 방식으로 사용이 가능
      - 프레임워크를 사용하는 방법들은 JDBC 코드로 변환이 되서 실행이 되는데 ORM은 코드 변환은 하는데 데이터를 가져와서 사용하는 방식이 조금 다르다.
2. **JDBC 프로그래밍 절차**

   1. 드라이버 클래스 로드 - 한 번만 수행
   2. 데이터베이스 연결 - Connection 클래스
   3. SQL문 실행

      Statement(완성된 SQL을 작성 - 보안 문제로 비추천), **PreparedStatement**(파라미터를 바인딩 가능한 실행 클래스 - 얘를 대부분 이용), CallableStatement(프로시저 이용)

   4. 결과 사용
      - int(select를 제외한 구문의 결과 - 영향받은 행의 개수)
      - ResultSet(select 구문의 결과 - 데이터를 순회할 수 있는 커서)
   5. 데이터베이스 연결 해제

3. **드라이버 로드 및 접속과 해제**

   1. 드라이버를 build path에 추가하고 데이터베이스 정보 필요

      ```java
      Class.forName(드라이버이름);
      // 드라이버 클래스가 로드 됨 - 데이터베이스 종류마다 드라이버 이름이 다르다.
      ```

   2. 연결

      ```java
      Connection 변수 = DriverManager.getConnection(데이터베이스경로/이름, 아이디, 비밀번호);
      // 아이디와 비밀번호는 상황에 따라 생략이 가능
      // MongoDB처럼 아이디와 비밀번호 없이 사용 가능한 경우와
      // 운영체제 인증(OS Authentication)을 사용하는 경우
      ```

      - 옵션 설정 - 트랜잭션 사용 여부
        자바는 기본이 Auto Commit이기 때문에 트랜잭션을 사용하고자 하면 Auto Commit을 해제해야 한다. **DB를 사용할 때는 기본 트랜잭션 모드가 무엇인지 늘 확인해야 한다.**

   3. 해제

      Connection 인스턴스로 close()를 호출하면 된다.

4. **MariaDB** **데이터베이스 연결**

   1. 의존성 설치 - **MariaDB Java Client**
   2. 데이터베이스 연결

      ```java
      import java.sql.Connection;
      import java.sql.DriverManager;

      public class MariaDB {
          public static void main(String[] args) {
              // 1. 데이터베이스 드라이버 로드
              // 드라이버 의존성을 설정하지 않거나 클래스 이름이 다르면 예외 발생
              try {
                  Class.forName("org.mariadb.jdbc.Driver");
                  System.out.println("드라이버 로드 성공");
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
                  e.printStackTrace();
              }

              // 2. 데이터베이스 접속
              try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "root")) {
                  System.out.println(con);
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
                  e.printStackTrace();
              }
          }
      }
      ```

   3. 데이터베이스 접속 정보를 별도의 파일에 작성

      - 특별한 경우를 제외하고는 데이터베이스 접속 정보는 하드 코딩을 하지 않는다.보안 문제와 운영 환경으로의 이전 문제 때문이다.
      - java나 Spring에서는 이러한 설정을 **properties** 파일이나 **xml** 파일에 많이 한다. 최근에 Spring boot에서는 **yaml(yml)**에도 많이 한다.
      - 프로젝트 루트 디렉토리에 데이터베이스 접속 정보를 저장할 db.properties 파일을생성하고 작성, 클래스 수정

        ```java
        import java.io.File;
        import java.io.FileInputStream;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.util.Properties;

        public class MariaDB {
            public static void main(String[] args) {
                // 1. 데이터베이스 접속에 필요한 정보 불러오기
                String driver = null;
                String url = null;
                String id = null;
                String password = null;

                // 2. 읽어올 파일 생성
                File file = new File("./db.properties");
                try (FileInputStream is = new FileInputStream(file)) {
                    // 파일의 내용을 properties에 저장
                    Properties properties = new Properties();
                    properties.load(is);

                    driver = properties.getProperty("driver");
                    url = properties.getProperty("url");
                    id = properties.getProperty("id");
                    password = properties.getProperty("password");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }

                // 3. 데이터베이스 드라이버 로드
                // 드라이버 의존성을 설정하지 않거나 클래스 이름이 다르면 예외 발생
                try {
                    Class.forName(driver);
                    System.out.println("드라이버 로드 성공");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }

                // 4. 데이터베이스 접속
                try (Connection con = DriverManager.getConnection(url, id, password)) {
                    System.out.println(con);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
        ```

   4. 트랜잭션 사용
      - Connection 인스턴스를 가지고 `setAutoCommit(boolean b)`로 설정
      - AutoCommit을 해제한 경우는 `commit()`과 `rollback`이라는 메서드로 트랜잭션을 사용
   5. SQL 구문
      - 실행 클래스
        - statement
          Connection 인스턴스의 createStatement()로 생성
          데이터 바인딩이 안됨 - 보안성이 떨어져서 거의 사용 x
        - **PreparedStatement**
          - Connection 인스턴스의 `prepareStatement(String sql)`로 생성
          - sql을 만들 때 정해지지 않은 값은 ?로 설정
          - `set자료형(? 인덱스, 실제 데이터)` 메서드를 호출해서 ?에 데이터를 바인딩
          - SQL 실행은 `int executeUpdate()` 와 `ResultSet executeQuery()` 를 호출
          - 날짜 및 시간의 경우 날짜는 `java.sqlDate` 와 시간만 매핑할 떄는 `java.sql.time` 그리고 날짜 시간을 모두 설정할 떄는 `java.sql.Timestamp`
          - 파일의 내용은 `blob` 사용
        - CallableStatement
          프로시저를 수행하는데 Connection 인스턴스의 `getCall(String procedureName)`로 생성
      - 결과 사용 - ResultSet
        - next()
          - 다음 데이터가 있으면 true, 없으면 false를 리턴
        - get자료형(컬럼이나 컬럼의 인덱스)
          - 컬럼 이름이나 인덱스에 해당하는 컬럼의 값을 리턴
          - 자료형을 String으로 설정하면 모든 데이터를 전부 받아올 수 있다.

5. **DTO & DAO 패턴**

   1. **DTO(Data Transfer Object) Pattern**

      여러 개의 데이터를 하나로 묶기 위해서 사용하는 패턴

   2. **DAO(Data Access Object) Pattern**

      데이터를 연동하는 로직을 별도의 클래스로 만들어서 처리

   3. **Singleton Desing Pattern**

      클래스의 인스턴스를 1개만 생성할 수 있도록 하는 Design Pattern(클래스를 설계하는 방법)

      Server에서 작업을 처리하는 클래스(서버에서는 동시에 처리하는 것이 아니고 대부분 Multi Thread를 이용)나 공유 자원을 소유하는 클래스를 이 디자인 패턴으로 디자인한다.

   4. **Template Method Pattern**

      인터페이스에 메서드의 모양을 만들고 이 인터페이스를 implements한 클래스를 만들어서 실제 내용을 구현하는 패턴

      예전에는 Sevice Layer와 DAO Layer 모두 적용했는데 최근의 프레임워크에서는 DAO Layer는 인터페이스 구현만으로 끝나기 때문에 최근에는 Service Layer에만 적용

   5. 실습

      - SQL

        ```sql
        show databases;

        use gyumin;

        drop table goods;

        # 테이블 생성
        create table goods(
        	code char(5) not null,
        	name varchar(50) not null,
        	manafacture varchar(2),
        	price int null,
        	primary key(code)
        )

        desc goods;

        # 샘플 데이터 생성
        insert into goods values('1', '사과', '한국', 1500);
        insert into goods values('2', '배', '호주', 400);
        insert into goods values('3', '바나나', '미국', 200);
        insert into goods values('4', '오렌지','중국', 1600);
        insert into goods values('5', '수박', '일', 1400);
        insert into goods values('6','감', '인',100);
        insert into goods values('7','', '대만', 1300);
        insert into goods values('8','귤', '독일', 1300);

        commit;

        select * from goods;
        ```

      - DTO 생성 - Good

        ```java
        public class Good {
            private String code;
            private String name;
            private String manufacture;
            private int price;

            public Good() {
                super();
            }

            public Good(String code, String name, String manufacture, int price) {
                super();
                this.code = code;
                this.name = name;
                this.manufacture = manufacture;
                this.price = price;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getManufacture() {
                return manufacture;
            }

            public void setManufacture(String manufacture) {
                this.manufacture = manufacture;
            }

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            @Override
            public String toString() {
                return "Good{" +
                        "code='" + code + '\'' +
                        ", name='" + name + '\'' +
                        ", manufacture='" + manufacture + '\'' +
                        ", price=" + price +
                        '}';
            }
        }
        ```

      - DAO 인터페이스 생성 - GoodDAO
        ```java
        // goods 테이블에 수행할 데이터베이스 작업의 원형을 소유할 인터페이스
        public interface GoodDAO {
        	public List<Good> getAll();
        }
        ```
      - DAO 클래스 생성 - GoodDAOImpl

        ```java
        import javax.xml.transform.Result;
        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;

        public class GoodDAOImpl implements GoodDAO {
            // 참조를 저장할 변수를 static으로 설정
            private static GoodDAO goodDAO;

            // 싱글톤을 만들기 위한 코드(중요하지 않음)
            // 외부에서 인스턴스를 생성을 못하도록 생성자를 private으로 설계
            private GoodDAOImpl() {}

            // 변수가 null이면 생성 후 리턴, null이 아니면 바로 리턴
            public static GoodDAO getInstance() {
                if(goodDAO == null) {
                    goodDAO = new GoodDAOImpl();
                }
                return goodDAO;
            }
        }
        ```

      - main 클래스 생성

        ```java
        public class GoodMain {
            public static void main(String[] args) {
                // 싱글톤 확인
                GoodDAO dao1 = GoodDAOImpl.getInstance();
                GoodDAO dao2 = GoodDAOImpl.getInstance();

                System.out.println(System.identityHashCode(dao1));
                System.out.println(System.identityHashCode(dao2));
            }
        }
        ```

      - 초기화 작업 - GoodDAOImpl
        ```java
        private Connection connection; // 연결
        private PreparedStatement pstmt; // sql 실행
        private ResultSet result; // select 구문의 결과
        ```
      - 데이터베이스 연결 및 접속 - GoodDAOImpl

        ```java
        // static 초기화 - 클래스가 로드 될 때 1번만 수행
            // static 속성만 사용 가능
            static {
                // 사용하고자 하는 데이터베이스 로드
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    System.out.println("드라이버 로드 성공");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }

            // 초기화 - 생성자를 호출하 때마다 먼저 호출된다.
            // 이 영역은 init이라는 메서드로 생성
            // 모든 속성 사용이 가능
            {
                // 데이터 베이스 접속 - java.sql.Connection
                try (Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/gyumin", "root", "root")) {

                    System.out.println(con);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        ```

      - 전체 데이터 가져오기 → 코드 참고. 너무 길다.
      - 데이터 삽입
        DAO 인터페이스에 삽입을 위한 메서드 선언
        DAOImpl에 구현을 위한 코드 작성

        ```java
        @Override
            public int insertGood(Good good) {
                int result = 0;
                // 삽입 작업이므로 트랜잭션을 고려
                try {
                    connection.setAutoCommit(false);

                    pstmt = connection.prepareStatement("insert into goods values(?, ?, ?, ?)");
                    pstmt.setString(1, good.getCode());
                    pstmt.setString(2, good.getName());
                    pstmt.setString(3, good.getManufacture());
                    pstmt.setInt(4, good.getPrice());

                    result = pstmt.executeUpdate();

                    connection.commit();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    try {
                        connection.rollback();
                    } catch (Exception e2) {
                    }

                    e.printStackTrace();
                }

                return result;
            }
        ```

      - 데이터 검색(like)

        ```java
        @Override
            public List<Good> likeGood(String content) {
                List<Good> list = new ArrayList<Good>();

                try {
                    pstmt = connection.prepareStatement("select * from goods where name like ? or manufacture like ?");
                    pstmt.setString(1, "%" + content + "%");
                    pstmt.setString(2, "%" + content + "%");

                    rs = pstmt.executeQuery();

                    while(rs.next()) {
                        Good good = new Good();
                        good.setCode(rs.getString("code"));
                        good.setName(rs.getString("name"));
                        good.setManufacture(rs.getString("manufacture"));
                        good.setPrice(rs.getInt("price"));

                        list.add(good);
                    }
                }catch(Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }

                return list;
            }
        ```
