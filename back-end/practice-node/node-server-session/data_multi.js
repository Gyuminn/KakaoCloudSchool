// 1. 웹 서버 모듈 가져오기
const express = require("express");

// 10. .env 파일의 내용을 읽어서 process.env에 저장해주는 모듈
const dotenv = require("dotenv");
dotenv.config();

// 2. 웹 서버 객체와 포트 생성
const app = express();
app.set("port", process.env.PORT);

// 11. 로그출력
// 로그는 공백으로 구분된다.
const morgan = require("morgan");
app.use(morgan("dev"));

// 12. post 방식의 파라미터를 읽을 수 있도록 설정
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

// 13. 쿠키 사용이 가능하도록 설정
const cookieParser = require("cookie-parser");
app.use(cookieParser(process.env.COOKIE_SECRET));

// 요청 처리 메서드 - get, post, put(patch), delete, options
// 응답 베서드 - send(직접 출력 내용 작성), sendFile(html 파일 경로), json(JSON 데이터)
// send와 sendFile은 서버에서 화면까지 만들겠다는 것이고 json은 화면은 안만든다.
// 4. path 모듈 가져오기
const path = require("path"); // 절대 경로 생성을 위해서 사용
// 포트번호(localhost:3000 -> ContextPath)까지 요청 처리

// 6. 세션 사용을 위한 모듈을 가져오기
const session = require("express-session");

// 8. 세션을 파일에 저장하기 위한 모듈 가져오기
const FileStore = require("session-file-store")(session);

// 7. 세션 사용을 위한 미들웨어 장착
// req.session으로 세션 객체 사용이 가능
app.use(
  session({
    secret: "keyboard cat",
    resave: false,
    saveUninitialized: true,
    // 9. store 설정
    store: new FileStore(),
  })
);

// 14. 파일을 업로드하기 위한 디렉토리를 생성
const fs = require("fs");
try {
  // 디렉토리를 읽는데 디렉토리가 없으면 예외가 발생
  fs.readdirSync("uploads");
} catch (error) {
  // 디렉토리를 생성
  fs.mkdirSync("uploads");
}

// 15. 파일 업로드 설정
const multer = require("multer");
const upload = multer({
  storage: multer.diskStorage({
    destination(req, file, done) {
      done(null, "uploads/");
    },
    filename(req, file, done) {
      file.originalname = Buffer.from(file.originalname, "latin1").toString(
        "utf8"
      );

      const ext = path.extname(file.originalname);
      done(null, path.basename(file.originalname, ext) + Date.now() + ext);
    },
  }),
  limits: { fileSize: 1024 * 1024 * 10 },
});

// 5. 사용자의 요청 처리
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "./index.html"));
});

// 16. 여러 개의 파일 업로드 처리
app.get("/multi", (req, res) => {
  res.sendFile(path.join(__dirname, "/data_multi.html"));
});

// 17. array의 'image'는 data_multi의 input title 이름과 동일해야 함.
app.post("/multi", upload.array("image"), (req, res) => {
  // res.send("성공");
  let result = { result: "success" };
  res.json(result);
});

// 3. 웹 서버 실행
app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 대기중");
});
