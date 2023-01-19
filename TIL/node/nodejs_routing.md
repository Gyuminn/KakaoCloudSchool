# [Node.js] Routing

최적의 경로를 탐색하는 것을 라우팅

1. **Node에서의 Routing**

   사용자의 요청을 처리하는 부분을 모듈화하는 것

   웹 애플리케이션 서버가 커지면 처리해야 할 URL이 늘어나는데 이를 하나의 파일에서 전부 처리하면 가독성이 떨어지게 되므로 url을 모듈화해서 처리

   `기본 요청`과 `user가 포함된 요청`과 `board가 포함된 요청`을 분리해서 구현

   index.js 파일을 만들고 기본 요청을 처리하는 코드를 작성

   ```jsx
   const express = require("express");
   const path = require("path");

   const router = express.Router();

   router.get("/", (req, res) => {
     res.sendFile(path.join(__dirname, "./index.html"));
   });
   module.exports = router;
   ```

   user.js & board.js 파일을 만들고 board가 포함된 요청을 처리하는 코드를 작성

   ```jsx
   const express = require("express");

   const router = express.Router();

   app.get("/", (req, res) => {
     res.send("Hello User"); // Board.js 에서는 Hello Board라고 써보자.
   });

   module.exports = router;
   ```

   app.js 파일에서 / 요청을 처리하는 부분을 삭제하고 추가

   ```jsx
   // 라우팅 관련 파일 가져오기
   const indexRouter = require("./index");
   const userRouter = require("./user");
   const boardRouter = require("./board");

   // url과 매핑
   app.use("/", indexRouter);
   app.use("/user", userRouter);
   app.use("/board", boardRouter);
   ```

   URL의 일부분을 파라미터로 사용하기

   최근에는 파라미터 1개인 경우 파라미터를 만들지 않고 URL에 포함시켜 전송을 한다.

   https://www.bloter.net/newsView/blt202211180006

   blt202211180006 이 블로그를 구분하는 아이디

   처리하는 URL을 설정할 때 `경로/:변수명` 의 형태로 작성한 후 내부에서 `req.params.변수명` 을 사용하면 된다.

   ```jsx
   app.get("https://www.bloter.net/newsView/:num", (req, res) => {
   		console.log(req.params.num); // blt202211180006이 출력된다.
   }|
   ```

2. **Front Controller 패턴**

   클라이언트의 모든 요청을 app.js가 받아서 각각의 라우팅 파일에서 실제로 처리를 한다.

   서버 애플리케이션 관련된 설정은 app.js 에서 하고 실제 처리는 각 라우팅 파일에서 수행하도록 한다.

   모든 요청이 먼저 들어오게 되는 app.js를 Front Controller라고 하고 실제 처리를 담당하는 라우팅 파일들을 Page Controller라고 한다.
