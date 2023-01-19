# [Spring] Legacy Project

1. **Spring**
   - Java Enterprise Application 개발을 편리하게 해주는 Open Source로 경량급 프레임워크
   - 빠른 구현 시간
   - IoC(제어의 역전 - 컴포넌트 기반 개발 방법론, 클래스는 개발자가 만들지만 인스턴스의 생성과 수명 주기 관리는 Framework나 Web Container가 하는 방식), DI(의존성 주입 - 클래스 내부에서 사용할 인스턴스를 외부에서 생성해서 주입시켜 주는 것), AOP(관점 지향 프로그래밍 - 프로그램을 관점에 따라 나누어 구현한 후 결합하는 방식) 지원
2. **개발 환경**
   - JDK
     설치는 11이상(8 이상이면 별 상관이 없는데 Eclipse 최신 버전이 JDK 11 이상 사용, 국내 개발 환경에서는 대부분 8)
   - IDE
     - Eclipse
       Spring Plug In 설치
       Spring 전용 Eclipse - STS(Legacy Project를 사용하고자 하는 경우는 3버전)
       기업이다 단체에서 만든 Framework - 전자 정부 프레임워크, AnyFramework 등
     - IntelliJ
       Spring Boot Project는 지원하는데 Legacy Project를 하고자 하는 경우는 별도의 설정들이 필요
3. **개발 환경 설정**

   1. STS 3 버전을 다운로드 - spring.io에 접속해서 [Project] 메뉴에서 Spring Tools 4 메뉴를 클릭

      STS 4 버전은 Legacy Project를 지원하지 않는다.

   2. lombok
      - 데이터를 표현하는 클래스를 만들 때 반복적으로 생성하는 getter/setter, 생성자, toString과 같은 코드를 컴파일할 때 자동으로 생성해주는 라이브러리
      - 설치
        https://projectlombok.org/download 에서 jar 파일을 다운로드
        터미널에서 java - jar 다운로드받은 파일의 경로를 실행해서 사용할 IDE를 설정

4. **lombok의 annotaion**

   1. 생성자
      - @NoArgsConstructor
        매개변수가 없는 생성자
      - @AllArgsConstructor
        모든 속성이 매개변수인 생성자
      - @RequiredArgsConstructor
        속성 중에서 final이나 @NonNull인 속성이 존재하는 경우에 외부에 동일한 자료형의 데이터가 있으면 자동으로 주입받아서 생성하는 생성자
   2. @Builder

      인스턴스를 생성할 때 생성자를 직접 호출하지 않고 Builder 패턴을 적용해서 생성하도록 해주는 어노테이션

      Builder 패턴은 필요한 부분을 추가하면서 만들어가는 패턴

      `클래스이름.builder().속성이름(값).속성이름(값)….build()`로 생성

      <aside>
      💻 Singleton: 인스턴스를 하나만 만드는 패턴
      Template Method Pattern: 인터페이스에 메서드를 선언하고 클래스에서 구현하는 패턴
      Decorate Pattern: 다른 인스턴스들을 조합해서 만드는 패턴
      Builder Pattern: 속성들을 추가해 나가면서 만드는 패턴

      생성

      - 처음에 생성
      - 필요할 때 생성

      </aside>

   3. @Getter & @Setter
   4. @ToString
   5. @EqualsAndHashCode
   6. @NotNull

      속성 위에 기재해서 null 여부를 체크해서 null 이면 NullPointerException 발생

   7. @Log

      log 변수 자동 생성

   8. @Data

      접근자 메서드, toString, EqulasAndHashCode, RequiredArgsConstructor 모두 생성

5. **Spring Project**

   1. Spring Legacy Project

      WAS를 별도로 사용해야하고 템플릿을 생성해서 사용하는 프로젝트

      Spring 공식 사이트에서는 더 이상 업데이트를 하지 않는다고 했는데 국내 많은 프로젝트들과 프레임워크가 이 방식을 채택

   2. Spring Boot Project

      내장된 WAS를 이용해서 실행 및 배포가 가능한 수준의 애플리케이션을 빠르게 생성

6. **일반 스프링 프로젝트를 생성하고 기본 설정을 변경**

   1. STS를 실행
   2. [File] - [New] - [Others] 에서 Spring을 선택하고 [Spring Legacy Project]를 선택하고 Simple Spring Maven 프로젝트를 생성 - IoCAndDI
   3. 프로젝트가 제대로 만들어지지 않는 경우에는
   4. pom.xml properties에서 속성 변경

      java.version `1.8`

      spring-framework.version - `5.0.7.RELEASE`

      junit.version - `4.12`

   5. JRE 버전을 변경

      프로젝트를 선택하고 마우스 오른쪽을 눌러서 Build Path - Configure Build Path를 선택해서 변경

      java 1.8 버전을 사용할 수 있도록 해주는 것.

