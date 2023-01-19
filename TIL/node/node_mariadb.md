# [Database] Maria DB 연동

1. **Maria DB 연동**

   1. 필요한 모듈
      - mysql
   2. 필요한 정보
      - 연결할 데이터베이스를 소유하고 있는 컴퓨터의 IP나 도메인과 포트번호
        localhost(127.0.0.1, ;;1):3306
      - 사용할 데이터베이스 이름(sid라고 하기도 함)
      - 계정: 아이디와 비밀번호
        root: root
   3. 연결

      ```jsx
      // 모듈 가져오기
      const mariadb = require('mysql');

      // 접속 정보 가져오기
      let connection = mariadb.createConnection({
      	host:'아이피나 도메인',
      	port: 포트번호,
      	user: '아이디',
      	password: '비밀번호',
      	database: '데이터베이스 이름',
      });

      // 연결
      connection.connect(function(error) {
      	if (errro) {
      		// 에러가 발생했을 때 수행할 내용
      	}
      });
      데이터베이스 연결이 되었을 때 수행할 내용
      ```

2. **SQL 실행**

   1. SELECT가 아닌 구문

      결과가 성공과 실패 또는 영향받은 개수의 형태

      ```jsx
      연결객체.query(SQL, [파라미터배열]);
      ```

      파라미터 배열은 SQL을 작성할 때 값의 자리에 직접 값을 작성하지 않고 ?로 설정한 후 나중에 값을 대입할 수 있다.

      ```jsx
      // 연결
      connection.connect((error) => {
        if (error) {
          console.log(error);
        } else {
          console.log(connection);

          // 테이블 생성 구문
          connection.query(
            "create table family(id int auto_increment primary key, name varchar(20))"
          );

          // 데이터 삽입 구문
          connection.query("insert into family(name) values(?)", "을지문덕");
          connection.query("insert into family(name) values(?)", "강감찬");
          connection.query("insert into family(name) values(?)", "조헌");
        }
      ```

      데이터베이스 접속 도구에서 SELECT \* FROM family로 데이터를 조회.

   2. SELECT 구문

      조회한 결과(Cursor 또는 하나의 객체나 배열)

      SELECT 구문은 콜백 함수를 매개변수로 추가하는데 콜백함수의 매개변수가 3개이다.

      첫 번째는 에러 객체이고, 두 번째는 검색된 내용인데 javascript 객체 형태로 제공되고 세 번째는 meta data로 검색된 결과에 대한 정보이다.

      화면에 출력할 것이라면 javascript 객체를 그대로 이용하면 되고 데이터 형태로 제공하고자 하면 JSON 문자열로 변환하면 된다.

   3. 테이블 1개를 SQL을 이용해서 연동

      - 기능

        - 테이블의 데이터 전체를 가져오기
        - 테이블의 데이터 일부분을 가져오기(페이지 단위로 가져오기)
        - 기본 키를 이용해서 데이터를 1개 가져오기
        - 데이터의 삽입, 삭제, 갱신
        - 파일 업로드와 다운로드
        - 가장 최근에 데이터를 수정한 시간을 기록하고 조회
            <aside>
            ▪️ 서버와 클라이언트의 데이터 교환
            - 접속할 때 마다 서버의 데이터를 가져와서 출력
            - 서버의 데이터를 클라이언트에 저장하고 접속할 때마다 서버의 데이터와 클라이언트의 데이터를 비교해서 수정이 발생했을 때만 업데이트 → 서버의 데이터와 클라이언트의 데이터를 비교할 수 있어야 하는데 가장 쉬운 방법은 양쪽에서 수정한 시간을 기록한 후 클라이언트의 수정 시간이 서버의 데이터 수정 시간보다 이전이면 업데이트를 수행.
            
            </aside>

      - 샘플 데이터 생성
        DBeaver켜서 작성.

