import {
  MdCheckBoxOutlineBlank,
  MdCheckBox,
  MdRemoveCircleOutline,
} from "react-icons/md";

import cn from "classnames";
import "./ToDoListItem.scss";

import React, { useCallback } from "react";

export const ToDoListItem = ({ todo, handleRemove }) => {
  const { id, text, checked } = todo;

  const handleDelete = useCallback(
    (e) => {
      const result = window.confirm(`${text}를 정말로 삭제하시겠습니까?`);
      if (result) {
        handleRemove(id);
      }
    },
    [handleRemove, id, text]
  );

  return (
    <div className="ToDoListItem">
      <div className={cn("checkbox", { checked })}>
        {checked ? <MdCheckBox /> : <MdCheckBoxOutlineBlank />}
        <div className="text">{text}</div>
      </div>
      <div className="remove" onClick={(e) => handleDelete(e)}>
        <MdRemoveCircleOutline />
      </div>
    </div>
  );
};
