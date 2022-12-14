package kakao.itstudy.controlstatement;

import java.util.Scanner;

public class PassIf {
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            System.out.println("아이디를 입력하세요: ");
            String id = sc.nextLine();
            System.out.println("비밀번호를 입력하세요: ");
            String password = sc.nextLine();

            // 문자열은 생성 방법에 따라 다른 인스턴스가 될 수 있어서
            // 값이 동일한 지 비교할 때 == 를 사용하면 안되고
            // equals 메서드를 이용해서 동일성 여부를 비교
            if(id.equals("root")  && password.equals("1234")) {
                System.out.println("로그인 성공");
            } else {
                System.out.println("로그인 실패");
            }
        } catch(Exception e)  {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
