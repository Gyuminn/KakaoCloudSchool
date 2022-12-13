package kakao.itstudy.operator;

public class ShiftOperator {
    public static void main(String[] args) {
        int x = -29;
        System.out.println(x << 2); // 1번 밀 때마다 2 곱하기
        System.out.println(x >> 3); // 1번 밀 때마다 2 나누기 -29 / 2 => -15 / 2 => -8 => -4
        System.out.println(x >>> 3); // 부호변경
        System.out.println(x << 33); // 32로 나눈 나머지만큼 밀기
    }
}
