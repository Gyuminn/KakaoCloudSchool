package kakao.itstudy.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CharacterStreamMain {
    public static void main(String[] args) {
        // 오늘 날짜로 파일명 생성: 2022-12-23.log
        // 디렉토리 경로 생성
        String directory = "/Users/gimgyumin/Documents/kakaoCloudSchool/back-end-java/javabasic/";

        // 오늘 날짜 문자열 만들기
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filename = sdf.format(date);

        String path = String.format("%s%s%s", directory, filename, ".log");
        // 문자 단위로 버퍼를 이용해서 기록
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            pw.println("안녕하세요");
            pw.println("반갑습니다");
            pw.println("1234");
            pw.flush();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            while(true) {
                String str = br.readLine();
                if(str == null) {
                    break;
                }
                System.out.println(str);
                // 숫자가 아닌 문자열을 숫자로 변환하면
                // NumberFormatException 발생
                // Web에서 파라미터는 무조건 문자열
                // 파라미터를 숫자로 변환하는 경우 발생할 수 있는 예외
                System.out.println(Integer.parseInt(str));
            }
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}

