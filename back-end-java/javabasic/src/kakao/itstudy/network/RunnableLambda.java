package kakao.itstudy.network;

public class RunnableLambda {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            // Runnable 인터페이스를 이용한 스레드 생성
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("일반적인 방식");
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
            }
        }) {
        }.start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("람다를 이용한 방식");
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
