import React from "react";
import { ToDoListItem } from "./ToDoListItem";

export const ToDoList = ({ todos, handleRemove }) => {
  return (
    <div className="ToDoList">
      {todos.map((todo) => (
        <ToDoListItem todo={todo} key={todo.id} handleRemove={handleRemove} />
      ))}
    </div>
  );
};
