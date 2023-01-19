# [Document]

1. **프로젝트를 소개하는 문서, 5-10분 안에 발표할 수 있는 분량, ppt의 경우 15 ~20**

   1. 개요
   2. 개발 환경
      - 데이터베이스
      - 프로그래밍 언어
      - IDE
      - 프레임워크
   3. 데이터베이스 구조
      - ER Diagram
   4. 클래스 구조(node의 경우는 어려움)나 API 명세

      클래스의 관계나 메서드의 기능(getter나 setter는 제외)

   5. 인터페이스 예
   6. 느낀점

2. 저장소
   - 예시
     (maria db, mongo db) <-> back end(node) <-> front end(html, css, js - 바닐라 스크립트, react)
     back end: serverless(firebase, aws의 람다)로 해도 됨.
3. **API 명세 기본**

   회원가입 - register(아이디 - 문자열, 비밀번호 - 문자열) ⇒ 리턴되는 자료형(참 거짓, 정수(삽입 삭제 수정 시 몇개 변경되는가)
