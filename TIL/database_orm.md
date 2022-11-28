# [Database] ORM

<aside>
▪️ 프로그래밍 언어에서 관계형 데이터베이스를 사용하는 방법
1. 데이터베이스 드라이버만 이용해서 작업
- 소스 코드 안에 SQL을 삽입해서 작업하는 방식
- 소스 코드 안에 SQL이 삽입되어 있어서 유지보수가 어려움

2. SQL Mapper 방식

- 소스 코드와 SQL을 분리해서 작성하는 방식
- 사용이 쉽기 떄문에 SI와 같은 여러 명이 공동으로 작업하는 프로젝트에서 많이 사용
- 성능은 떨어진다.
- Java나 ASP.net에서 사용하는 MyBatis가 가장 대표적인 프레임워크이다.

3. ORM

- 관계형 데이터베이스의 테이블을 클래스와 그리고 테이블의 행을 인스턴스와 매핑해서 사용하는 방식으로 SQL을 사용할 수도 있고 사용하지 않을 수도 있다.
- 성능이 SQL Mapper 보다 좋기 때문에 솔루션 개발에 많이 이용한다. 배우기가 어렵기 때문에 SI 업무에는 적합하지 않음.
- Java의 JPA(Hibernate로 구현하는 경우가 많음)나 node의 sequelize 그리고 python의Django 등이 대표적인 프레임워크이다.
- 대부분이 경우는 데이터베이스를 변경할 떄 설정만 변경하면 된다.

</aside>

1. **ORM(Object Relational Mapping)**

   객체 지향 패러다임을 관계형 데이터베이스에 적용하는 기술

   관계형 데이터베이스의 Table은 객체 지향 프로그래밍의 클래스와 유사한데 Table에서는 여러 개의 컬럼을 만들지만 Class에서는 속성을 만들어저 저장하는 것이 유사.

   이런 이유 때문에 Instance와 Row가 유사

   이를 적절히 이용해서 Instance를 가지고 관계형 데이터베이스 작업을 할 수 있도록 만든 프레임워크가 ORM이다.

   Instance를 가지고 작업을 수행하면 프레임워크가 SQL로 변경을 해서 데이터베이스에 작업을 수행하는 형태로 동작.

   node에서는 sequilize 모듈이 이러한 작업을 수행할 수 있다.

2. **Sequelize를 이용한 하나의 테이블 연동**

   1. 패키지 설치

      ```bash
      npm install sequelize sequelize-cli mysql2
      ```

   2. sequelize 초기화

      ```bash
      npx sequelize init
      ```

      초기화를 수행하면 config, migration, models, seeders 디렉토리가 생성됨.

      - config: 데이터베이스 접속 정보 설정
      - models: 각 테이블과 매핑되는 클래스를 설정
      - migrations: 데이터베이스 스키마(구조, 테이블)가 변경되는 경우를 위한 설정
      - seeders: 테스트 데이터 사용을 위한 설정

