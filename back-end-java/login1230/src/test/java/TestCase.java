import org.junit.jupiter.api.Test;
import persistence.MemberDAO;
import service.MemberService;
import service.MemberServiceImpl;

public class TestCase {
    @Test
    public void daoTest() {
        MemberDAO dao = MemberDAO.getInstance();
        System.out.println(dao.login("rhkdtlrtm12", "1111")); // id와 pw가 맞아서 데이터 출력
        System.out.println(dao.login("rhkdtlrtm12", "1234")); // pw가 틀려서 null
        System.out.println(dao.login("rhkdtlrtm13", "1111")); // id가 틀려서 null
    }

    @Test
    public void serviceTest() {
        MemberService service = MemberServiceImpl.getInstance();
        System.out.println(service.login("rhkdtlrtm12", "1111"));
        System.out.println(service.login("rhkdtlrtm13", "1111"));
    }
}
