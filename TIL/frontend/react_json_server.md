# [React] JSON Server

1. **패키지를 설치하고 data.json 파일을 생성**

   ```bash
   yarn add json-server
   ```

   ```jsx
   {
       "posts":[
           {
               "id":1,
               "content":"리액트",
               "done":true
           },
           {
               "id":2,
               "content":"Node",
               "done":true
           },
           {
               "id":3,
               "content":"Java",
               "done":true
           },
           {
               "id":4,
               "content":"Spring",
               "done":true
           }
       ]
   }
   ```

2. **서버 실행**

   ```bash
   json-server ./data.json --port 4000
   ```

   브라우저에서 확인

   `localhost:4000/posts`

   `localhost:4000/posts/1`

3. **react 프로젝트에서 외부 서버의 데이터를 proxy를 통해서 가져오기**

   `예를 들어 리액트 프로젝트는 포트번호 3000번이고 서버가 4000번이라면 도메인이 다르기 때문에 서버 데이터를 가져올 수 없다.`

   1. package.json 파일에 들어가서 proxy 설정

      ```jsx
      ...
      "browserslist": {
          "production": [
            ">0.2%",
            "not dead",
            "not op_mini all"
          ],
          "development": [
            "last 1 chrome version",
            "last 1 firefox version",
            "last 1 safari version"
          ]
        },
        "proxy":"http://localhost:4000"
      }
      ```

   2. 서버의 데이터 요청 URL을 수정

      [http://localhost:4000/posts](http://localhost:4000/posts) ⇒ `/posts`

      [http://localhost:4000/posts/1](http://localhost:4000/posts/1) ⇒ `/posts/1`
