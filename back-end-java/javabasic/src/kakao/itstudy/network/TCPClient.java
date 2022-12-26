package kakao.itstudy.network;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    public static void main(String[] args) {
        try {
            // 접속할 컴퓨터의 IP 정보를 가져오기
            InetAddress addr = InetAddress.getByName("192.168.0.143");
            Scanner sc = new Scanner(System.in);
            while (true) {
                // 9999번 포트로 연결
                Socket socket = new Socket(addr, 9999);
                // 문자열을 전송하기 위한 스트림
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                System.out.print("보낼 메시지: ");
                String msg = sc.nextLine();
                pw.println(msg);
                pw.flush();
                pw.close();
                socket.close();
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
