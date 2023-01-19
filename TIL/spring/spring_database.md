# [Spring] Database 연동

1. **데이터베이스 연동**
   - Java JDBC 코드 이용
   - Spring JDBC 코드 이용
   - SQL Mapper 이용 - MyBatis
   - ORM 이용 - Hibernate
2. **Spring JDBC**

   1. 스프링의 데이터베이스 지원
      - 템플릿 클래스를 이용해서 데이터를 접근
      - 의미있는 예외 클래스로 예외를 던짐 - JDBC는 데이터 베이스 예외는 모두 SQLException으로 던져서 구체적으로 예외 내용을 파악하는 것이 어려움.
      - 트랜잭션 처리가 간단
      - 다른 데이터베이스 프레임워크와 연동을 지원
   2. pom.xml의 기본 설정을 수정
      - test가 제대로 수행되지 않는 경우 scope를 제거
        ```java
        <!-- Test Artifacts -->
        		<dependency>
        			<groupId>org.springframework</groupId>
        			<artifactId>spring-test</artifactId>
        			<version>${spring-framework.version}</version>
        		</dependency>
        ```
      - 사용하고자 하는 데이터베이스 라이브러리를 추가
        ```java
        <!-- https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client -->
        		<dependency>
        		    <groupId>org.mariadb.jdbc</groupId>
        		    <artifactId>mariadb-java-client</artifactId>
        		    <version>3.1.0</version>
        		</dependency>
        ```
   3. 데이터베이스 접속 테스트

      - Spring/test/java 디렉토리에 테스트를 위한 클래스를 생성하고 작성

        ```java
        import java.sql.Connection;
        import java.sql.DriverManager;

        import org.junit.jupiter.api.Test;

        public class DBTest {
        	@Test
        	public void testConnection() {
        		try {
        			// 여기서 예외가 발생하면 이름을 틀렸꺼나 의존성 설정 잘못됨.
        			Class.forName("org.mariadb.jdbc.Driver");

        			// 연결
        			Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/gyumin", "root","root");
        			System.out.println(con);
        			con.close();
        		}catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

   4. DAO 패턴
      - 데이터베이스 연관된 로직은 별도의 클래스를 만들어서 작성
      - POJO(외부 프레임워크의 클래스를 상속하지 않은) 형태로 작성하는 것을 권장
      - 직접 인스턴스 생성을 하지 않고 IoC와 DI를 이용해서 사용하는 것을 권장
      - 예외를 외부에 던지는 것을 권장하지 않음.
   5. DataSource

      - Spring에서는 데이터베이스를 사용할 때 DataSource 사용을 강제
        데이터베이스 연결을 별도의 Bean을 이용해서 사용하도록 한다.
      - 설정할 때 옵션: https://commons.apache.org/proper/common-dbcp/configuration.html
      - pom.xml 파일에 스프링 jdbc 의존성을 설정 - **Spring에서 데이터베이스 사용시 필수**
        ```java
        <dependency>
        			<groupId>org.springframework</groupId>
        			<artifactId>spring-jdbc</artifactId>
        			<version>${spring-framework.version}</version>
        </dependency>
        ```
      - applicationContext.xml 가서 DataSource 설정
        ```java
        <bean id="datasource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        		<property name="driverClassName">
        			<value>org.mariadb.jdbc.Driver</value>
        		</property>
        		<property name="url">
        			<value>jdbc:mariadb://localhost:3306/gyumin</value>
        		</property>
        		<property name="username">
        			<value>root</value>
        		</property>
        		<property name="password">
        			<value>root</value>
        		</property>
        </bean>
        ```
      - test 클래스 수정

        ```java
        import java.sql.Connection;

        import javax.sql.DataSource;

        import org.junit.jupiter.api.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.test.context.ContextConfiguration;
        import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

        @RunWith(SpringJUnit4ClassRunner.class)
        @ContextConfiguration("applicationContext2.xml")
        public class DBTest {
        	@Autowired
        	private DataSource dataSource;

        	@Test
        	public void testConnection() {
        		try {
        			Connection con = dataSource.getConnection();
        			System.out.println(con);
        		}catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

   6. Spring JDBC Class

      직접 쓰는 경우보다 다른 프레임워크를 이용하는 경우가 많음

      - JdbcTemplate
        기본 클래스
      - NamedParameterJdbcTemplate
        파라미터를 설정할 떄 인덱스 대신에 이름을 사용
      - SimpleJdbcTemplate
        가변 인자 사용 가능
      - SimpleJdbcInsert
        삽입에만 이용
      - SimpleJdbcCall
        프로시저 호출

3. **MyBatis**

   SQL Mapper Framework: SQL을 자바 코드와 분리시켜서 사용하는 Framework

   1. 장점
      - 사용하기 편리
      - 파라미터 매핑이나 Select 구문의 결과 매핑이 자동
   2. 단점
      - 성능이 떨어짐
   3. 의존성
      - 사용하고자 하는 관계형 데이터베이스 라이브러리
      - spring-jdbc
      - mybatis
      - mybatis-spring
   4. 구현 방법

      - XML 이용

        - pom.xml에 의존성을 추가(총 4가지)
          ```java
          <dependency>
          			<groupId>org.mybatis</groupId>
          			<artifactId>mybatis</artifactId>
          			<version>3.5.7</version>
          </dependency>
          <dependency>
          			<groupId>org.mybatis</groupId>
          			<artifactId>mybatis-spring</artifactId>
          			<version>2.0.6</version>
          </dependency>
          <dependency>
          			<groupId>org.springframework</groupId>
          			<artifactId>spring-jdbc</artifactId>
          			<version>${spring-framework.version}</version>
          </dependency>
          <!-- https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client -->
          <dependency>
          		    <groupId>org.mariadb.jdbc</groupId>
          		    <artifactId>mariadb-java-client</artifactId>
          		    <version>3.1.0</version>
          </dependency>
          ```
        - src/main/resources 디렉토리에 mybatis 관련 파일을 저장하기 위한 디렉토리를 생성 - mybatis
        - 생성한 디렉토리에 MyBatis 환경 설정 파일을 작성 - mybatis-config.xml

          ```java
          <?xml version="1.0" encoding="UTF-8"?>
          <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
          <configuration>

          </configuration>
          ```

        - applicationContext.xml 파일에 MyBatis 사용을 위한 설정 Bean을 생성하는 코드를 추가
          ```java
          <bean class="org.mybatis.spring.SqlSessionFactoryBean" id="sqlSessionFactory">
          		<property name="dataSource" ref="dataSource" />
          		<property name="configLocation" value="classpath:/mybatis/mybatis-config.xml" />
          	</bean>
          ```
        - main 메서드에서 확인

          ```java
          import org.apache.ibatis.session.SqlSession;
          import org.apache.ibatis.session.SqlSessionFactory;
          import org.springframework.context.support.GenericXmlApplicationContext;

          public class DiMain {
          	public static void main(String[] args) {
          		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

          			SqlSessionFactory sqlFactory = context.getBean("sqlSessionFactory", SqlSessionFactory.class);
          			System.out.println(sqlFactory);
          			SqlSession session = sqlFactory.openSession();
          			System.out.println(session);
          		} catch(Exception e) {
          			System.out.println(e.getLocalizedMessage());
          		}
          	}
          }
          ```

      - 인터페이스 이용
        (f번부터 시작!)

   5. XML을 이용한 MyBatis 사용

      - 데이터베이스 테이블 생성 - goods

        ```java
        use gyumin;

        drop table goods;

        create table goods(
        	code int not null,
        	name varchar(50) not null,
        	manufacture varchar(50),
        	price int,
        	shelflife date,
        	primary key(code)
        );
        ```

      - 테이블의 데이터를 표현하기 위한 클래스를 생성 - domain.Good

        ```java
        package domain;

        import java.sql.Date;

        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public class Good {
        	private int code;
        	private String name;
        	private String manufacture;
        	private int price;
        	private Date shelflife;
        }
        ```

      - mybatis-config.xml 파일에 VO 클래스의 경로를 등록해서 클래스 이름만으로 사용할 수 있도록 설정(domain.Good 이 아니라 Good으로 쓸 수 있음.)
        ```java
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
        <configuration>
        	<typeAliases>
        		<package name="domain.Good" />
        	</typeAliases>
        </configuration>
        ```
      - SQL 작성 파일을 저장할 디렉토리를 생성 (여기에는 SQL만 있어야 함. DTD도 필요) - src/main/resources/mybatis/mappers
      - mappers 디렉토리 안에 SQL을 작성할 xml 파일을 생성하고 데이터를 삽입하는 SQL을 작성

        ```java
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

        <mapper namespace="good">
        	<insert id="insertGood" parameterType="Good"> insert into goods(code,
        		name, manufacture, price, shelflife) values(#{code}, #{name}, #{manufacture},
        		#{price}, #{shelflife})
        	</insert>
        </mapper>
        ```

      - applicationContext.xml 파일에 XML을 이용해서 작성한 MyBatis 사용을 위한 설정을 추가

        ```java
        <bean class="org.mybatis.spring.SqlSessionFactoryBean" id="sqlSessionFactory">
        		<property name="dataSource" ref="dataSource" />
        		<property name="configLocation" value="classpath:/mybatis/mybatis-config.xml" />
        		<property name="mapperLocations" value="classpath:/mybatis/mappers/good.xml" />
        	</bean>

        	<bean class="org.mybatis.spring.SqlSessionTemplate" id="sqlSession" destroy-method="clearCache">
        		<constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory" />
        	</bean>
        ```

      - SQL을 사용할 Repository 클래스를 만들고 데이터 삽입 메서드를 작성 - di.persistence.GoodRepository

        ```java
        package di.persistence;

        import org.apache.ibatis.session.SqlSession;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;

        import domain.Good;

        @Repository
        public class GoodRepository {
        	@Autowired
        	private SqlSession sqlSession;

        	public int insertGood(Good good) {
        		// 삽입을 제외한 모든 메서드는 리턴타입이 int
        		return sqlSession.insert("insertGood", good);
        	}
        }
        ```

      - main 메서드에서 사용

        ```java
        import org.apache.ibatis.session.SqlSession;
        import org.apache.ibatis.session.SqlSessionFactory;
        import org.springframework.context.support.GenericXmlApplicationContext;

        import di.persistence.GoodRepository;
        import domain.Good;

        public class DiMain {
        	public static void main(String[] args) {
        		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

        			GoodRepository repository = context.getBean(GoodRepository.class);
        			Good good = Good.builder()
        					.code(1)
        					.name("배")
        					.manufacture("나주")
        					.price(5000)
        					.build();
        			repository.insertGood(good);
        		} catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

      - good.xml 파일에 테이블 전체 데이터를 가져오는 select 구문을 추가
        ```java
        <select id="allGood" resultType="domain.Good">
        		select * from goods
        	</select>
        ```
      - GoodRepository 클래스에서 구문을 불러오는 메서드를 작성
        ```java
        public List<Good> allGood() {
        		return sqlSession.selectList("allGood");
        	}
        ```
      - main 메서드에서 확인

        ```java
        import java.util.List;

        import org.springframework.context.support.GenericXmlApplicationContext;

        import di.persistence.GoodRepository;
        import domain.Good;

        public class DiMain {
        	public static void main(String[] args) {
        		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

        			GoodRepository repository = context.getBean(GoodRepository.class);
        			List<Good> list = repository.allGood();
        			for(Good good: list) {
        				System.out.println(good);
        			}
        		} catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

      - code를 가지고 하나의 데이터를 찾아오는 SQL를 good.xml에 작성
        ```java
        <select id="getGood" resultType="domain.Good" parameterType="int">
        		select * from goods where code = #{code}
        </select>
        ```
      - GoodRepository 클래스에 하나의 데이터를 찾아오는 메서드를 작성

        ```java
        import java.util.List;

        import org.springframework.context.support.GenericXmlApplicationContext;

        import di.persistence.GoodRepository;
        import domain.Good;

        public class DiMain {
        	public static void main(String[] args) {
        		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

        			GoodRepository repository = context.getBean(GoodRepository.class);
        			Good good = repository.getGood(1);
        			System.out.println(good);
        		} catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

   6. 인터페이스를 이용한 MyBatis 사용

      - GoodRepository 클래스의 bean이 생성되지 않도록 어노테이션을 주석 처리
        ```java
        // @Repository
        public class GoodRepository {
        	// @Autowired
        	private SqlSession sqlSession;
        	...
        ```
      - Repository 역할을 수행할 인터페이스를 생성하고 메서드 선언 - GoodMaper

        ```java
        package di.persistence;

        import java.util.List;

        import org.apache.ibatis.annotations.Select;
        import org.springframework.stereotype.Repository;

        import domain.Good;

        @Repository
        public interface GoodMapper {
        	@Select("select * from goods")
        	public List<Good> allGood();
        }
        ```

      - applicationContext.xml 파일에 있는 bean 설정을 수정

        ```java
        <!--
        	<bean class="org.mybatis.spring.SqlSessionFactoryBean" id="sqlSessionFactory">
        		<property name="dataSource" ref="dataSource" />
        		<property name="configLocation" value="classpath:/mybatis/mybatis-config.xml" />
        		<property name="mapperLocations" value="classpath:/mybatis/mappers/good.xml" />
        	</bean>

        	<bean class="org.mybatis.spring.SqlSessionTemplate" id="sqlSession" destroy-method="clearCache">
        		<constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory" />
        	</bean>
        	-->

        	<!-- 인터페이스를 사용하는 MyBatis 설정 -->
        	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        		<property name="dataSource" ref="dataSource" />
        	</bean>
        	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        		<property name="basePackage" value="di.persistence" />
        	</bean>
        ```

      - main 메서드 수정

        ```java
        import java.util.List;

        import org.springframework.context.support.GenericXmlApplicationContext;

        import di.persistence.GoodMapper;
        import domain.Good;

        public class DiMain {
        	public static void main(String[] args) {
        		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

        			GoodMapper repository = context.getBean(GoodMapper.class);
        			List<Good> goods = repository.allGood();
        			System.out.println(goods);
        		} catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

4. **데이터베이스 작업 로그 출력**

   log4jdbc-log4j2 라이브러리 이용

   로그를 출력하기 때문에 잘못된 SQL이나 잘못된 속성의 이름을 파악하는데는 도움이 되지만 SQL 실행 속도는 느려짐.

   1. pom.xml 파일에 의존성 설정

      ```java
      <dependency>
      			<groupId>org.bgee.log4jdbc-log4j2</groupId>
      			<artifactId>log4jdbc-log4j2-jdbc4</artifactId>
      			<version>1.16</version>
      </dependency>
      ```

   2. src/main/resources 디렉토리에 [log4jdbc.log4j2.properties](http://log4jdbc.log4j2.properties) 파일을 만들고 작성

      ```java
      log4jdbc.spylogdelegator.name=net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator
      ```

   3. applicationContext.xml 파일의 DataSource 빈의 속성을 수정

      ```java
      <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
      		<property name="driverClassName">
      			<value>net.sf.log4jdbc.sql.jdbcapi.DriverSpy</value>
      		</property>
      		<property name="url">
      			<value>jdbc:log4jdbc:mariadb://localhost:3306/gyumin</value>
      		</property>
      		<property name="username">
      			<value>root</value>
      		</property>
      		<property name="password">
      			<value>root</value>
      		</property>
      	</bean>
      ```

   4. main 메서드 실행

5. **트랜잭션 처리**

   스프링은 트랜잭션을 어노테이션을 이용해서 처리할 수 있다.

   TransactionManager 클래스의 bean을 만들고 <tx:annotaion-driven/>을 설정 파일에 추가해주면 메서드 위에 @Transactional만 추가하면 트랜잭션 처리가 되어서 중간에 예외가 발생하면 rollback을 수행하고 예외가 발생하지 않으면 commit을 한다.

   1. 트랜잭션 처리를 위해서 Repository 클래스를 생성하고 메서드를 생성 - di.persistence.TransactionRepository

      ```java
      package di.persistence;

      import java.util.HashMap;
      import java.util.Map;

      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
      import org.springframework.stereotype.Repository;

      @Repository
      public class TransactionRepository {

      	@Autowired
      	private SimpleJdbcInsert template;

      	public void insert() {
      		template.withTableName("goods");
      		Map<String, Object> map = new HashMap<String, Object>();
      		map.put("code", 100);
      		map.put("name", "쌀");
      		map.put("manufacture", "이천");
      		map.put("price", 100000);

      		template.execute(map);
      		// 기본키 중복으로 예외가 발생
      		template.execute(map);
      	}
      }
      ```

   2. applicationContext.xml 파일에 SimpleJdbcInsert 클래스의 bean 생성 코드 작성

      ```java
      <bean class="org.springframework.jdbc.core.simple.SimpleJdbcInsert" id="simpleJdbcInsert">
      		<constructor-arg ref="dataSource" />
      </bean>
      ```

   3. main 메서드에 실행하는 코드를 작성하고 데이터베이스 확인

      ```java
      import org.springframework.context.support.GenericXmlApplicationContext;

      import di.persistence.TransactionRepository;

      public class DiMain {
      	public static void main(String[] args) {
      		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

      			TransactionRepository repository = context.getBean(TransactionRepository.class);
      			repository.insert();
      		} catch(Exception e) {
      			System.out.println(e.getLocalizedMessage());
      		}
      	}
      }
      ```

   4. 결과

      template.execute(map)을 두 번 썼기 때문에 예외가 발생했지만 DB확인 결과 하나의 데이터는 삽입됨 - SQL 문장 단위로 commit과 rollback이 설정됨.

   5. 트랜잭션 적용을 위해서 applicationContext.xml 파일에 tx 네임스페이스 추가

      ```java
      <?xml version="1.0" encoding="UTF-8"?>
      <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:c="http://www.springframework.org/schema/c"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xmlns:mvc="http://www.springframework.org/schema/mvc"
         xmlns:p="http://www.springframework.org/schema/p"
         xmlns:tx="http://www.springframework.org/schema/tx"
         xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.3.xsd
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
            http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd
            http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.3.xsd">
      			...

      <!-- MyBatis에서 트랜잭션을 사용하기 위한 매니저 클래스의 bean -->
      	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
      		<property name="dataSource" ref="dataSource" />
      	</bean>

      	<tx:annotation-driven />
      </beans>
      ```

   6. 트랜잭션을 적용할 메서드 위에 @Transactional을 추가

      ```java
      package di.persistence;

      import java.util.HashMap;
      import java.util.Map;

      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
      import org.springframework.stereotype.Repository;
      import org.springframework.transaction.annotation.Transactional;

      @Repository
      public class TransactionRepository {

      	@Autowired
      	private SimpleJdbcInsert template;

      	// 이제 중간에 예외가 발생하면 모든 작업이 Rollback
      	// 따라서 하나의 값도 DB에 추가되지 않음.
      	// 대부분의 경우 트랜잭션 적용은 Service 메서드에서 한다.
      	@Transactional
      	public void insert() {
      		template.withTableName("goods");
      		Map<String, Object> map = new HashMap<String, Object>();
      		map.put("code", 101);
      		map.put("name", "대");
      		map.put("manufacture", "삼척");
      		map.put("price", 120000);

      		template.execute(map);
      		// 기본키 중복으로 예외가 발생
      		template.execute(map);
      	}
      }
      ```

   7. 메인 메서드 실행 후 데이터베이스 확인 - 하나의 데이터도 삽입되지 않음.

6. **Hibernate**

   1. 개요
      - Java 기반의 ORM
      - Hibernate가 만들어진 이후에 Java에서도 ORM에 대한 표준 Spec을 발표했는데 이것이 JPA(Java Persistence API)
      - JPA는 인터페이스고 Hibernate는 구현체
   2. 장점
      - 복잡하고 중복되는 JDBC 코드를 제거
      - 성능이 우수
      - 데이터베이스 변경이 쉬움 - 자바 코드를 수정하지 않고 설정만 변경하면 된다.
   3. 단점
      - 배우기가 어렵다
      - 여러 명의 팀 작업이 필요한 경우는 MyBatis를 사용하고 적은 규모로 진행되는 솔루션 개발에는 JPA를 권장
   4. 필요한 의존성
      - 데이터베이스 라이브러리, Spring-JDBC, Spring-ORM, Hibernate
   5. 세팅

      - pom.xml 파일의 properties 태그 안에서 Hibernate 버전 변경
        ```java
        <!-- Hibernate / JPA Spring4 이후 부터는 Hibernate 5버전 이상만 지-->
        		<hibernate.version>5.4.2.Final</hibernate.version>
        ```
      - pom.xml 파일의 dependencies 태그에 Hibernate 사용을 위한 의존성 추가

        ```java
        <dependency>
        			<groupId>javax.xml.bind</groupId>
        			<artifactId>jaxb-api</artifactId>
        			<version>2.3.0</version>
        		</dependency>

        		<dependency>
        			<groupId>org.springframework</groupId>
        			<artifactId>spring-orm</artifactId>
        			<version>${spring-framework.version}</version>
        		</dependency>
        ```

      - 테이블과 연동할 모델 클래스 생성 - domain.Good 변경 ( di.domain으로 패키지 설정 바꿔주고)

        ```java
        package di.domain;

        import java.sql.Date;

        import javax.persistence.Column;
        import javax.persistence.Entity;
        import javax.persistence.Id;
        import javax.persistence.Table;

        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Entity
        @Table(name="goods")
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public class Good {
        	@Id
        	private int code;
        	@Column
        	private String name;
        	@Column
        	private String manufacture;
        	@Column
        	private int price;
        	@Column
        	private Date shelflife;
        }
        ```

      - applicationContext.xml 파일에 HIberante 사용 빈을 생성(중요)

        ```java
        <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
        		<property name="dataSource" ref="dataSource" />
        		<property name="annotatedClasses">
        			<list>
        				<value>di.domain.Good</value>
        			</list>
        		</property>
        		<property name="hibernateProperties">
        			<value>hibernate.dialect=org.hibernate.dialect.MySQLDialect</value>
        		</property>
        </bean>

        <!-- @Repository 클래스들에 대해 자동으로 예외를 Spring의 DataAccessException으로 일괄 변환해주는 설정 -->
        <bean class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor" />
        <bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
        		<property name="sessionFactory" ref="sessionFactory" />
        </bean>
        ```

   6. Repository 클래스를 생성하고 메서드를 작성

      ```java
      package di.persistence;

      import org.hibernate.Session;
      import org.hibernate.SessionFactory;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Repository;

      import di.domain.Good;

      @Repository
      public class HibernateRepository {
      	@Autowired
      	private SessionFactory sessionFactory;

      	public void insertGood(Good good) {
      		Session session = sessionFactory.getCurrentSession();
      		// 데이터 추가
      		session.save(good);
      	}
      }
      ```

   7. main 메서드 변경

      ```java
      import org.springframework.context.support.GenericXmlApplicationContext;

      import di.domain.Good;
      import di.persistence.HibernateRepository;

      public class DiMain {
      	public static void main(String[] args) {
      		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

      			HibernateRepository repository = context.getBean(HibernateRepository.class);
      			Good good = Good.builder()
      					.code(301)
      					.name("단감")
      					.manufacture("진영")
      					.price(2500)
      					.build();
      			repository.insertGood(good);
      		} catch(Exception e) {
      			System.out.println(e.getLocalizedMessage());
      		}
      	}
      }
      ```

   8. 이 후 여러가지 메서드 추가

      ```java
      package di.persistence;

      import java.util.List;

      import javax.persistence.criteria.CriteriaQuery;

      import org.hibernate.Session;
      import org.hibernate.SessionFactory;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.stereotype.Repository;
      import org.springframework.transaction.annotation.Transactional;

      import di.domain.Good;

      @Repository
      public class HibernateRepository {
      	@Autowired
      	private SessionFactory sessionFactory;

      	@Transactional
      	public void insertGood(Good good) {
      		Session session = sessionFactory.getCurrentSession();
      		// 데이터 추가
      		session.save(good);
      	}

      	@Transactional
      	public void updateGood(Good good) {
      		Session session = sessionFactory.getCurrentSession();
      		// 동일한 기본키의 데이터를 찾아서 나머지를 수정
      		session.update(good);
      	}

      	// 테이블의 전체 데이터 조회
      	@Transactional
      	public List<Good> listGood() {
      		Session session = sessionFactory.getCurrentSession();
      		// 제약 조건 생성
      		CriteriaQuery<Good> criteriaQuery = session.getCriteriaBuilder().createQuery(Good.class);
      		criteriaQuery.from(Good.class);
      		return session.createQuery(criteriaQuery).getResultList();
      	}
      }
      ```

   9. main 메서드에서 테스트

      **메모리 데이터베이스의 개념을 이용하기 때문에 성능이 우수**
