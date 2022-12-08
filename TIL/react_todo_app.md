# [React] ToDo Application

소스 코드 → 깃헙

1. 프로젝트 생성하고 필요한 라이브러리 설치

   1. 프로젝트 생성

      `yarn create react-app react-todo`

   2. 필요한 라이브러리
      - sass-loader: scss 파일을 사용하기 위해서 설치
      - sass: scss 파일을 사용하기 ㅜ이해서 설치
      - classnames: css를 작성할 classname을 편리하게 작성하기 위한 라이브러리
      - react-icons: 아이콘을 사용하기 위한 라이브러리, [https://react-icons.github.io/react-icons](https://react-icons.github.io/react-icons)
      - open-color: 색상을 직접 값으로 설정하는 것이 아니고 색상 이름과 정수 1개의 농도를 가지고 설정할 수 있도록 해주는 라이브러리 [https://yeun.github.io/open-color/](https://yeun.github.io/open-color/)
   3. src 디렉토리의 index.js 파일을 수정

      Web Application에서 body나 box 태그에 margin과 padding을 0으로 설정하는 경우가 있는데 IE 구부전과의 호환성 문제 때문이다.

      IE 구버전은 width와 height 안에 padding과 margin 그리고 border의 크기가 포함되고 나머지 브라우저는 content의 크기만 포함된다.

2. **UI**

   1. 구성

      - ToDoTemplate: Main 컴포넌트
      - ToDoInsert: 데이터 삽입을 위해서 하나의 input과 버튼을 가진 컴포넌트
      - ToDoListItem: 하나의 항목을 출력하기 위한 컴포넌트
      - ToDoList: ToDoListItem의 목록을 출력하기 위한 컴포넌트

      ToDoListItem을 ToDoList에 배치하고 ToDoList와 ToDoInsert를 ToDoTemplate에 배치해서 ToDoTemplate을 App에 배치

      ToDo의 내용은 구분하기 위한 값, 내용, 실행 여부로 구성

      모든 컴포넌트와 scss 파일은 components 디렉토리에 배치. 이 부분은 컴포넌트와 scss 파일과 index.js로 묶어서 별도의 디렉토리로 구성해도 된다. 이 경우 디렉토리의 이름은 컴포넌트의 이름과 같아야 하며 이렇게 하는 것이 재사용성을 증가시킨다.

      node를 기반으로 하는 프로젝트에서 `index.js의 역할은 디렉토리 안의 모든 것들을 외부에서 사용할 수 있도록 export하는 것`이다.

      `require나 import할 때 디렉토리 이름을 사용하면 디렉토리 안에 있는 index.js 파일에서 export한 내용을 가져온다.`

   2. Main 화면

      ToDoTemplate

   3. 데이터 삽입 화면

      ToDoInsert.jsx, ToDoInsert.scss

   4. 데이터 목록 화면

      ToDoListItem.jsx(하나의 항목 출력), ToDoListItem.scss

3. **기능 구현**

   1. 데이터 배열 출력

      App.js 파일을 수정해서 샘플 데이터 배열을 state(데이터가 수정되면 컴포넌트를 리렌더링)로 생성하고 ToDoList에게 전달(상위 컴포넌트에서 하위 컴포넌트에게 데이터를 전달할 때는 props를 사용)

   2. ToDoList.jsx 파일에서 데이터를 넘겨받아서 ToDoListItem에서 출력하도록 설정
   3. 데이터 추가 기능

      데이터를 처리하는 함수는 App.js(state가 App.js에 존재)에 만들어서 넘겨주는 구조

      App.js 파일을 수정해서 데이터 삽입 관련된 함수를 만들고 삽입 컴포넌트에게 넘겨주기

      ToDoInsert.jsx 파일에서 submit 이벤트를 처리하는 함수 작성

   4. 데이터 삭제 구현

      App.js 파일에 삭제 함수를 추가하고 ToDoListItem에게 전달

   5. 데이터 수정 구현

      데이터 목록 화면에서 데이터의 체크박스를 클릭하면 checked의 상태가 변경되도록 설정

      App.js 파일에 id를 넘겨받아서 id에 해당하는 데이터의 checked 값을 Toggle(반전)하는 함수를 만들고 ToDoList 컴포넌트에게 넘겨주도록 작성

4. **최적화**

   1. 컴포넌트가 리렌더링되는 경우
      - 전달받은 props가 변경되는 경우
      - 자신의 state가 변경되는 경우
      - 상위 컴포넌트가 리렌더링되는 경우
      - forceUpdate 함수를 호출하는 경우
   2. 많은 양의 데이터를 넣어본다.

      ```jsx
      const createBulkTodos = () => {
        const array = [];
        for (let i = 1; i <= 2000; i++) {
          array.push({
            id: i,
            text: `${i}번째 할일 ㅋ ㅋ`,
            checked: false,
          });
        }

        return array;
      };

      const [todos, setTodos] = useState(createBulkTodos);
      ```

   3. 하나의 데이터가 수정되면 전체가 리렌더링되는 문제를 해결.

      `현재 컴포넌트가 2000개인데 하나의 데이터가 수정이 발생하면 todos에 변경이 일어나고 todos는 App 컴포넌트의 sate이므로 App이 리렌더링을 할 텐데 이렇게 되면 화면 전체가 리렌더링되는 것과 같다.`

      자신의 props가 변경될 때만 리렌더링하도록 할 수가 있는데 Class Component에서는 shuoldComponentUpdate라는 생명주기 메서드를 이용하고 Function Domponent에서는 `React.memo` 함수로 감싸주기만 하면 됨.

   4. 함수가 업데이트 되지 않도록 하기

      useCallback을 이용해서 함수를 선언헀는데 useCallback을 이용하면 두 번째 매개변수인 deps 배열의 데이터에 변경이 생기면 함수를 새로 생성

      그러나 실제로 todos 배열에 변경이 생긴다고 해서 함수를 새로 만들 필요는 없음.

      대부분의 경우 이벤트 처리 함수는 다시 만들어질 필요가 없음.

      해결책

      - `useState의 setter에 함수형 업데이트를 사용하도록 하면 된다.(이게 쉬움)`

        ```jsx
        // 삽입을 처리하기 위한 함수
        // todos에 변화가 생기면 함수를 만들지만 그렇지 않으면
        // 기존 함수를 이용
        const handleInsert = useCallback((text) => {
          const todo = {
            id: nextId.current,
            text,
            checked: false,
          };
          // 함수형 업데이트
          setTodos((prevTodos) => [...prevTodos, todo]);
          nextId.current += 1;
        }, []);

        // 데이터 삭제를 위한 함수
        const handleRemove = useCallback((id) => {
          setTodos((prevTodos) => prevTodos.filter((todo) => todo.id !== id));
        }, []);

        // 데이터 수정을 위한 함수
        const handleToggle = useCallback((id) => {
          // todos를 복제해서 하나씩 순회하면서
          // todo의 id값과 매개변수로 받은 id가 일치하면
          // checked를 반전하고 그렇지 않으면 그대로
          setTodos((prevTodos) =>
            prevTodos.map((todo) =>
              todo.id === id ? { ...todo, checked: !todo.checked } : todo
            )
          );
        }, []);
        ```

      - useReducer를 이용해서 함수를 컴포넌트 외부에 생성
        함수는 변경할 state와 action을 매개변수로 받아서 action의 type을 가지고 분기를 만들어서 state에 작업을 수행해주면 된다.
        `컴포넌트 내부에서는 useState를 사용하지 않고 useReducer(함수이름, 초기값, 초기화하는 함수)를 이용해서 state와 state를 수정하는 함수를 생성`
        ```jsx
        // App.js를 수정 - state를 수정하는 함수를 컴포넌트 외부에 선언
        ```
        컴포넌트 내부에서 state를 수정하는 함수를 직접 생성하지 않는다.
        state가 변경되더라도 함수들을 다시 만드는 작업을 하지 않는다.
        이 작업은 컴포넌트의 개수가 많거나 state를 조작하는 함수가 많을 때 수행한다.
        함수를 만드는 작업은 그렇게 많은 작업을 소모하거나 많은 메모리를 사용하는 작업은 아니다.

5. **화면에 보여질 내용만 렌더링**

   스마트폰 앱에서 CollectionView(Table View, Map View, Web View 등)들은 메모리 효율을 높이기 위해서 Deque라는 자료구조를 이용해서 화면에 보여지는 만큼만 메모리 할당을 해서 출력을 하고 스크롤을 하면 Deque의 메모리를 재사용하는 메커니즘으로 메모리 효율을 높이게 된다.

   react에서도 외부 라이브러리를 이용하면 위와 유사하게 화면에 보여지는 만큼만 렌더링을 하고 나머지 데이터를 스크롤을 할 때 렌더링을 하도록 할 수 있다.

   이는 SPA(Single Page Application)에서 많이 사용.

   이게 안될 때 어쩔 수 없이 서버 렌더링을 사용한다.

   - react-virtualized 라는 라이브러리를 이용해서 지연 로딩을 구현할 수 있다.
     하나의 항목의 너비와 높이를 알아야 하고 목록의 높이를 알아야 한다.
     react에서 목록을 출력할 때는 스타일에서 width와 height를 설정하는 것이 좋다.

   1. 패키지 설치

      `yarn add react-virtualized`

   2. ToDoList.jsx 수정

      ```jsx
      import React from "react";
      import ToDoListItem from "./ToDoListItem";

      import { List } from "react-virtualized";
      import { useCallback } from "react";

      export const ToDoList = ({ todos, handleRemove, handleToggle }) => {
        // 하나의 항목을 렌더링하기 위한 함수를 생성
        const rowRenderer = useCallback(
          ({ index, key, style }) => {
            // 출력할 데이터 가져오기
            const todo = todos[index];
            return (
              <ToDoListItem
                todo={todo}
                key={key}
                handleRemove={handleRemove}
                handleToggle={handleToggle}
                style={style}
              />
            );
          },
          [handleRemove, handleToggle, todos]
        );

        return (
          <List
            className="ToDoList"
            width={512}
            height={513}
            rowCount={todos.length}
            rowHeight={57}
            rowRenderer={rowRenderer}
            list={todos}
            style={{ outline: "none" }}
          />
        );
      };
      ```
