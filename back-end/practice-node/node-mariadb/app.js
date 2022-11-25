const express = require("express");
const morgan = require("morgan");
const compression = require("compression");
const path = require("path");
const mysql = require("mysql");
const cookieParser = require("cookie-parser");
const session = require("express-session");
const multer = require("multer");
const dotenv = require("dotenv");
const FileStreamRotator = require("file-stream-rotator");
const fs = require("fs");

// 1. 설정 파일의 내용 가져오기
dotenv.config({ path: path.join(__dirname, ".env") });

// 2. 서버 설정
const app = express();
app.set("port", process.env.PORT || 9000);

// 5. 로그를 기록할 디렉토리 경로 생성
let logDirectory = path.join(__dirname, "log");

// 6. 로그가 없으면 생성
fs.existsSync(logDirectory) || fs.mkdirSync(logDirectory);

// 7. 로그 파일 옵션을 설정
let accessLogStream = FileStreamRotator.getStream({
  date_format: "YYYYMMDD",
  filename: path.join(logDirectory, "access-%DATE%.log"),
  frequency: "daily",
  verbose: false,
});

// 8. 로거 설정
app.use(morgan("combined", { stream: accessLogStream }));

// 9. 압축해서 전송하는 옵션 설정
app.use(compression());

// 10. POST 방식의 파라미터 읽을 수 있도록 설정
let bodyParser = require("body-parser");
app.use(bodyParser.json());
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
);

// 11. 데이터베이스 접속 정보
let options = {
  host: process.env.HOST,
  port: process.env.MYSQLPORT,
  user: process.env.USERNAME,
  password: process.env.PASSWORD,
  database: process.env.DATABASE,
};

// 12. 세션을 저장하기 위한 MySQL 데이터베이스 저장소 생성
const MariaDBStore = require("express-mysql-session")(session);

// 13. 세션 설정
app.use(
  session({
    secret: process.env.COOKIE_SECRET,
    resave: false,
    saveUninitialized: true,
    store: new MariaDBStore(options),
  })
);

// 14. 파일 업로드 설정
const upload = multer({
  storage: multer.diskStorage({
    destination(req, file, done) {
      done(null, __dirname + "/public/img");
    },
    filename(req, file, done) {
      const ext = path.extname(file.originalname);
      done(null, path.basename(file.originalname, ext) + Date.now() + ext);
    },
  }),
  limits: { fileSize: 10 * 1024 * 1024 },
});

// 15. 정적 파일의 경로를 설정
app.use("/", express.static(__dirname + "/public"));

// 16. 파일 다운로드를 위한 모듈
let util = require("util");
let mime = require("mime");

// 17. 데이터베이스 연결
let connection = mysql.createConnection(options);
connection.connect((error) => {
  if (error) {
    console.log(error);
    throw error;
  }
});

// 18. 기본 요청을 처리
app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
});

// 19. 데이터 전체 가져오기 처리
app.get("/item/all", (req, res) => {
  // HTML 출력: res.sendFile(파일 경로) 서버의 데이터 출력 못함 - ajax나 fetch api를 이용해야 함.
  // 템플릿 엔진: res.render(파일 경로, 데이터) - 템플릿 엔진에 넘겨주는 데이터는 프로그래밍 언어의 데이터
  // JSON 출력: res.json(데이터) - json 문자열의 형태로 데이터를 제공, Front End 에서 데이터를 수신해서 출력

  // 2개 이상의 데이터를 조회할 때는 정렬은 필수
  connection.query(
    "select * from goods order by itemid desc",
    (error, results, fields) => {
      if (error) {
        // 에러가 발생한 경우
        // 에러가 발생했다고 데이터를 전송하지 않으면 안됨.
        res.json({ result: false });
      } else {
        // 정상 응답을 한 경우
        res.json({ result: true, list: results });
      }
    }
  );
});

// 20. 데이터 일부분 가져오기
// URL은 /item/list
// 파라미터는 pageno 1개인데 없으면 1로 설정
app.get("/item/list", (req, res) => {
  // 파라미터 읽어오기
  let pageno = req.query.pageno;
  if (pageno == undefined) {
    pageno = 1;
  }
  console.log(pageno);
  // 브라우저에서 테스트
  // localhost:9000/item/list
  // localhost:9000/item/list?pageno=3

  // item 테이블에서 itemid를 가지고 내림차순 정렬해서 페이지 단위로 데이터 가져오기
  // select * from item order by itemid desc limit 시작번호, 데이터개수
  // 시작번호 = (pageno - 1) * 5
  // 파라미터는 무조건 문자열이다.
  // 파라미터를 가지고 산술연산을 할 때는 숫자로 변환을 수행
  // 성공과 실패 여부를 저장
  let result = true;
  // 성공했을 때 데이터를 저장
  let list;
  // 데이터 목록 가져오기
  connection.query(
    "select * from goods order by itemid desc limit ?, 5",
    [(+pageno - 1) * 5],
    (err, results, fields) => {
      if (err) {
        console.log(err);
        result = false;
      } else {
        list = results;
        // 테이블의 전체 데이터 개수를 가져오기
        let cnt = 0;
        connection.query(
          "select count(*) cnt from goods",
          [],
          (err, results, fields) => {
            if (err) {
              // 에러가 발생했을 떄
              result = false;
            } else {
              // 정상적으로 구문이 실행되었을 때
              // 하나의 행만 리턴되므로 0번쨰 데이터를 읽어내면 된다.
              cnt = results[0].cnt;
            }

            // 응답 생성해서 전송
            if (result == false) {
              res.json({ result: false });
            } else {
              res.json({ result: true, list: list, count: cnt });
            }
          }
        );
      }
    }
  );
});

