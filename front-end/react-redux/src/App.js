import "./App.css";
import Counter from "./components/Counter";
import ToDos from "./components/Todos";

function App() {
  return (
    <div>
      <Counter number={0} />
      <hr />
      <ToDos />
    </div>
  );
}

export default App;
