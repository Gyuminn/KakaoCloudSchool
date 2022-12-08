import React from "react";
import ToDoListItem from "./ToDoListItem";

import { List } from "react-virtualized";
import { useCallback } from "react";

export const ToDoList = ({ todos, handleRemove, handleToggle }) => {
  // 하나의 항목을 렌더링하기 위한 함수를 생성
  const rowRenderer = useCallback(
    ({ index, key, style }) => {
      // 출력할 데이터 가져오기
      const todo = todos[index];
      return (
        <ToDoListItem
          todo={todo}
          key={key}
          handleRemove={handleRemove}
          handleToggle={handleToggle}
          style={style}
        />
      );
    },
    [handleRemove, handleToggle, todos]
  );

  return (
    <List
      className="ToDoList"
      width={512}
      height={513}
      rowCount={todos.length}
      rowHeight={57}
      rowRenderer={rowRenderer}
      list={todos}
      style={{ outline: "none" }}
    />
  );
};
