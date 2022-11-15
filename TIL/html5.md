## HTML5

1. **HTML5**

   XHTML4에서 진화한 마크업 언어

   디자인과 내용의 분리 - 특별한 경우가 아니면 디자인은 CSS를 이용

   자바 Applet, Acitive X, 플래시와 같은 외부 플러그 인을 최소화 - 자바의 플렉스나 .Net의 실버라이트 배제

   문서 구조의 의미나 문서 안에 삽입된 데이터의 의미 등을 명확하게 하기 위한 사양이 추가 - Semantic

   마크업 언어에 웹 애플리케이션을 만들 수 있는 API(JavaScript를 이용해서 제작)를 포함.

2. **태그의 변화**

   1. 추가된 태그

      section, article, aside, header, hgroup, footer, nav: 문서 구조와 관련된 태그

      figure, audio, video, canvas, source: 외부 콘텐츠 사용

      keygen, output, progress, meter: 폼 관련 태그

      mark, ruby, time, command, details, datalist: 텍스트 관련 태그

   2. 변경된 태그

      hr, menu, small, strong

   3. 태그에 추가된 속성

      input: 모바일을 위한 속성과 유효성 검사 관련 속성이 추가

   4. 없어진 요소

      bgsound, applet 등

3. **API 변화**
   1. 로컬 저장소(이전에는 Cookie만 사용)
      - Web Storage
      - Web SQL
      - Indexed DB
   2. Drag And Drop API
   3. GeoLocation: 위치 정보
   4. Canvas: 그리기 API, 그래프나 이미지 출력을 외부 라이브러리 도움 없이 구현 가능
   5. Web Worker: Thread
   6. Math ML: 수식을 표현, FireFox에서만 적용 가능
   7. Communication API
      - ajax level 2
      - Server Sent Events(Web Push)
      - Web Socket API
4. **권장사항**

   1. Cookie보다는 Local Storage를 이용하는 것을 권장

      Cookie는 서버에게 요청할 때마다 전송되고 문자열만 저장 가능

      Local Storage는 자바스크립트 객체를 저장할 수 있고 필요할 때 서버에게 전송 가능

   2. Cache 보다는 로컬 데이터베이스를 사용하는 것을 권장
   3. 자바스크립트 애니메이션 대신 CSS의 전환 효과를 이용
   4. 부담을 많이 주는 작업은 웹 워커 사용을 권장 - 자바스크립트를 이용해서 플러그인이나 PC에서 직접 동작하는 애플리케이션 개발

5. **기본적인 문서 구조**

   1. DOCTYPE - DTD

   <!DOCTYPE html>

   2. 인코딩 설정

   <meta charset = “인코딩 방식”>

   3. 문서 검증

      http://html5.validator.nu

6. **HTML5를 지원하지 않는 브라우저를 위한 코딩 - 지금은 거의 의미가 없음.**

   http://caniuse.com/#index

7. **Mark up**

   1. 추가된 섹션 요소
      - header,section, article, asid ,nav, footer 등
      - div와 동일한 역할을 수행
      - 명확한 의미 전달을 위해서 추가
      - 브라우저의 내용을 인간이 아닌 robot이 읽었을 때 명확하게 의미를 전달하기 위해서
   2. figure, figcaption

      ```jsx
      <figure>
      	<img src = "이미지 파일의 경로"/>
      	<figcaption>그림에 대한 설명>
      </figure>

      // 이미지 아래에 그림에 대한 설명이 추가된다.
      // 음성 브라우저에서는 그림에 대한 설명을 읽어준다.
      ```

   3. ruby

      글자 위에 조그마한 글자를 표시하는 기능

      한자 사용할 때 위에 독음을 표시하는 용도로 많이 사용

