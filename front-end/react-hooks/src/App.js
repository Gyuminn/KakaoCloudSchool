import "./App.css";
import ErrorBoundary from "./ErrorBoundary";
import Iteration from "./Iteration";

function App() {
  return (
    <>
      <ErrorBoundary>
        <Iteration />
      </ErrorBoundary>
    </>
  );
}

export default App;
