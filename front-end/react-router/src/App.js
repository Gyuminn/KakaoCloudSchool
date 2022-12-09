import "./App.css";
import Home from "./Home";
import { Route, Routes } from "react-router-dom";
import About from "./About";
import Profile from "./Profile";
import Articles from "./Articles";
import Article from "./Article";
import Layout from "./Layout";
import Login from "./Login";
import MyPage from "./MyPage";
import ColorBox from "./ColorBox";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/mypage" element={<MyPage />} />
      <Route element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/profile/:username" element={<Profile />} />
        <Route path="/articles" element={<Articles />} />
        <Route path="/articles/:id" element={<Article />} />
      </Route>
      <Route path="/colorbox" element={<ColorBox />} />
      <Route path="*" element={<Article />} />
    </Routes>
  );
}

export default App;
