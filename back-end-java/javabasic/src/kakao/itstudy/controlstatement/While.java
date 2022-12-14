package kakao.itstudy.controlstatement;

import java.util.Arrays;

public class While {
    public static void main(String[] args) {
        for (int i = 0, j = 0; i < 10 && j < 5; i++, j = j + 2) {
            System.out.println(i);
            System.out.println(j);
        }

        String[] names = {"java", "spring", "springboot"};
        for (String name : names) {
            System.out.println(name);
        }

        // 제어문 안에서 만든 변수는 자신의 제어문 수행이 끝나면 소멸된다.
        for (int y = 0; y < 10; y++) {
            System.out.println(y);
        }

        for (int y = 0; y < 10; y++) {
            System.out.println(y);
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("*");
            }
            System.out.println();
        }

        for (int i = 1; i <= 25; i++) {
            System.out.print("*");
            if (i % 5 == 0) {
                System.out.println();
            }
        }

        // 데이터를 가지고 배열 생성
        String[] soccers = {"BTS", "봉준호", "손흥민", "제이팍", "레츠고"};

        // 배열의 데이터 순회
        int len = soccers.length;
        for (int i = 0; i < len; i++) {
            String imsi = soccers[i];
            System.out.println(imsi);
        }

        // 생성하고 데이터를 대입
        int[] ar = new int[3]; // 숫자는 0으로 초기화
        ar[0] = 20;
        ar[1] = 30;
        // 배열의 순회
        for (int temp : ar) {
            System.out.printf("%5d", temp);
        }

        try {
            // NullPointException 발생
            int[] br = null;
            System.out.println(br[0]);
        } catch (Exception e) {
        }

        // IndexOutOfBoundsException 발생
        // System.out.println(soccers[20]);
        System.out.println("종료");

        // 2차원 배열 생성
        String[][] programmers = {{"규민", "현우", "천우", "민규"}, {"해은", "정엽", "용수", "지덕"}};
        System.out.println(programmers.length); // 행의 개수
        System.out.println(programmers[0].length); // 열의 개수

        for (String[] cr : programmers) {
            for (String designer : cr) {
                System.out.print(designer);
            }
            System.out.println();
        }

        String[] actors = {"병헌", "중기", "강호"};
        for (int i = 0; i < actors.length; i++) {
            System.out.print(actors[i] + "\t");
            if (i % 3 == 2) {
                System.out.println();
            }
        }

        String[] dr = {"규민", "Gyuminn", "gyumin"};
        // 위의 배열의 내용을 가지고 데이터를 1개 추가한 배열을 생성
        // 직접 복제
        String[] er = new String[dr.length + 1];
        // 배열의 요소 복제
        for (int i = 0; i < dr.length; i++) {
            er[i] = dr[i];
        }
        System.out.println(Arrays.toString(er));

        // 배열을 복제
        String[] fr = Arrays.copyOf(dr, dr.length + 1);
        fr[3] = "김규민짱";
        // 배열의 요소들을 하나의 문자열로 생성
        System.out.println(Arrays.toString(fr));

        // 문자열을 올므차순 정렬
        Arrays.sort(fr);
        System.out.println(Arrays.toString(fr));

        // 선택 정렬
        int[] data = {1, 5, 3, 2, 4};
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (data[i] > data[j]) {
                    int temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(data));
    }
}
