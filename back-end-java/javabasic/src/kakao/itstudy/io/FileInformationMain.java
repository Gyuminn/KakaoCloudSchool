package kakao.itstudy.io;

import java.io.File;
import java.util.Date;

public class FileInformationMain {
    public static void main(String[] args) {
        // File 객체 생성
        File f = new File("/Users/gimgyumin/Documents/kakaoCloudSchool/back-end-java/javabasic");

        // 파일의 존재 여부
        if (f.exists() == true) {
            System.out.println("파일의 크기: " + f.length());
            System.out.println("마지막 수정 날짜: " + f.lastModified());
            Date date = new Date(f.lastModified());
            System.out.println("마지막 수정 날짜: " + date);
        } else {
            System.out.println("파일이 존재하지 않습니다");
        }

    }
}
