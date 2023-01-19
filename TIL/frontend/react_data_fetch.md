# [React] data fetch

https://jsonplaceholder.typicode.com/users

1. **서버에서 데이터 받아오는 방법**

   ajax 이용 - load 이벤트와 error 이벤트

   ```jsx
   onClick={(e) => {
             let request = new XMLHttpRequest();
             request.open("GET", "https://jsonplaceholder.typicode.com/users");
             // POST 방식일 때는 send에 파라미터를 대입
             request.send("");
             request.addEventListener("load", () => {
               let data = JSON.parse(request.responseText);
               console.log(data);
             });
             request.addEventListener("error", (error) => {
               console.log(error);
             });
           }}
   ```

   fetchAPI 이용

   ```jsx
   onClick={(e) => {
             fetch("https://jsonplaceholder.typicode.com/users")
               .then((response) => response.json())
               .then((data) => console.log(data))
               .catch((error) => console.log(error));
           }}
   ```

   axios 이용

2. **서버 설정(node에서)**

   1. node server에서 cors 설정

      ```bash
      yarn add cors
      ```

   2. 서버 실행 파일에 추가

      ```jsx
      const cors = require("cors");
      app.use(cors());
      ```

   3. 서버를 수정할 수 없을 때

      - proxy를 설정하는 방법
        package.json 파일에 설정을 추가
        ```jsx
        "proxy":"서버의 도메인"
        ```
        요청을 할 때 `/api/도메인/url` 로 적어주면 된다.
      - 라이브러리를 이용하는 방법(위의 방법이 안될 수 있다)

        ```bash
        yarn add http-proxy-middleware로 라이브러리를 설치
        ```

        `src 디렉토리에 setupProxy.js` 파일을 만들고 작성

        ```bash
        const {createProxyMiddleware} = require('http-proxy-middleware');

        module.exports = (app) => {
        	app.use(createProxyMiddleware('/클라이언트공통URL', {
        		target:'서버의 URL',
        		changeOrigin:true,
        	})
        )}
        ```
