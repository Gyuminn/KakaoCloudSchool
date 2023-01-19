# [Node.js] API Server

1. **API(Application Programming Interface)**

   프로그램과 프로그램을 연결시켜주는 매개체

   다른 애플리케이션을 개발할 수 있도록 도와주는 프로그램(Software, Development Kit) 또는 데이터

   - JDK - Java software Development Kit
   - Sony SDK - Sony 디바이스의 애플리케이션을 만들 수 있도록 도와주는 프로그램
   - Win API - Windows Application을 만들기 위한 함수(C)의 집합

   프로그램 개발에 도움을 주도록 또는 여러 프로그램에서 공통으로 사용되어야 하는 데이터가 있는 경우에는 프로그램이 아니라 데이터를 제공

   누구나 등록만 하면 사용할 수 있도록 API를 만들면 Open API라고 한다.

   데이터를 제공할 때는 데이터베이스에 직접 접근하도록 하는 것이 아니고 애플리케이션 서버를 통해서 제공

2. **API Server가 제공하는 데이터 포맷**

   1. txt 또는 csv

      일반 텍스트로 구분 기호를 포함하는 경우가 있음.

      변하지 않는 데이터를 제공하는데 주로 이용

      가끔 txt나 csv 대신에 excel이나 hwp 또는 pdf로 제공하는 경우가 있음.

   2. xml(eXtensible Markup Language)

      태그의 해석을 브라우저가 아닌 개발자 또는 개발자가 만든 라이브러리가 하는 형태로 문법이 HTML보다는 엄격(HTML은 데이터로 사용하기에는 부적합 - 구조적이지 못하기 때문)

      아직도 설정 파일이나 데이터를 제공하는 용도로 많이 사용

   3. json

      자바스크립트 객체 형태로 표현하는 방식

      XML보다 가볍기 때문에 데이터 전송에 유리

      자바스크립트 객체 표현법으로 데이터를 표현하기 때문에 JavaScript나 Python에서는 파싱하는 것이 쉬움.

      설정보다는 데이터를 제공하는 용도로 많이 사용

      Apple, Google, Twitter 등은 데이터 전송에는 json만 사용

   4. yaml

      email 표기 형식으로 표현하는 방식

      계층 구조를 가진 데이터 표현에 유리

      구글의 프로그램들이 설정을 할 때 yaml(확장자는 yml - 야믈)을 많이 이용

3. **API Server를 만들기 위한 기본 설정**

   1. 프로젝트 생성
   2. 필요한 패키지 설치
      - bcrypt: 복호화가 불가능한 암호화를 위한 모듈 - 비밀번호를 저장할 목적
      - uuid: 랜덤한 문자열을 생성하기 위한 모듈 - 키를 발급할 목적
   3. package.json 수정
   4. config, models, passport, env 설정
   5. 프로젝트에 화면에 출력되는 파일을 저장하기 위한 디렉토리를 생성 - views
   6. 에러가 발생했을 때 화면에 출력될 파일은 views 디렉토리에 생성하고 작성 - error.html

      ```html
      <h1>{{message}}</h1>
      <h2>{{error.stats}}</h2>
      <pre>{{error.stack}}</pre>
      ```

      message는 우리가 전달하는 문자열

      error.status 는 에러 코드

      error.stack(statck trace)는 에러가 발생하면 에러가 발생한 부분에서 호출되는 함수를 역순으로 출력한다.

      에러를 해결할 때는 맨 위에서부터 아래로 내려오면서 자신이 작성한 코드가 있는 부분을 찾아야 한다. 그 부분을 수정하는데 그 부분에서 다른 코드를 호출하면 순서대로 역추적해 나가야 한다.

4. **도메인을 등록해서 등록한 도메인에서만 API 요청이 이루어지도록 도메인과 키를 생성해서 저장**

   1. models 디렉토리에 위의 정보(`host - 클라이언트 URL, clientSecret - 키, type - free, premium`)를 저장할 모델을 생성 - domain.js

      <aside>
      ▪️ free, premium 두 가지만 구분하고자 하는 경우 자료형
      boolean: true와 false를 이용해서 구분 가능
      int: free와 premium을 0과 1 또는 1과 2 형태로 구분 가능
      string: free와 premium을 문자열로 저장해서 구분
      ENUM(Enum): 정해진 데이터만 삽입이 가능

      type varchar(100) check type in (’free’, ‘premium’))

      </aside>

   2. models 디렉토리의 index.js 파일에 Domain 사용을 위한 설정
   3. views 디렉토리에 login을 위한 파일 작성
   4. authentication 프로젝트 서버 띄우기

