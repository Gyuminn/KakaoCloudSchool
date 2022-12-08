import { useState, useRef, useCallback } from "react";
// useRef는 변수를 생성하거나 변수를 만들어서 DOM에 할당하기 위해서
// useCallback은 함수를 효율적으로 생성하기 위해서
import "./App.css";
import { ToDoInsert } from "./components/ToDoInsert";
import { ToDoList } from "./components/ToDoList";
import { ToDoTemplate } from "./components/ToDoTemplate";

function App() {
  const [todos, setTodos] = useState([
    {
      id: 1,
      text: "HTML, CSS, JavaScript",
      checked: true,
    },
    {
      id: 2,
      text: "React, Node",
      checked: true,
    },
    {
      id: 3,
      text: "MariaDB, MongoDB",
      checked: false,
    },
    {
      id: 4,
      text: "Java, Spring",
      checked: false,
    },
  ]);

  const nextId = useRef(5);

  // 삽입을 처리하기 위한 함수
  // todos에 변화가 생기면 함수를 만들지만 그렇지 않으면
  // 기존 함수를 이용
  const handleInsert = useCallback(
    (text) => {
      const todo = {
        id: nextId.current,
        text,
        checked: false,
      };
      setTodos([...todos, todo]);
      nextId.current += 1;
    },
    [todos]
  );

  // 데이터 삭제를 위한 함수
  const handleRemove = useCallback(
    (id) => {
      setTodos(todos.filter((todo) => todo.id !== id));
    },
    [todos]
  );

  return (
    <ToDoTemplate>
      <ToDoInsert handleInsert={handleInsert} />
      <ToDoList todos={todos} handleRemove={handleRemove} />
    </ToDoTemplate>
  );
}

export default App;
