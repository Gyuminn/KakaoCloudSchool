# [Java] IO

1. **path(경로)**
   - Windows 로컬: ₩₩
   - Mac, Unix, Linux, Web 등: /
   - java.io.File.seperator: 운영체제 별로 구분 기호를 설정
2. **절대 경로와 상대 경로**
   1. 절대 경로
      - Windows: `드라이브:₩경로`
      - Mac, Unix, Linux: `/경로`
      - Network: `프로토콜://경로`
   2. 상대 경로
      - ./: 현재 디렉토리인데 생략 가능한 경우가 있음.
      - ../: 상위 디렉토리로
      - /: 루트 디렉토리
3. **java.io.FIle**

   파일에 대한 정보를 가진 클래스

   파일을 생성하고 삭제하고 정보를 확인할 수 있도록 해주는 클래스

   제일 중요한 작업들은 파일의 존재 여부 파악, 마지막 수정 날짜를 파악하는 작업이 될 수 있다.

   클라이언트 - 서버 프로그래밍에서 클라이언트가 서버의 데이터를 받아서 저장한 후 실행하는 애플리케이션들이 있는 경우(게임 등)에 무조건 서버에서 데이터를 받아서 저장해서 사용하는 경우는 서버의 데이터가 아주 자주 갱신되는 경우이고 그 이외에는 서버에서 데이터를 받아서 저장한 후 서버의 데이터와 비교해서 다른 부분이 있으면 그 부분만 갱신하는 형태로 구현

