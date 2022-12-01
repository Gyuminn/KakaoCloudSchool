# [Database] Authentication

1. **Authentication(인증)과 Authorization(인가)**
   - 인증: 계정 관련, 로그인 관련
   - 인가: 권한 관련
2. **인증을 구현하는 방법**

   - 로컬 로그인: 회원 정보를 저장하고 있다가 인증
     회원 정보를 저장할 때는 비밀번호는 복호화가 불가능한 방식을 사용하고 개인을 식별할 수 있는 정보를 마스킹 처리를 하거나 복호화가 가능한 방식의 암호화를 활용해야 한다.
   - OAuth(공통된 인증 방식) 로그인: 다른 서버에 저장된 인증 정보를 활용해서 인증을 하는 방식

3. **인증을 위한 프로젝트 기본 설정**

   로그인을 할 수 있도록 회원 가입을 하고 로그인 처리를 수행하고 간단한 글과 파일을 업로드할 수 있는 프로젝트

   1. 프로젝트 생성
   2. 필요한 패키지 설치

      ```bash
      npm install express morgan dotenv compression morgan file-stream-rotator
      multer cookie-parser express-session express-mysql-session mysql2 sequelize
      sequelize-cli nunjucks

      npm install --save-dev nodemon
      ```

   3. package.json 파일을 수정

      ```bash
      "scripts" {
      	"start": "nodemon app",
      	"test" "test"
      }
      ```

   4. sequelize(node의 ORM) 초기화

      ```bash
      npx sequelize init
      ```

   5. 디렉토리 생성
      - `views(View)`: 화면에 출력할 파일이 저장되는 디렉토리
      - `routes(Controller)`: 사용자의 요청이 왔을 때 처리하는 라우팅 파일이 저장되는 디렉토리
      - `public(resource)`: 정적인 파일들이 저장되는 디렉토리
      - `App.js(Front Controller)`: 모든 클라이언트의 요청이 들어오는 곳
      - `routes 디렉토리 안의 js 파일(PageController)`: 특정 요청에 대한 처리를 수행
   6. 프로젝트에 .env 파일을 생성하고 작성
      - 소스 코드에 노출되서 안되는 내용이나 `개발 환경에서 운영 환경으로 이행(Migration)`할 때 변경될 내용을 작성하는데 이 내용은 실행 중에는 변경되지 않는 내용이어야 한다.
      - 대표적인 내용이 데이터베이스 접속 정보나 암호화를 하기 위한 키 또는 서버 포트 번호와 같은 것들이다.
      - 이러한 내용은 대부분 실행 중에는 변경되지 않지만 개발 환경에서 운영 환경으로 이행할 때 변경될 가능성이 높은 내용이다.
   7. 기본적인 처리
      - routes 디렉토리에 page.js 파일을 만들고 메인화면, 프로필화면, 회원 가입 화면을 위한 라우팅 코드를 작성
      - 공통된 레이아웃을 위한 내용을 views 디렉토리에 layout.html 파일을 만들고 생성
      - 메인 화면을 위한 내용을 views 디렉토리에 main.html 파일을 만들고 작성
      - 팔로워 목록을 출력할 내용을 views 디렉토리에 profile.html 파일을 생성하고 작성
      - 회원가입 화면을 위한 내용을 views 디렉토리에 join.html 파일을 만들고 작성

4. **데이터베이스 작업**

   1. 테이블 구조

      - user 테이블

        - 이메일
        - 닉네임
        - 비밀번호
        - 로그인 방법: 직접 로그인했는지 아니면 카카오로 로그인했는지 여부를 저장
        - 카카오아이디

        - 생성시간
        - 수정시간
        - 삭제시간 - 삭제할 때 실제 지워지지 않고 삭제한 시간을 기록

      - post 테이블
        - 게시글 내용
        - 이미지 파일의 경로
      - HashTag 테이블
        - 태그 이름

   2. 테이블 관계
      - User와 Post는 1:N 관계
      - HashTag와 Post는 N:N 관계
      - User와 User는 N:N 관계
   3. models 디렉토리에 user 테이블의 모델을 위한 user.js 파일을 생성하고 작성
   4. models 디렉토리에 post 테이블의 모델을 위한 post.js 파일을 생성하고 작성
   5. models 디렉토리에 hashtag 테이블의 모델을 위한 hashtag.js 파일을 생성하고 작성
   6. 데이터베이스 설계

      저장할 항목, 그리고 테이블 관의 관계 파악

   7. config 디렉토리의 config.json 파일의 내용을 수정해서 데이터 접속 정보 수정
   8. 데이터베이스가 존재하지 않는 경우 아래 명령을 한 번 수행

      `npx sequelize-cli db:create`

   9. 서버를 실행하면 데이터 베이스에 4개의 테이블 생성

      snsuser, posts, hashtags, PostHashtag

