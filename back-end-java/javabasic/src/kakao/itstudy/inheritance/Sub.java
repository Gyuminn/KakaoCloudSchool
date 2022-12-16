package kakao.itstudy.inheritance;

public class Sub extends Super {
    public Sub() {
        // System.out.println("상위 클래스의 생성자 호출 전 수행");
        // 상위 클래스의 생성자를 호출
        super(1, "gyumin");
        System.out.println("상위 클래스의 생성자 호출 뒤 수행");
    }

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        // 상위 클래스의 toString()을 호출하고자 하는 경우에는
        // 하위 클래스에도 toString()이 존재하므로
        // super.을 추가해야 한다. (아니면 무한루프걸린다.)
        return super.toString() + "Sub{" +
                "nickname='" + nickname + '\'' +
                '}';
    }
}
