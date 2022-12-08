import React from "react";

import "./ToDoTemplate.scss";

export const ToDoTemplate = ({ children }) => {
  return (
    <div className="ToDoTemplate">
      <div className="app-title">일정관리</div>
      <div className="content">{children}</div>
    </div>
  );
};
