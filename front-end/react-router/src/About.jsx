import React from "react";
import { useLocation } from "react-router-dom";

const About = () => {
  // query string을 읽을 수 있는 Hook
  const location = useLocation();
  console.log(location);
  return (
    <div>
      <h1>React Router 실습</h1>
      <p>Query String: {location.search}</p>
    </div>
  );
};

export default About;
