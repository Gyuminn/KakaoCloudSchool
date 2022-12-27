import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NoLibraryCSVMain {
    public static void main(String[] args) {
        // 문자열 파일을 읽기 위한 스트림 생성
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("./volley.csv")))) {
            // 파일의 경로 확인을 위해서 작성
            System.out.println(br);

            boolean flag = false;
            // 파싱한 결과를 저장하기 위한 List
            List<Player> list = new ArrayList<>();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                // 첫 줄은 데이터가 아니므로 배제하기 위해 작성
                if (flag == false) {
                    flag = true;
                    continue;
                }
                // System.out.println(line);
                // , 단위로 분할
                String[] ar = line.split(",");
                System.out.println(ar[0]);
            }
        } catch (
                Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}


