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
        for (int i = 0; i < 100000; i++) {
            Integer integer = al.get(i);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList 조회 시간: " + (end - start) + "ms");

        /*
        start = System.currentTimeMillis();
        for(int i = 0; i<100000; i++) {
            Integer integer = li.get(i);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList 조회 시간: " + (end - start) + "ms");

         */

        List<Integer> al2 = new ArrayList<>();
        List<Integer> li2 = new LinkedList<>();

        al2.add(1);
        al2.add(3);

        li2.add(1);
        li2.add(3);

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            al2.add(1, 2);
        }
        end = System.currentTimeMillis();
        System.out.println("ArrayList 중간 삽입 시간: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            li2.add(1, 2);
        }
        end = System.currentTimeMillis();
        System.out.println("LinkedList 중간 삽입 시간: " + (end - start) + "ms");

        List<String> list = new ArrayList<>();
        list.add("천규민");
        list.add("김규민");
        list.add("박규민");
        list.add("나규민");
        list.add("이규민");

        // 순회
        for (String temp : list) {
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

        Stack<String> stack = new Stack<>();
        // 스택에 데이터 추가
        stack.push("김규민");
        stack.push("이규민");
        stack.push("박규민");
        System.out.println(stack);
        System.out.println(stack.pop());

        // 데이터를 sort된 것처럼 저장.
        Queue<String> queue = new PriorityQueue<>();
        queue.offer("박규민");
        queue.offer("천규민");
        queue.offer("이규민");
        queue.offer("김규민");
        System.out.println(queue);
        System.out.println(queue.poll());

        // 저장 순서를 알 수 없는 Set 인스턴스 생성
        Set<String> set = new HashSet<>();
        // 데이터 추가
        set.add("천규민");
        set.add("박규민");
        set.add("김규민");
        set.add("이규민");
        // 동일한 데이터를 삽입 - equals 메서드로 비교해서
        // true이면 삽입하지 않는다.
        // Set은 데이터 삽입에 실패하면 false를 리턴하고
        // 성공하면 true를 리턴한다.
        set.add("김규민");
        set.add("나규민");
        System.out.println(set);


        /**
         * [o] 로또 번호 생성기 만들기
         * [o] 1-45까지의 숫자 6개를 입력받아서 저장한 후 출력
         * [o] 입력을 받을 때 1 ~ 45 사이의 숫자가 아니면 다시 입력하도록
         * [o] 중복되는 숫자를 입력하면 다시 입력하도록 한다.
         * [o] 데이터를 출력할 때는 정렬을 해서 출력
         */

        // 숫자 6개를 저장할 공간을 생성
        // 중복 검사가 수월하고 정렬을 수행하면서 삽입하는 TreeSet을 이용

        // 배열을 이용하는 경우
        // 입력받기 위한 인스턴스 생성
        /*
        Scanner sc = new Scanner(System.in);
        // 6개의 정수를 저장할 배열을 생성
        int[] lotto = new int[6];

        int len = lotto.length;
        for (int i = 0; i < len; i++) {
            try {
                System.out.print("로토 번호 입력: ");
                lotto[i] = sc.nextInt();

                // 1부터 45사이의 숫자만 입력받도록 하기
                if (lotto[i] < 1 || lotto[i] > 45) {
                    System.out.println("1-45 사이의 숫자만 입력하세요!!");
                    i--;
                    continue;
                }

                // 데이터 중복 검사
                // 첫 번째부터 현재 데이터 앞까지 비교
                boolean flag = false;
                for (int j = 0; j < i; j++) {
                    // 2개의 데이터가 같음
                    if (lotto[i] == lotto[j]) {
                        flag = true;
                        break;
                    }
                }

                // 중복된 경우
                if(flag == true) {
                    System.out.println("중복된 숫자 입니다.!!!");
                    i--;
                }
            } catch (Exception e) {
                // 예외의 경우 i를 다시 무효화 시켜서 총 입력 횟수를 유지해준다.
                i--;
                sc.nextLine();
                System.out.println("숫자를 입력하세요!!");
            }

        }
        // 배열의 데이터 정렬
        Arrays.sort(lotto);
        // 배열의 데이터를 출력
        System.out.println(Arrays.toString(lotto));
        // 스캐너 정리
        sc.close();
         */

        // Set을 이용해서 구현
        // 입력받기 위한 인스턴스 생성
        Scanner sc = new Scanner(System.in);

        // 중복된 데이터를 저장하지 않고 데이터를 정렬해서 저장하는
        // 자료구조 클래스
        Set<Integer> lottoSet = new TreeSet<>();
        // set에 6개의 데이터가 저장되지 않은 경우
        while(lottoSet.size() < 6) {
            System.out.println("로또 번호 입력: ");
            int temp = sc.nextInt();
            if(temp < 1 || temp > 45) {
                System.out.println("1부터 45사이의 숫자를 입력하세요");
                continue;
            }

            // 중복 검사
            boolean result = lottoSet.add(temp);
            // 삽입에 실패하면 - 중복된 데이터라면
            if(result == false) {
                System.out.println("중복된 숫자를 입력하였습니다.");
            }
        }
        System.out.println(lottoSet);

        // 배열로 사용 하고 싶지만 단순 형변환같은 아래의 방법은 에러 발생
        // Integer[] lottoArr = (Integer[])lottoSet.toArray();
        // System.out.println(Arrays.toString(lottoArr));

        // 스캐너 정리
        sc.close();
    }
}