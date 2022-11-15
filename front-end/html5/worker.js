// html에서 요청이 왔을 때
onmessage = (event) => {
  // html에서 전송한 데이터 받기
  let num = event.data;

  // 1부터 num까지 합계 구하기
  let result = 0;
  for (let i = 1; i <= num; i++) {
    result += i;
  }

  // html 파일로 결과를 전송
  postMessage(result);
};
