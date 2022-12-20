package kakao.itstudy.java.lang;

public class Main {
    public static void main(String[] args) {
        String[] names = {"gyumin", "Gyuminn", "gimgyumin"};
        // 인스턴스 생성
        Data original = new Data(1, "itstudy", names);

        // 인스턴스의 참조 복사
        // 참조 대상이나 원본이 내부 데이터를 변경하몉 영향을 준다.
        Data data = original;
        System.out.println(original);
        // 참조를 복사한 경우는 data의 변경이 곧 original의 변경이 된다.
        data.setNum(2);
        System.out.println(original);

        // 자바는 복제를 이용하고자 하는 경우 clone 메서드를 재정의해야한다.
        // 재정의할 때 Cloneable 인터페이스를 implements 하기를 권장한다.

        Data copy = original.clone();
        System.out.println(original);
        copy.setNum(3);
        System.out.println(original);
        String[] copyNicknames = copy.getNicknames();
        copyNicknames[0] = "규민";
        System.out.println(original);

    }
}
