package kakao.itstudy.thread;

public class ThreadAndProcess {
    public static void main(String[] args) {
        // 스레드를 사용하지 않은 경우
        /*
        new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(i);
                    } catch (Exception e) {

                    }
                }
            }
        }.run();

         */

        // 스레드를 사용하는 경우
        new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(i);
                    } catch (Exception e) {

                    }
                }
            }
        }.start();

        new Thread() {
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(i);
                    } catch (Exception e) {

                    }
                }
            }
        }.start();

    }
}