5. **로그인 처리와 도메인 등록 처리를 위한 내용을 routes 디렉토리의 index.js 파일에 작성**

   ```jsx
   const express = require("express");

   const { v4: uuidv4 } = require("uuid");

   const { User, Domain } = require("../models");
   const { isLoggedIn, isNotLoggedIn } = require("./middlewares");

   const router = express.Router();

   router.get("/", async (req, res, next) => {
     try {
       // 로그인 한 유저가 있으면 유저의 모든 데이터를 찾아서 대입
       const user = await user.findOne({
         where: { id: (req.user && req.user.id) || null },
         include: { model: Domain },
       });
       res.render("login", { user, domains: user && user.Domains });
     } catch (error) {
       console.error(error);
       next(error);
     }
   });

   // 도메인 등록 처리
   router.post("/domain", isLoggedIn, async (req, res, next) => {
     try {
       await Domain.create({
         UserId: req.user.id,
         host: req.body.host,
         type: req.body.type,
         clientSecret: uuidv4(),
       });
       // 삽입하고 메인 페이지로 이동
       res.redirect("/");
     } catch (error) {
       console.error(error);
       next(error);
     }
   });

   module.exports = router;
   ```

6. **App.js 파일에 routes 디렉토리의 index.js 파일을 사용할 수 있도록 설정**

   ```jsx
   //라우터 설정
   const indexRouter = require("./routes");
   app.use("/", indexRouter);
   ```

