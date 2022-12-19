package kakao.itstudy.nestedclass;

public class Singleton {
    // 외부에서 직접 인스턴스 생성을 못하도록 생성자의 접근 지정자 변경
    private Singleton() {

    }

    // 하나의 인스턴스 참조를 저장하기 위한 속성(변수)을 생성
    private static Singleton singleton;

    // 인스턴스의 참조를 리턴하는 메서드
    public static Singleton sharedInstance() {
        if(singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
