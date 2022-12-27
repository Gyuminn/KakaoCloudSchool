import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Arrays;

public class JSONCreate {
    public static void main(String[] args) {
        ToDoVo vo1 = new ToDoVo(1, 1, "한글", true);
        ToDoVo vo2 = new ToDoVo(2, 11, "영어", false);

        // 저장할 JSON 파일 생성
        File file = new File("./todo.json");
        // JSON 파일 기록을 위한 인스턴스 생성
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 기록
            mapper.writeValue(file, Arrays.asList(vo1, vo2));
        } catch (Exception e) {
            System.out.println("기록 실패");
        }
    }
}
