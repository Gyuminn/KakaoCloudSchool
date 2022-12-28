import java.util.List;

// goods 테이블에 수행할 데이터베이스 작업의 원형을 소유할 인터페이스
public interface GoodDAO {
    // goods 테이블의 전체 데이터 가져오기
    public List<Good> getAll();

    // goods 테이블에서 code를 가지고 데이터를 조회하기
    public Good getGood(String code);
}
