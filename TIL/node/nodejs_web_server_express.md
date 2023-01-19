# [Node.js] express 모듈을 이용한 웹 서버 생성 및 실행

1. **express 모듈**

   내장 모듈이 아님 - 설치가 필요

   http 모듈을 가지고 웹 서버를 만들 수 있는데 가독성이 떨어지고 확장성이 떨어짐.

   http 모듈보다는 코드 관리가 용이하고 편의성이 높은 모듈

   이 모듈을 제외하고 웹 서버를 생성해주는 모듈은 여러가지가 있고 최근에도 추가되고 있음.

   현재 가장 많이 사용되는 노드의 웹 서버 모듈

2. **패키지 설치**

   express(웹 서버 제작을 위한 모듈), nodemon(소스 코드를 수정하면 자동으로 재시작되도록 해주는 모듈)

   ```bash
   npm install express
   npm install --save-dev nodemon
   ```

3. **package.json 파일의 설정을 수정**
   - main 속성에 시작 파일의 이름을 설정: `app.js`
   - scripts 속성에 `“start”:”nodemon app”` 이라는 태그 추가
     (entry point - main 속성과 nodemon. 에 붙이는 것을 동일시
     npm start라는 명령을 실행하면 nodemon app 이라는 명령을 수행
4. **express web server의 기본 틀**

   ```jsx
   // 모듈 가져오기
   const express = require("express");

   // 웹 서버 인스턴스 생성
   const app = express();

   // 포트 설정
   app.set("port", 포트번호);

   // 사용자의 요청을 처리하는 코드

   app.listen(app.get("port"), () => {
     // 서버가 정상적으로 구동되었을 때 수행할 내용
     // 일반적으로는 콘솔에 메시지를 출력
   });
   ```

5. **사용자의 요청을 처리하는 함수**

   1. 종류 - 요청 방식에 맞는 함수를 호출
      - app.get
      - app.post
      - app.delete
      - app.put
      - app.patch
      - app.options
   2. 함수의 매개변수

      - 첫 번째는 url
      - 두 번째는 2개의 매개변수를 갖는 콜백 함수로 이 함수가 url 요청이 왔을 때 호출된다.

      콜백 함수의 첫 번째 매개변수는 사용자의 요청 객체(request)이고 두 번째 매개변수는 사용자에게 응답을 하기 위한 객체(response)

   3. 사용자에게 응답
      - 직접 작성: response.send(문자열)
      - html 파일 출력: reponse.sendFile(html 파일 경로)

6. **웹 서버 만들기**

   1. 출력할 html 파일을 1개 생성 - main.html(index도 괜찮고 마음대로)
   2. package.json 파일에 entry point(시작 포인트)로 설정한 파일을 생성하고 웹 서버를 구동시키는 코드를 작성

      ```jsx
      // 웹 서버를 위한 외부 모듈
      const express = require("express");

      // 현재 디렉토리에 대한 절대 경로를 알아내기 위한 내장 모듈
      // 내장 모듈이기 때문에 설치할 필요가 없다.
      const path = require("path");

      // 서버를 준비
      const app = express();
      app.set("port", 8001);

      // 사용자의 요청 처리
      // /는 포트번호까지의 요청
      app.get("/", (req, res) => {
        // 절대 경로를 쓸수 없기 때문에 path와 join을 이용한다.
        // 현재 디렉토리에 있는 main.html을 출력하는 것.
        res.sendFile(path.join(__dirname, "/main.html"));
      });

      // 서버를 구동하는 코드
      app.listen(app.get("port"), () => {
        console.log(app.get("port"), "번 포트에서 대기중");
      });
      ```

   3. 구동

      `npm start` 또는 `node 작성한스크립트파일명`

      특별한 경우가 아니면 npm start로 실행

   4. 정상적으로 구동되었으면 브라우저에서 확인

      [http://localhost:8001](http://localhost:8001)

7. **요청 객체: 일반적으로 request 객체라고 한다.**

   클라이언트의 요청 정보를 저장하고 있는 객체.

   - request.app
     app 객체에 접근(express 인스턴스)
   - request.body
     body-parser 미들웨어가 만드는 요청의 본문을 해석한 객체
     post나 put 요청이 왔을 때 파라미터 읽기
   - request.cookies
     쿠키 정보를 가지는 객체
   - request.ip
     요청을 전송한 클라이언트의 ip 정보 - ip를 알면 접속한 국가를 알 수 있다.
   - request.params
     라우트 매개변수에 담긴 정보
   - `request.query`
     `물음표 같은 것들은 얘가 읽어냄`
     query string - get이나 delete 요청에서 파라미터 읽기
   - `request.get(헤더이름)`
     `헤더이름에 Authorization 같은 것들을 넣음.`
     헤더의 값 가져오기, 인증에서 많이 사용, 최근에는 API 사용 권한을 토큰을 이용해서 발급하고 토큰의 값을 헤더에 저장해서 전송하도록 만드는 경우가 많다
   - request.signedCookies
     서명된 쿠키 정보
   - request.session
     세션 객체

8. **응답 객체: response 객체**
   - response.cookie(키, 값, 옵션)
     쿠키 생성
   - response.clearCookie(키, 값, 옵션)
     쿠키 삭제
   - response.end()
     데이터 없이 응답을 보냄
   - `response.json(JSON 문자열)`
     JSON 형식으로 응답
   - response.redirect(URL)
     리다이렉트할 URL
     - Forwarding과 Redirect
       서버에서 화면을 만들 때만 의미를 갖음. API 서버를 만드는 경우 해당사항 없음.
       Forwarding은 현재 흐름을 유지한 채 이동하는 것으로 URL이 변경되지 않음. node에서 html을 출력할 때 기본
       Redirect는 현재 흐름을 끊어버리고 이동하는 것으로 URL이 변경됨.
       Forwarding은 조회 작업에 사용하고 그 이외의 작업은 redirect를 해야 한다.
       Forwarding을 하게 되면 새로 고침을 했을 때 작업이 다시 이루어짐.
       Redirect를 하게 되면 새로 고침을 할 때 결과만 새로 고침이 됨.
     - 새로 고침을 판단하는 것이 좋을 때가 있다.
       대부분의 경우 조회는 forwarding을 해야 하지만 트래픽을 줄이고자 할 때는 조회도 redirect 하기도 함.
       삽입, 삭제, 갱신은 반드시 redirect해야 함.
   - response.render(뷰이름, 데이터)
     템플릿 엔진을 이용해서 서버의 데이터를 html에 출력하고자 할 때 사용.
     이를 서버 렌더링이라고 한다.
   - response.send(메시지)
     메시지를 화면에 출력
   - response.sendFile(파일 경로)
     파일을 읽어서 화면에 출력
   - response.set(헤더이름, 값)
     헤더를 설정
   - response.status(코드)
     응답 코드 설정
9. **dotenv**

   .env 파일을 읽어서 process.env로 생성해주는 패키지

   .env 파일에 작성한 내용을 소스 코드에서 process.env 객체를 이용해서 사용 가능하도록 하는 패키지

   환경의 변화(개발 환경, 운영 환경, 테스트 환경) 때문에 변경되는 설정을 별도의 텍스트 파일에 만들어두면 환경이 변경될 때 텍스트 파일의 내용만 변경하면 되기 때문에 컴파일이나 빌드를 다시할 필요가 없어진다.

   이러한 정보의 대표적인 것이 `데이터베이스 접속 위치`와 `API 키` 이다.

10. **Middle Ware - Filter, AoP**

    사용자의 요청이 발생하고 서버가 요청을 처리하고 응답을 전송하는 시스템에서 요청을 처리하기 전이나 후에 동작할 내용을 수행하는 객체

    요청을 처리하기 전에 수행하는 일은 `필터링`이고 요청을 처리한 후 수행하는 일은 `변환`이다.

    필터링을 할 때는 `유효성 검사 작업`과 `로그인 확인 작업`이 대표적이다.

    node에서는 `app.use(미들웨어)` 형태로 장착.

    - app.use(미들웨어): 모든 요청에서 미들웨어가 동작
    - app.use(url, 미들웨어): url에서만 미들웨어가 동작

    현재 미들웨어에서 다음 미들웨어로 넘어가는 함수: `next()`

    1. morgan

       로그를 기록해주는 미들웨어

       ```jsx
       morgan(format, options);
       ```

       - format
         - dev: 개발용, 배포를 할 때는 모두 삭제됨.
         - tiny
         - short
         - common
         - combined
       - options
         - immediate
           response에 기록하는 것이 아니고 request에서 기록(에러가 발생해도 기록)
         - skip
           로깅을 스킵하기 위한 조건을 설정
         - stream
           기본적으로 로그는 화면에 출력되지만 파일에 출력하고자 할 때 사용

       로그 파일을 생성해주는 패키지

       morgan의 아래 패키지를 이용하면 주기적으로 파일을 생성해서 로그를 기록하는 것이 가능.

       ```bash
       npm install file-stream-rotator
       ```

       로그 형식

       ```bash
       HTTP요청방식 요청URL 상태코드 응답속도 트래픽
       // 조금 더 자세한 로그를 원하는 경우 winston 패키지 사용
       ```

    2. static(정적 - 내용이 변하지 않는)

       정적인 파일의 경로를 설정하는 미들웨어

       사용하는 방식

       ```jsx
       app.use(url, express.static(실제 경로));
       // url 요청이 오면 실제 경로에 있는 파일을 출력

       // 예시
       app.use("/", express.static(path.join(__dirname, 'public')));
       // /index라고 요청하면 프로젝트 안에 있는 public 디렉토리의 index라는 파일을 출력한다
       // 요청 경로와 실제 파일의 경로를 일치하지 않도록 하기 위해서
       ```

    3. body-parser

       요청의 분문을 해석해주는 미들웨어로 별도로 설치할 필요는 없음.

       express를 설치하면 자동으로 설치가 된다.

       클라이언트에서 post 방식이나 put(patch) 방식으로 데이터를 전송할 때 그 데이터를 읽기 위한 미들웨어

       설정

       ```jsx
       app.use(express.json());
       app.use(express.urlencoded({ extended: false }));
       ```

       파일을 전송하는 경우에는 다른 미들웨어를 사용해야 한다.

    4. compression

       데이터를 압축해서 전송하기 위한 미들웨어

       클라이언트에게 결과를 전송할 때 압축을 해서 전송하기 때문에 트래픽이 줄어든다.

       외부 모듈이라서 설치해야 한다.

       ```bash
       npm install compression
       ```

       ```jsx
       const compression = require("compression");
       app.use(compression());
       ```

    5. cookie-parser

       쿠키를 해석할 수 있도록 해주는 미들웨어

       쿠키는 중요

       ```jsx
       app.use(cookieParser(키)); // 서버에서 쿠키를 읽을 수 있다.
       request객체.cookies; // 모든 쿠키가 넘어오게 된다.
       ```

    6. expression-session

       세션(사용자의 정보를 서버에 저장) 관리를 위한 미들웨어

       클라이언트 측에서 이전 작업에 이어서 다른 작업을 하고자 할 때 세션을 이용한다.

       세션은 서버의 메모리를 사용하기 때문에 세션이 너무 크커나 많아지면 서버의 성능이 저하된다.

       이런 경우에는 세션을 파일이나 데이터베이스에 유지하는 것이 좋다.

    7. 세션을 사용하는 예제: 새로 고침을 하면 이전 내용에 +1을 해서 출력하기

       세션을 사용하기 위해서는 `express-session` 패키지 이용

       브라우저에서 접속을 하고 새로 고침을 한 후 다른 브라우저에서 동일한 URL로 접속해서 num과 req.session.num을 비교

11. **프로젝트**

    프로젝트를 만들게 되면 프로젝트 내의 파일은 2가지로 분류한다.

    하나는 `소스 코드` 이고 다른 하나는 `소스 코드 이외의 자원` 이다.

    자원을 읽어서 사용하는 것이 소스 코드를 실행을 위한 코드이다.

    코드가 실행되는 과정

    - 소스 코드 → 컴파일해서 운영체제나 Virtual Machine이 이해할 수 있는 코드로 변경(이 상황에서 에러가 발생하면 문법 오류) → 컴파일이 끝나고나면 번역된 파일들을 실행할 수 있도록 작업을 한다. 이 작업을 build라고 한다.(이 과정에서 에러가 발생하면 구조를 잘못만든 것이다.) → 실행
    - 실제 배포를 할 때는 `소스 코드를 배포하는 것이 아니라 빌드된 내용을 배포`한다.
    - `소스 코드를 수정하면 컴파일과 빌드를 다시 해야 한다.`
    - 소스 코드를 되도록 수정하지 않고 업데이트를 하거나 환경을 변경하는 것이 가능하도록 프로그램을 제작하는 것이 바람직하다.
    - 이것 때문에 `DevOps나 CI/CD를 공부하는 것`이다.

    클라이언트가 요청을 보내면 서버가 처리를 하고 결과 또는 화면을 클라이언트에게 전송

    클라이언트에서 서버에게 적절하게 요청을 하면 서버가 그 요청을 받는데 이 때 클라이언트가 데이터를 같이 보내는 경우가 있는데 서버는 그 데이터를 읽어서 작업을 수행(비즈니스 로직을 처리하는 것과 데이터를 반 영구적으로 저장하고 읽어오는 것)하고 결과나 화면을 만들어서 전송을 한다.
