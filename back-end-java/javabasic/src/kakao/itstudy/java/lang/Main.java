package kakao.itstudy.java.lang;

import java.math.BigDecimal;

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


        Data data1 = new Data(1, "구름", null);
        Data data2 = new Data(1, "구름", null);
        // == 는 참조를 비교하는 연산자
        // 2개의 인스턴스는 각각 생성자를 호출해서 만들었기 때문에 참조가 다름.
        // 내용이 같은지 확인하고자 할 때는 equals 메서드를 재정의해서 사용
        System.out.println(data1 == data2);
        System.out.println(data1.equals(data2));

        // Wrapper 클래스 사용
        Double d = new Double(13.67);
        // 기본형을 대입하면 new Double(17.23)으로 변환
        // toString이 재정의되어 있으므로 출력은 참조로 가능
        d = 17.23;
        System.out.println(d);
        // 기본형 데이터로 변환
        double x = d;
        System.out.println(x);

        double d1 = 1.6000000000000000;
        double d2 = 0.1000000000000000;
        System.out.println(d1 + d2); // 1.7이 아님

        // 정확한 산술 연산
        // BigDecial로 데이터를 만들고 연산을 수행하는 메서드를
        // 호출하면 정확한 결과를 만들 수 있음.
        BigDecimal b1 = new BigDecimal("1.6000000000000000");
        BigDecimal b2 = new BigDecimal("0.1000000000000000");
        System.out.println(b1.add(b2));

        String str = "Hello";
        System.out.println(System.identityHashCode(str));
        // String은 데이터 수정이 안되기 때문에
        // 새로운 공간에 기존의 문자열을 복사한 후 작업을 수행하고 그 참조를 다시 리턴
        // 기존의 데이터가 저장된 공간은 메모리 낭비가 될 수 있음.
        str += "Java";
        System.out.println(System.identityHashCode(str));

        // 변경 가능한 문자열을 저장
        StringBuilder sb = new StringBuilder("Hello");
        System.out.println(System.identityHashCode(sb));
        // 문자열을 추가하면 현재 저장된 공간에 이어붙이기를 수행
        sb.append("Java");
        // 해시코드가 이전 데이터와 동일
        System.out.println(System.identityHashCode(sb));
    }
}
