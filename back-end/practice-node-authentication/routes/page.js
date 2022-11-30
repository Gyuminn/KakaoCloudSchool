const express = require("express");
const { Model } = require("sequelize");

const router = express.Router();

// 1. 공통된 처리 - 무조건 수행
router.use((req, res, next) => {
  // 변수 초기화
  // 로그인한 유저 정보
  res.locals.user = null;
  // 게시글을 follow하고 되고 있는 개수
  res.locals.followCount = 0;
  res.locals.followingCoung = 0;
  // 게시글을 follow하고 있는 유저들의 목록
  res.locals.followerIdList = [];
  next();
});

// 2. 메인 화면
router.get("/", (req, res, next) => {
  const twits = [];
  // 템플릿 엔진을 이용한 출력.
  // views 디렉토리의 main.html로 출력
  res.render("main", { title: "Node Authentication", twits });
});

// 3. 회원 가입
router.get("/join", (req, res, next) => {
  res.render("join", { title: "회원 가입 - NodeAuthentication" });
});

// 4. 프로필 화면 처리
router.get("/profile", (req, res, next) => {
  res.render("join", { title: "나의 정보 - NodeAuthentication" });
});

// 5. 내보내기
// 이제 App.js 파일에 라우팅 파일을 import시킨다.
module.exports = router;
