package kakao.itstudy.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        try {
            // 서버 소켓 생성
            ServerSocket ss = new ServerSocket(9999);
            while (true) {
                System.out.println("서버 대기 중");
                // 대기
                Socket socket = ss.accept();
                // 접속한 클라이언트로 정보 확인
                System.out.println(socket);
                // 클라이언트가 전송한 내용 읽기
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 한 줄의 메시지 읽기
                String msg = br.readLine();
                System.out.println(msg);

                br.close();
                socket.close();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
