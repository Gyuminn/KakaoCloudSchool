const { odd, even } = require("./variable");
const checkNumber = require("./function");

console.log(checkNumber(5));

const path = require("path");
// 현재 디렉토리를 확인
console.log(__dirname);
// 현재 디렉토리 내의 public 디렉토리의 경로
console.log(path.join(__dirname, "public"));
const url = require("url");
const addr = "https://www.naver.com/login?id=ggangpae1";

// url 분해
const p = url.parse(addr);
// pathname에는 서버 URL을 제외한 경로르 저장하고 있고
// query는 query string을 저장하고 있다.
console.log(p);

// addr에서 파라미터 부분만 가져오기
// searchParams 속성을 호출하면 파라미터 부분에 해당하는 객체를 리턴
const address = new URL("https://www.naver.com/login?id=ggangpae1");
console.log(address.searchParams);
// id의 값 추출하기
console.log(address.searchParams.get("id"));
