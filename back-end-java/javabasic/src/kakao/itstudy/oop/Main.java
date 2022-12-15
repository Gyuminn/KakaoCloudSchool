package kakao.itstudy.oop;

import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {
        /*
        // Student 클래스의 인스턴스 생성
        Student student = new Student();
        // 자신의 속성에 접근
        student.num = 1;
        student.name = "김규민";
        student.kor = 32;
        student.eng = 19;
        student.mat = 40;

        Student student1 = new Student();
        student1.num = 2;

        System.out.println(student.num);
        System.out.println(student1.num);

        // 자바는 static 속성에 인스턴스가 접근 가능
        student.schoolName = "강릉고등학교";
        student1.schoolName = "한양대학교";
        // static 속성은 모든 인스턴스가 공유하므로
        // 동일한 데이터가 출력된다.
        System.out.println(student.schoolName);
        System.out.println(student1.schoolName);
     */
        // static 메서드는 클래스 이름으로 호출 가능
        MethodClass.method1();
        // 클래스 이름을 가지고 메서드 호출 불가
        // MethodClass.method2();

        // 인스턴스 메서드 호출
        MethodClass methodClass = new MethodClass();
        methodClass.method2();
        // 자바는 인스턴스를 이용해서 static 메서드 호출 가능
        methodClass.method1();

        // 참조형 변수를 만들지 않고 인스턴스를 만들어서 메서드 호출
        // 딱 한번만 사용할 인스턴스는 이름을 만들지 않는 것이 좋다.
        // 연결된 변수가 없기 때문에 언제든지 인스턴스 메모리 해제를 할 수 있다.
        new MethodClass().method2();

        MethodClass.noArgsMethod();
        MethodClass.oneArgsMethod(3);
        MethodClass.twoArgsMethod(2, "Message");

        MethodClass obj = new MethodClass();
        int result = obj.addWithInteger(10, 30);
        System.out.println(result);
        result = obj.addWithInteger(result, 90);
        System.out.println(result);

        int x = 10;
        MethodClass.callByValue(x);
        // 기본형을 메서드에게 넘겨준 경우는 데이터가 변경되지 않음.
        System.out.println("x:" + x);

        int[] br = {10, 20, 30};
        MethodClass.callByReference(br);
        // 배열을 메서드에게 넘기면 배열의 내용이 변경될 수도 있음.
        // 메서드의 리턴이 없는 경우라면 print 메서드를 제외하고는
        // 원본을 변경해주는 것이다.
        System.out.println("br[0]:" + br[0]);

        MethodClass o1 = new MethodClass();
        o1.thisMethod();

        // 20번째 피보나치 수열의 값
        int f = MethodClass.noRecursionFibonacci(50);
        System.out.println("f:" + f);

        int f2 = MethodClass.recursionFibonacci(5);
        System.out.println("f2:" + f2);

        MethodClass.display("김규민");
        MethodClass.display("gyumin", "Gyuminn", "q.min");
    }
}