4. **java.nino.file.Path**

   파일 경로에 대한 클래스

   nio 패키지는 io 패키지의 최신 버전

   1. 생성
      - Paths.get(파일 경로)
      - Paths.get(URI url)
        URI 인스턴스는 URI.create(”file:///경로”)
   2. File 객체로 변환

      toFile이라는 메서드를 호출하려면 File 객체로 변환해서 사용하는 것이 가능

5. **Stream**

   데이터를 운반하는데 사용되는 통로

   1. 분류
      - 입력 스트림(읽어오는 것)과 출력 스트림(내보내는 것)
      - 바이트 스트림(바이트 단위로 입출력)과 문자 스트림(문자 단위로 입출력 - 양쪽의 인코딩 방식이 같아야 함.)
   2. 특징
      - FIFO 구조
      - 단방향성

6. **바이트 스트림**

   바이트 단위로 데이터를 입출력하기 위한 스트림 - 기본 스트림

   1. InputStream

      입력을 위한 스트림의 최상위 클래스로 추상 클래스

      읽기 작업을 위한 메서드가 선언되어 있다.

      - int available()
        읽을 수 있는 바이트 수 리턴
      - void close()
        스트림 닫기
      - int read()
        한 바이트 읽고 읽은 바이트를 정수로 리턴하는데 읽은 데이터가 없으면 -1 리턴
      - int read(byte [] b)
        b만큼 읽어서 b에 저장하고 읽은 바이트 수를 리턴하는데 읽은 데이터가 없으면 -1
      - int read(byte [] b, int offset, int length)
        offset부터 length만큼 읽어서 b에 저장하고 읽은 바이트 수를 리턴하는데 읽은 데이터가 없으면 -1을 리턴

   2. OutputStream

      바이트 단위로 출력하기 위한 스트림의 최상위 클래스로 추상 클래스

      쓰기 작업을 위한 메서드를 선언

      - void close()
      - void write(byte [] b)
        b를 버퍼에 기록
      - void write(byte [] b, int offset, int length)
        b에서 offset부터 length까지 기록
      - void wirte(int b)
        b를 문자로 변환해서 기록
      - void flush()
        버퍼의 내용을 스트림에 기록

   3. 파일에 바이트 단위로 읽고 쓰기 위한 스트림
      - FileInputStream
        파일에서 읽어오기 위한 스트림
        - `FileInputStream(String 경로)`
        - `FileInputStream(File file)`
      - FileOutputStream
        파일에 기록하기 위한 스트림
        - `FileOutputStream(String 경로)`
          매번 파일에 새로 기록
        - `FileOutputStream(String 경로, boolean appendMode)`
          파일에 존재하는 경우 추가할 수 있도록 설정 가능
        - `FileOutputStream(File file)`
        - `FileOutputStream(File file, boolean appendMode)`
   4. **버퍼를 이용하는 스트림**
      - 읽고 쓰기를 할 때 버퍼를 사용하는 이유
        - 파일에 읽고 쓰기를 하는 작업은 OS(운영체제)의 Native Method를 호출해서 수행한다. 버퍼를 사용하지 않으면 요청을 할 때마다 OS의 Native Method를 호출하는데 여러 번 작업을 수행하면 OS의 효율이 떨어지게 된다.
        - 버퍼에 모아서 버퍼가 가득차거나 또는 강제로 버퍼의 내용을 모두 비우는 형태로 작업을 수행하게 되면 Native Method 호출의 횟수를 줄일 수 있다.
        - 자바에서 **바이트 단위 입력**을 할 떄는 이러한 버퍼링을 사용할 수 있는 **BufferedInputStream 클래스**를 제공하고 **바이트 단위 출력**을 할 때는 **PrintStream 클래스**를 제공
        - 이 클래스들의 생성자는 다른 스트림의 인스턴스를 받아서 인스턴스를 생성할 수 있도록 만들어져 있다.
        - PrintStream에는 write 외에 print와 같은 출력 메서드가 제공된다. 문자열의 경우 byte로 변환하지 않고 기록이 가능하다.

7. **문자 스트림**

   문자 단위로 읽고 쓰기 위한 스트림

   1. Reader

      문자 단위로 읽는 스트림 중 최상위 클래스 - 추상 클래스

      - int read()
        하나의 문자를 읽어서 정수로 리턴, 못 읽으면 -1
      - int read(char [] buf)
        buf 만큼 읽어서 읽은 개수를 리턴, 못 읽으면 -1
      - int read(char [] buf, int offset, int length)
        buf에 저장하는데 offset부터 length만큼 읽어온다. 읽은 개수를 리턴하고 못 읽으면 -1
      - void close()

   2. Writer

      문자 단위로 기록을 하는 스트림 중 최상위 클래스 - 추상 클래스

      - void close()
      - void write(String str)
      - void write(Stirng str, int offset, int length)
      - void flush()

   3. InputStreamReader와 OutputStreamWriter

      바이트 스트림을 받아서 문자 스트림으로 변환해주는 스트림 클래스

      네트워크 프로그래밍에서는 바이트 스트림만 제공하는데 채팅 같은 서비스나 웹의 문자열을 가져오는 서비스에서는 읽고 쓰는 단위가 문자열이므로 이 클래스들을 이용해서 문자 스트림으로 변환해서 읽는게 편리하다.

   4. FileReader와 FileWriter

      파일에 문자 단위로 입출력하기 위한 스트림

   5. BufferedReader와 PrintWriter

      버퍼를 이용해서 문자 단위로 입출력하기 위한 스트림

      BufferedReader에는 줄 단위로 읽을 수 있는 readLine 메서드가 추가로 제공됨.

   6. properties file

      속성과 값을 쌍으로 저장하는 텍스트 파일

      용도

      - 처음 한 번 설정하면 변경되지 않고 사용되는 문자열을 저장하는 용도 - 환경 설정(spring boot의 기본 환경 서정 파일)
      - 국가나 위치에 따라 보여지는 문자열이 달라져야 하는 경우 properties 파일에 메시지를 작성하고 국가나 지역 설정에 따라 다르게 보여지도록 한다 - 지역화(확장자 앞에 국가 코드나 언어 코드를 사용해서 구분)

      읽는 방법

      - File 인스턴스로 생성하고 Properties 인스턴스의 load 메서드와 getProperty 메서드를 이용 - 대부분의 프레임워크는 여기까지는 자동으로 수행해 준다.
      - 프로젝트 디렉토리에 [config.properties](http://config.properties) 파일을 만들어서 작성
        ```java
        url=192.168.0.100
        id=gyumin
        password=\uC911\uC599
        ```

8. **Serializable**

   인스턴스를 스트림에 전송하기 위한 것

   스트림에서는 기본형 데이터나 문자열만 전송이 가능(직접 작성한 클래스의 인스턴스는 기본적으로 네트워크를 통해서 인스턴스 단위로 전송을 못함.)

   스트림을 통해서 직접 작성한 클래스의 인스턴스를 전송하기 위한 기술이 Serializable

   이것 때문에 응용 프로그램끼리 데이터 호환이 안되는 것이다.

   1. Serializable이 가능한 클래스 생성
      - Serializable 인터페이스를 implements하면 모든 속성이 Serializable 대상이 된다.
      - 속성 중에서 제외하고자 하는 경우는 앞에 transient를 추가하면 된다.
   2. 인스턴스 단위로 읽고 쓰기위한 스트림

      ObjectOutputStream, ObjectInputStream을 이용해서 쓰는 것은 writeObject이고 읽는 것은 readObject이다.

      ```java
      public class LogMain {
          public static void main(String[] args) {
              // 파일 경로 확인
              // File file = new File("log.txt");
              // System.out.println(file.exists());

              // 문자열을 읽기 위한 스트림을 생성
              try (BufferedReader br = new BufferedReader(new FileReader("log.txt"))) {
                  // 트래픽의 합계를 구할 변수
                  int sum = 0;

                  while (true) {
                      // 한 줄을 읽어서 읽은게 없으면 종료
                      // 읽은 내용이 있으면 출력
                      String log = br.readLine();
                      if (log == null) {
                          break;
                      }
                      // System.out.println(log);

                      // 공백을 기준으로 분할
                      String[] ar = log.split(" ");
                      // IP 확인
                      // System.out.println(ar[0]);

                      // 트래픽 확인
                      // System.out.println(ar[ar.length - 1]);

                      // 트래픽을 정수로 변환해서 더하기
                      try{
                          sum += Integer.parseInt(ar[ar.length - 1]);
                      } catch(Exception e) {}
                  }
                  System.out.println("트래픽 합계: " + sum);
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
              }
          }
      }
      ```

   3. **IP별 접속 횟수와 IP별 트래픽 합계를 구해보세요.**

      Map을 이용하는데 IP를 Map의 Key로 사용하면 중복된 IP는 한 번만 저장한다.
