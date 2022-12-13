package kakao.itstudy.operator;

public class ArithmeticOperator {
    public static void main(String[] args) {
        short s1 = 20;
        short s2 = 30;
        // short 사이의 덧세임고 결과도 50이라서 short로 저장 가능하지만
        // 이 문장은 에러
        // 자바의 산술 연산의 최소 단위는 int
        // 자바는 s1과 s2로 int 변환해서 연산을 수행하므로
        // 결과는 int가 되어서 short에 저장할 수 없다.
        // 이 경우는 결과를 int에 저장하던가 강제 형 변환을 통해서 short에 저장해야 한다.
        // short result = s1 + s2;
        int result = s1 + s2;

        // 실수의 산술 연산 결과
        double d = 0.1;
        double tot = 0.0;
        for(int i = 0; i < 100; i++) {
            tot = tot + d;
        }
        // 0, 1을 100번 더했는데 10이 아니고 9.99999999999998
        System.out.println("tot:" + tot);

        // 나누기 연산에서의 나누는 수 확인
        // 0으로 나누는 것이 에러가 아닐 수 있음.
        // 연산이 안되는 경우 Infinity나 NaN이 될 수 있음.
        System.out.println(5/0.0);
    }
}
