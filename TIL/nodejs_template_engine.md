# [Node.js] 템플릿 엔진

서버의 데이터를 HTML과 합쳐서 출력할 수 있도록 해주는 라이브러리

거의 모든 웹 프레임워크들이 가지고 있으며 종류는 다양함.

서버에서 뷰를 만들어서 클라이언트에게 제공하겠다라는 의미이다.

템플릿 엔진을 사용하려면 템플릿 엔진에 대한 별도의 학습(HTML, CSS, JavaScript 이외의 학습)이 필요하다.

1. **Jade**

   지금은 저작권 문제로 Pug로 개명

   [https://pugjs.org/api/getting-started.html에서](https://pugjs.org/api/getting-started.html에서) 도큐먼트 제공

   1. 설치

      ```bash
      npm install pug
      ```

   2. 설정

      ```jsx
      app.set("views", path.join(__dirname, "출력할 html 파일들의 위치"));
      app.set("view engine", "pug");
      ```

   3. 처리

      ```bash
      res.render('html 파일 경로', 데이터)
      ```

   4. html 파일에서 데이터를 pug 문법에 맞춰서 출력

2. **Nunjucks**
