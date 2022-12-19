# [React] router

1. **Routing**

   요청 URL에 따라 분기를 해서 출력을 하는 것

2. **SPA(Single Page Application)**

   1. Server Rendering

      웹 브라우저가 서버에게 요청을 전송하면 서버가 HTML을 전송해서 전체를 다시 출력하는 방식

      사용자와 인터랙션이 많은 웹 앱에서는 속도 측면에서 문제가 발생할 수 있다.

   2. SPA

      첫 번쨰 요청을 전송했을 때만 HTML이 전송되고 그 이후부터는 요청을 하면 서버가 JSON(XML도 가능) 형태의 데이터를 전송하고 브라우저가 이 데이터를 파싱해서 렌더링하는 구조

      단점은 앱의 규모가 커지면 자바스크립트 파일의 사이즈가 너무 커지게 돼서 트래픽과 로딩 속도에 문제가 발생할 수 있는데 이 문제를 해결하기 위해서는 Code Spliting을 이용해서 해결

      브라우저에서 자바스크립트 코드를 관리하는 경우 크롤러가 페이지의 정보를 제대로 받아가지 못하는 현상이 발생해서 검색 엔진에서 페이지의 정보를 검색 결과에 포함하지 못하는 경우가 발생.

      처음 자바스크립트가 실행될 때까지 페이지가 비어 있기 때문에 빈 페이지가 보여질수 있고 이를 방지하기 위해서는 첫 번째 페이지는 서버에서 렌더링을 해서 보여주고 다음 페이지부터 클라이언트 렌더링을 하는 것이 좋다.

      적절한 라우팅을 이용해서 여러 컴포넌트를 하나의 페이지에 출력

      라우팅에 많이 사용되는 라이브러리: react-router-dom(5버전과 6버전이 많이 다름)

3. **react-router-dom**

   1. URL에 컴포넌트 매핑

      ```jsx
      <Route path=URL element={출력하고자 하는 컴포넌트 } />
      ```

   2. App.js 수정

      ```jsx
      function App() {
        return (
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="about" element={<About />} />
          </Routes>
        );
      }
      ```

   3. 링크

      Link라는 컴포넌트 이용

      Link 컴포넌트를 이용하면 페이지를 새로 불러오지 않고 그대로 유지한 상태에서 HTML5 History API를 사용해서 페이지의 URL만 갱신

      실제로는 a 태그로 만들어져 있는 페이지 전환을 방지하는 기능을 추가

      ```jsx
      <Link to=URL>내용</Link>
      ```

   4. URL Prameter & Query String
      - URL Prameter
        URL의 마지막이나 중간에 데이터를 전송하는 것.
        `/profiles/guymin` → gyumin 부분을 데이터처럼 변경하면서 사용할 수 있는데 이를 URL Parameter라고 한다.
        이 경우 라우팅을 할 때 URL을 `/profiles/:이름` 의 형태로 작성.
      - Query String
        URL 뒤에 ? 를 추가하고 이름과 값을 전달할 때 사용
        `/profiles?name=gyumin&email=rhkdtlrtm12@gmail.com`
   5. Query String 읽기

      근본적으로 Query String은 Client가 Server에게 전달하는 데이터

      react 측면에서는 Query String을 읽는 것보다는 만드는 것이 중요

      만드는 것은 JavaScript 문법을 이용.

      Query String은 반드시 인코딩을 해서 만들어야 한다.

      - `useLocatin`이라는 Hook을 이용해서 location 객체를 리턴받아서 사용
      - location: 객체의 정보
      - pathname: query string을 제외한 경로
      - search: ?를 포함한 query string
      - hash: #문자열 형태의 값, segment라고 부르기도 하는데 하나의 페이지 내에서 이동하기 위해서 사용 예전의 구형 브라우저에서 클라이언트 라우팅을 할 떄 사용, jquery(Cross Browsing) mobile이 hash를 사용
      - state: 페이지 이동 시 임의로 넣을 수 있는 상태값.

      react-router-dom에서도 `useSearchParams` 라는 Hook으로 읽을 수 있다.

      `qs 라이브러리`를 이용하면 query string을 객체로 변환할 수 있다.

   6. Sub Routing

      Router 내부에 다시 Router를 만드는 것.

   7. 공통 레이아웃

      - 공통된 레이아웃을 위한 컴포넌트를 만들고 각 페이지 컴포넌트에서 직접 출력
      - 중첩된 라우트와 Outlet을 이용해서 구현 - 한 번만 설정하면 된다.
      - Layout.jsx 파일을 만들고 작성, App.js 파일 추가

        ```jsx
        import { Outlet } from "react-router-dom";

        const Layout = () => {
          return (
            <div>
              <header
                style={{ background: "lightgray", padding: 16, fontsize: 24 }}
              >
                Header
              </header>
              <main>
                <Outlet />
              </main>
            </div>
          );
        };

        // App.js
        function App() {
          return (
            <Routes>
              <Route element={<Layout />}>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<About />} />
                <Route path="/profile/:username" element={<Profile />} />
              </Route>
              <Route path="/articles" element={<Articles />} />
              <Route path="/articles/:id" element={<Article />} />
            </Routes>
          );
        }
        ```

   8. 라우팅에서의 index 속성

      index라는 props가 존재하는데 이 props가 “/” 이다.

   9. Router의 부가 기능
      - useNavigate
        Link 컴포넌트를 이용하지 않고 다른 페이지로 이동하고자 할 때 사용하는 Hook
        Redirect 하고자 하는 경우 사용
        useNavigate Hook이 리턴한 함수를 호출해서 처리할 수 있는데 매개변수로는 정수나 문자열 하나, 그리고 옵션을 설정한다
        - 첫 번째 매개변수가 숫자이면 숫자만큼 뒤나 앞으로 이동한다. 문자열이면 이동할 URL이 된다.
        - 두 번째 매개변수로 객체를 만들어서 replace 옵션에 true를 설정하면 현재 페이지에 대한 기록을 남기지 않는다.
      - NavLink
        Link와 거의 유사한데 현재 경로와 Link에서 사용하는 경로가 일치하는 경우 특정 스타일을 적용할 수 있도록 해주는 컴포넌트
      - Navigate
        화면에 보여지는 순간 다른 페이지로 이동하고자 할 때 사용하는 컴포넌트
   10. 404에러(클라이언트의 URL이 잘못된 경우)에 대한 대응

       Route를 만들 때 path를 “\*”로 설정하면 모든 경우에 반응하는데 이 컴포넌트를 Route의 맨 아래에 추가하면 앞의 path를 확인한 후 앞에 일치하는 path가 없으면 자신에게 설정된 컴포넌트를 출력
