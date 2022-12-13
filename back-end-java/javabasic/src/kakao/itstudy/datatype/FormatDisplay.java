package kakao.itstudy.datatype;

public class FormatDisplay {
    public static void main(String[] args) {
        int n = 37;
        double d = 23.768952;
        String name = "gyumin";

        System.out.printf("n:%d\n", n);

        // 10자리를 확보해서 출려
        System.out.printf("n:%10d\n", n);
        // 남는 자리는 0을 추가해서 출력
        System.out.printf("n:%010d\n", n);

        // 소수 셋째 자리까지출력 후 반올림
        System.out.printf("d:%.3f\n", d);

        // %s는 문자열을 출력하는 것이 아니고
        // 원래 %s는 시작위치부터 바이트 단위로 데이터를 가져와서
        // 문자로 변환한 후 출력하는 것으로
        // null을 만날 떄 까지 출력

    }
}
