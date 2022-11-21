# [Node.js] express session 심화

1. **세션 활용**

   Session

   클라이언트의 정보를 서버에 저장하는 객체

   기본적으로 서버의 메모리에 만들어짐.

   세션이 크거나 많아지면 서버의 메모리에 부담을 주게 되어서 서버의 성능을 저하시킬 수 있기 때문에 `파일`이나 `데이터베이스`에 저장하는 것을 고려할 필요가 있음.

   1. 설치한 패키지

      ```bash
      npm install express
      npm install express-session
      npm install morgan
      npm install cookie-parser

      npm install --saved-dev nodemon
      ```

   2. 세션 활용

      ```jsx
      // 1. 웹 서버 모듈 가져오기
      const express = require("express");

      // 2. 웹 서버 객체와 포트 생성
      const app = express();
      app.set("port", 3000);

      // 요청 처리 메서드 - get, post, put(patch), delete, options
      // 응답 베서드 - send(직접 출력 내용 작성), sendFile(html 파일 경로), json(JSON 데이터)
      // send와 sendFile은 서버에서 화면까지 만들겠다는 것이고 json은 화면은 안만든다.
      // 4. path 모듈 가져오기
      const path = require("path"); // 절대 경로 생성을 위해서 사용
      // 포트번호(localhost:3000 -> ContextPath)까지 요청 처리

      // 6. 세션 사용을 위한 모듈을 가져오기
      const session = require("express-session");

      // 7. 세션 사용을 위한 미들웨어 장착
      // req.session으로 세션 객체 사용이 가능
      app.use(
        session({
          secret: "keyboard cat",
          resave: false,
          saveUninitialized: true,
        })
      );

      // 5. 사용자의 요청 처리
      app.get("/", (req, res) => {
        res.sendFile(path.join(__dirname, "./index.html"));
      });

      // 3. 웹 서버 실행
      app.listen(app.get("port"), () => {
        console.log(app.get("port"), "번 포트에서 대기중");
      });
      ```

      세션을 메모리에 저장하게 되면 재사용이 안되고 다른 서버와의 공유도 어렵기 때문에 세션을 파일에 저장해서 공유하고 재사용 가능하도록 만들 수 있다.

   3. 필요한 의존성을 설치

      ```bash
      npm install session-file-store
      ```

      app.js 파일에 설정을 추가하고 session 설정 미들웨어를 수정

      ```jsx
      const FileStore = require("session-file-store")(session);

      app.use(
        session({
          secret: "keyboard cat",
          resave: false,
          saveUninitialized: true,
          store: new FileStore(),
        })
      );
      ```

