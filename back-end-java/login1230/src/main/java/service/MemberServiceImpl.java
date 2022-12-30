package service;

import domain.MemberVO;
import dto.MemberDTO;
import persistence.MemberDAO;

public class MemberServiceImpl implements MemberService {
    // DAO 변수(외부로부터 주입받아야 함)
    private MemberDAO memberDAO;

    private MemberServiceImpl() {
        // 지금은 주입받을 수 없으니 일단 생성자에서 만들어준다.
        memberDAO = MemberDAO.getInstance();
    }

    private static MemberService service;

    public static MemberService getInstance() {
        if (service == null) {
            service = new MemberServiceImpl();
        }
        return service;
    }

    @Override
    public MemberDTO login(String mid, String mpw, String uuid) {
        MemberDTO dto = null;
        MemberVO vo = memberDAO.login(mid, mpw);

        // vo를 dto로 변환
        if (vo != null) {
            dto = new MemberDTO();
            // 비밀번호는 반환할 필요가 없다.
            dto.setMid(vo.getMid());
            dto.setMname(vo.getMname());
            // UUID 업데이트
            memberDAO.updateUUID(mid, uuid);
        }

        return dto;
    }

    @Override
    public MemberDTO login(String uuid) {
        MemberDTO dto = null;

        MemberVO vo = memberDAO.login(uuid);
        if(vo != null) {
            dto = new MemberDTO();
            dto.setMid(vo.getMid());
            dto.setMname(vo.getMname());
        }
        return dto;
    }
}
