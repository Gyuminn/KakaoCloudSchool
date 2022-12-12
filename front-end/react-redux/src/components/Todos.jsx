import React from "react";

// 한 개의 ToDo를 출력하기 위한 컴포넌트
const ToDoItem = ({ todo, onToggle, onRemove }) => {
  return (
    <>
      <li>
        <input type="checkbox" />
        <span>todo</span>
        <button>삭제</button>
      </li>
    </>
  );
};

// 여러 개의 ToDoItem을 출력할 컴포넌트
const ToDos = ({
  input,
  todos,
  onChangeInput,
  onInsert,
  onToggle,
  onRemove,
}) => {
  const onSubmit = (e) => {
    e.preventDefault();
  };

  return (
    <div>
      <form onSubmit={onSubmit}>
        <input />
        <button type="submit">등록</button>
      </form>
      <div>
        <ToDoItem />
        <ToDoItem />
        <ToDoItem />
        <ToDoItem />
        <ToDoItem />
      </div>
    </div>
  );
};

export default ToDos;
