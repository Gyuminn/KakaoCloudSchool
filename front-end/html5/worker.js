// html에서 요청이 왔을 때
onmessage = (event) => {
  // html에서 전송한 데이터 받기
  let num = event.data;
  let result = 0;
  for (let i = 1; i <= num; i++) {
    result += i;
  }
  postMessage(result);
};
