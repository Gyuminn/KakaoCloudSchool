import "./App.css";
import Home from "./Home";
import { Route, Routes } from "react-router-dom";
import About from "./About";
import Profile from "./Profile";
import Articles from "./Articles";
import Article from "./Article";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="/profile/:username" element={<Profile />} />
      <Route path="/articles" element={<Articles />} />
      <Route path="/articles/article/:id" element={<Article />} />
    </Routes>
  );
}

export default App;
