import { useState } from "react";
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

  return (
    <ToDoTemplate>
      <ToDoInsert />
      <ToDoList todos={todos} />
    </ToDoTemplate>
  );
}

export default App;
