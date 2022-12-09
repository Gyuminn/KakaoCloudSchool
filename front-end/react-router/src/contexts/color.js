import { createContext } from "react";

// 공유한 데이터 설정
const ColorContext = createContext({ color: "black" });

export default ColorContext;
