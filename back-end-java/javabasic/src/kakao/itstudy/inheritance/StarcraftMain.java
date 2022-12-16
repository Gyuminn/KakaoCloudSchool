package kakao.itstudy.inheritance;

public class StarcraftMain {
    public static void main(String[] args) {
        // Protoss의 공격 호출
        Starcraft star = new Protoss();
        star.attack();

        // Zerg 공격 호출
        star= new Zerg();
        star.attack();

        star = new Terran();
        star.attack();
    }
}
