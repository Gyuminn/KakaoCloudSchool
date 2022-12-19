# [Java] Inheritance & Polymorphism

1. **Inheritance - is a**

   1. 상속
      - 하위 클래스가 상위 클래스의 모든 멤버를 물려받는 것.
      - 장점
        여러 클래스들에 중복되는 코드를 1번만 작성할 수 있음.
        재사용성이 증가
        자신이 만든 클래스를 상속하는 경우는 중복되는 코드를 제거하기 위해서이고 라이브러리나 프레임워크가 제공하는 클래스를 상속하는 이유는 기능 확장 때문
      - 용어
        - 단일 상속
          하나의 클래스로부터 상속받는 경우
        - 다중 상속
          여러 개의 클래스로부터 상속받는 경우 - 자바는 지원하지 않음.
          상속하는 클래스: super(상위), base(기반)
          상속받는 클래스: sub(하위), derivation(파생)
   2. java에서 상속하는 방법

      ```java
      class 클래스이름 extends 상위 클래스 이름
      ```

      자바는 상위 클래스를 1개밖에 갖지 못함.

      extends를 생략하면 java.lang.Object 클래스로부터 상속받는 것으로 간주

      java.lang.Object 클래스는 java의 최상위 클래스

      `상위 클래스의 모든 멤버는 상속되지만 private 멤버는 하위 클래스에서 접근할 수 없다.`

      초기화 블럭과 생성자는 상속되지 않음.

      static 변수는 상속이 된다.

   3. 상속 실습

      상위 클래스로 사용할 클래스를 생성 - Super

      Super 클래스를 상속받을 클래스를 생성 - Sub

      실행 클래스를 생성 - InheritanceMain

   4. super

      하위 클래스의 instance 메서드에 숨겨진 매개변수

      상위 클래스의 멤버를 호출하고자 할 때 사용

      instance 메서드에서 변수 이름을 사용하면 현재 메서드 내부 그리고 거기에 없으면 자신이 속한 클래스의 속성을 확인하고 자신의 클래스에 없으면 상위 클래스에서 확인하고 없으면 에러가 난다.

      `this.` 을 추가하면 자신의 메서드 안에서는 찾지 않고 클래스에 확인한 뒤 없으면 상위 클래스에서 확인

      `super.` 을 추가하면 상위 클래스에서만 확인

      하위 클래스와 상위 클래스에 동일한 이름의 속성이나 메서드가 존재할 때 상위 클래스의 것을 호출하고자 하면 super.을 이용해야 한다.

      속성의 이름이 중복되는 경우는 거의 없고 대부분 메서드의 이름이 중복되는 경우가 많기 때문에 `super.메서드이름()` 의 형태로 사용하는 경우가 많다.

   5. this(), super()
      - `this()`: 생성자 안에서 자신의 생성자를 호출할 때 사용
      - `super()`: 생성자 안에서 상위 클래스의 생성자를 호출할 때 사용
      - java의 모든 클래스의 생성자에서 상위 클래스의 생성자를 호출하지 않으면 super()가 가장 먼저 있는 것으로 간주한다.
        상위 클래스의 default constructor를 호출하는 문장이 있는 것으로 간주한다. 이것 때문에 상위 클래스에 default constructor가 없는 경우 상속받으면 에러가 발생한다. 이런 경우는 `상위 클래스에 default constructor를 만들어 주던가(상위 클래스를 직접 생성했을 때)` 아니면 `하위 클래스에서 생성자를 만든 후 상위 클래스의 생성자를 호출(상위 클래스를 직접 생성하지 않은 경우, 즉 배포를 하면 소스 코드가 아니고 class 코드가 오기 때문에 클래스를 수정할 수 없다)`해주어야 한다.
      - 상위 클래스의 생성자를 호출하는 super()는 생성자 안에서 가장 먼저 나와야 한다. 상위 클래스의 인스턴스를 만들고 하위 클래스의 인스턴스를 추가하기 때문이다.
      - this()는 자신의 클래스에 속한 생성자를 호출하는 구문이다.
        여러 개의 생성자를 가진 경우 중복되는 내용이 있는 경우 사용
        생성자 안에서 super() 다음에 나와야 한다.
   6. Method Overriding
      - 하위 클래스에 상위 클래스의 메서드 원형과 동일한 메서드를 만드는 것.
      - java에서는 Overriding을 할 때 `하위 클래스에 만드는 메서드의 접근 지정자가 더 크거나 같아야 한다.` 접근 지정자가 작은 쪽으로는 Overriding을 할 수가 없다.
      - public(클래스 내부에서 사용 가능하고 외부에서 인스턴스나 클래스를 통해 사용 가능) > protected(자신의 클래스와 하위 클래스 내부 그리고 동일한 패키지의 경우는 외부에서도 사용 가능) > package(자신의 패키지에서는 public 다른 패키지에서는 private, 접근 지정자를 생략하는 것) > private(자신의 클래스 내부에서는 사용 가능하지만 다른 곳에서는 접근이 안됨.)
      - 목적
        자신의 클래스에서 오버라이딩하는 경우는 대부분 다형성 구현이 목적이거나 기능 확장.
        직접 생성하지 않은 클래스의 메서드를 오버라이딩하는 경우는 대부분 기능 확장 - 특별한 경우(상위 클래스의 메서드에 내용이 없는 경우)를 제외하고는 상위 클래스의 메서드를 호출해야 한다.(안드로이드나 iOS에서는 에러)
        호출할 때 파괴를 하는 메서드가 아니라면 상위 클래스의 메서드를 먼저 호출해야 한다.
      - `@Override`
        어노테이션은 이 메서드가 오버라이딩된 메서드라는 것을 알려주는 어노테이션인데 만약 메서드가 상위 클래스에 존재하지 않는다면 에러를 표시해준다.
      - `annotation`
        반복되는 기능이나 자주 사용되는 복잡한 기능을 직접 구현하지 않고 빌드 단계에서 삽입하도록 해주는 기능으로 자바에서는 클래스로 취급.
   7. 참조형 변수의 대입
      - 참조형 변수에는 자신의 참조형 데이터의 참조만 대입 가능.
      - 상속 관계인 경우는 참조형 변수에 하위 클래스의 인스턴스 참조를 대입할 수 있다. 상위 클래스의 참조형 변수에는 하위 클래스 타입의 인스턴스 참조를 대입할 수 없지만 강제 형 변환을 하면 대입이 가능하다. 책임은 개발자가 져야 하는데 인스턴스의 원래 자료형이 하위 클래스 타입이면 가능하지만 그렇지 않으면 예외가 발생한다. 이것을 가지고 다형성을 구현한다.
   8. 참조형 변수의 멤버 호출
      - 컴파일 단계에서는 변수를 선언할 때 사용한 자료형이 있는지 그리고 변수가 자신의 자료형이 호출할 수 있는 멤버를 호출하는지 확인해서 에러 여부를 결정
      - 실행을 할 때는 실제 대입된 인스턴스를 가지고 자신이 호출할 수 있는 것을 결정한다.
   9. Polymorphism(다형성)

      동일한 메시지에 대하여 다르게 반응하는 성질

      이 원리 때문에 GUI 운영체제가 만들어졌고 스마트폰의 지역화가 가능

      - CLI: 하나의 명령어가 한 가지 기능을 수행
      - GUI: 하나의 명령어가 어떤 기능을 할 지 예측하기가 어렵다.

   10. 스타크래프트 게임 만들기

       종족이 3개이고 유저는 종족 중 하나를 선택해서 게임을 수행

       기능은 공격이라는 기능만 생성 - 프로토스의 공격, 저그의 공격, 테란의 공격

       - 클래스 구현

         ```java
         // StarcraftMain.java
         public class StarcraftMain {
             public static void main(String[] args) {
                 // Protoss의 공격 호출
                 Protoss protoss = new Protoss();
                 protoss.attack();

                 // Zerg 공격 호출
                 Zerg zerg = new Zerg();
                 zerg.attack();

                 Terran terran = new Terran();
                 terran.attack();
             }
         }
         ```

         ```java
         // Zerg.java
         public class Zerg extends Starcraft{

             public void attack() {
                 System.out.println("저그의 공격");
             }
         }

         // Protoss.java
         public class Protoss extends Starcraft {
             public void attack() {
                 System.out.println("프로토스의 공격");
             }
         }

         // Terran.java
         public class Terran extends Starcraft{

             public void attack() {
                 System.out.println("테란의 공격");
             }
         }
         ```

       - 다형성 적용 → 상위 클래스 생성

         ```java
         // Starcraft.java
         public class Starcraft {
             // 오버라이딩을 위해서 생성한 메서드
             public void attack();
         }
         ```

         ```java
         // Zerg.java
         public class Zerg extends Starcraft{

             public void attack() {
                 System.out.println("저그의 공격");
             }
         }

         // Protoss.java
         public class Protoss extends Starcraft {
             public void attack() {
                 System.out.println("프로토스의 공격");
             }
         }

         // Terran.java
         public class Terran extends Starcraft{

             public void attack() {
                 System.out.println("테란의 공격");
             }
         }
         ```

         ```java
         // StarcraftMain.java
         public class StarcraftMain {
             public static void main(String[] args) {
                 // Protoss의 공격 호출
                 Starcraft star = new Protoss();
                 star.attack();

                 // Zerg 공격 호출
                 star= new Zerg();
                 star.attack();

                 star = new Terran();
                 star.attack();
             }
         }
         ```

   11. Abastract(추상)

       abstract method(추상 메서드)

       - 내용이 없는 메서드로 오버라이딩을 위해서 생성
       - 결과형 앞에 abstract 라는 키워드를 추가하고 { }를 제거해서 생성
       - 자바에서는 abstract 메서드가 abstract class나 interface에 존재해야 한다.
       - abstract 메서드를 소유한 class를 extends 하거나 interface를 implements하게 되면 반드시 overriding을 해서 내용을 만들어야 한다.

       abstract class(추상 클래스)

       - 인스턴스를 생성할 수 없는 클래스
       - 다른 클래스에 상속을 해서 사용
       - 참조형 변수의 자료형으로는 사용할 수 있다.

       ```java
       // Protoss, Zerg, Terran 클래스의 인스턴스를
       // 하나의 변수에 대입할 수 있도록 하기 위한 상위 클래스
       public abstract class Starcraft {
           // 오버라이딩을 위해서 생성한 메서드
           // 추상 메서드는 추상 클래스나 인터페이스만 존재할 수 있음.
           public abstract void attack();
       }
       ```

