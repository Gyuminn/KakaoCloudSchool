// 비동기식 파일 읽기 - error는 에러의 내용이고 data가 Buffer
let fs = require("fs");

fs.readFile("./text.txt", (error, data) => {
  if (error) {
    // 에러가 발생했을 때
    console.log(error.message);
  } else {
    // 에러가 발생하지 않았을 때
    console.log(data.toString());
  }
});

// 콜백 대신 Promise 사용 가능
fs = require("fs").promises;

fs.readFile("./text.txt")
  .then((data) => {
    console.log(data.toString());
  })
  .catch((error) => {
    console.log(error.message);
  });

// 비동기 처리이기 때문에 아래 내용이 먼저 출력된다.
console.log("파일 읽기 종료");
