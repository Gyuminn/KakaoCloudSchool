// 파일을 읽고 쓸 수 있는 모듈 가져오기
const fs = require("fs");

let data = fs.readFileSync("./text.txt");
console.log(data.toString());

// Enter 단위로 분할해서 읽기
let ar = data.toString().split("\n");
console.log(ar[0]);

// 연습문제: 이전에 양방향 암호화할 때 사용했던 key와 iv 값을 텍스트 파일에 저장하고 이를 읽어서 적용하는 형태로 변경
