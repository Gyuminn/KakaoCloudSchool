package kakao.itstudy.network;

import java.util.Arrays;
import java.util.Comparator;

public class ComparatorLambda {
    public static void main(String[] args) {
        // 데이터 정렬을 위해서 문자열 배열 생성
        String[] ar = {"축구", "야구", "배구", "족구", "농구", "당구"};

        Arrays.sort(ar, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println(Arrays.toString(ar));
        // 코드는 간결해졌지만 가독성이 떨어진다.
        Arrays.sort(ar, (o1, o2) ->
            o1.compareTo(o2)
        );
        System.out.println(Arrays.toString(ar));
    }
}