5. **Passport 모듈**

   Node에서 인증 작업을 도와주는 모듈

   세션이나 쿠키를 직접 처리하지 않고 이 모듈의 도움을 받으면 쉽게 구현이 가능하다.

   Social 로그인 작업을 쉽게 처리할 수 있도록 해준다.

   https://www.passportjs.org

   <aside>
   ▪️ 인증 작업
   로그인에 성공하면 세션을 생성해서 세션에 아이디나 기타 정보를 저장하고 다음부터 로그인을 확인할 때는 세션의 정보가 있는지를 확인해서 로그인 여부를 판단하고 로그아웃을 하면 세션의 정보를 삭제한다.

   </aside>

6. **로컬 로그인 구현**

   1. 필요한 모듈 설치

      passport, passport-local, bcrypt

   2. App.js 파일에 필요한 기능 추가

      ```jsx
      // 2. 로컬 로그인 구현
      const passport = require("passport");
      const passportConfig = require("./passport");
      // passport에서 모듈을 함수로 내보내주고 여기서 함수로 실행
      passportConfig();
      app.use(passport.initialize());
      // 세션 기능은 passport 모듈이 알아서 사용
      app.use(passport.session());
      ```

   3. passport의 index.js 파일에 기능 추가

      ```jsx
      module.exports = () => {
        // 로그인 성공했을 떄 정보를 deserializeUser 함수에게 넘기는 함수
        passport.serializeUser((user, done) => {
          done(null, user.id);
        });

        // 넘어온 id에 해당하는 데이터가 있으면 데이터베이스에서 찾아서 세션에 저장
        passport.deserializeUser((id, done) => {
          User.findOne({ where: { id } })
            .then((user) => done(null, user))
            .catch((err) => done(err));
        });
        local();
      };
      ```

      이렇게 하면 로그인 여부를 request 객체의 isAuthenticated() 함수로 할 수 있게 된다.

      <aside>
      ▪️ 로그인 여부 판단
      웹 애플리케이션을 구현하게 되면 로그인 여부를 판단해서 작업을 해야 하는 경우가 발생하는데 이는 비즈니스 로직과는 상관이 없음. 이런 부분은 별도로 처리해야 한다.
      node는 이런 로직을 middleware로 처리하고 java web에서는 Filter로 처리하고 Spring에서는 AOP나 Interceptor를 이용해서 처리

      </aside>

   4. 로그인 여부를 판단할 수 있는 함수를 routes 디렉토리의 middlewares.js 파일을 추가하고 작성

      ```jsx
      // 로그인 여부 판단
      exports.isLoggedIn = (req, res, next) => {
        if (req.isAuthenticated()) {
          next();
        } else {
          res.status(403).send("로그인 필요");
        }
      };

      exports.isNotLoggedIn = (req, res, next) => {
        if (!req.isAuthenticated()) {
          next();
        } else {
          // 메시지를 생성하는 query string(parameter)로
          // 사용할 것이라서 encoding을 해주어야 한다.
          const message = encodeURIComponent("로그인 한 상태입니다.");
          // 이전 request 객체의 내용을 모두 삭제하고
          // 새로운 요청 흐름을 만드는 것으로 새로 고침을 하면
          // 결과 화면만 새로고침 된다.
          res.redirect(`/?error=${message}`);
        }
      };
      ```

   5. routes 디렉토리의 page.js 파일 수정

      ```jsx
      // 1. 공통된 처리 - 무조건 수행
      router.use((req, res, next) => {
        // 변수 초기화
        // 로그인한 유저 정보
        // 6. 유저 정보를 res.locals.user에 저장
        res.locals.user = req.user;
        // 게시글을 follow하고 되고 있는 개수
        res.locals.followCount = 0;
        res.locals.followingCoung = 0;
        // 게시글을 follow하고 있는 유저들의 목록
        res.locals.followerIdList = [];
        next();
      });

      // 3. 회원 가입 - 7. 로그인이 되어있지 않은 경우만 수행
      router.get("/join", isNotLoggedIn, (req, res, next) => {
        res.render("join", { title: "회원 가입 - NodeAuthentication" });
      });

      // 4. 프로필 화면 처리 - 8. 로그인이 되어 있는 경우에만 처리
      router.get("/profile", isLoggedIn, (req, res, next) => {
        res.render("join", { title: "나의 정보 - NodeAuthentication" });
      });
      ```

