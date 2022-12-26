package kakao.itstudy.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class CollectionAccess {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Java");
        list.add("Java Web Programming");
        list.add("Spring Framework");

        // 반복문을 이용하는 방밥
        int len = list.size();
        for (int i = 0; i < len; i++) {
            System.out.print(list.get(i) + "\t");
        }
        System.out.println();

        // 이터레이터 이용
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + "\t");
        }
        System.out.println();

        // 빠른 열거
        for (String temp : list) {
            System.out.print(temp + "\t");
        }
        System.out.println();

        // 스트림 API 활용
        // 내부 반복자를 이용하기 때문에 가장 빠름
        Stream<String> stream = list.stream();
        stream.forEach(temp -> System.out.print(temp + "\t"));
    }
}
