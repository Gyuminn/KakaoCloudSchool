package kakao.itstudy.operator;

public class RelationOperator {
    public static void main(String[] args) {
        System.out.println(10 > 3);
        // 정수끼리는 자료형이 달라도 크기 비교는 가능
        System.out.println(10 >= 10L);
        System.out.println(10L > 10);

        // 실수는 크기 비교에서도 정확한 결과를 만들어내지 못할 수 있음.
        System.out.println((1.0 - 0.8) >= 0.2);

        int n1 = 10;
        int n2 = 10;
        long n3 = 10L;
        System.out.println(n1 == n2);
        System.out.println(n1 == n3);
        // 실수나 문자열은 주의

        // 문자열의 경우는 리터럴으로 만들었느냐 외부에서 입력을 받느냐에 따라서
        // 다른 결과가 만들어질 수 있음.
        String s1 = "JAVA";
        String s2 = "JAVA";
        System.out.println(s1 == s1);
        System.out.println(System.identityHashCode(s1));
        System.out.println(System.identityHashCode(s2));

        java.util.Scanner sc = new java.util.Scanner(System.in);
        System.out.print("문자열을 입력:");
        // 문자열을 입력받아서 생성 - 리터를을 만든 것이 아님.
        String s3 = sc.nextLine();

        // 동일한 JAVA를 입력해도 해시코드가 다름
        System.out.println(System.identityHashCode(s3));

        // 해시코드가 달라서 false
        System.out.println(s1 == s3);

        // 인스턴스의 경우는 equals로 내용을 비교
        System.out.println(s1.equals(s3));
    }
}
