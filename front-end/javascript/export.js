// 내보낼 데이터 만들기
const multiplyPIwithSQRT2 = Math.PI * Math.SQRT2;

const cube = (x) => {
  return x * x * x;
};

let graph = {
  options: {
    color: "white",
    thickness: "2px",
  },
  draw: function () {
    console.log("Draw Function");
  },
};

export { multiplyPIwithSQRT2, graph, cube };
