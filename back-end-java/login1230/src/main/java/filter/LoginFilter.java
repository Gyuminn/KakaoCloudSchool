package filter;

import dto.MemberDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MemberService;
import service.MemberServiceImpl;

import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    private MemberService memberService;
    public LoginFilter() {
        super();
        memberService = MemberServiceImpl.getInstance();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // request와 response의 형 변환이 필요하다
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Login 요청이 오면
        if (req.getRequestURI().equals("/login")) {
            // 쿠키들을 읽은뒤 해당하는 쿠키가 존재하면 쿠키의 값으로 로그인한다.
            Cookie[] cookies = req.getCookies();
            for(Cookie cookie: cookies) {
                if(cookie.getName().equals("remember-me")) {
                    System.out.println("1231");
                    String uuid = cookie.getValue();
                    MemberDTO dto = memberService.login(uuid);
                    System.out.println(dto);
                    req.getSession().setAttribute("logininfo", dto);
                    // 메인 페이지로 리다이렉트
                    res.sendRedirect("./");
                    // 꼭 리턴해주어야 한다.
                    // 이제 한 번 로그인하면 이틀동안 로그인 안해도 됨.(설정값을 이틀로 했기 때문에)
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}
