package kakao.itstudy.oop;

public class Data {

    private static int sequence;

    static {
        sequence = 0;
    }

    private int num;
    private String name;

    public Data() {
        super();
        // sequence를 num에 대입하고 1 증가
        num = ++sequence;
    }

    public Data(String name) {
        this.num = ++sequence;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Data{" +
                "num=" + num +
                ", name='" + name + '\'' +
                '}';
    }
}
