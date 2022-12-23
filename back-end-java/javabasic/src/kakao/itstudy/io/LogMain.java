package kakao.itstudy.io;

import java.io.BufferedReader;
import java.io.FileReader;

public class LogMain {
    public static void main(String[] args) {
        // 파일 경로 확인
        // File file = new File("log.txt");
        // System.out.println(file.exists());

        // 문자열을 읽기 위한 스트림을 생성
        try (BufferedReader br = new BufferedReader(new FileReader("log.txt"))) {
            // 트래픽의 합계를 구할 변수
            int sum = 0;

            while (true) {
                // 한 줄을 읽어서 읽은게 없으면 종료
                // 읽은 내용이 있으면 출력
                String log = br.readLine();
                if (log == null) {
                    break;
                }
                // System.out.println(log);

                // 공백을 기준으로 분할
                String[] ar = log.split(" ");
                // IP 확인
                // System.out.println(ar[0]);

                // 트래픽 확인
                // System.out.println(ar[ar.length - 1]);

                // 트래픽을 정수로 변환해서 더하기
                try{
                    sum += Integer.parseInt(ar[ar.length - 1]);
                } catch(Exception e) {}
            }
            System.out.println("트래픽 합계: " + sum);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