3. **기본 설정**

   1. 모듈 설치

      ```bash
      npm install express morgan multer mysql cookie-parser express-session
      express-mysql-session dotenv compression file-stream-rotator
      ```

      - express: 웹 서버 모듈
      - morgan: 로그 기록을 위한 모듈
      - file-stream-rotator: 로그를 파일에 기록하기 위한 모듈
      - multer: 파일 업로드 처리를 위한 모듈
      - mysql: mysql이나 mariadb를 사용하기 위한 모듈
      - cookie-parser: 쿠키를 사용하기 위한 모듈
      - express-session: 세션을 사용하기 위한 모듈
      - express-mysql-session: 세션을 mysql이나 maria db에 저장하기 위한 모듈
      - dotenv: .env 파일의 내용을 process.env로 저장해서 사용하기 위한 모듈
      - compression: 서버가 처리한 결과를 압축해서 클라이언트에게 전송하기 위한 모듈

   2. 개발 모드로 설치

      ```bash
      npm istall --save-dev nodemon
      ```

      nodemon은 소스 코드를 수정하면 자동으로 재시작할 수 있도록 해주는 모듈

      package.json 파일의 scripts 부분에 “start”:”nodemon app”, 을 추가하면 좋다.

      npm start 명령을 사용하면 nodemon app 이라는 명령이 수행되서 entry point로 설정한 파일을 실행시켜주는 역할을 한다.

   3. 서버의 데이터 업데이트 시간을 기록할 update.txt 파일을 프로젝트에 생성
   4. 프로젝트에 .env 파일을 만들고 데이터베이스 정보를 기재

      <aside>
      ▪️ 정적 파일은 웹에서는 소스 코드를 제외하고 서버의 데이터를 출력하는 용도가 아닌 파일
      html(서버의 데이터를 출력할 때는 아닐 수 있다), css, js, 이미지, 사운드, 동영상 등

      </aside>

   5. 서버 구동하고 확인
   6. 프로젝트에 public 디렉토리 생성하고 샘플 이미지 넣어주기
   7. 메인화면 설정(html, css 등)
   8. 데이터베이스에서 아래 SQL을 실행해서 세션이 만들어지고 있는지 확인

      ```sql
      SELECT * FROM sessions;
      ```

4. **데이터 전체 가져오기**

   - 원하는 url(예시 - item/all)
   - app.js 파일에 소스 코드 추가

     ```jsx
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
     ```

   - HTML 조작

5. **데이터 일부분 가져오기**

   데이터의 일부분 가져오기를 처리할 때는 일반적으로 2개의 데이터가 필요하나의 페이지 번호는 시작하는 데이터의 번호(시작하는 데이터 번호를 주는 경우는 데이터의 개수가 고정인 경우가 많다.)

   다른 하나는 한 페이지에 출력할 데이터의 개수

   1. get 방식에서의 서버로의 데이터 전달

      - 전달하고자 하는 데이터가 1개인 경우
        - 파라미터로 전달: `URL?이름=값`
        - URL에 전달: `URL/값` - 최근에는 이 방식을 선호
      - 전달하고자 하는 데이터가 2개 이상인 경우는 파라미터 형태로 전달
        - `URL?이름=값&이름=값…`

      ```jsx
      // URL 결정 - /item/list
      // 파라미터 읽기: req.query.파라미터이름 (많이 안씀)
      // URL 읽기: req.params.파라미터이름
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
      ```

      <aside>
      ▪️ 서버는 클라이언트의 데이터를 정확하게 읽어내야 한다.
      테스트를 할 때는 클라이언트가 데이터를 전송한 경우와 그렇지 않은 경우를 나누어서 확인해야 한다.
      웹 프로그래밍에서는 요청 방식이 get과 post 2가지가 기본 형식이므로 get 방식일 때와 post 방식일 때 모두 읽어내야 한다.

      </aside>

      <aside>
      ▪️ MySQL이나 MariaDB에서 데이터의 일부분을 가져오기(TOP-N)
      SELECT 구문의 마지막에 limit(시작하는 번호, 건너뛸 데이터 개수)를 추가해주면 된다.
      데이터개수: 10
      1페이지의 시작번호는 0
      2페이지의 시작번호는 10
      3페이지의 시작번호는 20
      4페이지의 시작번호는 30

      건너뛸 데이터의 개수 = (페이지번호 - 1) \* 데이터개수

      페이지 번호가 전체 데이터 개수보다 커지는 상황이 발생하면 데이터를 하나도 못찾아온다. 클라이언트가 서버에게 전체 데이터 개수보다 더 뒤에 있는 데이터를 달라고 요청을 하면 서버는 데이터베이스에 요청을 하고 처리하려고 한다.
      클라이언트 측에서 서버에게 전체 데이터 이후의 데이터를 요청하지 못하도록 하고자 하면 이 경우에는 전체 데이터의 개수를 전달해주면 된다.

      </aside>

   2. index.html 파일에 페이지 단위 보기 구현

      body 부분에 요청을 할 DOM 설정