// 21. 상세보기 처리를 위한 코드
app.get("/item/detail/:itemid", (req, res) => {
  // 파라미터 읽기
  let itemid = req.params.itemid;
  // itemid를 이용해서 1개의 데이터를 찾아오는 SQL을 실행
  connection.query(
    "select * from goods where itemid=?",
    [itemid],
    (err, results, fields) => {
      if (err) {
        console.log(err);
        res.json({ result: false });
      } else {
        res.json({ result: true, item: results[0] });
      }
    }
  );
});

// 22. 이미지 다운로드 처리
app.get("/img/:pictureurl", (req, res) => {
  let pictureurl = req.params.pictureurl;

  // 이미지 파일의 절대 경로를 생성
  let file =
    "/Users/gimgyumin/Documents/kakaoCloudSchool/back-end/practice-node/node-mariadb/public/img/" +
    pictureurl;

  console.log(__dirname);
  // 파일 이름을 가지고 타입을 생성
  let mimetype = mime.lookup(pictureurl);
  res.setHeader("Content-disposition", "attachment; filename=" + pictureurl);
  res.setHeader("Content-type", mimetype);

  // 파일의 내용을 읽어서 res에 전송
  let fileStream = fs.createReadStream(file);
  fileStream.pipe(res);
});

// 23. 현재 날짜를 문자열로 리턴하는 함수
const getDate = () => {
  let date = new Date();

  let year = date.getFullYear();
  let month = date.getMonth() + 1;
  let day = date.getDay();

  month = month >= 10 ? month : "0" + month;
  day = day >= 10 ? day : "0" + day;

  return year + "-" + month + "-" + day;
};

// 24. 날짜와 시간을 리턴하는 함수
const getTime = () => {
  let date = new Date();
  let hour = date.getHours();
  let minute = date.getMinutes();
  let second = date.getSeconds();

  hour = hour >= 10 ? hour : "0" + hour;
  minute = minute >= 10 ? minute : "0" + minute;
  second = second >= 10 ? second : "0" + second;

  return getDate() + " " + hour + ":" + minute + ":" + second;
};

// 25. 데이터 삽입을 처리해주는 함수
// pictureurl 이라는 url로 요청해야한다는 것을 프론트쪽에 알려줘야 함.
// 또한 body에 들어가는 이름들도 알려주어야 함.
app.post("/item/insert", upload.single("pictureurl"), (req, res) => {
  // 파라미터 읽어오기
  const itemname = req.body.itemname;
  const description = req.body.description;
  const price = req.body.price;

  // 파일 이름 - 업로드하는 파일이 없으면 default.jpg
  let pictureurl;
  if (req.file) {
    pictureurl = req.file.filename;
  } else {
    pictureurl = "default.jpg";
  }

  // 가장 큰 itemid 찾기
  connection.query(
    "select max(itemid) maxid from goods",
    [],
    (err, results, fields) => {
      let itemid;
      // 최대값이 있으면 + 1을 하고 없으면 1로 설정
      if (results.length > 0) {
        itemid = results[0].maxid + 1;
      } else {
        itemid = 1;
      }

      connection.query(
        "insert into goods(itemid, itemname, price, description, pictureurl, updatedate) values(?, ?, ?, ?, ?, ?)",
        [itemid, itemname, price, description, pictureurl, getDate()],
        (err, results, fields) => {
          if (err) {
            console.log(err);
            res.json({ result: false });
          } else {
            // 현재 날짜 및 시간을 update.txt에 기록
            const writeStream = fs.createWriteStream(__dirname + "/update.txt");
            writeStream.write(getTime());
            writeStream.end();
            res.json({ result: true });
          }
        }
      );
    }
  );
});

// 26. 데이터를 삭제하는 함수
app.post("/item/delete", (req, res) => {
  // post 방식으로 전송된 데이터 읽기
  let itemid = req.body.itemid;

  // itemid를 받아서 goods 테이블에서 삭제하기
  connection.query(
    "delete from goods where itemid=?",
    [itemid],
    (err, results, fields) => {
      if (err) {
        console.log(err);
        res.json({ result: false });
      } else {
        // 현재 날짜 및 시간을 update.txt에 기록
        const writeStream = fs.createWriteStream(__dirname + "/update.txt");
        writeStream.write(getTime());
        writeStream.end();
        res.json({ result: true });
      }
    }
  );
});

// 27. 수정을 get으로 요청했을 때 - 수정 화면으로 이동
app.get("/item/update", (req, res) => {
  // public 디렉토리의 update.html을 읽어내서 리턴
  fs.readFile(__dirname + "/public/update.html", (err, data) => {
    res.end(data);
  });
});

// 3. 에러 발생시 처리
app.use((err, req, res, next) => {
  console.log(err);
  res.status(500).send(err.message);
});

// 4. 서버 구동
app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 대기중");
});
