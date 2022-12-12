import "./App.css";
import ToDos from "./components/Todos";
import CounterContainer from "./containers/CounterContainer";

function App() {
  return (
    <div>
      <CounterContainer />
      <hr />
      <ToDos />
    </div>
  );
}

export default App;
