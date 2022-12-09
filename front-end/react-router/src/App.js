import "./App.css";
import Home from "./Home";
import { Route, Routes } from "react-router-dom";
import About from "./About";
import Profile from "./Profile";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="about" element={<About />} />
      <Route path="/profile/:username" element={<Profile />} />
    </Routes>
  );
}

export default App;