7. **lombok 사용**

   1. pom.xml dependencies에서 lombok 라이브러리를 사용하기 위한 의존성 추가

      ```java
      <dependency>
      			<groupId>org.projectlombok</groupId>
      			<artifactId>lombok</artifactId>
      			<version>1.18.24</version>
      </dependency>
      ```

   2. src/main/java에 클래스를 만들고 속성만 설정한 후 확인

      ```java
      package domain;

      import lombok.Data;

      @Data
      public class Item {
      	private int itemId;
      	private String itemName;
      	private int price;
      	private String description;
      	private String pictureUrl;

      }
      ```

   3. Item 클래스를 사용하는 ItemRepository 클래스를 만들고 메서드 생성 - presistence.ItemRepository

      ```java
      package persistence;

      import domain.Item;

      public class ItemRepository {
      	public ItemRepository() {}

      	public Item get() {
      		Item item = new Item();
      		item.setItemId(1);
      		item.setItemName("망고");
      		item.setDescription("가장 좋아하는 과일");
      		item.setPrice(3000);
      		item.setPictureUrl("mango.jpg");
      		return item;
      	}
      }
      ```

   4. 실행을 위해서 main 메서드를 소유한 클래스를 만들어서 실행 - Main

      ```java
      import domain.Item;
      import persistence.ItemRepository;

      public class Main {
      	public static void main(String[] args) {
      		ItemRepository itemRepository = new ItemRepository();
      		Item item = itemRepository.get();
      		System.out.println(item);
      	}

      }
      ```

8. **생성자 대신 정적 팩토리 또는 다른 클래스의 메서드를 호출해서 인스턴스 생성**

   1. Factory 패턴
      - 클래스의 인스턴스를 다른 클래스에서 생성하는 패턴
      - 생성자를 이용하게 되면 생성자가 Overloading된 경우 이름이 모두 동일하기 때문에 어떤한 방식으로 생성되는지 파악하기가 어려움
      - 싱글톤 패턴을 적용하기 편리
      - 매개변수에 따라 다양한 형태의 인스턴스를 생성할 수 있음 - 상속 관계에 있는 여러 클래스의 인스턴스
      - 단점으로는 API 문서에서 알아보기가 어려워지고 생성자를 private로 만들기 때문에 상속이 불가능
   2. Factory 메서드의 Naming
      - from
        매개변수 1개를 받아서 생성
      - of
        매개변수 여러 개를 받아서 생성
      - valueOf
        of의 자세한 버전
      - sharedInstance
        모두 동일한 인스턴스 - 싱글톤
      - instance, getInstance
        매개변수를 받는 경우에는 모두 동일한 인스턴스가 아닐 수 있음.
      - create, newInstance
        항상 새로운 인스턴스를 생성해서 리턴
      - getType
        getInstance와 동일하지만 현재 클래스가 아닌 다른 클래스의 인스턴스를 리턴하고 Type 부분이 그에 해당하는 클래스 이름
      - newType
        newInstance와 동일하지만 현재 클래스가 아닌 다른 클래스의 인스턴스를 리턴하고 Type 부분이 그에 해당하는 클래스 이름
      - type
        getType과 newType의 간결화된 버전
   3. 정적 메서드를 이용한 인스턴스 생성

      ItemRepository의 인스턴스를 생성해주는 Factory 클래스를 만들고 정적 메서드를 생성 - persistence.RepositoryFactory

      ```java
      package persistence;

      // 인스턴스를 생성해주는 Factory 클래
      public class RepositoryFactory {
      	// create 대신에 newInstance를 사용해도 같은 의미
      	// 매번 인스턴스를 생성해서 제
      	public static ItemRepository create() {
      		return new ItemRepository();
      	}
      }
      ```

      ItemRepository 클래스에 외부에서 인스턴스 생성을 못하도록 생성자를 package로 추가

   4. main 메서드 수정

      ```java
      import domain.Item;
      import persistence.ItemRepository;
      import persistence.RepositoryFactory;

      public class Main {
      	public static void main(String[] args) {
      		// ItemRepository itemRepository = new ItemRepository();

      		// 인스턴스 생성을 다른 팩토리 클래스를 이용해서 생성
      		// 다른 클래스의 메서드를 이용해서 인스턴스를 생성하는 것을
      		// Factory Method Pattern이라고 한다.
      		ItemRepository itemRepository = RepositoryFactory.create();

      		Item item = itemRepository.get();
      		System.out.println(item);
      	}
      }
      ```

