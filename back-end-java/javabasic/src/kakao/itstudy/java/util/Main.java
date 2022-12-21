package kakao.itstudy.java.util;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 정수 배열
        int[] ar = {60, 40, 20, 87, 56};

        // 문자열 배열
        String[] br = {"김규민", "규민", "gyumin", "kimgyumin", "Gyuminn"};

        // 배열의 데이터 확인
        System.out.println(Arrays.toString(ar));

        // 정수 배열 정렬
        Arrays.sort(ar);
        System.out.println(Arrays.toString(ar));

        // 문자열 배열 정렬
        Arrays.sort(br);
        System.out.println(Arrays.toString(br));

        // VO 클래스의 인스턴스 5개를 소유하는 배열
        VO[] datas = new VO[5];
        datas[0] = new VO(1, "나희도", 30);
        datas[1] = new VO(2, "김규민", 27);
        datas[2] = new VO(3, "람머스", 28);
        datas[3] = new VO(4, "마동석", 20);
        datas[4] = new VO(5, "다람쥐", 26);
        System.out.println(Arrays.toString(datas));

        // 에러
        Arrays.sort(datas);
        System.out.println(Arrays.toString(datas));

        String[] names = {"리치히", "하일스베르", "로섬", "고슬링 "};
        System.out.println(Arrays.toString(names));

        //복사본 만들기
        String[] copy = Arrays.copyOf(names, names.length);
        Arrays.sort(copy);
        System.out.println(Arrays.toString(copy));

        Arrays.sort(copy, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(Arrays.toString(copy));

        int[] nums = {30, 20, 50, 10, 40};
        // 정렬을 수행하지 않고 하면 논리적인 오류가 발생
        System.out.println(Arrays.binarySearch(nums, 20));
        Arrays.sort(nums);
        System.out.println(Arrays.binarySearch(nums, 20));
        // sort가 된 상태이므로 0이 아니고 2
        System.out.println(Arrays.binarySearch(nums, 30));

        // 정수를 저장하기 위한 ArrayList
        List<Integer> al = new ArrayList<>();
        // 정수를 저장하기 위한 LinkedList
        List<Integer> li = new LinkedList<>();

        for (int i = 0; i < 100000; i++) {
            al.add(i);
            li.add(i);
        }

        long start;
        long end;
        start = System.currentTimeMillis();
        for(int i = 0; i<100000; i++) {
            Integer integer = al.get(i);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList 조회 시간: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for(int i = 0; i<100000; i++) {
            Integer integer = li.get(i);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList 조회 시간: " + (end - start) + "ms");

        List<Integer> al2 = new ArrayList<>();
        List<Integer> li2 = new LinkedList<>();

        al2.add(1);
        al2.add(3);

        li2.add(1);
        li2.add(3);

        start = System.currentTimeMillis();
        for(int i = 0; i<100000; i++) {
            al2.add(1,2);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList 중간 삽입 시간: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for(int i = 0; i<100000; i++) {
            li2.add(1,2);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList 중간 삽입 시간: " + (end - start) + "ms");

        List <String> list = new ArrayList<>();
        list.add("천규민");
        list.add("김규민");
        list.add("박규민");
        list.add("나규민");
        list.add("이규민");

        // 순회
        for(String temp:list) {
            System.out.println(temp);
        }

        // 데이터 정렬
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(list);
    }
}