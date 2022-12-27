import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SuperCSVMain {
    public static void main(String[] args) {
        // 저장할 List 생성
        List<Player> list = new ArrayList<>();
        // 읽을 파일의 경로 생성
        Path path = Paths.get("./volley.csv");
        // 제약조건 설정
        // super csv 라이브러리를 직접 추가해야 함. 다운받지 않은 상태.

        /*
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(),
                new ParseInt(new NotNull()),
                new ParseDate("yyyy-MM-dd"),
                new Optional(),
                new Optional()
        };

        // Csv 읽기 위한 경로 생성
        try (ICsvBeanReader beanReader = new CsvBeanReader(Files.newBufferedReader(path), CsvPreference.STANDARD_PREFERENCE)) {
            // 첫줄 읽어오기
            String[] header = beanReader.getHeader(true);
            System.out.println(Arrays.toString(header));

            // 데이터 읽어서 list에 추가
            Player player1 - null;
            // 한 줄씩 읽어서 header에 맞추어 Player 클래스 타입의
            // 인스턴스를 생성
            while((player1 = beanReader.read(Player.class, header, processors)) != null) {
                list.add(player1);
            }
            System.out.println(list);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

         */
    }
}