8. **멀티 미디어**

   1. video

      비디오 재생을 위한 태그

      자바스크립트 객체로 추가됨.

      <video id = “video”></video>

      document.getElementByID(”video”);

      외부 플로그인 없이 동영상을 제어할 수 있음.

      브라우저 별로 코덱이 다르기 때문에 모든 동영상 파일이 전부 재생되는 것은 아님.

      동일한 동영상을 여러 포맷으로 만들어 제공하는 것이 좋다.

      ```jsx
      // 기본 형태
      <video src="동영상 URL"></video>

      // 재생할 수 있는 source를 재생
      <video>
      	<source src="동영상 URL" />
      	<source src="동영상 URL" />
      </video>

      ```

   2. audio

      video 태그와 사용법이 같음.

   3. canvas

      그래픽을 자유롭게 출력할 수 있는 html5에서 추가된 API

      2D 그래픽을 지원

      3D 그래픽은 Web GL을 이용해서 구현

      API: https://www.khronos.org/webgl/

      Sample: https://websamples.org/

      - 그래픽 가속 기술: Open GL(Windows는 Direct X)
        Open GL이 어려워서 이를 쉽게 사용할 수 있도록 만든 그래픽 엔진이 Unity 3D, Unreal이다. 스마트 폰은 Open GL ES 인데 애플은 자체 API를 제공
      - 웹 그래픽 가속: Web GL
      - 오디오 가속: Open AL
      - 그래픽 편집: Open CV

      태그로는 아무 일도 하지 못함. 태그 만들기만 하고 모든 작업은 자바스크립트를 이용해 수행한다.

      ```jsx
      // html
      <canvas id="아이디" width="너비" height="높이"></canvas>;

      // js
      let 캔버스변수 = document.getElementById("아이디");

      // context변수를 이용해서 그리기 작업
      let context변수 = 캔버스변수.getContext("2d");

      // 컨텍스트 정보 저장
      context변수.save();

      // 컨텍스트 정보 복원
      context변수.restore();
      ```

      `Context`: 어떤 작업을 하기 위해 필요한 부가 정보

      그림을 그리기 위해서는 선 색상, 면 색상, 선 두께 등의 정보가 필요한데 이 정보를 일일이 설정하게 되면 그림을 하나 그리는데 설정을 너무 많이 해야 한다. 설정에 대한 부분을 Context에 저장해 두고 필요한 부분만 수정해가면서 사용을 한다.

      ImageSprite: 여러 개의 이미지를 하나의 파일로 만들고 이미지를 적절한 크기로 잘라서 사용. 컴퓨터에서 가장 많이 시간을 소모하는 작업 중의 하나가 보조 기억 장치에 있는 파일을 읽어오는 부분이기 때문에 이미지를 각각의 파일로 저장해서 읽어오는 것보다는 여러 개의 이미지를 하나의 파일로 만든 후 이를 불러들인 후 나누어서 출력하는 것이 효율적인 경우가 많다.

      각 픽셀의 값을 제어하는 것이 가능한데 원형 UI 클릭을 만들고자 하는 경우 배경이 투명인 상태로 원을 그리고 마우스를 클릭했을 때 그 영역의 색상 값을 추출해서 알파 값이 0이면 동작을 하지 않고 알파 값이 0이 아니면 동작하도록 만들면 원형 UI를 만들 수 있다.

   4. SVG(Scalable Vector Graphics)

      XML을 이용해서 벡터 그래픽을 표시하는 API - 웹 표준

      Canvas는 Pixel 단위로 그림을 그리지만 SVG 벡터 이미지

      |                  | canvas                                | SVG                                    |
      | ---------------- | ------------------------------------- | -------------------------------------- |
      | 이미지 처리 방식 | Bitmap(Pixel 단위)                    | 벡터 그래픽(선이나 도형 단위)          |
      | DOM              | 존재(Javasript 사용 가능)             | 존재하지 않음                          |
      | 성능             | 높은 해상도 이미지를 사용시 성능 저하 | 이미지가 복잡해지면 Mark Up이 복잡해짐 |
      | 애니메이션       | 애니메이션 없음                       | 애니메이션 가능                        |
      | 외부 이미지 저장 | 저장 가능                             | 저장 불가능                            |
      | 적합한 서비스    | 게임                                  | 세밀한 해상도를 지원하는 UI(Graph)     |

      실제 그래프에 사용하는 경우는 직접 작성하는 것도 가능하지만 d3js나 jqplot같은 자바스크립트 라이브러리를 사용하는 경우가 많다.

