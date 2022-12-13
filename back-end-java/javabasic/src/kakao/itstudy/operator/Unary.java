package kakao.itstudy.operator;

public class Unary {
    public static void main(String[] args) {
        int n1 = 20;
        int n2 = -20;
        // 정수 데이터가 2진수로 저장된 구조를 출력
        System.out.println("n1:" + Integer.toBinaryString(n1));
        System.out.println("n2:" + Integer.toBinaryString(n2));

        // 1의 보수 구하기 + 21
        System.out.println("n3:" + Integer.toBinaryString(~n2));

        int n = 20;
        System.out.println(n++);
        System.out.println(++n);
    }
}
