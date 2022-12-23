package kakao.itstudy.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesMain {
    public static void main(String[] args) {
        // 현재 디렉토리 위치
        // File f = new File("./");
        // System.out.println(f.getAbsolutePath());

        File file = new File("config.properties");
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties properties = new Properties();
            // 메모리에 로드
            // 보통 로드까지는 Spring이 해줌.
            properties.load(fis);
            // 읽어오기
            System.out.println(properties.getProperty("url"));
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