9. **FORM**

   1. 기능 변화

      여러가지 타입 추가

      유효성 검사 기능이 추가

      진행 상황을 표시해주는 progress와 meter가 추가

   2. input의 type 추가(대부분 모바일을 위한 속성 - 자판이 바뀌는 등)

      기존 input의 type은 10개(text, password, checkbox, radio, file, button, image, submit, reset, hidden)였다.

      추가된 type은 search, tel, url, email, number, range, color, date, month, week, time, datetime-local이 있다.

      ```jsx
      // 네트워크를 맞추었을 때 스마트 폰에서 접속하는 방법
      http://127.0.0.1:5500/front-end/html5/html5_input.html
      192.168.0.23:5500
      // 이 부분에서 숫자 부분을 IP주소로 바꿔주면 된다.
      // 맥의 경우 시스템 설정에서 확인 가능
      // 다른 컴퓨터에서 접속이 가능하도록 할 때는 방화벽을 해제
      ```

      jQuery는 왜 사용했는가

      - HTML5는 브라우저별로 지원 범위가 다름
      - HTML5에서 추가된 기능을 사용하게 되면 브라우저 별로 다르게 설정을 해야 동일한 컨텐츠를 사용할 수 있다.
      - jQuery는 이 부분(cross browsing)을 쉽게 해주는 자바스크립트 라이브러리이다.
      - 국내에서는 IE 비중이 많이 낮아졌고 IE 대신에 나온 Edge는 HTML5를 지원하기 때문에 최근에는 jQuery 사용을 잘 하지 않는 추세이다.
      - jQuery의 단점 중의 하나가 렌더링 속도가 느리다는 것이다.

   3. progress와 meter

      진행율을 보여주고자 할 때 사용하는 요소

      자바스크립트를 이용해서 제어

      기본적으로 max value는 100으로 설정되어 있고 value 속성을 이용해서 진행율을 표시한다. 최대값이나 최소값을 변경하고자 하는 경우는 min과 max 속성의 값을 변경하면 된다.

   4. 추가된 속성

      - file: multiple 속성이 추가되서 속성을 설정하면 한 번에 여러 개의 파일을 선택할 수 있음.
      - autocomplete: 자동 완성 기능 사용 여부로 on과 off로 설정(스마트폰에서 고민)
      - list 속성과 datalist 속성을 이용하면 입력 값을 선택하도록 할 수 있다.

      ```jsx
      <input type="tel" list="telephonelist" />

      // 선택할 수 있도록 만드는 것
      <datalist id="telephonelist">
      	<option value="010-1234-5678" />
      	<option value="010-3456-7890" />
      </datalist>
      ```

      - placeholder 속성을 이용해서 입력할 때 설명을 위한 텍스트 설정
      - autofocus 속성을 이용해서 첫 번째 포커스 설정

   5. `유효성 검사(Validation Check)`

      - required: 필수 입력
      - maxlength: 최대 글자 수
      - min, max: 최소 최대 값 - 숫자나 날짜에 사용
      - pattern: 정규 표현식 설정 가능

      ![Untitled](HTML5%2093f1105561374a65b36d5b290a0c2839/Untitled.png)

      Web Server부터 Data Server까지는 애플리케이션과 다른 곳에 위치 - Web Client와 Server 간에 통신을 하게되면 Traffic이 발생하고 비용이 발생

      Web Client에서 Web Server로 전송하기 전에 유효성 검사를 해야하고 Application Server가 Web Client로부터 Data를 전송받으면 유효성 검사를 해야 한다.

      Web Client에서 유효성 검사를 수행해서 조건에 맞지 않는 경우 전송을 하지 않으면 Traffic을 아낄 수 있기 때문에 수행해야 하고 Application Server에서 다시 하는 이유는 데이터 전송 중에 변형이 이루어졌을지 모르기 때문이다.

