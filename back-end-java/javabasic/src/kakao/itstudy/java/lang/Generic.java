package kakao.itstudy.java.lang;

// 미지정 자료형의 이름은 한 글자로 하는 것이 관례
public class Generic<T> {
    private T[] data;

    // ...은 데이터 개수에 상관없이 매개변수로 받아서
    // 배열로 만들어주는 문법 - varargs
    public Generic(T... n) {
        data = n;
    }

    // 배열의 데이터를 출력해주는 메서드
    public void disp() {
        for(T temp: data) {
            System.out.println(temp);
        }
    }
}
