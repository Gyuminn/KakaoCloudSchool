public class GoodMain {
    public static void main(String[] args) {
        // 싱글톤 확인
        GoodDAO dao = GoodDAOImpl.getInstance();
        System.out.println(dao.getAll());

        System.out.println(dao.getGood("1"));
        System.out.println(dao.getGood("20"));
    }
}
