import "./App.css";
import { ToDoInsert } from "./components/ToDoInsert";
import { ToDoList } from "./components/ToDoList";
import { ToDoTemplate } from "./components/ToDoTemplate";

function App() {
  return (
    <ToDoTemplate>
      <ToDoInsert />
      <ToDoList />
    </ToDoTemplate>
  );
}

export default App;