2. **Interface - Protocol**

   1. 개요

      상수(static final)와 abstract method와 default method를 소유한 객체

      인터페이스에 변수를 선언하면 상수가 되고 메서드를 만들면 기본적으로 추상 메서드가 된다.

      인스턴스를 생성할 수 없지만 변수의 자료형으로는 사용 가능

   2. 생성

      ```java
      interface 이름 {
      	상수 선언
      	추상 메서드 선언
      	default method 생성
      }
      ```

   3. 클래스에 구현

      ```java
      class 클래스이름 extends 상위클래스이름 implements 인터페이스 나열{
      	클래스 내용
      }
      ```

      인터페이스에 추상 메서드가 있다면 클래스에서 반드시 구현을 해야 한다.

      인터페이스를 implements 하게 되면 인터페이스에 있는 메서드가 클래스에 반드시 구현되어있다고 보장할 수 있다.

   4. 사용하는 이유
      - 다형성 구현을 위해서 사용(다중 상속은 절대 틀린 말임. java는 단일상속).
      - **특정한 메서드가 구현되어있다라는 보장을 하기 위해서**
   5. 인터페이스끼리 구현 가능

      ```java
      interface 이름 extends 인터페이스이름 나열{}
      ```

   6. default method

      인터페이스에 내용이 있는 메서드를 만들고자 할 때 결과형 앞에 `default` 라는 키워드를 추가하고 작성하면 된다.

   7. Template Method Pattern

      - 내용을 구현하기 전에 모양을 가진 인터페이스를 만들고 그 인터페이스를 implements해서 구현하는 디자인 패턴
      - 인터페이스는 다른 사람과의 대화의 수단이 된다.
      - **일반적인 데이터베이스 연동 프로젝트를 만들면 5가지 종류의 내용을 만들어 낸다.**
        - View(별도의 앱 or Controller가 만들어서) ←→ Controller ←→ Service ←→ Repository
        - DTO, VO, Entitiy 와 같은 데이터를 표현하기 위한 클래스
        - **Service 클래스는 반드시 Template Method Pattern을 적용해야 한다.**
        - **Repository**도 예전에는 Template Metohd Pattern을 적용했는데 JPA와 같은 프레임워크를 사용하면 인터페이스만 만들면 내부 구현을 JPA가 다 해주기 때문에 Repository 부분은 최근에는 Template Method Pattern을 적용하지 않는다.
      - 클래스를 상속받는 클래스가 1개밖에 없는 경우 하위 클래스 이름은 상위클래스이름Ex로 만드는 것이 일반적이다.
      - 인터페이스를 구현하는 클래스가 1개 밖에 없는 경우 클래스 이름은 인터페이스이름Impl로 만드는 것이 일반적이다.

        ```java
        interface Service{}

        class ServiceImpl implements Service{}
        ```

      - 하나의 인터페이스에 메서드가 여러 개가 될 것 같으면 나누어서 선언해야 한다.
