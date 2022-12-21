package kakao.itstudy.java.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// 데이터 클래스 - VO2
class VO2 {
    private int num;
    private String name;

    public VO2(int num, String name) {
        super();
        this.num = num;
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "VO{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}

public class MapMain {
    public static void main(String[] args) {
        // 데이터 생성 - 모델
        VO2 vo2 = new VO2(1, "gyumin");

        // 데이터 출력 - View
        // 모델의 근원이 되는 VO 클래스 안에 속성 이름을 변경하면
        // View도 수정이 되어야 한다.
        // 관계형 데이터베이스는 VO 클래스를 활용
        /*
        System.out.println("번호: " + vo2.getNum());
        System.out.println("이름: " + vo2.getName());


        // VO2 클래스의 인스턴스 역할을 수행하는 Map을 생성
        Map<String, Object> map = new HashMap<>();
        // 데이터 저장
        map.put("번호", 1);
        map.put("이름", "gyumin");

        // map의 모든 키를 가져와서 출력
        Set<String> keys = map.keySet();
        for(String key: keys) {
            System.out.println(key + ": " + map.get(key));
        }
         */

        // 배열의 배열 - 2차원 배열 : Matrix - 태그가 없음
        String[] team1 = {"김규민", "천규민", "이규민", "박규민", "나규민"};
        String[] team2 = {"박지성", "손흥민"};
        // 2차원 배열 생성
        String[][] volley = {team1, team2};


        int i = 0;
        for (String[] temp : volley) {
            if (i == 0) {
                System.out.println("team1: ");
            } else {
                System.out.println("team2: ");
            }
            i++;
            for (String imsi : temp) {
                System.out.println(imsi + "\t");
            }
            System.out.println();
        }

        // 2차원 배열 대신에 Map의 배열 사용 - Table
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "k-team");
        map1.put("team", team1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "world-class");
        map2.put("team", team2);

        Map[] v = {map1, map2};
        for(Map map: v) {
            System.out.println(map.get("name") + "\t");
            String [] temp = (String[])map.get("team");
            for(String imsi: temp) {
                System.out.println(imsi + "\t");
            }
            System.out.println();
        }
    }
}