7. **회원 가입, 로그인, 로그아웃 처리를 위한 내용을 routes 디렉토리에 auth.js 파일을 만들고 작성**

   이 내용은 page.js에 작성해도 된다.

   page.js는 화면을 보여주는 역할을 하고 auth.js는 처리하는 역할을 하도록 분리하는 것도 좋다.

   ```jsx
   const express = require("express");

   // 로그인 및 로그아웃 처리를 위해서 가져오기
   const passport = require("passport");

   // 회원 가입을 위해서 가져오기
   const bcrypt = require("bcrypt");

   const { isLoggedIn, isNotLoggedIn } = require("./middlewares");

   const User = require("../models/user");

   const router = express.Router();

   // 회원 가입 처리 - 원래는 /auth/join 인데 라우팅 할 때 /auth 추가
   router.post("/join", isNotLoggedIn, async (req, res, next) => {
     // 데이터 찾아오기
     const { email, nick, password } = req.body;
     try {
       // email 존재 여부 확인
       const exUser = await User.findOne({ where: { email } });

       // 이미 존재하는 이메일
       if (exUser) {
         // 회원 가입 페이지로 리다이렉트 하는데
         // error 키에 메시지를 가지고 이동
         return res.redirect("/join?error=exist");
       } else {
         // 비밀번호를 해싱
         const hash = await bcrypt.hash(password, 12);
         // 저장
         await User.create({ email, nick, password: hash });
         return res.redirect("/");
       }
     } catch {
       console.log(error);
       return next(error);
     }
   });

   // 로그인 처리
   router.post("/login", isNotLoggedIn, (req, res, next) => {
     // passport 모듈을 이용해서 로그인
     passport.authenticate("local", (authError, user, info) => {
       if (authError) {
         console.error(authError);
         return next(authError);
       }

       // 일치하는 User가 없을 때
       if (!user) {
         return res.redirect(`/?loginError=${info.message}`);
       }
       return req.login(user, (loginError) => {
         if (loginError) {
           console.error(loginError);
           return next(loginError);
         }
         // 로그인 성공하면 메인 페이지로 이동
         return res.redirect("/");
       });
     })(req, res, next);
   });

   // 로그아웃 처리
   router.get("/logout", isLoggedIn, (req, res, next) => {
     req.logout((error) => {
       if (error) {
         return next(error);
       }
       // 세션을 초기화
       req.session.destroy();
       req.redirect("/");
     });
   });

   module.exports = router;
   ```

8. **passport 디렉토리에 로컬 로그인을 위한 localStrategy.js 파일을 생성하고 작성**

   ```jsx
   // 로컬 로그인 관련 기능 구현
   const passport = require("passport");
   const LocalStrategy = require("passport-local").Strategy;
   const bcrypt = require("bcrypt");
   const User = require("../models/user");

   module.exports = () => {
     passport.use(
       new LocalStrategy(
         {
           usernameField: "email",
           passwordField: "password",
         },
         async (email, password, done) => {
           try {
             // 로그인 처리를 위해서 email에 해당하는 데이터 찾기
             const exUser = await User.findOne({ where: { email } });
             if (exUser) {
               // 비밀번호 비교
               const result = await bcrypt.compare(passport, exUser.password);
               if (result) {
                 done(null, exUser);
               } else {
                 done(null, false, { message: "비밀번호 틀림" });
               }
             } else {
               done(null, false, { message: "없는 회원" });
             }
           } catch (error) {
             console.error(error);
             done(error);
           }
         }
       )
     );
   };
   ```

