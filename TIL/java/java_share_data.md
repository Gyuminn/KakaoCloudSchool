# [Java] 데이터 공유

1. **데이터 공유**

   1. **전역 변수(Global Variable) - 모든 곳에서 사용가능한 데이터**

      - **public class 안에 static 속성을 만들어서 사용 - 권장하지 않음.**
      - **singleton 패턴**으로 클래스를 디자인해서 속성이나 메서드를 만들어서 사용 - 권장, 안드로이드나 iOS 또는 Web Application 들이 이 방법을 이용해서 entry point에 접근 가능하도록 한다.
      - **Design Pattern**: 클래스를 용도에 맞게 설계하는 기법, 가장 많이 사용되고 알려진 패턴으로는 GoF의 디자인 패턴이 있다.

        - **SingleTon**
          클래스의 인스턴스를 1개만 만들 수 있도록 하는 패턴으로 Server Application에서 주로 이용
          생성자를 private으로 생성
          자신의 type으로 만들어진 static 속성을 정의하고 static 속성에 데이터를 만들어서 주입하고 리턴하는 static 메서드를 생성

          ```java
          public class Singleton {
              // 외부에서 직접 인스턴스 생성을 못하도록 생성자의 접근 지정자 변경
              private Singleton() {

              }

              // 하나의 인스턴스 참조를 저장하기 위한 속성(변수)을 생성
              private static Singleton singleton;

              // 인스턴스의 참조를 리턴하는 메서드
              public static Singleton sharedInstance() {
                  if(singleton == null) {
                      singleton = new Singleton();
                  }
                  return singleton;
              }
          }
          ```

          대부분의 경우 서버는 **하나의 인스턴스를 이용해서 멀티 스레드로 클라이언트의 요청을 처리하기 때문에 서버에서는 하나의 클래스에 대한 인스턴스를 1개만 만드는 것이 일반**적이다.

   2. **동일한 클래스로부터 만들어진 인스턴스 사이의 데이터 공유**

      static 속성을 사용

      일련번호 생성을 위한 클래스 디자인

      ```java
      package kakao.itstudy.nestedclass;

      public class Table {
          private static int sequence; // 일련번호
          private static int step = 1;

          public static int getSequence() {
              return sequence;
          }

          public static void setSequence(int sequence) {
              Table.sequence = sequence;
          }

          public static int getStep() {
              return step;
          }

          public static void setStep(int step) {
              Table.step = step;
          }

          // 인스턴스가 별도로 소유
          private int num;

          public Table() {
              sequence += step;
              num = sequence;
          }

          public int getNum() {
              return num;
          }
      }
      ```

   3. **클래스 안에서 다른 클래스의 인스턴스를 만드는 경우**
      - has a 관계
      - 포함하고 있는 클래스의 인스턴스는 포함되는 클래스의 인스턴스 참조를 알기 때문에 참조를 이용해서 바로 접근이 가능하지만 포함된 클래스의 인스턴스에서는 외부 클래스의 속성에 바로 접근이 안됨.
      - 생성자나 setter 메서드를 이용해서 포함하는 클래스의 인스턴스 참조를 넘겨주어야 한다. 이러한 방식을 생성자를 이용한 주입 또는 setter 메서드를 이용한 주입이라고 한다.
   4. **데이터 공유**
      - 데이터를 공유하기 위해서는 매개변수로 계속 넘겨주는 방법 가능 - react에서 props를 사용하는 방식
      - 공유 데이터를 모든 곳에서 접근 가능하도록 만들어서 사용할 수 있도록 하는 방법 가능 - 모바일 앱은 이 방식으로 기본적으로 제공하고 탭 형태의 애플리케이션에서 많이 사용.
      - 포함되는 형태의 데이터 공유 - 네비게이션 구조에서 사용