7. **JWT(JSON Web Token) 토큰**

   https://jwt.io

   JSON 데이터 구조로 표현한 토큰

   API Server나 로그인을 이용하는 시스템에서 매번 인증을 하지 않고 서버와 클라이언트가 정보를 주고받을 때 HttpRequest Header에 JSON 토큰을 넣은 후 인증하는 방식

   HMAC 알고리즘을 사용하여 비밀키나 RSA 기법을 이용해서 Public Key와 Private Key를 이용해서 서명(암호화 키와 해독키가 한 쌍 - 암호화 키와 해독키를 다르게 생성하여 암호화 키는 누구나 알 수 있는 형태로 공개를 하지만 해독키는 비밀로 하는 방식)

   구성

   - HEADER: 토큰 종류와 해시 알고리즘 정보
   - PAYLOAD: 토큰의 내용물이 인코딩된 부분
   - SIGNATURE: 토큰이 변조되었는지 여부를 확인할 수 있는 부분

   클라이언트가 서버에게 데이터를 요청할 때 키와 domain을 json token에 포함시켜 전송하고 서버는 이를 확인해서 유효한 요청인지 판단하고 데이터를 전송한다.

   쿠키는 동일한 도메인 내에서만 읽을 수 있다. 서버와 클라이언트 애플리케이션의 도메인이 다르면 쿠키는 사용할 수 없다. 설정을 하면 서로 다른 도메인 간에도 쿠키를 공유할 수는 있지만 위험

   서버와 클라이언트 애플리케이션의 도메인이 다른 경우는 세션을 이용해서 사용자 인증을 할 수가 없다. 이 경우에는 서버에서 클라이언트에게 키를 발급하고 클라이언트는 서버에 요청을 할 때 키를 전송을 해서 키를 평문으로 전송하게 되면 중간에서 가로채서 사용할 수 있다.

   키와 클라이언트 URL을 합쳐서 하나의 암호화된 문장을 생성해서 전송을 하게 되면 서버는 이를 해독하면 키와 인가된 클라이언트의 URL을 확인할 수 있다.

   다른 곳에서 문장을 탈취해서 데이터를 요청한 경우 URL이 다르기 때문에 인가된 사용자가 아님을 확인할 수 있다.

   1. 노드에서 JWT 인증을 위한 모듈을 설치

      ```bash
      npm install jsonwebtoken
      ```

   2. JWT 생성에 필요한 문자 코드를 .env 파일에 생성

      ```jsx
      JWT_SECRET = jwtSecret;
      ```

   3. routes 디렉토리의 middlewares.js 파일에 JWT 인증을 위한 미들웨어 함수를 추가

      ```jsx
      const jwt = require("jsonwebtoken");
      exports.verifyToken = (req, res, next) => {
        try {
          // 토큰 확인
          req.decoded = jwt.verify(
            req.headers.authorization,
            process.env.JWT_SECRET
          );
          // 인증에 성공하면 다음 작업 수행
          return next();
        } catch (error) {
          if (error.name === "TokenExpiredError") {
            return res.stats(419).json({
              code: 419,
              message: "토큰이 만료되었습니다.",
            });
          }
          return res.status(401).json({
            code: 401,
            message: "유효하지 않은 토큰입니다.",
          });
        }
      };

      // 401 에러가 권한이 없음을 나타내는 에러 코드 번호
      ```

   4. routes 디렉토리에 토큰을 발급하는 처리를 v1.js에 작성

      ```jsx
      const express = require("express");
      const jwt = require("jsonwebtoken");
      const { Domain, User } = require("../models");
      const { verifyToken } = require("./middlewares");

      const router = express.Router();

      router.post("/token", async (req, res) => {
        const { clientSecret } = req.body;
        try {
          // 도메인 찾아오기
          const domain = await Domain.findOne({
            where: { clientSecret },
            include: {
              model: URLSearchParams,
              attribute: ["nick", "id"],
            },
          });
          if (!domain) {
            return res.status(401).json({
              code: 401,
              message: "등록되지 않은 도메인입니다.",
            });
          }
          // 토큰 생성
          const token = jwt.sign(
            {
              id: domain.User.id,
              nick: domain.User.nick,
            },
            process.env.JWT_SECRET,
            {
              expiresIn: "1m", // 유효기간
              issuer: "gyumin", //발급자
            }
          );

          return res.json({
            code: 200,
            message: "토큰이 발급되었습니다",
            token,
          });
        } catch (error) {
          console.error(error);
          return res.json(500).json({
            code: 500,
            message: "서버에러",
          });
        }
      });
      // 토큰을 확인하기 위한 처리
      router.get("/test", verifyToken, (req, res) => {
        res.json(req.decoded);
      });

      module.exports = router;
      ```

   5. App.js 파일에 v1 등록하는 코드를 추가

      ```jsx
      const v1 = require("./routes/v1");
      app.use("/v1", v1);
      ```

   6. 클라이언트 애플리케이션 생성

      - 필요한 패키지
        ```bash
        npm install express dotenv axios cookie-parser express-session morgan nunjucks
        npm install --save-dev nodemon
        ```
      - App.js 설정

        ```jsx
        const express = require("express");
        const morgan = require("morgan");
        const cookieParser = require("cookie-parser");
        const session = require("express-session");
        const nunjucks = require("nunjucks");
        const dotenv = require("dotenv");

        dotenv.config();
        const indexRouter = require("./routes");

        const app = express();
        app.set("port", process.env.PORT || 4000);
        app.set("view engine", "html");
        nunjucks.configure("views", {
          express: app,
          watch: true,
        });

        app.use(morgan("dev"));
        app.use(cookieParser(process.env.COOKIE_SECRET));
        app.use(
          session({
            resave: false,
            saveUninitialized: false,
            secret: process.env.COOKIE_SECRET,
            cookie: {
              httpOnly: true,
              secure: false,
            },
          })
        );

        app.use("/", indexRouter);

        app.use((req, res, next) => {
          const error = new Error(
            `${req.method} ${req.url} 라우터가 없습니다.`
          );
          error.status = 404;
          next(error);
        });

        app.use((err, req, res, next) => {
          res.locals.message = err.message;
          res.locals.error = process.env.NODE_ENV !== "production" ? err : {};
          res.status(err.status || 500);
          res.render("error");
        });

        app.listen(app.get("port"), () => {
          console.log(app.get("port"), "번 포트에서 대기중");
        });
        ```

      - views 디렉토리를 생성하고 에러가 발생하면 보여질 error.html 파일을 생성
        ```html
        <h1>{{message}}</h1>
        <h2>{{error.status}}</h2>
        <pre>{{error.stack}}</pre>
        ```
      - .env 파일에 발급받은 토큰을 입력
        ```bash
        COOKIE_SECRET=nodeclient
        CLIENT_SECRET=c219dd4a-1958-4614-a9c2-8f164b332f57
        ```
      - routes 디렉토리에 라우팅 관련 코드를 index.js 파일에 작성

        ```bash
        const express = require("express");
        const axios = require("axios");

        const router = express.Router();

        router.get("/test", async (req, res, next) => {
          try {
            if (!req.session.jwt) {
              const tokenResult = await axios.post("http://localhost:8000/v1/token", {
                clientSecret: process.env.CLIENT_SECRET,
              });
              if (tokenResult.data && tokenResult.data.code === 200) {
                req.session.jwt = tokenResult.data.token;
              } else {
                // 토큰 발급 실패
                return res.json(tokenResult.data);
              }
            }

            // 토큰 내용 확인
            const result = await axios.get("http://localhost:8000/v1/test", {
              headers: { authorization: req.session.jwt },
            });
            return res.json(result.data);
          } catch (error) {
            console.error(error);
            return next(error);
          }
        });

        module.exports = router;
        ```

      - 실행을 하고 [localhost:4000](http://localhost:4000) 번으로 들어가면 토큰 내용이 출력됨.

8. **API 요청을 위해서 node-app-client routes의 index.js 수정**

   ```jsx
   const express = require("express");
   const axios = require("axios");

   //매번 동일한 요청을 위한 URL을 상수로 설정
   const URL = "http://localhost:8000/v1";
   // ajax 요청을 할 때 누가 요청했는지 확인해주기 위해서
   // origin header를 추가
   axios.defaults.headers.origin = "http://localhost:4000";

   // 토큰 발급 코드
   const request = async (req, api) => {
     try {
       if (!req.session.jwt) {
         const tokenResult = await axios.post(`${URL}/token`, {
           clientSecret: process.env.CLIENT_SECRET,
         });
         req.session.jwt = tokenResult.data.token;
       }
       // 토큰 내용 확인
       const result = await axios.get(`${URL}${api}`, {
         headers: { authorization: req.session.jwt },
       });
       return result;
     } catch (error) {
       // 토큰 유효 기간 만료
       if (error.response.status === 419) {
         // 기존 토큰을 삭제
         delete req.session.jwt;
         // 다시 토큰을 생성해달라고 요청
         return request(req, api);
       }
       return error.response;
     }
   };

   const router = express.Router();

   router.get("/mypost", async (req, res, next) => {
     try {
       const result = await request(req, "/posts/my");
       res.json(result.data);
     } catch (error) {
       console.error(error);
       next(error);
     }
   });

   router.get("/test", async (req, res, next) => {
     try {
       if (!req.session.jwt) {
         const tokenResult = await axios.post(
           "http://localhost:8000/v1/token",
           {
             clientSecret: process.env.CLIENT_SECRET,
           }
         );
         if (tokenResult.data && tokenResult.data.code === 200) {
           req.session.jwt = tokenResult.data.token;
         } else {
           // 토큰 발급 실패
           return res.json(tokenResult.data);
         }
       }

       // 토큰 내용 확인
       const result = await axios.get("http://localhost:8000/v1/test", {
         headers: { authorization: req.session.jwt },
       });
       return res.json(result.data);
     } catch (error) {
       console.error(error);
       return next(error);
     }
   });

   module.exports = router;
   ```

   브라우저에 [localhost:4000/mypost를](http://localhost:4000/mypost를) 입력하고 데이터가 넘어오는지 확인.

9. **서버 수정**

   1. 사용량 제한

      API Server를 만들었을 때 데이터를 무제한 제공하게 되면 트래픽이 많이 발생해서 속도가 느려질 수 있다. DDos 공격의 대상이 될 수도 있다.

      일정한 주기를 가지고 제한을 하기도 하고 사이즈나 횟수 제한을 가하기도 한다.

      kakao 같은 경우는 횟수 제한과 사이즈 제한을 동시에 한다.

   2. 기존 서버를 수정했을 때 처리

      기존 코드를 무조건 바꾸는 것은 위험

      기존 코드는 그대로 두고 deprecated나 서비스 중지 메시지를 전송하는 형태로 새로운 내용을 적용하는 것이 좋다.

   3. node의 middleware와 Java의 Filter, Spring의 Interceptor와 AOP

      실제 처리를 하기 전이나 후에 동작하는 로직을 작성하는 용도로 사용

      위의 것들을 사용하는 경우는 Business Logic과 Common Concern의 분리를 하기 위해서나 공통된 처리를 해야하는 경우이다.

   4. 사용량 제한을 위한 api server 프로젝트 수정

      - 사용량 제한을 위한 패키지 설치
        ```bash
        npm install express-rate-limit
        ```
      - middlewares.js 파일에 사용량 제한을 위한 미들웨어를 생성

        ```jsx
        // 사용량 제한을 위한 미들웨어
        const RateLimit = require("express-rate-limit");

        exports.apiLimiter = RateLimit({
          windowsMs: 60 * 1000, // 1분
          max: 10,
          delayMs: 0,
          handler(req, res) {
            res.status(this.statusCode).json({
              code: this.statusCode,
              message: "1분 단위로 요청을 해야 합니다.",
            });
          },
        });

        // 구버전 API 요청 시 동작할 미들웨어
        exports.deprecated = (req, res) => {
          res.status(410).json({
            code: 410,
            message: "새로운 버전이 나왔습니다. 새 버전을 사용하세요",
          });
        };
        ```

      - routes 디렉토리에 새로운 버전의 요청을 처리할 v2.js 파일을 생성하고 작성
        apiLImiter를 임포트 시켜주고, router 요청에 매개변수로 넣어준다.
      - v1.js 파일을 수정

        ```jsx
        const { verifyToken, deprecated } = require("./middlewares");

        router.use(deprecated);
        ```

      - App.js에서 v2 추가
        ```jsx
        const v2 = require("./routes/v2");
        app.use("/v2", v2);
        ```
      - api client 프로젝트의 routes 디렉토리의 index.js 파일에서 URL을 수정

        ```jsx
        // const URL = "http://localhost:8000/v1";
        const URL = "http://localhost:8000/v2";

        // 만약 env 파일에 적었다면 그걸로 수정해주면 된다.
        // 재실행을 방지할 수 있는 방법!
        ```

10. **CORS(Cross-Origin Resource Sharing)**

    - SOP(Same Origin Policy - 동일 출처 정책)
      어떤 출처에서 불러온 문서나 스크립트가 다른 출처에서 가져온 리소스와 상호작용하는 것을 제한하는 브라우저의 보안 방식
      브라우저에서는 XMLHttpRequest(ajax)와 Fetch API 같은 경우는 다른 출처에 리소스를 요청할 때 적용
      img, link, script, video, audio, object, embed, applet 태그는 SOP의 적용을 받지 않는다.
    - CORS(교차 출처 정책)
      추가 HTTP 헤더를 사용해서 한 출처에서 실행 중인 웹 애플리케이션이 다른 출처의 자원에 접근할 수 있는 권한을 부여해서 브라우저에 알려주는 것
      ajax나 Fetch API가 다른 출처의 데이터를 가져와서 사용하기 위해서는 올바른 CORS 헤더를 포함한 응답을 반환해야 한다.
      서버를 만들 때 이 부분을 고려해서 작성을 해야하고 이미 만들어진 경우나 다른 곳에서 만든 API를 이용해야 하는 경우는 Proxy를 이용해야 한다.
    - ajax 오류 확인
      클라이언트 프로젝트의 routes 디렉토리의 index.js 파일에 라우팅 코드 추가
      ```jsx
      router.get("/", (req, res) => {
        res.render("main", { key: process.env.CLIENT_SECRET });
      });
      ```
      view 디렉토리에 main.html을 만들기
      ```jsx
      <!DOCTYPE html>
      <html lang="en">
        <head>
          <meta charset="UTF-8" />
          <meta http-equiv="X-UA-Compatible" content="IE=edge" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <title>Document</title>
        </head>
        <body>
          <div id="result">
            <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
            <script>
              axios
                .get("http://localhost:8000/v2/token", {
                  clientSecret: "{{key}}",
                })
                .then((res) => {
                  document.getElementById("result").innerHTML = JSON.stringify(
                    res.data
                  );
                })
                .catch((error) => {
                  console.error(error);
                });
            </script>
          </div>
        </body>
      </html>
      ```
    - CORS 설정
      서버 프로젝트에서 CORS 구현을 위한 패키지를 설치

      ```bash
      npm install cors
      ```

      v2 파일에 cors 관련 설정을 추가(v2.js 파일의 모든 요청에 대해 CORS 적용되도록)

      ```jsx
      const cors = require("cors");

      router.use(
        cors({
          credentials: true,
        })
      );
      ```

      v2 파일을 수정에서 키가 일치하는 경우에만 CORS가 적용되도록 수정

      ```jsx
      const cors = require("cors");
      const url = require("url");
      router.use(async (req, res, next) => {
        const domain = await Domain.findOne({
          where: { host: url.parse(req.get("origin")).host },
        });
        if (domain) {
          cors({
            origin: req.get("origin"),
            credentials: true,
          })(req, res, next);
        } else {
          next();
        }
      });
      ```

      (추가적인 헤더 설정 필요)

11. **기타**

    1. request 모듈

       node에서 다른 서버의 데이터 가져오기

    2. websocket

       클라이언트와 서버의 연결을 유지한 상태로 데이터를 주고받을 수 있는 HTML5 Spec

       https나 https는 연결을 유지하지 않고 header의 오버헤드가 크다.

       짧은 메시지를 자주 전송하는 시스템에서는 적합하지 않은 프로토콜

    3. push - Sever Sent Events

       클라이언트의 요청이 없어도 서버가 메시지를 전송하는 것.

       마지막 예제는 상품을 물건을 등록하고 1초마다 시간이 경과한 것을 서버가 클라이언트에게 전송을 하고 상품을 등록한지 하루가 지나면 낙찰을 해서 최고가를 입력한 유저에게 등록을 한다.
