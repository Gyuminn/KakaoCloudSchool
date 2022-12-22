package kakao.itstudy.thread;

// Thread 클래스로부터 상속받는 클래스
class ThreadEx extends Thread {
    @Override
    public void run() {
        // 1초마다 스레드 이름을 10번 출력
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.out.println(getName());
            } catch (Exception e) {

            }
        }
    }
}

public class ThreadCreateMain {
    public static void main(String[] args) {
        // 클래스를 상속받은 경우
        // 대부분의 경우는 변수를 만들 때는 상위 클래스 이름을 사용
        // ThreadEx th1 = new ThreadEx();
        Thread th1 = new ThreadEx();
        th1.start();

        // class로 만든 ThreadEx는 계속 메모리에 상주하게 된다.
        // 한 번만 사용하고 싶기 때문에 anonymous 클래스를 사용해보자.
        Runnable r = new Runnable() {
            @Override
            public void run() {
                // 1초마다 스레드 이름을 10번 출력
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(i);
                    } catch (Exception e) {

                    }
                }
            }
        };
        Thread th2 = new Thread(r);
        th2.start();
    }
}