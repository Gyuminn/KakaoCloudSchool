package kakao.itstudy.inheritance;

// SuperClass 상속받는 SubClass
public class SubClass extends SuperClass {
    public void subMethod() {
        System.out.println("SubClass 만의 메서드");
    }

    // 메서드 오버라이딩 - 상위 클래스에 존재하는 메서드를 다시 정의
    @Override
    public void display() {
        System.out.println("하위 클래스의 메서드");
    }

}
