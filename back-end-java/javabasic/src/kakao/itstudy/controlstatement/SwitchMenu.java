package kakao.itstudy.controlstatement;

import java.util.Scanner;

public class SwitchMenu {
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            final int CHINA = 1;
            System.out.println("메뉴 입력(1 ~ 3)");
            int menu = sc.nextInt();
            switch(menu) {
                case CHINA:
                    System.out.println("중식");
                    break;
                case 2:
                    System.out.println("한식");
                    break;
                case 3:
                    System.out.println("분식");
                    break;
                default:
                    System.out.println("잘못된 입력입니다");
            }
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
     }
}
