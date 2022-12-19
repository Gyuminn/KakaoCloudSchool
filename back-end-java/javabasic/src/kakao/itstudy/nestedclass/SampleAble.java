package kakao.itstudy.nestedclass;

// 인터페이스 안에 속성을 만들면 final 변수가 된다.
// 인터페이스 안에 메서드를 만들면 abstract method가 된다.
// 인터페이스 안에 메서드를 만들 떄 내용을 추가하고자 하면 default라는 키워드를 추가해야 한다.
// 인터페이스는 인스턴스 생성을 못함. - 변수 생성은 가능하다.
// 클래스에 implements 해서 사용.
public interface SampleAble {
    // 자동으로 추상 메서드가 된다.
    // 추상 메서드는 하위 클래스에서 반드시 재정의를 해야 한다.
    public void method();
}
