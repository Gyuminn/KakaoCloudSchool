const express = require("express");
const path = require("path");

const app = express();
app.set("port", process.env.PORT || 9001);

// sequelize 연결
const { sequelize } = require("../models");
sequelize
  .sync({ force: false })
  .then(() => console.log("데이터베이스 연결 성공"))
  .catch((error) => {
    console.log(error);
  });

const User = require("../models/users");
const Comment = require("../models/comments");

app.get("/", async (req, res) => {
  User.create({
    name: "gyumin",
    age: 27,
  });
});

app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 서버 대기 중");
});