6. **데이터 상세보기**

   데이터 한 개의 정보를 전부 가져와서 출력

   테이블에서 데이터 1개를 가져오는 방법은 기본키나 unique 속성을 이용한 조회만 가능.

   기본키나 unique 속성의 값을 받아서 서버에서 처리

   itemid를 URL에 포함시켜 받아서 처리하는 구조로 구현

   1. App.js 파일에 상세보기를 위한 코드를 추가

      ```jsx
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
      ```

   2. index.html 파일에서 상세보기를 위한 코드를 작성

7. **첨부 파일(이미지) 다운로드**

   웹 브라우저에서는 파일에 링크가 걸려있으면 자신이 출력할 수 있는 파일은 출력을 하고 출력할 수 없는 파일은 다운로드를 수행.

   1. 이미지 파일 출력하는 부분을 수정

      ```jsx
      display +=
        "<tr><td><a href='/img/" +
        item.pictureurl +
        "'/>" +
        "<img src='/img/" +
        item.pictureurl +
        "'/></td>";
      ```

8. **데이터 삽입**

   데이터 삽입은 삽입 화면을 먼저 출력하고 데이터를 입력받은 후 데이터를 서버에게 전송하면 처리

   데이터 삽입은 get 방식이 아닌 post 방식으로 처리

   post 방식은 화면을 만들지 않고 웹브라우저에서 테스트가 불가능

   서버에 post 방식 요청을 만들고 테스트를 할 때는 별도의 프로그램을 설치(postman)해서 하게 된다.

   1. app.js 파일에 데이터 삽입을 위한 코드를 작성

      어떤 데이터를 받아야하는지 결정

      - itemid: 가장 큰 itemid를 찾아서 +1
      - itemname, price, description, pictureurl(파일)은 직접 입력
      - updatedate는 현재 날짜를 문자열로 입력

      삽입 삭제, 갱신 작업이 발생하면 updatedate.txt 파일에 발생한 시간을 기록.

      현재 데이터가 업데이트된 시간을 알기 위해서 기록.

   2. index.html 파일에 삽입 요청을 위한 코드를 작성

      삽입 요청을 위한 DOM을 추가

9. **데이터 삭제**

   기본 키를 매개변수로 받아서 삭제를 한다.

   삭제는 GET이나 POST 별 상관 없다.

   삽입 요청과 동일하고 delete query를 날리면 된다.

10. **데이터 수정**

    현재 데이터를 수정할 수 있도록 화면에 출력하고 데이터를 입력받아서 수정

    텍스트는 입력받은 내용을 수정하면 되는데 이전 내용을 화면에 출력한 상태에서 내용을 고치지 않았어도 set으로 출력하면 되는데 파일은 input에 설정할 수 없다.(value 설정 불가능)

    1. 수정 화면을 위한 html 파일을 생성
    2. app.js 파일에 수정의 get 요청이 오면 처리할 함수를 작성

       ```jsx
       // 수정을 get으로 요청했을 때 - 수정 화면으로 이동
       app.get("/item/update", (req, res) => {
         // public 디렉토리의 update.html을 읽어내서 리턴
         fs.readFile("./public/update.html", (err, data) => {
           res.end(data);
         });
       });
       ```

    3. index.html 파일에 수정 화면으로 이동하기 위한 코드를 작성

       상세보기를 출력하는 부분에 데이터 수정 DOM을 추가(삭제를 추가한 부분의 위나 아래에 추가)