3. **데이터베이스 접속 설정**

   1. config 디렉토리의 config.json 파일 수정

      ```jsx
      {
        "development": {
          "username": "계정",
          "password": "비밀번호",
          "database": "데이터베이스이름",
          "host": "아이피",
      		"port": 포트번호, // mysql 혹은 mariadb인 경우 3306번을 사용한다면 생략 가능.
          "dialect": "데이터베이스 종류"
        },
      ```

   2. models 디렉토리의 index.js 파일 수정

      ```jsx
      // 모듈 import
      const Sequelize = require("sequelize");

      // 환경 설정
      const env = process.env.NODE_ENV || "development";

      // 환경 설정 내용 가져오기
      const config = require("../config/config.json")[env];

      // 내보낼 객체 생성
      const db = {};

      // ORM 설정
      const sequelize = new Sequelize(
        config.database,
        config.username,
        config.password,
        config
      );

      db.sequelize = sequelize;

      module.exports = db;
      ```

   3. app.js 파일에 데이터베이스 연결 코드를 작성하고 실행

      ```jsx
      const { sequelize } = require("../models");

      sequelize
        .sync({ force: false })
        .then(() => {
          console.log("데이터베이스 연결 성공");
        })
        .catch((err) => {
          console.log("데이터베이스 연결 실패");
        });
      ```

   4. 하나의 테이블을 연동

      테이블을 먼저 만들고 연결을 시켜도 되고 모델을 만들고 처음 실행을 하면 테이블이 존재하지 않을 경우 테이블이 생성됨. 테이블이 이미 존재하면 그 테이블과 연결을 한다.

      실무 환경에서는 테이블을 먼저 만든 후 모델을 만들고, 학습을 할 때는 모델을 가지고 테이블을 생성

      - 연동할 테이블을 위해서 기존 테이블을 삭제
        ```sql
        drop table goods;
        ```
      - 테이블과 연결할 모델 생성 - model 디렉토리
        Sequelize.Model을 상속받은 클래스를 생성
        2개의 메서드 오버라이딩
        - static init 메서드: 현재 테이블에 대한 설정
          - 첫 번째 인수: 컬럼에 대한 설정
            | 자료형 매핑 | 데이터베이스 | 시퀄라이저 |
            | ----------- | ------------ | ------------ |
            | | VARCAHR | STRING |
            | | CHAR | CHAR |
            | | TEXT | TEXT |
            | | TYNYINT(1) | BOOLEAN |
            | | INT,INTEGER | INTEGER |
            | | FLOAT | FLOAT |
            | | DOUBLE | DOUBLE |
            | | DATETIME | DATE |
            | | DATE | DATEONLY |
            | | TIME | TIME |
            | | BLOB | BLOB |
            | | JSON | JSON |
            | 제약조건 | | allowNull |
            | | | unique |
            | | | defaultValue |
            | | | validate |
          - 두 번째 인수: 테이블에 대한 설정
            - timestamps
              true로 설정하면 createdAt와 updateAt 컬럼이 자동으로 생성되서 데이터가 생성된 날짜와 수정된 날짜를 자동으로 삽입
            - underscored
              시퀄라이즈는 이름을 기본적으로 Camel Case로 만드는데 이를 Snake Case로 변경하고자 할 때 사용
            - modelName
              노드 프로젝트에서 사용할 모델 이름을 설정
            - tableName
              데이터베이스의 테이블 이름
            - paranoid
              데이터를 삭제할 때 삭제하지 않고 deletedAt이라는 컬럼을 생성해서 이 컬럼의 값을 true로 만들고 조회할 때 제외하기 위한 옵션
            - charset과 collate
              캐릭터 셋으로 한글을 사용하고자 할 때는 utf8이나 utf8_general로 설정하고 이모티콘까지 사용하고자 하면 utf8mb4나 utf8mb4_general_ci를 설정
        - static associate 메서드: 다른 테이블과의 관계를 위한 설정
          - 자신의모델이름.hasMany나 belongsTo를 호출하는데 hasMany는 참조되는 경우(부모 테이블로 외래키의 참조 대상)이고 belongsTo는 참조하는 경우(외래키로 소유한 경우)에 호출
          - 매개변수
            ```jsx
            상대방모델이름,
              { foreignKey: "외래키이름", targetKey: "참조하는 속성" };
            ```
      - 테이블 작업
        - 삽입: create
        - 조회: findOne, findAll
        - 수정: update
        - 삭제: delete

   5. models 디렉토리의 indexjs 파일에 생성한 모델을 사용할 수 있도록 추가 설정
   6. app.js 파일에 good을 사용하기 위한 설정을 추가
   7. app.js 파일의 내용을 수정해서 sequelize를 이용해서 데이터 CRUD를 설정
   8. 메서드의 리턴

      검색을 하는 경우에는 검색 결과가 리턴되지만 삽입, 삭제, 갱신의 경우는 삽입, 삭제, 갱신된 데이터가 리턴된다.

4. **2개 테이블 연결**

   1. 프로젝트 생성 - relativetable
   2. 패키지 설치 - express, sequelize sequelize-cli, mysql2, nodemon
   3. sequelize 초기화 - npx sequelize init

      4개의 디렉토리가 생성되는지 확인: config, migrations, models, seeders

   4. package.json에서 npm start 명령을 내리면 app.js를 실행하고 수정하면 자동 실행되도록 설정
   5. 테이블 설계
      - users 테이블
        - id - 정수, 기본키
        - name - 문자열로 20이고 not null
        - age - 정수이고 not null
        - created_at - 날짜
        - updated_at - 날짜
      - comments 테이블
        - id - 정수
        - comment - 문자열 100자이고 not null
        - created_at - 날짜
        - updated_at - 날짜
        - commenter - 정수이고 users 테이블의 id를 참조하는 외래키
   6. 테이블을 위한 model을 생성 - models 디렉토리에서 수행

      models 디렉토리에 users 테이블의 model을 위한 users.js

      models 디렉토리에 comments 테이블의 model을 위한 comments.js

      서로의 테이블에 외래키를 설정해주자.
