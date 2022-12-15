package kakao.itstudy.oop;

public class MethodClass {
    private static int num;
    private String name;

    // static method
    public static void method1() {
        num = 1;
        // static 메서드에서는 instance 속성이 안됨.
        // name = "gyumin";
        System.out.println("static method");
    }

    // instance method
    public void method2() {
        num = 1;
        name = "gyumin";
        System.out.println("instance method");
    }

    // static: 인스턴스를 생성할 필요가 없음.
    // 매개 변수가 없음.
    // MethodClass.noArgsMethod() 로 호출한다.
    public static void noArgsMethod() {
        for (int i = 0; i < 5; i++) {
            System.out.println("method");
        }
    }

    // 매개변수가 정수 1개인 메서드
    // MethodClass.oneArgsMethod(정수) 로 호출한다.
    public static void oneArgsMethod(int n) {
        for (int i = 0; i < n; i++) {
            System.out.println("method");
        }
    }

    // 매개변수가 2개인 메서드
    // 매개변수가 2개 이상인 경우는 순서대로 대입을 해야 한다.
    public static void twoArgsMethod(int n, String msg) {
        for (int i = 0; i < n; i++) {
            System.out.println(msg);
        }
    }

    // 리턴이 없는 메서드는 연속해서 호출하는 것이 불가능
    // 2개의 정수를 받아서 더해누는 메서드
    // 인스턴스.addWithInteger(정수1, 정수2)로 호출
    public int addWithInteger(int first, int second) {
        return (first + second);
    }

    // 매개 변수가 기본형 1개
    // 내부에서 매개변수의 값을 수정해도 넘겨준 데이터는 변경되지 않음.
    public static void callByValue(int n) {
        n = n + 1;
        System.out.println("n:" + n);
    }

    // 매개변수가 참조형인 경우
    public static void callByReference(int[] ar) {
        ar[0] = ar[0] + 1;
        System.out.println("ar[0]:" + ar[0]);
    }

    public int score = 100;

    public void thisMethod() {
        int score = 200;
        // scope의 법칙 적용 - 가까이에서 만든 것을 사용
        System.out.println("score:" + score); // 200
        // 인스턴스가 사진 score 호출
        // this가 붙으면 메서드 안에서는 찾지 않
        System.out.println("this.score:" + this.score);
    }

    public static int noRecursionFibonacci(int n) {
        int n_1 = 1; // 현재 구하고자 하는 피보나치 값의 바로 앞
        int n_2 = 1; // 현재 구하고자 하는 피보나치 값의 두 번째 앞
        // 기본 값 설정
        int fibo = 1;
        // 규칙이 3번쨰 부터 적용
        int i = 3;
        while (i <= n) {
            fibo = n_1 + n_2;
            // n_1과 n_2를 이동
            n_2 = n_1;
            n_1 = fibo;
            i++;
        }
        return fibo;
    }

    // 재귀를 이용한 피보나치 수열
    public static int recursionFibonacci(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }
        return recursionFibonacci(n - 1) + recursionFibonacci(n - 2);
    }

    // 매개변수의 개수를 가변으로 설정
    public static void display(String... args) {
        for (String temp : args) {
            System.out.println(temp);
        }
    }
}
