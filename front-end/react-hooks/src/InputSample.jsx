import React, { useRef } from "react";
import { useState } from "react";

export const InputSample = () => {
  // 2개의 속성을 가진 state 생성
  const [inputs, setInputs] = useState({
    name: "",
    nickname: "",
  });

  // state를 편리하게 사용하기 위해서 비 구조화할당
  const { name, nickname } = inputs;

  // react에서 다른 컴포넌트나 DOM을 참조할 수 있는 변수를 생성
  const nameInput = useRef();

  const handleChange = (e) => {
    setInputs({
      [e.target.name]: e.target.value,
    });
  };

  const handleReset = (e) => {
    setInputs({
      name: "",
      nickname: "",
    });
    // 이름 입력 란으로 포커스 옮기기
    nameInput.current.focus();
  };

  return (
    <>
      <input name="name" value={name} onChange={handleChange} ref={nameInput} />
      <input name="nickname" value={nickname} onChange={handleChange} />
      <button onClick={handleReset}>초기화</button>
    </>
  );
};
