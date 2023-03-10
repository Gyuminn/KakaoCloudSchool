package com.gyumin.websocket0310.service;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
// chat이라는 URL을 처리할 수 있는 WebSocket 클래스
@ServerEndpoint(value = "/chat")
public class WebSocketChat {
    // 접속한 클라이언트(Session) 목록을 저장할 Collections
    // Collections.synchronizedSet를 이용하면 동기적으로 만들 수 있다. 여러 곳에서 건드는 것을 방지
    // 하나만 만들면 되므로 static을 붙여주자.
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    // 여러 유저 중 한 명에게 귓말 기능을 줄 수 있는 경우
    // 로그인이 없어서 이렇게 하긴 했는데 중요한 건 이러한 구조가 특정 유저를 찾기에 빠르다는 것이다.
    private Map<String, Session> users = new HashMap<>();

    // 클라이언트가 접속했을 때
    @OnOpen
    public void onOpen(Session s) {
        // 클라이언트 정보 출력
        System.out.println("접속: " + s.toString());

        // 존재하는 Session인지 확인
        if (!clients.contains(s)) {
            clients.add(s);
            System.out.println("유저 접속");
        } else {
            System.out.println("이미 접속된 유저");
        }
    }

    // 클라이언트가 메시지를 전송했을 때
    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{
        // 여기서 DB에 저장할 수도 있음.
        System.out.println("받은 메시지: " + msg);

        // 브로드캐스트 통신
        for (Session s : clients) {
            // 클라이언트에게 문자열 메시지를 전송
            s.getBasicRemote().sendText(msg);
        }
    }

    // 클라이언트가 접속을 해제할 때
    @OnClose
    public void onClose(Session s) {
        System.out.println("접속 종료: " + s.toString());
        clients.remove(s);
    }
}