10. **Geo Location**

    1. 개요

       디바이스의 물리적 위치 정보를 파악하기 위한 JavaScript API

       위치 정보를 가져오는 법

       - GPS와 같은 위성 정보를 이용해서 가져오는 방법
       - 가까운 라우터의 위치
       - 기지국의 위치

       위치 정보 사용을 허용을 해야만 사용 가능

    2. 위치 정보 사용 가능 여부 확인

       navigator.geolocation의 값을 확인.

    3. 위치 정보를 가져와서 한 번만 사용하기

       ```jsx
       navigator.geolocation.getCurrentPosition(위치 정보를 가져오는데 성공했을 때 호출되는 함수, 위치 정보를 가져오는데 실패했을 때 호출되는 함수, 옵션)
       ```

       성공했을 때 호출되는 함수에는 매개변수로 위치 정보와 관련된 객체가 전달된다. 이 객체가 저장하고 있는 정보는 JavaScript 뿐만 아니라 모바일 API에서도 동일하다.

       - coords
         - latitude: 위도
         - longitude: 경도
         - altitude: (고도 - GPS가 아니면 없음)
         - accuracy: 정확도
         - altitudeAccuracy: 고도의 정확도
         - heading: 방향
         - speed: 속도
       - timestamp: 위치 정보를 가져온 시간.

       실패했을 때 호출되는 함수에도 매개변수가 전달되는데 이 경우에는 에러 객체가 전달되고 code 속성을 확인하면 실패한 이유를 알 수 있다.

    4. 위치 정보를 계속 가져와서 사용하기

       ```jsx
       let 변수 = navigator.geolocation.watchPosition(위치 정보를 가져오는데 성공했을 때 호출되는 함수, 위치 정보를 가져오는데 실패했을 때 호출되는 함수, 옵션)
       ```

       위치 정보를 계속해서 파악하는 것을 변수로 선언하는 이유는 clearWatch(변수)를 호출해서 더 이상 위치 정보를 가져오지 않도록 하기 위함이다.

    5. 옵션

       객체 형태로 대입

       ```jsx
       {
       	enableHighAccuracy: // 정확도가 높은 위치 정보를 사용하도록 하는 옵션인데 기본값은 false
       	timeout: // 일정 시간이 지난 데이터는 폐기하는 옵션으로 밀리초 단위로 설정
       	maximumAge: // 0을 설정하면 항상 최신의 데이터를 가져온다.
       }
       ```

       스마트 폰을 이용하는 경우는 옵션 설정이 중요하다.

       스마트 폰에서 배터리를 가장 많이 소비하는 2가지가 블루투스를 사용하는 것과 gps를 이용하는 경우이다.

    6. 웹 화면에 현재 위치에 해당하는 카카오 맵을 출력

       Kakao Open Api: https://developers.kakao.com

       애플리케이션을 생성하고 key 발급

       javascript키 복사

       키는 바로 사용이 불가능 - 플랫폼을 등록해야 한다.

       네이티브 앱은 앱의 패키지 이름을 등록해야 하고 웹의 경우는 도메인을 등록해야한다.

       연습할 때는 웹의 경우는 ‘http://localhost:포트번호’ 형태로 등록하고 실제 서비스에 사용을 할 때는 localhost:포트번호 대신에 등록한 도메인이나 실 사용이 가능한 공인 IP 형태로 변경을 해야 한다.

11. **File API**

    File을 읽고 쓰기 위한 API

    input 타입의 file에 multiple 속성이 추가되어서 이 속성의 값을 설정하면 여러 개의 파일을 선택하는 것이 가능.

    텍스트 파일을 읽을 때는 인코딩 설정에 주의해야 한다.

    일반 파일을 읽을 때는 FileReader 객체를 생성한 후 reader.readAsDataURL(파일 객체)를 호출하고 load 이벤트와 error 이벤트를 처리한다.

    load는 전부 읽었을 때 FileReader 객체의 result에 읽은 내용을 저장하고 error 이벤트는 읽기에 실패했을 때 실패한 이유를 저장하고 있는 객체를 넘겨준다.

    이미지 미리보기

12. **Drag And Drop API**

    브라우저 내에서 사용할 수도 있고 외부 프로그램과 브라우저 사이에서도 사용할 수 있음.

    외부 프로그램과 사용할 때는 외부 프로그램에서 드래그를 하고 브라우저에서 드랍을 해야 한다.

    파일을 첨부할 때 많이 사용.

13. **브라우저에 데이터를 저장**

    1. 브라우저에 데이터를 저장하는 이유

       불필요한 트래픽을 줄이기 위해서

       메일 앱의 경우 매번 서버에 접속해서 서버의 데이터를 받아오는 것은 자원의 낭비가 될 수 있다.

       맨 처음 접속을 할 때는 데이터를 다운로드 받고(파일의 존재 여부를 확인)

       양쪽의 시간이 다르면 데이터가 수정된 것이므로 다운로드를 받고 양쪽의 시간이 같다면 업데이트 된 내용이 없으므로 다운로드를 받지 않도록 구현을 한다.

       offline 상태에서도 데이터를 사용할 수 있도록 하기 위해서

    2. 브라우저에 데이터를 저장하는 방법
       - Web Storage: Map의 형태로 저장
       - Web SQL: 관계형 데이터베이스(SQLite3 - 외부에서는 접속이 불가능한 저용량 데이터베이스, 사용법은 MySQL과 유사) 이용.
       - Indexed DB: 자바스크립트 객체 형태로 저장 - NoSQL과 유사
    3. Web Storage

       종류는 2가지

       기존에는 cookie를 사용했는데 Cookie를 사용하게 되면 문자열만 저장할 수 있고 서버에게 매 번 전송된다. 전송 여부를 클라이언트가 결정할 수 없다.

       - LocalStorage: 브라우저에 저장해서 지우지 않는한 절대 삭제가 되지 않는 저장소
       - SessionStorage: 현재 접속 중인 브라우저에 해당하는 저장소로 접속이 종료되면 소멸된다.

       데이터 저장과 가져오기 그리고 삭제

       ```jsx
       // localstorage와 sessionstorage 동일

       // 저장
       스토리지.키이름 = 데이터;
       스토리지["키이름"] = 데이터;
       스토리지.setItem("키이름", 데이터);

       // 가져오기
       스토리지.키이름;
       스토리지["키이름"];
       스토리지.getItem("키이름");

       // 삭제
       delete 스토리지.키이름;
       delete 스토리지["키이름"];
       스토리지.removeItem("키이름");
       ```

       저장소에 데이터가 변경되면 window 객체에 storage 이벤트가 발생하고 이벤트 객체에는 key, oldVlaue, newValue, url, sotrageArea 같은 속성이 만들어진다.

       Local Stroage는 전역변수 `localStorage` 로 사용할 수 있고 Session Storage는 `sessionStorage` 로 사용할 수 있다.

       저장된 내용을 확인하는 방법은 브라우저의 검사 창에서 application을 확인하면 된다.

       - 세션 스토리지
         브라우저를 종료했을 때 내용이 소멸되는지와 현재 창에서 새 창을 출력했을 때 내용이 복제가 되는지 확인
       - 로컬 스토리지
         id 저장을 구현하는데 브라우저를 종료하고 다시 연결했을 때 내용이 존재하는지 여부를 확인.
         보안이 중요하지 않고 많지 않은 양의 데이터를 저장하는데 용이.(장바구니)

       동일한 패턴의 데이터가 많은 경우는 로컬 스토리지 보다는 Web SQL이나 Indexed DB를 권장

