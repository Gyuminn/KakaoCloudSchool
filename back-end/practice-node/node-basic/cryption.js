// 암호화 모듈 가져오기
const crypto = require("crypto");
let password = "12345";

// 단방향 암호화 수행
let p1 = crypto.createHash("sha256").update(password).digest("base64");
console.log(p1);

let str = "12345";
let p2 = crypto.createHash("sha256").update(str).digest("base64");
// 동일한 문자열로 암호화를 하면 동일한 결과를 생성하기 때문에 true
console.log(p1 === p2);

str = "123456";
p2 = crypto.createHash("sha256").update(str).digest("base64");
// false
console.log(p1 === p2);

// 알고리즘은 정해진 것을 써야 한다.
const algorithm = "aes-256-cbc";
// Node의 crypto 모듈에서는 key는 32자리, iv는 16자리 자유롭게 설정.
const key = "01234567890123456789012345678901";
const iv = "1234567890123456";

// 암호화 객체 생성
let cipher = crypto.createCipheriv(algorithm, key, iv);
let result = cipher.update("01012341234", "utf8", "base64");
result += cipher.final("base64");
console.log(result);

// 복호화
let decipher = crypto.createDecipheriv(algorithm, key, iv);
let result2 = decipher.update(result, "base64", "utf8");
result2 += decipher.final("utf8");
console.log(result2);
