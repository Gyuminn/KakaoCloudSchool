package kakao.itstudy.exceptionhandling;

import java.util.Arrays;

public class DebuggingMain {
    public static void test(){
        int x = 10;
        int [] ar = {30, 20, 30, 50};
        x = 30;
        System.out.println(x);
        ar[2] = 90;
        System.out.println(Arrays.toString(ar));
    }
    public static void main(String[] args) {
        test();
    }
}