14. **Web Worker**

    JavaScript를 이용한 백그라운드 처리

    JavaScript에서는 Thread 표현 대신에 Worker라는 표현을 사용

    HTML과 함께 있는 Javascript 코드에서 긴 작업을 수행하게 되면 작업이 끝날 때까지 다른 작업을 수행할 수 없음(UI는 아무것도 할 수 없는 상태)

    예전에 개념에서는 모든 작업을 수행하고 클라이언트는 서버가 렌더링한 뷰를 출력하는 역할만 수행했었고 자바스크립트를 가지고 작업을 수행하는 것은 특별한 경우였다.

    Web Worker는 UI 변경을 하지 못하고 DOM 객체 제어를 할 수 없지만 localStorage와 XMLHttpRequest(ajax) 사용은 가능

    ```jsx
    // 워커는 별도의 스크립트 파일에 만들어야 함.
    let 워커변수 = new Worker("자바스크립트 파일 경로");

    // 워커와 브라우저 사이의 메시지 전송
    워커변수.postMessage("메시지"); // 워커에서는 message 이벤트가 발생
    // 워커 파일에서는 postMessage("메시지") => 워커 변수에 message가 발생

    // sendMessage는 바로 처리해달라는 요청이고 postMessage는 다른 작업이 없으면
    // 처리해달라고 하는 요청이다.
    ```

    message 이벤트가 발생하면 매개변수에 data와 error를 가진 객체가 전달된다.

    워커는 terminate()를 호출해서 중지가 가능.

15. **Application Cache**

    리소스의 일부분을 로컬에 저장하기 위한 기능

    오프라인 브라우징이 가능해지고 리소스를 빠르게 로드할 수 있고 서버 부하를 감소시킬 수 있다.

    css나 js 그리고 이미지 파일 등을 캐싱

16. **Web Push**

    Server Sent Events: 클라이언트의 요청 없이 서버가 클라이언트에게 메시지를 전송하는 것. 사용하는 이유는 알림.

    Apple Server가 보내는 Push를 APNS(Apple Push Notification Service)라고 하고 Google Server가 보내는 Push를 FCM(Firebase Cloud Messge)라고 한다.

17. **Web Socket**

    Web에서의 TCP 통신을 위한 API

    일반적인 Web 요청의 처리 방식은 Client가 Server에게 접속을 한 후 하나의 request를 전송하고 그 request를 Server가 받으면 처리를 하고 response를 Client에게 전송하고 접속이 끊어짐.

    연속되는 작업을 처리하기 위해 Cookie(클라이언트의 브라우저에 저장)와 Session(서버에 저장)이라는 개념을 도입

    일반적인 Web 요청(HTTP나 HTTPS)은 본문 이외에 헤더 정보를 같이 전송해야 한다. 작은 사이즈의 데이터를 보내는 경우 오버헤드가 너무 크다.

    Web Socket을 이용하면 헤더가 거의 없기 때문에 이러한 오버헤드를 줄일 수 있음. 따라서 작은 양의 메시지를 자주 주고 받는 경우는 ajax나 Fetch API보다는 Web Socket을 사용하는 것을 권장
