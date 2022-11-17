// variable.js에서 내보낸 내용 가져오기
// variable.js 에서 객체로 내보냈기 때문에 변수를 가져올 때 같은 이름으로 가져온다.
const { odd, even } = require("./variable");

const checkOddOrEvent = (num) => {
  if (num % 2 == 0) {
    return even;
  } else {
    return odd;
  }
};

// 이렇게 내보내면 가져올 때는 아무 이름이나 사용해서 받으면 된다.
module.exports = checkOddOrEvent;
