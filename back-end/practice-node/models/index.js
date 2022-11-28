// 1. 모듈 import
const Sequelize = require("sequelize");

// 6. 모델 가져오기 - good.js 을 세팅하고 난 후
const Good = require("./good");

// 2. 환경 설정
const env = process.env.NODE_ENV || "development";

// 3. 환경 설정 내용 가져오기
const config = require("../config/config.json")[env];

// 4. 내보낼 객체 생성
const db = {};

// 5. ORM 설정
const sequelize = new Sequelize(
  config.database,
  config.username,
  config.password,
  config
);

db.sequelize = sequelize;

db.Sequelize = Sequelize;
// 7. 데이터베이스 추가 연동
db.Good = Good;
Good.init(sequelize);

module.exports = db;
