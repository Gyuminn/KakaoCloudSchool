package com.example.webbasic;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebListener
public class ApplicationListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    // 접속자 수를 저장
    private static int count;

    public static int getCount() {
        return count;
    }

    public ApplicationListener() {
    }

    // 웹 서버가 구동될 때 호출되는 메서드
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        System.out.println("웹 서버 시작");
    }

    // 웹 서버가 종료될 때 호출되는 메서드
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
        System.out.println("웹 서버 종료");
    }

    // 세션이 만들어 질 떄 - 새로운 접속이 온 경우
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        count++;
        System.out.println("접속자 수: " + count);

        HttpSession session = se.getSession();
        System.out.println("세션 아이디: " + session.getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}
