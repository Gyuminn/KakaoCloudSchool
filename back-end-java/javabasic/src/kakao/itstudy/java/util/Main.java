package kakao.itstudy.java.util;

import java.util.Arrays;

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
        datas[0] = new VO(1,"나희도", 30);
        datas[1] = new VO(2, "김규민", 27);
        datas[2] = new VO(3, "람머스", 28);
        datas[3] = new VO(4, "마동석", 20);
        datas[4] = new VO(5, "다람쥐", 26);
        System.out.println(Arrays.toString(datas));

        // 에러
        Arrays.sort(datas);
        System.out.println(Arrays.toString(datas));
    }
}
