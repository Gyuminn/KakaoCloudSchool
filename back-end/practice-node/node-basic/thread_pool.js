const crypto = require("crypto");

const pass = "pass";
const salt = "salt";
const start = Date.now();

// thread를 4개 만들어놓기 때문에 8개이지만 4개씩 출력(delay)되게 된다.
// 또한 pool을 만들어놓고 작업해서 출력 순서는 동일하지 않다.
crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("1: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("2: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("3: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("4: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("5: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("6: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("7: ", +Date.now() - start);
});

crypto.pbkdf2(pass, salt, 1000000, 128, "sha512", () => {
  console.log("8: ", +Date.now() - start);
});
