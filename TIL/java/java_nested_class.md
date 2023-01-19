# [Java] Nested Class

자바는 클래스 안에 클래스를 생성하는 것이 가능. (클래스 안에 인터페이스 생성 가능, 인터페이스 안에 인터페이스 생성 가능)

자바는 소스 파일 단위로 컴파일 되는 것이 아니고 클래스 단위로 컴파일 된다.

당장 안드로이드를 할 것이 아니라면 덜 중요할 수 있다.

안드로이드를 할 때는 반드시 Anonymous Class 문법을 숙지해야 한다.

Inner Class 접근 지정자에 private와 protected 사용 가능

Inner Class는 클래스 특성으로 static 사용 가능

일반 클래스는 접근 지정자가 public이나 package(생략)만 가능하고 클래스 특성으로는 abstract와 final만 가능

1. **종류**
   - Instance Inner Class
     클래스 안에 만들어진 클래스 - static 멤버가 없어야 함.
   - Static Inner Class
     클래스 안에 만들어진 클래스 - static 멤버 가능
   - Local(Method, Function) Inner Class
     메서드 안에 만들어진 클래스
   - Anonymous Class
     이름없는 클래스
2. **Instance Inner Class**

   클래스 안에 만들어지는 클래스

   ```java
   class Outer {
   	class Inner {
   		속성
   		메서드
   	}
   }
   ```

   컴파일이 되고 나면 Outer.class와 Outer$Inner.class 2개의 클래스가 생성됨.

   외부에서 사용하고자할 때는 클래스의 접근 지정자를 public으로 설정하고 Outer 클래스의 인스턴스를 만들고 그 인스턴스를 통해서 Inner 클래스의 인스턴스를 생성해야 한다.

3. **Static Inner Class**

   Inner Class 안에 static 멤버가 있는 경우 일반 클래스로 생성하면 에러가 발생

   static 멤버는 클래스 이름만으로 호출이 가능해야 하는데 Instance Inner Class로 만들면 인스턴스를 만들고 호출해야하기 때문이다.

   이런 경우 에러를 없애고자 하면 Inner Class에 static을 추가해주면 된다.

   ```java
   public class Main {
       public static void main(String[] args) {
           // 외부 클래스의 인스턴스를 생성
           InstanceInner instanceInner = new InstanceInner();
           // 내부 클래스의 인스턴스 생성 - Instance Inner의 경우
           /*
           InstanceInner.Inner inner = instanceInner.new Inner();
           */

           // Static Inner Class의 인스턴스 생성
           InstanceInner.Inner obj = new InstanceInner.Inner();
       }
   }
   ```

4. **Local Inner Class**

   내부 클래스가 메서드 안에서 생성되는 형태

   이 클래스는 메서드 안에서만 사용이 된다.

   Local Inner Class에서는 자신을 포함하고 있는 메서드의 지역 변수를 사용하는 것이 안된다. final 변수만 사용이 가능.

   메서드는 stack에 만들어지지만 클래스는 static 영역에 만들어지기 때문이다. static한 영역은 소멸되지 않고 한 번 만들어지면 계속 존재하기 때문에 stack이나 heap에서 접근이 가능하지만 static한 영역에서 stack이나 heap의 멤버는 접근할 수 없다.

   stack이나 heap에 존재하는 것들은 반드시 존재한다는 보장을 못하기 때문이다.

5. **Anonymous Class - 익명 객체, 클래스**

   상속을 받거나 인터페이스를 구현해야하는 경우에 별도의 하위 클래스를 만들지 않고 인스턴스를 바로 생성하는 문법

   인스턴스를 1개만 생성해서 사용해야하는 클래스의 경우 클래스를 별도로 만드는 것은 자원의 낭비가 될 수 있다.

   클래스는 한 번 만들어지면 메모리에서 삭제가 안됨.

   ```java
   new 상위클래스나인터페이스이름() {
   	// 필요한 속성이나 메서드를 정의
   }
   ```

   최근에는 인터페이스에 메서드가 1개인 경우 여기서 확장된 람다를 이용하는 경우가 많음.

6. **Java에서 클래스를 상속받거나 인터페이스를 구현하는 방법**
   - 하위 클래스를 만들어서 사용
     인스턴스를 2개 이상 생성하고자 하는 경우
   - Anonymous class를 이용
     인스턴스를 1개만 생성해서 사용하는 경우 - 안드로이드의 이벤트 처리에서 많이 이용하는데 최근에는 lambda와 혼용해서 사용, Android Studio의 경우는 lambda 문법으로 코드를 수정하기도 함.
       <aside>
       💻 final
       1. final 변수: 읽기 전용의 변수, 값을 수정할 수 없음.
       2. final 메서드: overriding 할 수 없는 메서드
       3. final 클래스: 상속할 수 없는 클래스
       
       final 메서드나 final 클래스는 기능 확장이 안되는 것이다.
       기능 확장을 못하게 하는 이유의 대부분은 시스템을 핸들링하기 때문이다.
       
       </aside>
