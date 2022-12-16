package kakao.itstudy.inheritance;

public class InheritanceMain {
    public static void main(String[] args) {
        Sub sub = new Sub();

        // Sub 클래스에 만들지 않았던 setNum과 setName 사용 가능.
        sub.setNum(1);
        sub.setName("kimgyumin");
        sub.setNickname("Gyuminn");

        System.out.println(sub);

        Super s1 = new Super();
        Sub s2 = new Sub();

        // 상위 클래스 타입의 참조형 변수에는
        // 하위 클래스 타입의 인스턴스 참조를 바로 대입할 수 있다.
        Super s3 = new Sub();

        // 하위 클래스 타입의 참조형 변수에는
        // 상위 클래스 타입의 인스턴스 참조를 대입할 수 없다.
        // 강제 형 변환을 하면 가능.
        // Sub s4 = new Super();

        Sub s4 = (Sub) s3;
        // s3에 대입된 인스턴스는 원래 Sub 타입이어서 문제 없음.

        // Sub s5 = (Sub)(new Super());
        // 원래 타입이 Super이기 때문에 예외 발생
        // 매우 중요하다!!!!! 원래 타입이 무엇이었는지 기억해야 한다. 무조건 외워
        // List 는 new ArrayList로 만들고 타입이 다른데
        // 이는 ArrayList가 List를 implements했다는 것을 의미한다.
        // 보여지는 것이 최상위이다.

    }
}
