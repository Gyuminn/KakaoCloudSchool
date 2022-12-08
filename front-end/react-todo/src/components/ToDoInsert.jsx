import React, { useCallback } from "react";
import { useState } from "react";

// react-icons의 MaterialDesign의 MdAdd라는 아이콘 이용
import { MdAdd } from "react-icons/md";

import "./ToDoInsert.scss";

export const ToDoInsert = ({ handleInsert }) => {
  // 입력된 데이터를 저장하기 위한 state
  const [value, setValue] = useState("");

  // 입력 내용이 변경될 때 호출될 함수
  const handleChange = useCallback((e) => {
    setValue(e.target.value);
  }, []);

  // form에서 submit 이벤트가 발생하면 호출될 함수
  // form 안에서 submit 버튼을 눌러도 submit 이벤트가 발생하지만
  // form 안에서 Enter를 눌러도 submit 이벤트가 발생한다.
  const handleSubmit = useCallback(
    (e) => {
      const result = window.confirm(`추가할 내용:${value}`);
      if (result === false) {
        e.preventDefault();
        return;
      }
      handleInsert(value);
      setValue("");
      // 제공되는 기본 이벤트 처리 코드를 수행하지 않음.
      // form의 submit이나 a 태그는
      // 화면 전체를 갱신하기 때문에 이전 내용을 모두 잃어버림.
      e.preventDefault();
    },
    [handleInsert, value]
  );
  return (
    <form className="ToDoInsert" onSubmit={handleSubmit}>
      <input
        placeholder="할 일을 입력하세요"
        value={value}
        onChange={handleChange}
      />
      <button type="submit">
        <MdAdd />
      </button>
    </form>
  );
};
