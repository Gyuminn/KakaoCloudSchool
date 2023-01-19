# [Java] Network

1. **용어**

   Protocol: 통신을 하기 위한 규칙

   TCP/IP: 인터넷 프로토콜

   HTTP: Hyper Text Transfer Protocol

   HTTPS: Hyper Text Transfer Protocol over Secure Socket Layer

   FTP: 파일 전송 프로토콜

   TELNET: 원격 접속

   1. IP
      - 인터넷 프로토콜
      - IPv4
        - 32비트 주소 체계로 0.0.0.0 ~ 255.255.255.255
        - 사설 IP 대역(내부 망에서 구분하기 위해 설정하는 대역)
          10.0.0.0 ~ 10.255.255.255,
          172.16.0.0 ~ 172.31.255.255,
          168.0.0 ~ 192.168.255.255
      - IPv6
        - 128비트 주소 체계
        - Loopback
          자기 자신의 컴퓨터의 IP로 Ipv4에서는 127.0.0.1이고(구형 표현 방식) IPv6에서는 0::0::0::0::0::0::0::1
   2. PORT
      - 서비스를 구별하기 위한 번호
      - 하나의 컴퓨터 안에서 Process를 구별하기 위한 번호
      - 0 ~ 65535까지 사용 가능
      - 시스템 예약(0 ~ 1024)
        http(80), https(443), ftp(20, 21), SSH(22), Telnet(23)…
      - 프로그램의 기본 포트
        - Oracle(1521, 8080)
        - Tomcat(8080)
        - MySQl(3306)
        - MariaDB(3306)
        - MSSQL(1443)
        - MongoDB(27017)
   3. DOMAIN

      IP와 PORT에 매핑시킨 문자열 주소

   4. URI(Uniform Resource Identifier)

      인터넷에 있는 자원을 나타내는 유일한 주소

      - URL: 네트워크 상의 자원 식별자
      - URN: 영속적인 식별자

   5. Routing

      최적의 경로를 찾는 것을 의미하는데 웹 프로그래밍에서 URL과 요청방식에 따라 분리해서 요청을 처리하는 것

   6. **Proxy**

      클라이언트가 외부 네트워크에 요청을 하고자 하는 경우 네트워크 내부에서 처리할 수 있도록 해주는 컴퓨터 시스템이나 프로그램

      **React 프로젝트**에서 Node나 Spring Server에 ajax나 fetch API, 또는 axios 라이브러리를 이용해서 데이터를 가져오고자 하면 React에서 설정할 때는 proxy를 package.json에 설정

      **서버 애플리케이션**에서 하고자 할 때는 CORS 설정을 해야 한다.

   7. Firewall

      외부에서 내부 네트워크르ㅗ 접속할 때 제한을 가하는 프로그램이나 하드웨어

      **자신의 컴퓨터를 서버로 사용하고자 하는 경우 Firewall을 해제해야 한다.**

   8. RPC(Remote Procedure Call - 원격 프로시저 호출)

      다른 컴퓨터의 프로시저를 호출하는 것

   9. Open API

      누구나 사용할 수 있도록 공개된 API(라이브러리나

   10. REST API

       소프트웨어 아키텍쳐의 한 형식

       일관성 있는 URL 사용

   11. **ping 명령: echo**

2. **InetAddress**

   컴퓨터의 IP 정보를 저장하는 클래스

   1. 생성 방법

      static method인 getLocalHost(), getByName(호스트이름), getAllByName(호스트이름) 등의 메서드를 사용

   2. 메서드
      - getHostName
      - getAddress
      - getHostAddress
      - toString
   3. IP 확인

      ```java
      try {
      			//자신의 컴퓨터의 IP 정보 확인
      			InetAddress localhost =
      					InetAddress.getLocalHost();
      			System.out.println(localhost);
      			//도메인을 가지고 IP 정보 확인
      			InetAddress [] naver =
      				InetAddress.getAllByName(
      					"www.naver.com");
      			System.out.println(Arrays.toString(naver));

      		}catch(Exception e) {
      			System.out.println(e.getLocalizedMessage());
      			//오늘 날짜로 텍스트 파일을 생성해서 예외를 기록
      		}
      ```

3. **Socket**

   네트워크 어댑터를 추상화한 클래스

   1. 종류

      steramSocket: TCP 통신을 위한 소켓

      DatagramSocet: UDP 통신을 위한 소켓

   2. socket의 생성자
      - Socket()
      - Socket(InetAddress addr, int port)
        상대방의 IP와 port를 기재해서 연결 준비
      - Socket(String addr, int port)
        상대방의 IP와 port를 기재해서 연결 준비
      - Socket(InetAddress addr, int port, InetAddress localAddr, int localhost)
        상대방의 IP와 port를 기재해서 연결 준비.
   3. 메서드

      - close()
      - getInetAddress(), getPort(), getLocalAddress(), getLocalPort()
      - InputStream getInputStream()
      - OutputStream getOutputStream()

      문자 단위로 통신을 하고자 하는 경우에는 스트림을 CharacterStream으로 변환하던지 바이트 단위로 받은 후 new String(바이트 배열[, 인코딩 방식])을 이용해서 변환

4. **StreamSocket**

   TCP 통신을 위한 소켓

   ```java
   package kakao.itstudy.network;

   import java.io.*;
   import java.net.InetAddress;
   import java.net.Socket;

   public class WebTextDownload {
       public static void main(String[] args) {
           try {
               // 카카오 주소 정보 가져오기
               InetAddress ia = InetAddress.getByName("www.kakao.com");

               // 카카오에 연결
               Socket socket = new Socket(ia, 80);
               // 요청을 전송할 수 있는 스트림을 생성
               PrintWriter ps = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

               // 요청 전송
               ps.println("GET http://www.kakao.com");
               ps.flush();

               // 문자 단위로 전송을 받기 위한 스트림
               BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

               // 여러 줄의 문자열을 하나돌 만들기
               StringBuilder sb = new StringBuilder();
               while (true) {
                   String imsi = br.readLine();
                   if (imsi == null) {
                       break;
                   }
                   sb.append(imsi + "\n");
               }
               String html = sb.toString();
               System.out.println(html);

               // 사용한 자원 정리
               br.close();
               ps.close();
               socket.close();
           } catch (Exception e) {
               System.out.println(e.getLocalizedMessage());
               e.printStackTrace();
           }
       }
   }
   ```

5. **TCP 통신**

   ServerSocket - TCP 요청을 받을 수 있는 소켓을 위한 클래스

   1. 생성자
      - ServerSocket()
      - ServerSocket(int port)
        port 번호로 요청을 받을 수 있는 소켓 생성
      - ServerSocket(int port, int backlog)
        port 번호로 요청을 받을 수 있는 소켓을 생성하는데 backlog는 동시에 접속할 수 있는 개수
   2. 메서드
      - Socket accept()
        클라이언트의 접속을 대기하는 메서드로 클라이언트가 접속을 할 때 까지 코드를 블럭시키고 접속을 하면 다음으로 넘어가고 클라이언트와 통신할 수 있는 Socket을 리턴
   3. 통신 과정
      - ServerSocket을 생성해서 accept()를 호출
      - Socket을 생성해서 ServerSocket에 접속

6. **UDP 통신**

   비연결형 통신

   DatagramSocket 클래스를 이용

   1. DatagramSocket 클래스
      - 생성자
        - DatagramSocket()
          데이터를 전송하기 위한 소켓을 생성할 때 사용
        - DatagramSocket(int port)
          데이터를 전송받기 위한 소켓을 생성할 때 사용
        - DatagramSocket(int port, InetAddress addr)
          데이터를 전송받기 위한 소켓을 생성할 때 사용
      - 메서드
        - close()
        - receive(DatagramPacket packet)
          전송 받기
        - send(DatagramPacket packet)
          전송 하기
   2. DatagramPacket 클래스의 생성자
      - DatagramPacket(byte[] buf, int length)
        전송받기 위한 생성자 - 빈 배열을 대입
      - DatagramPacket(byte[] buf, int length, InetAddress addr, int port)
        전송을 하기 위한 생성자 - 내용이 있는 배열을 설정
   3. 데이터를 주고 받기 위한 패킷 클래스의 메서드
      - byte[] getData
        받은 데이터를 리턴
      - int getLength()
        받은 데이터의 길이를 리턴
   4. 문자열과 바이트 배열의 변환

      문자열을 byte 배열로 변환: getBytes([인코딩 방식])

      byte 배열을 문자열로 변환: new String(바이트배열[, 인코딩 방식])

   5. 문자열 전송
      - 문자열을 전송받을 클래스를 생성
      - 문자열을 전송할 클래스를 생성

7. **통신 방식**
   - Unicast
     일대 일로 통신
     상대방의 IP 정보만 있으면 구현 가능
   - Broadcast
     일 대 전체의 통신 - IP 주소와 Subnetmask를 알아야 구현 가능
     Multicast
   - 일 대 그룹의 통신 - 멀티캐스트 대역의 IP를 알아야 한다.
     IPv4에서는 224.0.0.0 ~ 239.255.255.255 대역이 멀티캐스트 대역
   - Anycast
     가까운 것과 통신
8. **URL 통신**

   직접 소켓을 생성해서 통신을 하는 방식을 저수준 통신이라고 부르는데 이 방식은 효율은 좋지만 구현하기 어렵다.

   소켓을 직접 생성하지 않고 라이브러리의 도움을 받아서 통시하는 방법을 고수준 통신이라고 한다. 가장 대표적인 방식이 URL 통신이다.

   최근에는 web에서도 저수준 통신을 할 수 있는 websokcet도 등장

   1. URL 클래스
      - 주소를 생성하기 위한 클래스
      - 생성자
        - URL(String spec)
        - URL(String protocol, String host, int port, String file)
      - 메서드
        각각의 정보를 리턴하는 get 메서드
        연결을 해주는 메서드는 openConncetion인데 이 메서드는 URLConnection 타입의 인스턴스를 리턴.
   2. URLConnection
      - HTTPURLConnection, HttpUrlConnection, JarUrlConnection의 공통된 메서드를 소유한 추상 클래스
      - 강제 형 변환을 해서 사용
      - 각종 옵션(ConncetTimeout, ReadTimeout, UseCaches, RequestProperty - 헤더를 설정할 수 있는 set 메서드가 존재
      - 상태코드 확인: getResponseCode()
      - 연결이 되면 사용할 수있는 스트림을 리턴해주는 getInputStream과 getOutputStream이 존재
   3. 문자열을 다운로드

9. **카카오 Open API 사용**
