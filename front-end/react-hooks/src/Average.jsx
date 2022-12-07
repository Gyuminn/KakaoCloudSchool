import React, { useCallback } from "react";
import { useMemo, useState } from "react";

const getAverage = (numbers) => {
  console.log("평균 계산");

  if (numbers.length === 0) {
    return 0;
  }
  // reduce는 배열을 순회하면서 연산을 수행한 후 하나의 값을 리턴
  // 매개변수는 2개인데 첫 번쨰 매개변수는 수행할 함수이고
  // 두 번쨰 매개변수는 연산을 시작할 때의 초기값이다.
  // 두 번쨰 매개변수를 생략하면 배열의 첫 번째 요소로 설정
  // 첫 번째 매개변수인 함수는 매개변수를 4개까지 갖는데
  // 첫 번째는 누적값이고 두 번쨰는 배열의 요소
  // 세 번쨰는 배열의 인덱스이고 네 번쨰는 배열
  const sum = numbers.reduce((acc, cur) => acc + cur);
  return sum / numbers.length;
};

const Average = () => {
  const [list, setList] = useState([]);
  const [number, setNumber] = useState("");

  const handleChange = useCallback((e) => {
    setNumber(e.target.value);
  }, []);

  // 추가를 눌렀을 때 호출될 메서드
  // 이 함수는 number와 list가 변경될 때만 다시 생성
  const handleInsert = useCallback(
    (e) => {
      const nextList = [...list, +number];
      setList(nextList);
      setNumber("");
    },
    [number, list]
  );

  // list에 변화가 생긴 경우에만 메서드를 호출하고
  // 그 외의 경우에는 결과를 재사용한다.
  const avg = useMemo(() => getAverage(list), [list]);

  return (
    <div>
      <input value={number} onChange={handleChange} />
      <button onClick={handleInsert}>추가</button>
      <ul>
        {list.map((v, i) => (
          <li key={i}>{v}</li>
        ))}
      </ul>
      <div>
        <b>평균: </b>
        {avg}
      </div>
    </div>
  );
};

export default React.memo(Average);