9. **IoC(Inversion of Control - 제어의 역전 또는 제어의 흐름)**

   개발자가 작성한 프로그램이 재사용 라이브러리(Framework)의 흐름 제어를 받게 되는 소프트웨어 디자인 패턴

   1. 사용 이유
      - 구현(개발자)과 수행(Framework)의 분리 - 개발자는 수명 주기나 디자인 패턴에 신경쓰지 않아도 됨.
      - 다른 모듈과의 결합에 대해서 신경쓸 필요가 없어짐.
      - 모듈을 변경해도 다른 시스템에 부작용을 일으킬 가능성이 줄어듬
   2. Spring Bean(bean 이라고도 함)
      - 스프링이 제어권을 가지고 생성하고 관계를 설정하는 오브젝트 - Android나 React에서는 Component라고 함
      - 스프링에서 제어의 역전이 적용된 인스턴스
      - Bean을 관리하는 인스턴스를 Bean Factory라고 하고 Spring에서는 BeanFactory라는 인터페이스로 제공
   3. ApplicationContext
      - BeanFactory 인터페이스에 여러가지 기능이 추가된 인터페이스
      - IoC 컨테이너이면서 Singleton을 저장하고 관리하는 Singleton Registory
      - 기본적으로 모든 인스턴스를 Singleton으로 생성 - 이유는 Spring은 대부분 서버 환경에서 구동되기 때문
   4. AnnotaionApplicationContext

      - 어노테이션을 이용해서 Spring Bean을 생성할 수 있도록 해주는 Application Context
      - Factory 클래스 위에는 @Configuration을 추가하고 SpringBean을 생성해주는 메서드 위에는 @Bean을 추가
      - AnnotaionApplicationContext 클래스의 인스턴스를 만들 때 생성자에 Factory 클래스를 설정하고 인스턴스를 생성할 때는 getBean 메서드를 호출
      - `getBean(메서드이름)`: Object 타입으로 리턴
      - `getBean(클래스이름)`: 클래스 타입으로 리턴
      - `getBean(메서드이름, 클래스이름.class)`: 메서드를 호출해서 클래스 타입으로 리턴 - 가장 많이 사용
      - RepositoryFactory 클래스를 수정

        ```java
        package persistence;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        // 인스턴스를 생성해주는 Factory 클래스
        // 팩토리 클래스라는 어노테이션
        @Configuration
        public class RepositoryFactory {
        	// create 대신에 newInstance를 사용해도 같은 의미
        	// 매번 인스턴스를 생성해서 제공
        	// 인스턴스를 만들어주는 메서드
        	@Bean
        	public static ItemRepository create() {
        		return new ItemRepository();
        	}
        }
        ```

      - main 메서드를 통해 정말 스프링이 하나의 인스턴스를 생성하는 것인지 확인

        ```java
        import org.springframework.context.annotation.AnnotationConfigApplicationContext;

        import domain.Item;
        import persistence.ItemRepository;
        import persistence.RepositoryFactory;

        public class Main {
        	public static void main(String[] args) {
        		// ItemRepository itemRepository = new ItemRepository();

        		// 인스턴스 생성을 다른 팩토리 클래스를 이용해서 생성
        		// 다른 클래스의 메서드를 이용해서 인스턴스를 생성하는 것을
        		// Factory Method Pattern이라고 한다.

        		// 스프링이 인스턴스를 생성
        		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RepositoryFactory.class);

        		// RepositoryFactory 클래스의 create 메서드를 호출해서
        		// ItemRepository 클래스의 인스턴스를 생성
        		ItemRepository itemRepository = context.getBean("create", ItemRepository.class);
        		Item item = itemRepository.get();

        		System.out.println(item);

        		ItemRepository itemRepository2 = context.getBean("create", ItemRepository.class);
        		// 스프링은 싱글톤 패턴으로 생성하므로 2개의 해시코드는 일
        		System.out.println(System.identityHashCode(itemRepository));
        		System.out.println(System.identityHashCode(itemRepository2));

        		context.close();
        	}
        }
        ```

   5. xml을 이용하는 Spring Bean 생성(DTD에 의해서 결정)

      - src/main/resources 디렉토리에서 마우스 오른쪽을 눌러서 Spring Bean Configuration 파일을 추가

        ```java
        <?xml version="1.0" encoding="UTF-8"?>
        <beans xmlns="http://www.springframework.org/schema/beans"
        	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

        	<!-- persisitence.ItemRepository 클래스의
        	인스턴스를 itemRepository라는 이름으로 생성 -->
        	<bean id="itemRepository"
        		class="persistence.ItemRepository"/>

        </beans>
        ```

      - main 메서드에서 ItemRepository 인스턴스 생성하는 코드를 작성

        ```java
        import org.springframework.context.annotation.AnnotationConfigApplicationContext;
        import org.springframework.context.support.GenericXmlApplicationContext;

        import domain.Item;
        import persistence.ItemRepository;
        import persistence.RepositoryFactory;

        public class Main {
        	public static void main(String[] args) {
        		// ItemRepository itemRepository = new ItemRepository();

        		// 인스턴스 생성을 다른 팩토리 클래스를 이용해서 생성
        		// 다른 클래스의 메서드를 이용해서 인스턴스를 생성하는 것을
        		// Factory Method Pattern이라고 한다.

        		// 스프링이 인스턴스를 생성 - 어노테이션을 이용한 생성
        		/*
        		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RepositoryFactory.class);
        		*/

        		// RepositoryFactory 클래스의 create 메서드를 호출해서
        		// ItemRepository 클래스의 인스턴스를 생성
        		/*
        		ItemRepository itemRepository = context.getBean("create", ItemRepository.class);
        		Item item = itemRepository.get();
        		System.out.println(item);

        		ItemRepository itemRepository2 = context.getBean("create", ItemRepository.class);
        		// 스프링은 싱글톤 패턴으로 생성하므로 2개의 해시코드는 일
        		System.out.println(System.identityHashCode(itemRepository));
        		System.out.println(System.identityHashCode(itemRepository2));


        		context.close();
        		*/

        		// XML을 이용한 bean 생성
        		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml")) {
        			// bean 태그의 id와 클래스 이름을 기재
        			ItemRepository itemRepository = context.getBean("itemRepository", ItemRepository.class);
        			Item item = itemRepository.get();
        			System.out.println(item);
        		}catch(Exception e) {
        			System.out.println(e.getLocalizedMessage());
        		}
        	}
        }
        ```

   6. xml을 이용한 ApplicationContext 사용
      - beans 태그
        - 루트 엘리먼트
        - 스키마 관련 정보를 가짐
        - 하위 태그로 bean, description, alias, import가 있음
      - import 태그는 다른 설정 파일을 가져와서 사용할 때 필요
        - `<import resource = "다른 설정 파일 경로" />`
      - bean 태그
        - 클래스를 등록해서 인스턴스를 생성하기 위한 태그
        - bean 태그의 속성
          - class
            생성할 인스턴스의 클래스 경로로 필수
          - id
            구별하기 위한 이름, 다른 곳에서 참조할 때 필요
          - name
            id에 대한 별명으로 특수문자 가능
          - init-method
            인스턴스가 만들어질 때 호출되는 메서드
          - destroy-method
            인스턴스가 소멸될 때 호출되는 메서드
          - lazy-init
            지연 생성 옵션
          - scope
            수명주기로 기본은 singleton인데 prototype, request, session 등을 설정하는 것이 가능.
          - abstract
            추상, 상속시킬 목적으로 설정
          - parent
            상속받을 때 상위 클래스를 설정
          - factory-method
            생성자 아닌 메서드로 생성할 때 설정
          - depends-on
            다른 bean을 만들고 만들어져야 하는 경우 설정

