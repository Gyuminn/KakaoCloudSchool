package persistence;

import domain.MemberVO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {
    // 싱글톤 패턴을 위한 코드 - Spring에서는 필요 없음
    private MemberDAO() {
    }

    private static MemberDAO dao;

    public static MemberDAO getInstance() {
        if (dao == null) {
            dao = new MemberDAO();
        }
        return dao;
    }

    // 데이터베이스 접속을 위한 드라이버 로드 코드
    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("드라이버 로드 성공");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    // 데이터베이스 사용을 위한 속성
    private Connection connection;
    private PreparedStatement pstmt;
    private ResultSet rs;

    // 데이터베이스 연결
    {
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/login", "root", "root");
            System.out.println("데이터베이스 접속 성공");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    // 로그인 처리를 위한 메서드
    // 아이디와 비밀번호를 받아서 처리한 후 회원 정보를 리턴
    public MemberVO login(String mid, String mpw) {
        MemberVO vo = null;
        try {
            // 수행할 SQL을 생성
            String sql = "select * from tbl_member where mid=? and mpw=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, mid);
            pstmt.setString(2, mpw);
            // SQL 실행
            rs = pstmt.executeQuery();

            if (rs.next()) {
                vo = new MemberVO();
                // 비밀번호는 세션에 저장할 필요없기 때문에 제외한다.
                vo.setMid(rs.getString("mid"));
                vo.setMname(rs.getString("mname"));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return vo;
    }

    // uuid를 가지고 로그인하는 메서드
    public MemberVO login(String uuid) {
        MemberVO vo = null;
        try {
            String sql = "select * from tbl_member where uuid=?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, uuid);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                vo = new MemberVO();
                vo.setMid(rs.getString("mid"));
                vo.setMname(rs.getString("mname"));

            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return vo;
    }

    // uuid를 업데이트 하는 메서드: 로그인 성공 시 호출되는 메서드
    public void updateUUID(String mid, String uuid) {
        try {
            String query = "update tbl_member set uuid=? "
                    + "where mid=?";
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, uuid);
            pstmt.setString(2, mid);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
