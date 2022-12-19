package kakao.itstudy.nestedclass;

public class Table {
    private static int sequence; // 일련번호
    private static int step = 1;

    public static int getSequence() {
        return sequence;
    }

    public static void setSequence(int sequence) {
        Table.sequence = sequence;
    }

    public static int getStep() {
        return step;
    }

    public static void setStep(int step) {
        Table.step = step;
    }

    // 인스턴스가 별도로 소유
    private int num;

    public Table() {
        sequence += step;
        num = sequence;
    }

    public int getNum() {
        return num;
    }
}
