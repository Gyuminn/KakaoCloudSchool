const fs = require("fs");
/*
// 용량이 큰 파일 생성
const file = fs.createWriteStream("./data.txt");
for (let i = 0; i < 10000000; i++) {
  file.write("용량이 큰 파일 만들기\n");
}
file.end();
*/

// 스트림을 사용하지 않고 읽어서 쓰기
console.log("복사하기 전 메모리 사용량: " + process.memoryUsage().rss);

const data1 = fs.readFileSync("./data.txt");
fs.writeFileSync("./nostreamdata.txt", data1);

console.log(
  "스트림을 사용하지 않고 복사한 후 메모리 사용량: " + process.memoryUsage().rss
);

// 스트림을 사용하여 읽고 쓰기
// 읽기와 쓰기 스트림 생성
const readStream = fs.createReadStream("./data.txt");
const writeStream = fs.createWriteStream("./streamdata.txt");

// 읽고 쓰기
readStream.pipe(writeStream);
readStream.on("end", () => {
  console.log(
    "스트림을 사용하고 복사한 후 메모리 사용량: " + process.memoryUsage().rss
  );
});
