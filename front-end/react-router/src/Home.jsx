import React from "react";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div>
      <h1>메인 페이지</h1>
      <ul>
        <li>
          <Link to="/about">소개</Link>
        </li>
        <li>
          <Link to="/profile/gyumin">gyumin</Link>
        </li>
        <li>
          <Link to="/profile/Gyuminn">Gyuminn</Link>
        </li>
        <li>
          <Link to="/profile/void">모르는 유저</Link>
        </li>
        <li>
          <Link to="/articles">게시물</Link>
        </li>
      </ul>
    </div>
  );
};

export default Home;
