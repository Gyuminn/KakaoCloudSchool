# [React] Context

1. **react 프로젝트에서의 데이터 공유**

   Component의 props를 이용해서 하위 Component에게 넘겨주는 구조를 이용

   구조가 간단할 때는 크게 어려움이 없지만 구조가 복잡해지면 번거로운 작업이 많아진다.

   여러 곳에서 하나의 데이터를 사용하는 경우도 유사한 작업이 반복된다.

   → Context API를 이용하면 공유 데이터 작성이 쉬워진다.

2. **사용**

   1. src 디렉토리에 contexts라는 디렉토리를 생성
   2. 데이터를 공유하기 위한 context 파일을 contexts 디렉토리에 생성하고 작성 - color.js

      ```jsx
      import { createContext } from "react";

      // 공유할 데이터 설정
      const ColorContext = createContext({ color: "black" });

      export default ColorContext;
      ```

   3. 공유 데이터를 사용할 컴포넌트를 생성 - ColorBox.jsx

      ```jsx
      import ColorContext from "./contexts/color";

      const ColorBox = () => {
        return (
          <ColorContext.Consumer>
            {(value) => (
              <div
                style={{
                  width: "64px",
                  height: "64px",
                  background: value.color,
                }}
              />
            )}
          </ColorContext.Consumer>
        );
      };

      export default ColorBox;
      ```
