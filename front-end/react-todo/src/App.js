import "./App.css";
import { ToDoInsert } from "./components/ToDoInsert";
import { ToDoListItem } from "./components/ToDoListItem";
import { ToDoTemplate } from "./components/ToDoTemplate";

function App() {
  return (
    <ToDoTemplate>
      <ToDoInsert />
      <ToDoListItem />
    </ToDoTemplate>
  );
}

export default App;
