package controller;

import dto.MemberDTO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import service.MemberService;
import service.MemberServiceImpl;

import java.io.IOException;

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {

    // 일단은 생성자에서 주입해주도록 하자
    private MemberService memberService;

    public LoginController() {
        super();
        memberService = MemberServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 이동 처리 해주는데 보통 포워딩이다.
        // webapp 디렉토리의 member 디렉토리의 login.jsp로 포워딩
        request.getRequestDispatcher("/member/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 로그인 처리
        // 파라미터 가져오기
        String mid = request.getParameter("mid");
        String mpw = request.getParameter("mpw");

        // 서비스 메서드 호출
        MemberDTO dto = memberService.login(mid, mpw);

        // 결과를 가지고 분기
        // 세션부터 찾아오기
        HttpSession session = request.getSession();
        if(dto == null) {
            // 로그인 실패했을 때
            session.invalidate();
            // 로그인 페이지로 되돌아가기
            response.sendRedirect("login?error=error");
        } else {
            // 로그인 성공했을 때
            session.setAttribute("logininfo", dto);
            // 메인 페이지로 리다이렉트
            response.sendRedirect("./");
        }
    }
}
