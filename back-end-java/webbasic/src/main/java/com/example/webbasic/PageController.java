package com.example.webbasic;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "PageController", value = "/process.jsp")
public class PageController extends HttpServlet {
    // 서비스에 대한 참조 변수
    private PageService pageService;

    public PageController() {
        // 생성자에서 서비스 생성 - 나중에는 주입을 받음
        pageService = new PageServiceImpl();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 파라미터 읽기
        String first = request.getParameter("first");
        String second = request.getParameter("second");

        // 서비스 메서드 호출
        // 파라미너트이 자료형 변환은 Service에서 수행해도 된다.
        // Spring은 일반적으로 Controller에서 한다.
        int result = pageService.add(Integer.parseInt(first), Integer.parseInt(second));

        // 결과를 저장
        request.setAttribute("result", result);
        // 세션이 여기서는 없기 때문에 만들어서 써야한다.
        request.getSession().setAttribute("result", result);
        request.getServletContext().setAttribute("result", result);

        // 뷰 페이지 결정하고 데이터를 전달
        response.sendRedirect("output.jsp");

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
