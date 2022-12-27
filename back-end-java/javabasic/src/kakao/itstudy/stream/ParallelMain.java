package kakao.itstudy.stream;

import java.util.Arrays;
import java.util.List;

public class ParallelMain {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // 일반 스트림으로 1초씩 쉬기
        long start = System.currentTimeMillis();
        list.stream().forEach(imsi -> {
            try{
                Thread.sleep(1000);
            } catch(Exception e) {}
        });
        long end = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (end - start));

        // 병렬 스트림으로 1초씩 쉬기
        start = System.currentTimeMillis();
        list.stream().parallel().forEach(imsi -> {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {}
        });
        end = System.currentTimeMillis();
        System.out.println("걸린 시간: " + (end - start));
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