2. **미들웨어 사용**

   1. morgan, cookie-parser, dotenv 설치

      ```bash
      npm install morgan cookie-parser dotenv
      ```

   2. 프로젝트에 .env 파일을 생성하고 작성

      여기에 작성된 내용은 node 프로그램에서 `process.env` 이름으로 사용가능.

   3. multer - 파일 업로드 처리

      web service에서 파일을 업로드하려면 `multipart/form-data` 형태로 데이터를 전송해야 한다.

      node에서 multer를 가지고 파일 업로드를 처리할 때는 4가지로 나누어서 처리한다.

      - none: 파일 업로드가 없을 때
      - single: 하나의 파일만 업로드 될 때
      - array: 한 번에 여러 개의 파일이 업로드 가능한데 하나의 파라미터로 업로드
      - fields: 여러 개의 파일을 여러 개의 파라미터로 업로드 하는 경우 사용

      파일 업로드 처리를 할 때 파일의 이름을 유일 무이하게 변경하는 경우가 있음.

      이런 경우에는 UUID(유일한 문자열)나 현재 시간을 파일 이름에 추가해서 생성하는 것이 일반적이다.

      `실제 운영을 할 떄는 애플리케이션 서버 디스크가 아닌 별도의 디스크(AWS의 S3 서비스나 Google의 Firebase 서비스 등)에 저장을 해야 한다.`

      저장을 할 때 디렉토리는 미리 만들어져 있어야 한다.

      ```bash
      npm install multer
      ```

      app.js에 파일의 디렉토리를 생성해주는 코드를 추가

      ```jsx
      // 파일을 업로드하기 위한 디렉토리를 생성
      const fs = require("fs");
      try {
        // 디렉토리를 읽는데 디렉토리가 없으면 예외가 발생
        fs.readdirSync("uploads");
      } catch (error) {
        // 디렉토리를 생성
        fs.mkdirSync("uploads");
      }
      ```

      app.js 파일에 파일을 업로드 해주는 설정을 추가

      ```jsx
      // 파일 업로드 설정
      const multer = require("multer");
      const upload = multer({
        sotrage: multer.diskStorage({
          destination(req, file, done) {
            done(null, "uploads/");
          },
          filename(req, file, done) {
            const ext = path.extname(file.originalname);
            done(
              null,
              path.basename(file.originalname, ext) + Date.now() + ext
            );
          },
        }),
        limits: { fileSize: 1024 * 1024 * 10 },
      });
      ```

      app.js 파일에 처리하는 코드를 추가

      ```jsx
      // 하나의 파일 업로드 처리
      app.get("/single", (req, res) => {
        res.sendFile(path.join(__dirname, "/data_single.html"));
      });

      // single의 'image'는 data_signle의 input title 이름과 동일해야 함.
      app.post("/single", upload.single("image"), (req, res) => {
        // title 파라미터 읽기
        // post 방식에서의 파라미터는 req.body.파라미터이름
        console.log(req.body.title);
        console.log(req.file.originalname);
        res.send("성공");
      });
      ```

      파일에 한글이 포함되어 있을 때 한글이 깨지는 문제는 파일을 업로드할 때 파일의 원래 이름을 같이 전송해서 파일의 원래 이름을 데이터베이스에 저장한 뒤 다운로드할 때 파일을 변경해서 저장하는 방법으로 해결이 가능 → app.js 파일의 파일 업로드 방법을 수정한다.

      ```jsx
      filename(req, file, done) {
            file.originalname = Buffer.from(file.originalname, "latin1").toString(
              "utf8"
            );

            const ext = path.extname(file.originalname);
            done(null, path.basename(file.originalname, ext) + Date.now() + ext);
          }
      ```

      여러 개의 파일 업로드 - 폼의 내용을 ajax로 전송

      ```jsx
      <script>
            document.getElementById("btn").addEventListener("click", () => {
              // 전송할 폼 데이터 생성
              const formData = new FormData();

              // 내용 추가
              let files = docuemnt.getElementById("image").files;
              for (let i = 0; i < files.length; i++) {
                formData.append("image", document.getElementById("image").files[i]);
              }
              forData.append("title", document.getElementById("title").value);
            });

            // ajax를 이용해서 전송
            let xhr = new XMLHttpRequest();
            xhr.open("POST", "/data_multi", true);
            xhr.send(formData);
            xhr.addEventListener("load", (e) => {
      					// 서버가 JSON 문자열을 파싱
                let text = xhr.responseText;
                let result = JSON.parse(text);
                // 파싱한 결과를 출력
                alert(result.result);
            });
          </script>
      ```

      ```jsx
      // 여러 개의 파일 업로드 처리
      app.get("/multi", (req, res) => {
        res.sendFile(path.join(__dirname, "/data_multi.html"));
      });

      // array의 'image'는 data_multi의 input title 이름과 동일해야 함.
      app.post("/multi", upload.array("image"), (req, res) => {
        // res.send("성공");
        let result = { result: "success" };
        res.json(result);
      });
      ```

      여러 개의 파라미터로 전송하는 경우

      ```jsx
      upload.fields([{name:파라미터이름}, {name:파라미터이름},...]);
      ```
