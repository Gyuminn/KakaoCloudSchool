package kakao.itstudy.network;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ReductionOperator {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(
                "김규민", "이규민", "나규민", "박규민", "천규민", "나규민"
        );
        // 스트림 생성
        Stream<String> stream = list.stream();
        // 전체 출력
        // stream.forEach((temp) -> System.out.print(temp + "\t"));

        // 2개 빼고 출력
        // stream.skip(2).forEach((temp) -> System.out.print(temp + "\t"));

        // 2개 건너 뛰고 1개 사용
        // stream.skip(2).limit(1).forEach((temp) -> System.out.print(temp + "\t"));

        // 중복 제거
        // stream.distinct().forEach((temp) -> System.out.print(temp + "\t"));

        // 필터링
        // 매개 변수가 1개이고 Boolean을 리턴하는 함수를 대입
        // stream.filter(name -> name.charAt(0) == '나').forEach((temp) -> System.out.print(temp + "\t"));

        // 0으로 시작하는
        stream.filter(name -> name.charAt(0) >= '박' && name.charAt(0) <= '이').sorted().forEach((temp) -> System.out.print(temp + "\t"));

    }
}
