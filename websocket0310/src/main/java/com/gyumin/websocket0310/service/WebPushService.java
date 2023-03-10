package com.gyumin.websocket0310.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.Random;

@Service
public class WebPushService {
    public void push(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter pw = null;
        try {
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");
            pw = response.getWriter();
            Random random = new Random();
            pw.write("data: " + (random.nextInt(46) + 1) + "\n\n");
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            pw.close();
        }
    }
}
