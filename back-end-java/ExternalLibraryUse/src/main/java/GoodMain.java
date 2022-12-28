public class GoodMain {
    public static void main(String[] args) {
        // 싱글톤 확인
        GoodDAO dao = GoodDAOImpl.getInstance();
        System.out.println(dao.getAll());

        // 기본 키를 가지고 조회하면 존재하는 경우는 데이터가 리턴되고
        // 없는 경우에는 null이 리턴된다.
        System.out.println(dao.getGood("1"));
        System.out.println(dao.getGood("20"));

        Good good = new Good();
        good.setCode("10");
        good.setName("과자");
        good.setManufacture("서울");
        good.setPrice(50);

        int r = dao.insertGood(good);
        if (r == 1) {
            System.out.println("삽입 성공");
        } else {
            System.out.println("삽입 실패");
        }
    }
}
