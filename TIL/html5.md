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

      Web Client에서 Web Server로 전송하기 전에 유효성 검사를 해야하고 Application Server가 Web Client로부터 Data를 전송받으면 유효성 검사를 해야 합니다.

      Web Client에서 유효성 검사를 수행해서 조건에 맞지 않는 경우 전송을 하지 않으면 Traffic을 아낄 수 있기 때문에 수행해야 하고 Application Server에서 다시 하는 이유는 데이터 전송 중에 변형이 이루어졌을지 모르기 때문이다.
