# [Java] API

JDK 1.8 Document: https://docs.oracle.com/javase/8/docs/api/

샘플 코드

- http://www.java2s.com/Code/JavaAPI/CatalogJavaAPI.htm
- http://www.java2s.com/Tutorial/Java/CatalogJava.html

**API 문서를 볼 때는 상속 계층과 구현된 인터페이스를 확인**

- **Field Summary** - static final(클래스 이름으로 호출)로 만들어져 있는데 이 클래스 안에서 사용되는 옵션 값이다.
- **Constructor** - 생성자
- **Method** - 하나씩 설명된 메서드는 자신의 클래스에만 있는 메서드이거나 상속된 메서드 중에서 재정의된 메서드이고 박스에 한꺼번에 나열된 메서드는 상속된 메서드를 그대로 사용

1. **java.lang.pakage**

   기본 패키지

   이 패키지의 클래스들은 import하지 않고 사용 가능

   String → java.lang.String

   Wrapper 클래스와 String 그리고 시스템 관련 클래스들이 존재

   1. java.lang.Object 클래스

      자바의 최상위 클래스

      자바의 모든 클래스는 이 클래스로부터 상속

      - JDK의 API 클래스에서 Obeject 사용되는 경우에 대한 이해
        - Object 클래스가 메서드의 매개변수로 사용되는 경우
          메서드의 매개변수로 어떤 종류의 데이터든지 가능
        - 메서드의 리턴 타입이 Object인 경우
          반드시 원래의 자료형으로 형 변환해서 사용
      - Object 클래스의 메서드 - 모든 인스턴스가 사용 가능
        - Object.clone()
          복제를 위한 메서드로 재정의해서 사용
          clone을 재정의할 때 Cloneable 인터페이스(약속, 규칙, 규약 - Protocol)를 implements하는 것을 권장
          API 클래스 중에 Cloneable이라는 인터페이스를 implements한 클래스는 clone이라는 메서드를 호출하면 깊은 복사를 수행한 결과를 리턴.
          자료구조와 관련된 클래스들은 대부분 구현되어 있고 Wrapper나 String 클래스는 복사 생성자(자신과 동일한 데이터 타입을 매개변수로 받는 생성자)를 이용해서 복제를 한다.
        - boolean equals(Object object)
          참조가 아니라 내용을 비교하고자 할 때 사용하는 메서드
          재정의해서 사용해야 한다.
        - int hashCode()
          해시코드를 리턴하는 메서드인데 재정의 가능하다.
          이 메서드의 리턴 값은 실제 해시코드가 아니다. 개발자가 재정의해서 이 값이 같으면 equals의 리턴이 true가 되도록 하는 것을 권장한다.
        - void finalize()
          인스턴스가 메모리에서 정리될 때 호출되는 메서드
          재정의해서 사용, 다른 곳에서는 이러한 메서드를 소멸자라고도 한다.
        - String toString()
          인스턴스를 문자열로 표현하기 위해서 제공되는 메서드
          재정의해서 사용한다.
          출력하는 메서드에 인스턴스 참조를 대입하면 이 메서드의 리턴 값이 출력된다.
        - notify(), notifyAll(), wait()
          쉬고 있는 스레드를 꺠우는 메서드와 동작 중인 스레드를 쉬도록 해주는 메서드
      - 인스턴스의 등가성 판단
        - ==: 참조가 같은지 여부를 리턴
        - equals(): 내용이 같은지 여부를 리턴 - 재정의해서 사용
      - weak copy와 deep copy
        weak copy는 참조를 대입하지 않고 내부 데이터를 복사를 해서 복사본을 만드는 것인데 재귀적으로 복사를 하지는 않는 것 - 참조하는 형태의 데이터 안에 다시 참조하는 데이터가 있는 경우 복사본이 원본에 영향을 줄 수 있음.
        deep copy는 재귀적으로 복사본을 만들어서 주는 것으로 복사본이 원본에 영향을 줄 수 없음.
      - equals와 clone 재정의

        ```java
        // 복제를 위한 메서드
            public Data clone() {
                Data data = new Data();
                // 이 방식은 얕은 복사(weak copy)
                // num은 숫자 데이터이므로 바로 복제가 가능
                // name와 nicknames는 숫자 데이터가 아니다.
                // 바로 대입하면 참조가 대입된다.
                data.setNum(this.num);
                data.setName(this.name);

                // 배열을 복제
                // 깊은 복사를 진행.
                String[] nicknames = Arrays.copyOf(this.nicknames, this.nicknames.length);
                data.setNicknames(nicknames);

                return data;
            }

            // 데이터의 내용이 같은지 확인하는 메서드
            @Override
            public boolean equals(Object other) {
                // boolean result = false;
                // 원래 자료형으로 변환
                Data other1 = (Data) other;

                // 숫자나 boolean은 ==로 일치 여부를 판단하지만
                // 그 이외의 자료형은 equals로 판단
                // if(this.num == other1.getNum() && this.name.equals(other1.getName())) {
                //     return true;
                // }

                // Objects.hash(데이터 나열)을 이용하면
                // 데이터를 가지고 정수로 만든 해시코드를 생성
                // 이렇게 만든 해시 코드 값을 리턴하는 것이 속도가 더 빠르다.
                return (Objects.hash(num, name))== Objects.hash(other1.getNum(), other1.getName());
            }
        ```

   2. Wrapper Class

      자바의 기본형 데이터를 인스턴스로 생성할 수 있도록 해주는 클래스

      자바의 기본형은 참조를 저장하기는 하지만 null을 대입할 수 없는 구조로 만들어져 있다. `int a = null` // 에러

      기본형과 다른 자료형은 데이터의 의미가 다르기 때문에 형 변환도 불가능하다.

      데이터베이스에 정수 데이터를 저장하도록 설계한 후 이 데이터를 자바의 데이터와 매핑을 시키고자 하는 경우 int나 long을 사용하면 에러 발생 가능성이 있음.

      - 기본 자료형의 데이터를 인스턴스화 해서 저장할 수 있는 클래스
        - boolean → Boolean
        - byte → Byte
        - short → Short
        - char → Character
        - int =→ Integer
        - long → Long
        - float → Float
        - double → Double
          8개의 클래스는 Obejct 클래스로부터 상속을 받았기 때문에 인스턴스를 생성해서 Obejct 클래스 타입의 변수에 대입을 할 수 있고 기본형이 아니기 때문에 null도 저장할 수 있고 클래스이므로 자신과 관련된 메서드나 속성도 소유할 수 있다.
          데이터베이스에서 null 저장 가능한 컬럼의 경우는 기본 자료형 보다는 Wrapper Class를 사용하는 것이 좋다
      - 인스턴스 생성
        Default Constructor가 존재하지 않는다.
        `new Ineger()` // 에러

        - 매개 변수가 1개인 생성자를 이용
          `new Integer(10);`
        - 다른 종류의 데이터(문자열)를 이용해서 생성 - static 메서드인 parse자료형(데이터)를 이용
          `Integer.parseInt("10");`
        - Wrapper Class의 데이터를 문자열로 변환
          toString()을 호출하면 된다.
        - Wrapper 클래스와 String 클래스는 내부 데이터를 수정할 수 없다.
          데이터를 변경하면 새로운 공간을 할당받아서 데이터를 저장한 후 그 참조를 기억한다.
        - Auto Boxing과 Auto UnBoxing
          AutoBoxing은 기본형 데이터를 자동으로 Wrapper Class 타입으로 변환하는 것.

          ```java
          // 원래 방식
          int a = 10;
          Integer i = new Integer(a);

          // Auto Boxing
          // a 부분을 JDK가 new Integer(a)로 변환해서 수행
          Integer k = a;
          ```

          Auto Unboxing은 Wrapper Class 타입의 데이터를 기본형으로 변경하는 것.

          ```java
          // 원래 방식
          Integer x = new Integer(10);
          int y = x.intValue();

          // Auto Unboxing
          // 메서드를 호출하지 않고 직접 대입이 가능
          int z = x;
          ```

        - 메서드 재정의
          equals와 hashCode 메서드가 재정의 되어 있음.
          크기 비교하는 compare 메서드도 재정의 되어 있음.
          toString도 재정의 되어 있어서 toString을 호출하면 저장하고 있는 데이터가 문자열로 리턴된다.
        - BigInteger와 BigDecimal
          - BigInteger
            아주 큰 정수를 저장하기 위한 자료형
            내부는 int 배열로 구성
            문자열로 된 숫자를 생성자의 매개변수로 받아서 사용
            Long이 64비트로 사용빈도는 낮음
          - BigDecimal
            정밀한 숫자를 저장하기 위한 자료형
            소수의 연산 때문에 만들어진 클래스 - float이나 double로는 정밀한 소수 계산을 할 수 없음.
            숫자로 된 문자열을 생성자의 매개변수로 받아서 생성
            일반 정수나 실수로 변환할 때는 intValue나 floatValue, doubleValue 같은 메서드를 이용
            Oracle에서 number로 만든 숫자 데이터를 자료형을 설정하지 않고 가져오면 BigDecimal로 가져온다. - MyBatis Framework를 사용해서 Map으로 테이블을 매핑하면 발생
            ```java
            // 실수 연산 - BigDecimal을 사용해야 하는 경우
            // 정확한 산술 연산
            // BigDecial로 데이터를 만들고 연산을 수행하는 메서드를
            // 호출하면 정확한 결과를 만들 수 있음.
            BigDecimal b1 = new BigDecimal("1.6000000000000000");
            BigDecimal b2 = new BigDecimal("0.1000000000000000");
            System.out.println(b1.add(b2));
            ```

   3. String 클래스
      - 문자열 클래스
        - String
          고정된 문자열 저장, 문자열을 수정할 수 없음.
        - StringBuffer
          변경 가능한 문자열 저장, Multi Thread에 Safe - 사용을 할 때 다른 Thread가 사용 중인지 확인, Legacy Class
        - StringBuilder
          변경 가능한 문자열 저장, Multi Thread에 Unsafe - 다른 Thread가 사용 중인지 확인하지 않음.
        - 문자열 연산을 많이 해야 하는 경우에는 StringBuilder를 이용해서 연산을 하고 String으로 변환해서 사용하는 것을 권장.
      - 인스턴스 생성
        문자열 리터를을 이용해서 생성이 가능하고 생성자를 이용해서도 생성 가능
      - 메서드
        - int length()
          문자열의 길이를 리턴
        - char charAt(int idx)
          idx번째 문자 리턴
        - 문자열이 변경되는 메서드는 전부 변경을 한 후 리턴을 한다.
      - 문자열 처리 시 고려사항
        - 영문의 경우는 대소문자 구분
        - 좌우 공백 제거
        - 문자열 분할 - 위치를 가지고 분할하기도 하고 특정 패턴을 찾아서 분할하기도 한다.
        - 특정 문자열 패턴의 존재 여부 확인 - 정규식 이용
        - 문자열의 동일성 여부 - equals
        - 문자열의 크기 비교 - compare
        - 여러 데이터를 조합해서 하나의 문자열로 만들기
          - String.format 메서드를 이용
          - printf 메서드와 사용법이 동일하다.
        - 한글의 경우 인코딩
          getBytes 메서드를 이용하면 인코딩 방식을 설정해서 byte 배열로 리턴받을 수 있고 new String(바이트 배열, “인코딩 방식”)을 이용하면 바이트 배열을 인코딩 방식으로 변환 후 문자열로 리턴
          이 기종간의 통신에서 사용하는 경우가 많다.
          최근에는 거의 utf-8을 사용하기 때문에 이 문제가 흔하지는 않다.
   4. System 클래스
      - 실행 환경과 관련된 속성과 메서드를 제공하는 클래스
      - 공개된 생성자가 없음 - 외부에서 인스턴스 생성을 할 수 없음.
      - **Document에 생성자가 없는 경우 확인할 사항**
        - **interface나 abstract class인지 확인**
          인스턴스를 만들 수 없기 때문에 보이지 않음.
        - **모든 멤버가 static인지 확인**
          인스턴스를 만들 필요가 없기 때문
        - **자기 자신을 리턴하는 static 메서드가 있는지 확인**
          디자인 패턴이 적용된 클래스
        - **자신의 이름 뒤에 Builder나 Factory가 붙는 클래스가 있는지 확인**
          직접 생성하는 것이 번거로워서 대신 생성해주는 클래스를 이용
      - **System 클래스는 모든 멤버가 static**이라서 인스턴스 생성이 필요 없기 때문에 생성자를 숨김
      - 속성
        - in
          표준 입력 객체 - 일반적으로 키보드
        - out
          표준 출력 객체 - 일반적으로 모니터
        - err
          표준 에러 객체 - out처럼 출력으로 사용할 수 있는데 err을 이용해서 출력하면 빨간색으로 표시되고 out과는 별개로 동작하기 때문에 out과 같이 사용하면 출력 순서를 알 수 없다.
      - 메서드
        - currentTimeMillis, nanoTime
          현재 시간을 밀리초나 나노초 단위로 리턴
        - getProperty, getenv
          환경 설정 값을 가져오는 메서드
        - identityHashCode
          해시 코드를 리턴해주는 메서드
   5. Math
      - 수학 관련 메서드를 소유한 클래스
      - 모든 멤버가 static이라서 인스턴스를 생성할 필요가 없는 클래스 - 공개된 생성자가 없음.
      - 클래스의 메서드가 리턴하는 결과는 운영체제 별로 다르게 나오기도 한다.
      - 운영체제의 특정 기능을 사용하지 않고 공통 기능만을 사용해서 연산하는 StrictMath 클래스도 제공
   6. **Runtime**
      - 프로세스 실행과 관련된 클래스
      - **Singleton 패턴으로 디자인된 클래스라서 getRuntime 메서드로 인스턴스를 생성**
      - exec 메서드를 이용하면 특정 프로세스를 실행할 수 있다.
   7. Fast Enumeration(빠른 열거)

      - Collection의 데이터를 순서대로 빠르게 접근하는 것.
      - 배열과 Collection 데이터들에서 가능
      - jdk 1.8에서는 이 작업도 내부적으로 구현해서 더 빠르게 접근하는 **Stream API**가 추가됨.

      ```java
      for(임시변수: 배열 또는 Colletion 데이터) {
      	Collection 데이터를 순차적으로 접근하면서 수행할 작업
      }
      ```

   8. Generics

      - Template Programming(일반화 프로그래밍)
        - 데이터 타입을 미리 결정짓지 않고 실행할 때 결정하는 것.
        - 클래스 내부에서 사용할 데이터 타입을 인스턴스를 생성할 때 결정하는 것.
        - 초창기에는 Object 클래스를 이용해서 많이 구현했는데 1.5 버전에서 추가
        - **자료구조나 알고리즘을 구현할 때 동일한 알고리즘을 사용함에도 불구하고 데이터의 자료형이 달라서 여러 개 구현해야하는 번거로움을 없애기 위해서**
        - 클래스를 만들 때는 미지정 자료형을 사용하고 인스턴스를 만들 때 실제 자료형을 설정.
          Object 클래스를 사용하는 것보다는 에러 발생 가능이 낮아진다.
      - Generics 선언 형식

        - 클래스 생성
          ```java
          class 클래스이름<미지정 자료형 이름 나열> {
          	미지정 자료형
          }
          ```
        - 인스턴스 생성
          ```java
          // 클래스이름<미지정 자료형의 실제 자료형을 나열> 변수 = new 생성자<>(매개변수 나열);
          ```
          생성자를 호출할 때는 <>만 해도 되고 자료형을 다시 나열해도 됨.
          인스턴스를 생성할 때 자료형을 기재하지 않으면 경고가 발생하고 데이터는 Object 클래스 타입으로 간주 - 데이터를 형 변환해서 사용해야 함.
        - 실습

          ```java
          // 미지정 자료형의 이름은 한 글자로 하는 것이 관례
          public class Generic<T> {
              private T[] data;

              // ...은 데이터 개수에 상관없이 매개변수로 받아서
              // 배열로 만들어주는 문법 - varargs
              public Generic(T... n) {
                  data = n;
              }

              // 배열의 데이터를 출력해주는 메서드
              public void disp() {
                  for(T temp: data) {
                      System.out.println(temp);
                  }
              }
          }
          ```

        - 특징
          - 변수를 선언하거나 메서드의 매개변수 그리고 리턴 타입으로도 사용 가능
          - static 속성이나 메서드에는 적용 안됨. static 멤버는 클래스를 메모리에 로드할 때 만들어져야 하기 때문
          - 미지정 자료형을 이용해서 배열 변수를 만드는 것은 되지만 배열을 만드는 것은 안됨.
          - **미지정 자료형을 만들 때 `<자료형이름 extends 인터페이스나 클래스>`로 작성하면 인터페이스나 클래스를 상속하는 클래스의 자료형만 가능**
          - **자료형이 일치하지 않으면 실행 시 ClassCastException이 발생**
          - 미지정 자료형을 만들 때 <자료형 이름 super 인터페이스나 클래스>로 작성하면 동일한 자료형 또는 상위 타입의 자료형만 가능
          - <?> 라고 쓰면 모든 자료형이 가능 - 자료형 이름만 기재해도 기본적으로 모든 자료형이 가능

   9. enum(나열형 상수, 열거형 상수)
      - 개요
        - 상수의 모임을 만드는 것
        - 선택을 제한하기 위한 목적으로 사용 - jdk api에서는 enum보다는 static final 상수를 많이 이용
      - 선언
        ```java
        enum 이름{
        	상수 이름 나열;
        }
        ```
        - 사용을 할 때는 `이름.상수이름` 으로 사용
        - 변수를 만들 때 enum의 이름을 이용해서 생성하면 enum에 정의된 상수만 대입이 가능.
      - 특징
        - 생성자와 일반 메서드 생성 가능
        - 자바는 enum을 하나의 클래스로 간주하고 각 상수는 하나의 인스턴스로 간주
        - == 으로 일치 여부를 확인할 수 있고 compareTo라는 메서드로 크기 비교 가능
   10. Annotation
       - @ 다음에 특정한 단어를 기재해서 주석을 만들거나 자바 코드에 특별한 의미 또는 기능을 부여하는 것.
       - 용도
         - 컴파일러가 특정 오류를 체크하도록 지시. - 자바가 사용
         - Build나 Batch를 할 때 코드를 자동 생성 - 프레임워크가 이 기능을 많이 사용
         - Runtime 시 특정 코드 실행
       - JDK가 기본적으로 제공하는 것
         - @Override
           오버라이딩을 한다라는 의미를 전달
         - @Deprecated
           더 이상 사용하지 않는 것을 권장
         - @SupressWarning
           경고를 발생시키지 않도록 하는 기능
         - @SafeVarargs
           varargs에 Generics를 적용한다는 의미를 전달
         - @FunctionInterface
           함수형 인터페이스(추상 메서드 하나로 만들어진 인터페이스)라는 의미를 전달
           이 인터페이스는 람다 적용이 가능하다라고 알리는 것(안드로이드에서는 이 인터페이스로 anonymous class를 구현하면 람다식으로 변경함)

