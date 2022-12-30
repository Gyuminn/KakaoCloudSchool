package com.example.web1230;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebFilter(filterName = "LogFilter")
public class LogFilter implements Filter {
    // 메모리 할당 후 처음 사용될 때 호출되는 메서드
    public void init(FilterConfig config) throws ServletException {
    }

    // 필터가 파괴될 때 호출되는 메서드
    public void destroy() {
    }

    // URL에 해당하는 요청이 왔을 때 호출되는 메서드
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 여기에 작성하면 Controller가 처리하기 전에 수행
        System.out.println("처리하기 전");
        chain.doFilter(request, response);

        // Controller가 처리한 후
        System.out.println("처리한 후");
    }
}
