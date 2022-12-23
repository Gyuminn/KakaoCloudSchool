package kakao.itstudy.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.Buffer;
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

        /*
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
        */

        // 버퍼 단위로 기록
        try (PrintStream ps = new PrintStream(new FileOutputStream("./sample2.txt", true))) {
            String msg = "안녕하세요 반갑습니다. 김규민입니다.";
            // write는 바이트 단위 기록
            ps.write(msg.getBytes());
            // print는 문자열을 스스로 바이트로 변환해서 기록
            ps.print(msg);
            ps.flush();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/Users/gimgyumin/Documents/kakaoCloudSchool/back-end-java/javabasic/sample2.txt"))) {
            // 파일의 용량이 크면 한 번에 받아오면 터질 수 있다.
            // 나누어서 읽기 - 웹에서 파일 다운로드 받을 떄 사용
            while (true) {
                // 16바이트 단위로 읽어오기
                // 일반적으로 128 또는 128의 배수를 많이 이용
                byte[] b = new byte[1024];
                int len = bis.read(b, 0, b.length);
                if (len < 0) {
                    break;
                }

                // 받을 내용을 가지고 작업
                // 다운로드라면 파일에 기록
                // 문자열이라면 하나로 모아서 읽어야 함.
                System.out.println((new String(b)).trim());
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