2. **java.util 패키지**

   1. Arrays 클래스

      - 배열을 조작하기 위한 클래스로 static 메서드만 존재하기 때문에 공개된 생성자는 없음.
      - copyOf(원본 배열, 데이터 개수)
        - 원본 배열에서 데이터 개수만큼 복제를 해서 배열로 리턴
        - 배열의 데이터 개수보다 더 많은 데이터 개수를 기재하면 나머지는 기본값으로 채워서 리턴
        - 내부적으로는 System 클래스의 arraycopy라는 메서드를 이용
      - toString(배열)
        - 배열의 모든 요소를 toString을 호출해서 그 결과를 다시 하나의 문자열로 만들어서 리턴
      - sort
        - 데이터를 정렬해주는 메서드
        - 배열만 대입하는 메서드는 크기 비교를 해서 오름차순 정렬을 수행
          숫자 데이터는 부등호를 이용해서 비교
          그 이외의 데이터는 Comparable 인터페이스의 compareTo 라는 메서드를 이용해서 비교.
          숫자 데이터가 아닌 경우는 Comparable 인터페이스의 compareTo라는 메서드를 재정의 해야만 한다. 그렇지 않으면 ClassCastException이 발생
          compareTo 메서드는 정수를 리턴하는데 양수를 리턴하면 데이터의 순서를 변경하고, 0이나 음수를 리턴하면 데이터의 순서를 변경하지 않는다.
          배열과 Comparator<T> 인터페이스를 구현한 인스턴스를 대입하는 메서드는 배열의 데이터를 Comparator에 정의된 대로 오름차순 정렬. 최근에는 이 방법을 많이 사용. 인터페이스를 구현한 클래스의 인스턴스를 대입할 때는 대부분의 경우 anonymous class를 사용
      - 사용자 정의 클래스의 정렬

        - 사용자 정의 클래스 생성 - VO

          ```java
          package kakao.itstudy.java.util;

          public class VO {
              private int num;
              private String name;
              private int age;

              public VO() {
                  super();
              }

              public VO(int num, String name, int age) {
                  super();
                  this.num = num;
                  this.name = name;
                  this.age = age;
              }

              public int getNum() {
                  return num;
              }

              public void setNum(int num) {
                  this.num = num;
              }

              public String getName() {
                  return name;
              }

              public void setName(String name) {
                  this.name = name;
              }

              public int getAge() {
                  return age;
              }

              public void setAge(int age) {
                  this.age = age;
              }

              @Override
              public String toString() {
                  return "VO{" +
                          "num=" + num +
                          ", name='" + name + '\'' +
                          ", age=" + age +
                          '}';
              }
          }
          ```

          ```java
          // VO 클래스의 인스턴스 5개를 소유하는 배열
          VO[] datas = new VO[5];
          datas[0] = new VO(1,"김규민", 26);
          datas[1] = new VO(2, "gyumin", 27);
          datas[2] = new VO(3, "kimgyumin", 28);
          datas[3] = new VO(4, "규민", 29);
          datas[4] = new VO(5, "Gyuminn", 30);
          System.out.println(Arrays.toString(datas));

          // 에러
          Arrays.sort(datas);
          ```

        - 실행을 하면 정수 배열과 문자열 배열은 오름차순 정렬이 되지만 VO 클래스의 배열은 ClassCastException이 발생 - Comparable 인터페이스로 형 변환이 안된다는 메시지
        - 데이터를 정렬하기 위해서는 크기 비교를 위한 방법이 제공이 되어야 하는데 제공되지 않아서 이다. 자바는 반드시 있어야 하는 무엇인가를 만들 때 인터페이스를 이용한다. 데이터 클래스에 Comparable 인터페이스를 구현해야 한다라는 의미이다.
        - VO 수정
          CompareTo를 implements해야 하는데 자기 자신을 제네릭으로 해야함.
          그리고 compareTo 메서드를 오버라이딩한다.

          ```java
          public class VO implements Comparable<VO> {
          	...생략
          	@Override
              public int compareTo(VO o) {
                  // 숫자는 뺄셈을 해서 리턴하면 된다.
                  // 순서를 변경하면 내림차순이 된다.
                  // return this.age - o.age;

                  // 문자열을 뺄셈이 안됨.
                  // 문자열은 Comparable 인터페이스를 implements했기 때문에
                  // compareTo 메서드로 비교하면 된다.
                  return this.name.compareTo(o.name);
              }
          }
          ```

      - 동적 정렬

        - Comparable 인터페이스를 implements해서 크기 비교하는 메서드를 만들면 정렬에 이용할 수 있는데 이렇게 되면 한 가지 방법만으로만 정렬 가능
        - Arrays.sort를 Overloading해서 Arrays.sort(배열, Comparator<T>)를 이용해서 두 번째 매개변수에 정렬 방법을 설정s
        - 인터페이스나 추상 클래스 또는 일반 클래스를 상속받아서 사용하는 방법

          - 인터페이스나 추상 클래스를 상속하는 클래스를 생성해서 인스턴스를 생성한 후 사용하는 방법

            ```java
            class Temp implements Comparator<String> {
                메서드 재정의
            }

            Temp temp = new Temp();
            ```

          - anonymous를 이용하는 방법
            ```java
            new Comparator<String>{메서드 재정의}
            ```
          - 하나의 추상 메서드만 가진 인터페이스의 경우는 lambda 이용 가능

      - 검색
        - 순차 검색
          앞에서부터 끝까지 순회하면서 조회, 데이터가 정렬되지 않아서 수행
          검색 속도는 가장 느림, 데이터가 존재하지 않는 경우 모든 데이터를 조회해야 알 수 있다.
        - 제어 검색
          데이터를 정렬해 놓고 검색하는 방식
          - 이분 검색(Binary Search)은 중앙의 데이터와 비교해서 같으면 찾은 것이고 작으면 왼쪽 부분에서 동일한 작업을 수행하고 크면 오른쪽에서 동일한 작업을 수행
          - 피보나치 검색: 피보나치 수열 이용(1, 2, 3, 5, 8, 13, 22 …)
          - 보간 검색: 검색 위치를 계산
            (찾고자 하는 데이터 - 최소 데이터)/(가장 큰 데이터 - 최소 데이터) \* 데이터 개수
          - 트리 검색
            데이터를 저장할 때 트리 구조로 저장
          - 블록 검색
            블록끼리는 정렬이 되어 있고 블럭 내부는 정렬을 하지 않은 상태에서 검색
          - 해싱
            데이터의 저장 위치를 계산하는 방식으로 모든 데이터의 접근 속도가 같음.
            한 번의 계산만으로 데이터를 조회할 수 있으므로 가장 빠른 검색 방법
            계산은 우리가 하지 않고 운영 체제가 한다.
        - Arrays 클래스는 binarySearch라는 메서드를 제공해서 이분 검색을 할 수 있도록 해준다.
      - 배열이나 리스트를 공부할 때 반드시 해야 할 일
        - 전체 순회
        - 정렬
        - 데이터의 존재 여부나 존재 위치

   2. **Collection**
      - 여러 개의 데이터를 모아 놓은 것 - **Vector Data**라고 하기도 한다.
        자바는 java.util.Vector 클래스가 존재
      - Collection 인터페이스가 존재하는데 이 인터페이스는 Set과 List의 상위 인터페이스.
      - List나 Set에서 공통으로 사용할 메서드의 원형을 선언해 둠.
      - add, addAll, clear, contains, equals, isEmpty, remove, removeAll, retainAll, size, toArray, iterator
      - Generics가 적용되어 있어서 인스턴스를 만들 때 자료형을 기재하면 그 자료형으로 결과를 리턴하지만 자료형을 기재하지 않으면 Object 타입으로 리턴하게 되고 이렇게 되면 사용을 할 때는 강제 형 변환을 해서 사용해야 한다.
   3. 반복자
      - Collection의 데이터를 순차적으로 접근할 수 있도록 해주는 포인터
      - 부르는 이름은 여러가지가 있는데 enumerator, iterator, cursor(데이터베이스), generator 등이 있다.
      - Enumeration 인터페이스와 Iterator 인터페이스가 제공되는데 Enumeration 인터페이스가 legacy API이다.
      - 다음 데이터의 존재 여부와 실제 다음 데이터를 가져오는 메서드가 제공
      - Stream API를 제공하기 때문에 잘 사용하지는 않는다.
   4. List 구조
      - List 인터페이스
        - List들이 공통으로 가져야하는 메서드를 소유하고 있는 인터페이스
        - Generics가 적용되어 있다.
          인스턴스를 만들 때 실제 자료형을 설정하는 것이 좋다.
        - toString 메서드를 재정의해 놓았기 때문에 toString 메서드로 데이터의 내용을 확인하는 것이 가능
        - 메서드
          - add(데이터)
          - add(인덱스, 데이터)
          - set(인덱스, 데이터)
          - get(인덱스)
          - sort(Comparator<T> c)
          - remove(인덱스나 인스턴스)
          - size()
      - Vector, ArrayList 클래스
        데이터를 물리적으로 연속해서 저장한 클래스
        조회는 LinkedList보다 우수
        데이터를 중간에 삽입하거나 삭제하는 것은 LinkedList에 비해서 효율이 떨어진다.
      - LinkedList 클래스
        데이터를 논리적으로 연속해서 저장한 클래스
   5. 확장 클래스
      - java.util.concurrent.CopyOnWriteArrayList
        멀티 스레드 환경에서 하나의 리스트를 사용하고 있는 중에 다른 곳에서 List에 변경을 가하면 ConcurrentModificationException이 발생하는데 이러한 문제를 해결하기 위해서 데이터 읽기 작업을 할 때 복사본을 만들어서 수행하는 클래스
        멀티 스레드 환경에서 ArrayList를 사용하고자 할 때는 이 클래스를 이용하면 동시 사용 문제가 해결된다.
      - UnmodifiableList
        내부 데이터를 수정하지 못하도록 하는 클래스
   6. Stack
      - LIFO 구조로 동작하는 List
      - 삽입은 push로 하고 꺼내는 동작은 pop(마지막 데이터를 삭제)과 peek(마지막 데이터를 삭제하지 않고 리턴)가 존재
   7. Queue
      - FIFO 구조의 자료구조
      - 자바에서는 인터페이스로 제공
      - 삽입은 offer
      - 첫 번째 데이터 리턴은 peek와 poll
      - Queue를 implements한 대표적인 클래스는 데이터의 크기 순으로 조회할 수 있는 PriorityQueue 클래스가 있다.
      - 데이터를 삽입할 때 정렬을 수행하기 때문에 크기 비교가 가능한(Comparable 인터페이스를 implements)
   8. Deque
      - 양쪽에서 삽입과 삭제가 가능한 자료구조
      - 자바에서는 인터페이스 형태로 제공하고 ArrayDeque 클래스가 Deque를 구현한 대표적인 클래스
   9. Set
      - 개요
        - 데이터를 중복없이 해싱을 이용해서 저장하는 자료구조
        - key 없이 저장
        - 저장 순서도 알 수 없음
        - 전체 데이터 순회는 이터레이터나 빠른 열거를 이용해서 수행
        - 자바에서는 인터페이스로 제공
      - 구현한 클래스
        - HashSet: 저장 순서를 알 수 없음.
        - LinkedHashSet: 저장 순서를 기억하는 Set
        - TreeSet: 크기 순서대로 저장 - 비교 가능한 메서드를 가진 인스턴스만 저장 가능.
   10. Map
       - 개요
         - Map은 Key와 Value를 쌍으로 저장하는 자료구조
         - Dictionary(dict)나 HashTable이라고도 한다.
         - java에서는 Map은 인터페이스이고 2개의 Generic을 사용
           하나의 key의 자료형이고 다른 하나는 value의 자료형이다.
         - `Map<키의 자료형, 값의 자료형>변수명`의 형태로 작성
         - 특별한 경우가 아니면 key의 자료형은 String으로 하는 것이 좋다.
         - key를 Set으로 구성하기 때문에 중복될 수 없다.
         - put(key, value)
           - 데이터 추가
           - 존재하는 key를 이용하면 수정이 된다.
         - get(key)
           - 데이터 1개 가져오기
           - 존재하지 않는 key를 사용하면 null을 리턴
         - remove(key)
           - 데이터 삭제
         - size()
           - 데이터 개수
         - Set<K> keySet()
           - 모든 key를 리턴
         - 다른 언어에서 이 자료구조를 사용할 때는 존재하지 않는 key를 사용했을 때 리턴을 확인해야 한다.
       - 구현 클래스
         - HashMap: key의 순서를 알 수 없음.
         - LinkedHashMap: key가 저장한 순서
         - TreeMap: key를 정렬
       - 용도
         데이터를 저장해서 전달하는 용도로 주로 이용
       - **MVC Pattern**
         - Model, View, Controller
         - 역할 별로 구별해서 구현
           이렇게 구현하는 이유는 Model이 변경되더라도 View에는 영향을 주지 않기 위해서이다.
           **2차원 배열의 경우는 복잡하더라도 Map의 배열로 만드는 것이 MVC 패턴을 적용하는데 편리하다.**
           **모든 언어의 Web Programming에서는 데이터 전달을 Map으로 한다.**
   11. Properties
       - 이름과 값의 쌍으로 데이터를 저장하는 구조
       - Map은 Key와 Value에 모든 자료형을 사용할 수 있는데 Key와 Value에 String만 가능
       - 텍스트 파일에 이름과 값의 쌍으로 작성한 후 읽어서 사용할 때 이용하던 클래스
         현재도 Spring Framework에서는 이 형태를 사용하고 있는데 최근에는 XML이나 YML(yaml) 형태로 사용 - 환경 설정에 많이 이용
       - 메서드
         - String getProperty(String key)
         - String setProperty(String key, String value)
         - void store(OutputStream out, String comment)
           스트림에 일반 텍스트 형식으로 저장
         - void storeToXML(OutputStream out, String comment)
           스트림에 XML(태그와 유사) 형태로 저장
         - Enumeration<K> keys()
           모든 키들을 접근할 수 있는 이터레이터를 리턴
   12. Legacy Collection
       - 예전에 만들어진 Collection API Class
       - Vector → ArrayList
       - Enumeration → Iterator
       - Stack → List
       - Dictionary Hashtable → HashMap
       - Properties → HashMap
       - BitSet → HashSet
   13. Colletion의 동기화
       - Collection 클래스들을 사용할 때 다른 스레드가 수정 중인 경우 대기했다가 사용하도록 하고자 하는 경우에는 Collections 클래스를 이용해서 생성하면 된다.
       - Collections.synchronizedCollection(Collection 클래스의 인스턴스)를 호출하면 동기화된 Collection을 리턴해주므로 리턴된 인스턴스를 사용하면 다른 곳에서 수정 중인 경우 대기한다.
       - 다른 곳에서 수정할 수 없도록 만들고자 할 때는 Collections.unmodifiableCollection(Collection 클래스의 인스턴스)를 호출해서 리턴된 인스턴스를 사용하면 된다.
   14. Random 클래스
       - 랜덤한 데이터를 생성해주는 클래스
       - 정수형 난수의 경우는 정수 범위 내에서 생성하고 실수형 난수의 경우는 0.0 ~ 1.0 사이의 난수를 생성. 실수형 난수는 Math.random()으로도 가능하다.
       - 랜덤을 추출할 때는 seed 라고 하는 숫자를 설정한 후 이 seed를 기반으로 난수표를 만든 후 그 난수표에서 하나씩 숫자를 읽어온다.
         seed를 예측할 수 없다면 랜덤한 데이터이고 seed를 알고 있다면 순선대로 숫자를 읽어온다.
       - java는 기본적으로 seed가 랜덤이다.
       - 랜덤을 사용하는 대표적인 경우가 머신러닝에서 샘플 데이터 추출할 때이다.
       - 생성자
         - Random(): seed가 랜덤
         - Random(long seed): seed 고정
       - 메서드
         - float nextFloat()
         - boolean newxtBoolean()
         - int nextInt(): 정수 범위 내에서 리턴
         - int nextInt(int n): 0~ n 사이의 값을 리턴
   15. StringTokenizer
       - 문자열을 분할해주는 클래스
       - String 클래스의 split 메서드를 조금 더 잘 활용할 수 있도록 만든 클래스
   16. java.util.Date
       - 날짜 관련 클래스로 javascript의 Date와 동일 - javascript의 Date가 Java의 Date를 가져가서 사용
       - 1970년 1월 1일 자정(epoch time)을 기준으로 지나온 시간을 정수로 관리 - 밀리초 단위. 2038년까지만 사용 가능
       - 많은 메서드가 Dprecated 되어있는데 그래도 많이 쓰는 이유는 관계형 데이터베이스의 날짜 자료형과 매핑 가능하기 때문이다.
       - 생성자
         - Date(): 현재 시간
         - Date(long timeInMillis): epoch 시간을 가지고 설정
         - Date(int year, int month, int date): 년 - 1900년에서 지나온 연도, 월(현재 월에서 -1), 일
       - 메서드
         - get으로 시작하는 메서드를 이용해서 특정값을 추출하는 값이 가능
         - toString 메서드가 재정의되어 있어서 바로 출력 가능하다.
   17. java.util.Calendar
       - 추상 클래스
       - GregorianCalendar 클래스가 하위 클래스
       - 인스턴스 생성
         - Calendar.getInstance(): 현재 날짜 및 시간
         - new GregorianCalendar() 나 년월일 또는 년월일시분초를 설정해서 생성(월만 -1을 해서 설정)
       - 날짜 및 시간 가져오기와 설정
         - 가져오기
           get(Calendar.상수)
         - 설정하기
           set(Calendar.상수,값)
         - add 메서드를 이용해서 추가하거나 감소시키는 것도 가능
       - Date와의 변환
         - Date 변수 = new Date(Calendar.getTimeInMillis())
         - Calendar객체.setTime(Date객체)
   18. java.text.SimpleDateFormat
       - [java.util.Date](http://java.util.Date) 타입을 받아서 원하는 형식의 문자열로 변환해주는 클래스
       - 사용
         new SimpleDateFormat(String 날짜 서식)).format(Data 인스턴스)
   19. 날짜와 시간 관련 클래스
       - java.sql.Date, java.sql.Time, java.sql.DateTime, java.sql.Timestamp
       - java.time.LocalDate, java.time.LocalTime, java.time.LocalDateTime - 시간대 설정이 가능
       - 최근의 JPA 같은 프레임워크에서는 [java.util.Date](http://java.util.Date) 대신에 java.time.LocalDateTime을 사용하기도 한다.
   20. **정규 표현식**
       - 특정 문자열 패턴을 만드는데 사용하는 클래스
       - java에서는 java.util.regex 패키지에 Match 클래스와 Pattern 클래스를 제공
       - Pattern 클래스로 정규 표현식 인스턴스를 생성하고 matcher 메서드를 이용해서 처리를 한 후 Matcher 클래스로 결과를 사용한다.
       - 문자열의 유효성 검사에 많이 이용