9. **App.js 파일에 로그인 관련 라우터 등록**

   ```jsx
   const authRouter = require("./routes/auth");
   app.use("/auth", authRouter);
   ```

10. **카카오 로그인 구현**

    1. 사용하는 모듈: passport-kakao

       도큐먼트: https://www.passportjs.org/packages/passport-kakao/

    2. 카카오 로그인 사용을 위한 설정

       [developers.kakao.com](http://developers.kakao.com) 에 접속해서 로그인

       애플리케이션이 없으면 추가

       REST API 키를 복사하고 .env 파일에 추가

       플랫폼 등록: Web에 자신의 도메인과 포트 번호를 추가

       내 애플리케이션 > 제품 설정 > 로그인 활성화

       `Redirect URI` 를 설정해야 한다.

       동의 항목에서 항목에 따른 것들을 설정해 준다.

    3. passport 디렉토리의 index.js를 수정

       ```jsx
       const passport = require("passport");
       // 로컬 로그인 구현
       const local = require("./localStrategy");

       // 카카오 로그인 구현
       const kakao = require("./kakaoStrategy");

       const User = require("../models/user");

       module.exports = () => {
         // 로그인 성공했을 떄 정보를 deserializeUser 함수에게 넘기는 함수
         passport.serializeUser((user, done) => {
           done(null, user.id);
         });

         // 넘어온 id에 해당하는 데이터가 있으면 데이터베이스에서 찾아서 세션에 저장
         passport.deserializeUser((id, done) => {
           User.findOne({ where: { id } })
             .then((user) => done(null, user))
             .catch((err) => done(err));
         });

         local();
         kakao();
       };
       ```

    4. passport 디렉토리에 kakaoStrategy.js 파일을 만들고 작성

       ```jsx
       const passport = require("passport");
       const kakaoStrategy = require("passport-kakao").Strategy;

       // 유저 정보
       const User = require("../models/user");

       // 카카오 로그인
       module.exports = () => {
         passport.use(
           new kakaoStrategy(
             {
               clientID: process.env.KAKAO_ID,
               callbackURL: "/auth/kakao/callback",
             },
             async (accessToken, refreshToken, profile, done) => {
               // 로그인 성공했을 때 정보를 출력
               console.log("kakao profile", profile);
               try {
                 // 이전에 로그인한 적이 있는지 찾기 위해서
                 // 카카오 아이디와 provider가 kakao로 되어있는
                 // 데이터가 있는지 조회
                 const exUser = await User.findOne({
                   where: { snsId: profile.id, provider: "kakao" },
                 });

                 // 이전에 로그인 한 적이 있으면
                 if (exUser) {
                   done(null, exUser);
                 } else {
                   // 이전에 로그인 한 적이 없으면 데이터를 저장
                   const newUser = await User.create({
                     email: profile._json && profile._json.kaccount_email,
                     nick: profile.displayName,
                     snsId: profile.id,
                     provider: "kakao",
                   });
                   done(null, newUser);
                 }
               } catch (error) {
                 console.log(error(error));
                 done(error);
               }
             }
           )
         );
       };
       ```

    5. routes 디렉토리의 auth.js 파일에 카카오 로그인 라우팅 처리 코드를 추가

       ```jsx
       // 카카오 로그인을 눌렀을 때 처리
       router.get("/kakao", passport.authenticate("kakao"));

       // 카카오 로그인에 실패했을 때
       router.get(
         "/kakao/callback",
         passport.authenticate("kakao", {
           failureRedirect: "/",
         }),
         (req, res) => {
           res.redirect("/");
         }
       );
       ```

11. **게시글 작업 및 팔로우 기능 추가**
