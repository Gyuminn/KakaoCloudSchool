package kakao.itstudy.controlstatement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ConsoleInput {
    public static void main(String[] args) {
        // BufferedReader를 이용한 숫자와 문자열 입력받기
        /*
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));) {
            System.out.print("이름을 입력하세요:");
            // 한 줄의 문자열 입력받기
            String str = br.readLine();
            System.out.println(str);
            System.out.print("나이를 입력하세요:");
            String age = br.readLine();
            // age라는 문자열을 정수로 변환
            int nai = Integer.parseInt(age);
            System.out.println(nai);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
        */
        try (Scanner sc = new Scanner(System.in);) {
            System.out.print("나이를 입력하세요: ");
            int age = sc.nextInt();

            System.out.print("이름을 입력하세요: ");
            // 숫자를 입력받고 문자열을 입력받는 경우에는
            // 앞에 입력한 ENTER를 제거하기 위해서 한 번 더 호출
            sc.nextLine();
            String name = sc.nextLine();

            System.out.println(name + ":" + age);
        } catch(Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