10. **DI(Dependency Injection - 의존성 주입)**

    1. 개요
       - 의존성
         인스턴스 내부에서 다른 클래스의 인스턴스를 사용하는 것
       - 주입
         내부의 속성을 대입받는 것
       - 의존성 주입
         인스턴스 내부에서 사용하는 다른 클래스의 인스턴스를 직접 생성하지 않고 외부에서 생성한 것을 대입받아서 사용하는 것
    2. 목적
       - 인스턴스의 생성과 사용의 관심을 분리하는 것.
       - 가독성이 높아짐
       - 재사용성이 증가
    3. 전제
       - 사용되는 서비스 객체
       - 사용하는 서비스에 의존하는 클라이언트 객체
         Repository ↔ Service ↔ Controller
       - 클라이언트의 서비스 사용 방법을 정의하는 인터페이스
         - 템플릿 메서드 패턴을 적용
           Repository와 Service 에만 적용하는데 최근에는 Repository 에도 적용하지 않는데 이유는 Repository를 인스턴스로 만들면 나저미 프레임워크가 구현해주기 때문 - JPA를 사용하는 이유
         - 서비스를 생성하고 클라이언트로 주입하는 책임을 갖는 주입자 - 프레임워크
    4. 주입 방법

       - 생성자를 이용하는 방법
         ```java
         <bean id="아이디" class="클래스 경로">
         		<constructor-org-value="값" 또는 ref="다른 beain의 id" />
         <bean>
         ```
         - 타입을 설정하지 않으면 타입은 기본적으로 String
         - 생성자의 매개변수가 여러 개이면 여러 개를 만드는데 이 경우 순서는 의미가 없고 자료형이 일치하는 매개변수를 이용한다. index 속성을 추가해서 몇 번째 인지 설정하는 것이 가능
       - setter를 이용하는 방법

         ```java
         <bean id = “아이디” class="클래스 경로">
         	<property name="속성의 value = “값” 또는 ref” 다른 bean의 Id”/>
         >/bean>
         ```

         - 속성에 작성할 때는 문자열의 형태로 값을 설정하고 태그와 태그 사이에 설정할 때는 값만 설정하면 된다.

           ```java
           <property name="age" value="27" />

           <property name="age">
           	<value>27</value>
           </property>
           ```

       - Item 클래스의 bean을 Spring Bean Configuration 파일에 작성해서 main에서 사용

         - appplicationContext.xml 파일에 Item 클래스의 bean을 생성하는 코드를 작성
           ```java
           <bean id="item" class="domain.Item" >
           		<property name="itemId"><value>1</value></property>
           		<property name="itemName"><value>키위</value></property>
           		<property name="price" value="2000"></property>
           		<property name="description"><value>비타민 C가 풍부한 과일</value></property>
           		<property name="pirctureUrl" value="kiwi.png"></property>
           	</bean>
           ```
         - main 메소드 변경

           ```java
           import org.springframework.context.support.GenericXmlApplicationContext;

           import domain.Item;

           public class Main {
           	public static void main(String[] args) {
           		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml")) {
           			Item item = context.getBean("item", Item.class);
           			System.out.println(item);
           		}catch(Exception e) {
           			System.out.println(e.getLocalizedMessage());
           		}
           	}
           }
           ```

         - applicationContext.xml 파일의 코드를 수정해서 다른 bean을 참조하도록 설정한 후 실행

           ```java
           <!-- 생성자를 이용한 주입 String itemName = new String("배") -->
           	<bean id="itemName" class="java.lang.String">
           		<constructor-arg><value>배</value></constructor-arg>
           	</bean>

           	<bean id="item" class="domain.Item" >
           		<property name="itemId"><value>1</value></property>
           		<property name="itemName"><ref bean="itemName"/></property>
           		<property name="price" value="2000"></property>
           		<property name="description"><value>비타민 C가 풍부한 과일</value></property>
           		<property name="pictureUrl" value="kiwi.png"></property>
           	</bean>
           ```

    5. Collection Type의 주입
       - `<list>`
         java.util.List 타입이나 배열에서 사용
         ```java
         <list value-type="자료형">
         	<value>값</value>
         	<value>값</value>
         	...
         </list>
         ```
       - `<set>`
         java.util.Set 타입
         ```java
         <set value-type="자료형">
         	<value>값</value>
         	<value>값</value>
         	...
         </set>
         ```
       - `<map>`
         [java.util.Map](http://java.util.Map) 타입
         ```java
         <map>
         	<entry>
         		<key><value>키값</value></key>
         		<value><value>값</value></value>
         	<entry>
         	...
         </map>
         ```
       - `<properties>`
         Properties 타입
         ```java
         <props>
         	<prop key="값">값</prop>
         	...
         </props>
         ```

11. **어노테이션을 이용한 의존성 주입**

    1. **@Autowired**
       - 속성 위에 기재해서 동일한 자료형의 bean이 존재하면 자동으로 주입해주는 어노테이션
       - 동일한 자료형의 bean이 없거나 2개 이상이면 예외 발생
       - 필수 해제: `@Autowired(required=false)`로 설정
       - 동일한 자료형의 bean이 2개 이상인 경우
         ```java
         @Autowired
         @Qualifier("사용할 bean의 아이디")
         ```
    2. 동일한 기능을 해주는 어노테이션
       - `@Resource(name=”bean의 아이디”)`
       - `@Inject`와 `@Named: javax.inject` 라이브러리의 의존성을 설치해야 한다.
    3. 생성자에서 생성
       - 매개변수가 있는 생성자 위에 `@ConstructorProperties({”bean 아이디”})`
       - 주입받을 속성에 final을 적용하고 클래스 상단에 `**@RequiredArgsConstructor**`를 이용해도 동일한 기능 수행
    4. 어노테이션과 xml을 같이 사용하고자 하는 경우 필요한 설정(스프링 부터에서는 필요 없음)
       - context:annotaion-config가 설정되어야 한다.
    5. 관계형 데이터베이스를 사용하는 Web Application 구조를 생성

       - 관계형 데이터베이스 테이블과 연동되는 Entitiy 클래스 - di.entitiy.MemberEntitiy

         ```java
         package di.entity;

         import lombok.AllArgsConstructor;
         import lombok.Builder;
         import lombok.Data;
         import lombok.NoArgsConstructor;

         @Data
         @NoArgsConstructor
         @AllArgsConstructor
         @Builder // NoArgsConstructor 없으면 에러
         public class MemberEntity {
         	private String id;
         	private String name;
         	private String nickname;
         }
         ```

       - 데이터베이스 연동 관련 메서드를 선언할 인터페이스 - di.persistence.MemberRepository

         ```java
         package di.persistence;

         import di.entity.MemberEntity;

         public interface MemberRepository {
         	//기본 키를 가지고 하나의 데이터를찾아오는 메서
         	public MemberEntity findById(String id);
         }
         ```

       - 데이터베이스 연동 관련 메서드를 구현할 클래스 - di.persistence.MemberRepositoryImpl

         ```java
         package di.persistence;

         import di.entity.MemberEntity;

         public class MemberRepositoryImpl implements MemberRepository {

         	@Override
         	public MemberEntity findById(String id) {
         		MemberEntity memberEntity =
         				MemberEntity.builder()
         					.id("gyumin")
         					.password("1234")
         					.nickname("규민")
         					.build();
         		return memberEntity;
         	}
         }
         ```

       - Service Layer와 Controller Layer 그리고 View Layer 사이의 데이터 교환을 위한 DTO 클래스를 생성 - di.dto.MemberDTO

         ```java
         // 현재는 MemberEntity와 동일한 상태
         package di.dto;

         import lombok.AllArgsConstructor;
         import lombok.Builder;
         import lombok.Data;
         import lombok.NoArgsConstructor;

         @Data
         @NoArgsConstructor
         @AllArgsConstructor
         @Builder
         public class MemberDTO {
         	private String id;
         	private String password;
         	private String nickname;
         }
         ```

       - 사용자의 요청을 처리할 메서드의 원형을 가진 Service 인터페이스를 생성 - di.service.MemberService

         ```java
         package di.service;

         import di.dto.MemberDTO;

         public interface MemberService {
         	// 기본키 1개를 받아서 하나의 데이터를 리턴하는 메서드
         	// 매개변수나 리턴 타입에 Entity Type을 사용하면 안됨.(JPA를 사용하고 나서부터는 절대로 안됨.)
         	public MemberDTO findById(String id);
         }
         ```

       - 사용자의 요청을 처리할 메서드를 구현할 Service 클래스를 생성 - di.service.MemberServiceImpl

         ```java
         package di.service;

         import di.dto.MemberDTO;
         import di.entity.MemberEntity;
         import di.persistence.MemberRepository;

         public class MemberServiceImpl implements MemberService {
         	/// Service는 Repository를 주입받아서 사용. 따라서 아래 내용이 꼭 있어야 함.
         	private MemberRepository memberRepository;

         	@Override
         	public MemberDTO findById(String id) {
         		// Repository에 필요한 매개변수를 생성


         		// Repository 메서드를 호출함.
         		MemberEntity memberEntity = memberRepository.findById(id);

         		// Controller에게 전달할 데이터를 생성
         		MemberDTO memberDTO = MemberDTO.builder()
         									.id(memberEntity.getId())
         									.password(memberEntity.getPassword())
         									.nickname(memberEntity.getNickname())
         									.build();

         		return memberDTO;
         	}

         }
         ```

       - 사용자의 요청에 따라 필요한 Business Logic을 호출하고 그 결과를 뷰에게 전달해주는 Controller 클래스를 생성(인터페이스를 만들지 않는다.) - di.controller.MemberController

         ```java
         package di.controller;
         // Controller는 다른 클래스에 주입되지 않기 때문에
         // 템플릿 메서드 패턴을 사용하지 않음 -> 인터페이스 없이 구현

         import di.service.MemberService;

         public class MemberController {
         	// Service를 주입
         	private MemberService memberService;

         	// 뷰 이름을 리턴한는데 일단 이렇게 작성
         	public void findById(String id) {
         		System.out.println(memberService.findById(id));
         	}
         }
         ```

       - 원래는 이렇게 Context 파일을 이렇게 작성해줘야 하지만

         ```java
         <?xml version="1.0" encoding="UTF-8"?>
         <beans xmlns="http://www.springframework.org/schema/beans"
         	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         	xmlns:c="http://www.springframework.org/schema/c"
         	xmlns:context="http://www.springframework.org/schema/context"
         	xmlns:aop="http://www.springframework.org/schema/aop"
         	xmlns:mvc="http://www.springframework.org/schema/mvc"
         	xmlns:p="http://www.springframework.org/schema/p"
         	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.3.xsd
         		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
         		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
         		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">

         	<bean id="memberRepository" class="di.persistence.MemberRepositoryImpl"></bean>
         	<bean id="memberService" class="di.service.MemberServiceImpl">
         		<property name="memberRepository">
         			<ref-bean="memberRepository"></ref-bean>
         		</property>
         	</bean>
         	<bean id="memberController" class="di.controller.MemberController">
         		<property name="memberService">
         			<ref-bean="memberService"></ref-bean>
         		</property>
         	</bean>
         </beans>
         ```

       - 하지만 위에처럼이 아니라 이렇게 어노테이션을 이용한 설정을 사용할 수 있도록 해주는 것이다.

         ```java
         <?xml version="1.0" encoding="UTF-8"?>
         <beans xmlns="http://www.springframework.org/schema/beans"
         	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         	xmlns:c="http://www.springframework.org/schema/c"
         	xmlns:context="http://www.springframework.org/schema/context"
         	xmlns:aop="http://www.springframework.org/schema/aop"
         	xmlns:mvc="http://www.springframework.org/schema/mvc"
         	xmlns:p="http://www.springframework.org/schema/p"
         	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.3.xsd
         		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
         		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
         		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">

         	<bean id="memberRepository" class="di.persistence.MemberRepositoryImpl">
         	</bean>
         	<bean id="memberService" class="di.service.MemberServiceImpl">
         	</bean>
         	<bean id="memberController" class="di.controller.MemberController">
         	</bean>

         	<!-- 어노테이션을 이용한 설정을 사용할 수 있도록 해주는 것-->
         	<context:annotation-config />

         </beans>
         ```

       - MemberServiceImpl → @Autowired

         ```java
         package di.service;

         import org.springframework.beans.factory.annotation.Autowired;

         import di.dto.MemberDTO;
         import di.entity.MemberEntity;
         import di.persistence.MemberRepository;

         public class MemberServiceImpl implements MemberService {
         	// Service는 Repository를 주입받아서 사용. 따라서 아래 내용이 꼭 있어야 함.
         	// setter 메서드를 생성해주고
         	// 동일한 자료형의 bean이 있으면 자동으로 주입
         	@Autowired
         	private MemberRepository memberRepository;

         	@Override
         	public MemberDTO findById(String id) {
         		// Repository에 필요한 매개변수를 생성


         		// Repository 메서드를 호출함.
         		MemberEntity memberEntity = memberRepository.findById(id);

         		// Controller에게 전달할 데이터를 생성
         		MemberDTO memberDTO = MemberDTO.builder()
         									.id(memberEntity.getId())
         									.password(memberEntity.getPassword())
         									.nickname(memberEntity.getNickname())
         									.build();

         		return memberDTO;
         	}
         }
         ```

       - MemberController → @Autowired

         ```java
         package di.controller;
         // Controller는 다른 클래스에 주입되지 않기 때문에
         // 템플릿 메서드 패턴을 사용하지 않음 -> 인터페이스 없이 구현

         import org.springframework.beans.factory.annotation.Autowired;

         import di.service.MemberService;

         public class MemberController {
         	// Service를 주입
         	@Autowired
         	private MemberService memberService;

         	// 뷰 이름을 리턴한는데 일단 이렇게 작성
         	public void findById(String id) {
         		System.out.println(memberService.findById(id));
         	}
         }
         ```

       - main 클래스 작성 - DiMain

         ```java
         import org.springframework.context.support.GenericXmlApplicationContext;

         import di.controller.MemberController;

         public class DiMain {
         	public static void main(String[] args) {
         		try(GenericXmlApplicationContext context = new GenericXmlApplicationContext("applicationContext2.xml")) {

         			// Controller 가져오기
         			MemberController controller = context.getBean("memberController", MemberController.class);
         			controller.findById("1");
         		} catch(Exception e) {
         			System.out.println(e.getLocalizedMessage());
         		}
         	}
         }
         ```

         **구조와 Autowired 잊어버리지 말것!!!!!!!! 이제 직접 엮지 않아도 어노테이션으로 가능하다.**

    6. @Autowired 대신에 @RequiredArgsConstructor 사용 - 어노테이션을 클래스 위에 작성하고 주입받는 속성을 final로 설정해야 한다. 고쳐야할 것 → Service와 Controller

       - MemberServiceImpl 수정

         ```java
         package di.service;

         import di.dto.MemberDTO;
         import di.entity.MemberEntity;
         import di.persistence.MemberRepository;
         import lombok.RequiredArgsConstructor;

         // final 속성으로 만들어진 속성들에 동일한 자료형의 bean이 있으면
         // 생성자를 이용해서 자동 주입
         @RequiredArgsConstructor
         public class MemberServiceImpl implements MemberService {
         	// Service는 Repository를 주입받아서 사용. 따라서 아래 내용이 꼭 있어야 함.
         	// setter 메서드를 생성해주고
         	// 동일한 자료형의 bean이 있으면 자동으로 주입
         	// @Autowired
         	private final MemberRepository memberRepository;

         	@Override
         	public MemberDTO findById(String id) {
         		// Repository에 필요한 매개변수를 생성


         		// Repository 메서드를 호출함.
         		MemberEntity memberEntity = memberRepository.findById(id);

         		// Controller에게 전달할 데이터를 생성
         		MemberDTO memberDTO = MemberDTO.builder()
         									.id(memberEntity.getId())
         									.password(memberEntity.getPassword())
         									.nickname(memberEntity.getNickname())
         									.build();

         		return memberDTO;
         	}
         }
         ```

       - MemberController 클래스 수정

         ```java
         package di.controller;
         // Controller는 다른 클래스에 주입되지 않기 때문에
         // 템플릿 메서드 패턴을 사용하지 않음 -> 인터페이스 없이 구현

         import di.service.MemberService;
         import lombok.RequiredArgsConstructor;

         @RequiredArgsConstructor
         public class MemberController {
         	// Service를 주입
         	// @Autowired
         	private final MemberService memberService;

         	// 뷰 이름을 리턴한는데 일단 이렇게 작성
         	public void findById(String id) {
         		System.out.println(memberService.findById(id));
         	}
         }
         ```

    7. **bean 자동 생성**

       - 설정 파일에 <context:component-scan base-package=”패키지이름” />을 추가하고 패키지 안에 클래스에 특정 어노테이션을 추가하면 bean을 자동 생성해준다.
       - @Component(인스턴스 생성), @Controller(Servlet), @Service(인스턴스 생성), @Repository(인스턴스 생성), @RestController(뷰 대신에 데이터 전송에 사용) 등이다.
         spring boot는 기본 패키지 이름을 설정하면 자동으로 component-scan이 추가된다.
       - applicationContext에서 bean을 주석처리하고 RepositoryImpl, ServiceImpl, Controller 클래스에 어노테이션 추가

         ```java
         <!--
         	<bean id="memberRepository" class="di.persistence.MemberRepositoryImpl">
         	</bean>
         	<bean id="memberService" class="di.service.MemberServiceImpl">
         	</bean>
         	<bean id="memberController" class="di.controller.MemberController">
         	</bean>
         	-->
         ```

         ```java
         package di.persistence;

         import org.springframework.stereotype.Repository;

         import di.entity.MemberEntity;

         // bean을 자동 생성해주는 어노테이션
         @Repository
         public class MemberRepositoryImpl implements MemberRepository {

         	@Override
         	public MemberEntity findById(String id) {
         		MemberEntity memberEntity =
         				MemberEntity.builder()
         					.id("gyumin")
         					.password("1234")
         					.nickname("규민")
         					.build();
         		return memberEntity;
         	}
         }
         ```

         ```java
         package di.service;

         import org.springframework.stereotype.Service;

         import di.dto.MemberDTO;
         import di.entity.MemberEntity;
         import di.persistence.MemberRepository;
         import lombok.RequiredArgsConstructor;

         // final 속성으로 만들어진 속성들에 동일한 자료형의 bean이 있으면
         // 생성자를 이용해서 자동 주입
         @RequiredArgsConstructor
         // bean을 자동 생성해주는 코드
         @Service
         public class MemberServiceImpl implements MemberService {
         	// Service는 Repository를 주입받아서 사용. 따라서 아래 내용이 꼭 있어야 함.
         	// setter 메서드를 생성해주고
         	// 동일한 자료형의 bean이 있으면 자동으로 주입
         	// @Autowired
         	private final MemberRepository memberRepository;

         	@Override
         	public MemberDTO findById(String id) {
         		// Repository에 필요한 매개변수를 생성


         		// Repository 메서드를 호출함.
         		MemberEntity memberEntity = memberRepository.findById(id);

         		// Controller에게 전달할 데이터를 생성
         		MemberDTO memberDTO = MemberDTO.builder()
         									.id(memberEntity.getId())
         									.password(memberEntity.getPassword())
         									.nickname(memberEntity.getNickname())
         									.build();

         		return memberDTO;
         	}
         }
         ```

         ```java
         package di.controller;
         // Controller는 다른 클래스에 주입되지 않기 때문에
         // 템플릿 메서드 패턴을 사용하지 않음 -> 인터페이스 없이 구현

         import org.springframework.stereotype.Controller;

         import di.service.MemberService;
         import lombok.RequiredArgsConstructor;

         @RequiredArgsConstructor
         // bean을 자동 생성해줌과 동시에 Servlet 클래스의 인스턴스로 생성
         @Controller
         public class MemberController {
         	// Service를 주입
         	// @Autowired
         	private final MemberService memberService;

         	// 뷰 이름을 리턴한는데 일단 이렇게 작성
         	public void findById(String id) {
         		System.out.println(memberService.findById(id));
         	}
         }
         ```

       - apllicationContext.xml 파일에 설정을 추가

         ```java
         <?xml version="1.0" encoding="UTF-8"?>
         <beans xmlns="http://www.springframework.org/schema/beans"
         	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         	xmlns:c="http://www.springframework.org/schema/c"
         	xmlns:context="http://www.springframework.org/schema/context"
         	xmlns:aop="http://www.springframework.org/schema/aop"
         	xmlns:mvc="http://www.springframework.org/schema/mvc"
         	xmlns:p="http://www.springframework.org/schema/p"
         	xsi:schemaLocation="http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.3.xsd
         		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
         		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.3.xsd
         		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-4.3.xsd">

         	<!--
         	<bean id="memberRepository" class="di.persistence.MemberRepositoryImpl">
         	</bean>
         	<bean id="memberService" class="di.service.MemberServiceImpl">
         	</bean>
         	<bean id="memberController" class="di.controller.MemberController">
         	</bean>
         	-->

         	<!-- 어노테이션을 이용한 설정을 사용할 수 있도록 해주는 것-->
         	<context:annotation-config />

         	<!-- di 패키지에 어노테이션이 추가된 클래스의 bean을 자동 생성해주는 설정으로
         	spring boot에서는 프로젝트 만들 떄 설정한 패키지 이름이 자동으로 설정된다. -->
         	<context:component-scan base-package="di" />
         </beans>
         ```

       - main 메서드 실행(잘됨!!)

12. **수명 주기(Life Cycle)**
    1. 어노테이션을 이용한 설정
       - 메서드 위에 `@PostConstructor`를 추가하면 생성자 다음에 호출
       - 메서드 위에 `@PreDestroy`를 추가하면 인스턴스가 소멸되기 직전에 호출
    2. InitializingBean과 DisposableBean 인터페이스를 구현하면 유사한 역할을 수행하는 메서드를 사용하는 것이 가능
    3. 메서드를 생성하고 bean 태그의 ini-method 속성과 destroy-method 속성에 메서드 이름을 등록해도 유사한 효과
13. **Test**
    - 테스트 클래스 위에 `@RunWith(SpringJUnit4ClassRunner.class)`를 추가하고 @ContextConfiguration(설정 파일 경로)를 이용하면 설정 파일의 bean을 이용해서 테스트가 가능.
