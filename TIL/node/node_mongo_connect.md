# [Database] MongoDB 연동

1. **Node + MongoDB**

   1. Mongo DB 연결
      - 드라이버 패키지
        mongodb
      - 연결코드
        ```jsx
        let MongoClient = require("mongodb").MongoClient;
        MongoClient.connect(
          "mongodb://아이디:비밀번호@ip:포트번호",
          (err, database) => {
            if (err) {
              // 에러가 발생했을 떄 처리
            } else {
              // 정상적으로 처리되었을 때 처리
              let db = database.db("데이터베이스이름");
              // db를 가지고 작업을 수행
            }
          }
        );
        ```
   2. 샘플 데이터 생성
      - use node
      - 데이터베이스 작업
   3. node 프로젝트 생성

      entry point는 app.js

   4. 필요한 패키지 설치

      express, morgan, multer, mongodb, ejs

      개발모드(--save-dev 옵션을 추가해서 설치)로 nodemon

   5. 필요한 디렉토리 생성 - img와 view 디렉토리 생성
   6. App.js 파일에 기본 설정 코드를 추가하고 실행

   <aside>
   ▪️ 데이터베이스 사용
   1. 데이터 베이스 연결 → 필요한 구문 수행 → 데이터베이스 연결 해제

   데이터베이스 연결을 미리 해두고 필요할 때 마다 구문만 수행하고 앱이 종료될 때 연결을 해제하는 방식이 있고, 구문 수행을 하기 전에 연결을 하고 구문을 수행 후 연결을 종료하는 방식이 있다.

   </aside>

2. **Node + MongoDB + mongoose(Node의 ODM)**

   1. RDBMS와 NoSQL의 차이
      - RDBMS는 스키마를 생성하고 데이터를 저장하는 형식을 취하지만 NoSQL은 스키마를 생성하지 않고 데이터를 저장하는 것이 가능
      - NoSQL은 스키마의 구조가 변경이 되어도 데이터 구조를 변경할 필요없이 데이터 저장이 가능.
      - RDBMS는 데이터의 값으로 다른 테이블의 데이터나 배열을 삽입할 수 없기 때문에 테이블을 분할하고 Foreing Key와 Join의 개념을 이용해서 여러 종류의 데이터를 저장하지만 NoSQL들은 데이터의 값으로 객체나 배열이 가능하기 때문에 하나의 Collection에 여러 종류의 데이터를 저장할 수 있어 Join을 하지 않아도 되기 떄문에 처리 속도가 빠를 수 있다. Join 대신에 Embedding이나 Linking의 개념을 사용
      - RDBMS는 일반적으로 엄격한 트랜잭션을 적용하지만 NoSQL들은 느슨한 트랜잭션을 적용
      - NoSQL은 복잡한 거래가 없는 경우나 비정형 데이터만을 저장하기 위한 용도로 많이 사용
   2. ODM

      - Relation이라는 개념 대신에 Document를 하나의 객체에 매핑하는 방식
      - 하나의 Document에 대한 모양을 만들고 사용해야 하기 때문에 NoSQL의 Collection에도 하나의 정형화된 모양을 가져야 한다.
      - MongoDB에서 ODM을 사용할 수 있도록 해주는 대표적인 라이브러리가 mongoose

      <aside>
      ▪️ 프로그래밍 언어가 데이터베이스와 연동하는 방식
      - 드라이버의 기능만을 이용해서 사용하는 방식
      - SQL Mapper: 관계형 데이터베이스에만 존재하는 방식으로 SQL과 프로그래밍 언어 코드의 분리를 이용하는 방식.
      - ORM이나 ODM같은 객체 지향 문법을 이용해서 사용할 수 있도록 해주는 방식

      </aside>
