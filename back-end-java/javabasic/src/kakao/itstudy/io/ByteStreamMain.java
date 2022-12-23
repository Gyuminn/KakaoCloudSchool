package kakao.itstudy.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class ByteStreamMain {
    public static void main(String[] args) {
        /*
        try (FileOutputStream fos = new FileOutputStream("./sample.txt", true)) {
            // 파일에 기록할 내용 만들기
            String msg = "Hello Java ";
            fos.write(msg.getBytes());
            fos.flush();
        } catch (Exception e) {
            // 이 메시지를 파일에도 적고
            // 어딘가로 쏘아라.
            // 로그분석의 기초임.
            System.out.println(e.getLocalizedMessage());
        }
        */

        try (FileInputStream fis = new FileInputStream("/Users/gimgyumin/Documents/kakaoCloudSchool/back-end-java/javabasic/sample.txt")) {
            while(true) {
                // 읽을 수 있는 크기로 바이트 배열 생성
                byte [] b = new byte[fis.available()];
                int len = fis.read(b);
                if(len <= 0) {
                    System.out.println("읽은 데이터 없음");
                    break;
                } else {
                    // 숫자 배열 출력 - 텍스트가 아닌 경우
                    System.out.println(Arrays.toString(b));
                    // 문자열로 변환해서 출력
                    System.out.println(new String(b));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
