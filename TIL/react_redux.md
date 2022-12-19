# [React] Redux

1. **Redux**

   상태 관리 라이브러리

   react 에서만 사용하는 것이 아니고 node 관련된 프로젝트에서는 어디서든 사용 가능

   Context API나 useReducer가 나오기 전부터 존재

   redux를 이용하면 상태 관련 로직을 별도의 파일로 분리해서 관리할 수 있다.

   데이터 공유도 수월해짐.

   프로젝트의 규모가 크거나 비동기 작업을 주로 하는 경우에 사용

2. **키워드**

   1. Action

      상태에 어떠한 변화가 필요하게 될 때 발생시키는 객체

      이 객체는 type이 필수이고 나머지는 옵션

      type을 가지고 동작을 구분해서 작업을 수행

   2. Action Creater

      Action을 생성하는 함수

      필수는 아님

      직접 Action 객체를 생성해도 되지만 별도의 함수를 만들어서 사용하기도 한다.

      ```jsx
      export function addToDo(Data) {
      	return (
      			type: "ADD_TODO",
      			data
      		)
      }
      ```

   3. Reducer

      상태 변화를 일으키는 함수

      ```jsx
      function 이름(state, action) {
      	return 변경된 state
      }

      function counter(state, action) {
      	switch(action.type) {
      			case "INCREASE":
      					return state + 1;
      			case "DECREASE":
      					return state - 1;
      			default:
      					return state;
      ```

   4. Store

      redux를 사용하는 애플리케이션에는 Store가 생성되는데 이 Store에는 애플리케이션의 state와 reducer가 들어있고 몇 개의 내장 함수를 제공

      - dispatch
        실제 액션을 발생시키는 함수로 이 함수에 action 객체를 대입하면 dispatch가 reducer를 호출해서 함수를 실행하여 상태를 변경시킴.
      - subscribe
        이 함수는 함수 형태의 파라미터를 받아서 action이 dispatch될 때 호출되서 파라미터로 받은 함수를 실행.

3. **규칙**

   1. 하나의 애플리케이션에 하나의 Store를 갖는다.
   2. state는 읽기 전용
   3. reducer는 pure function(순수 함수 - 외부에서 넘겨받은 매개변수는 수정하지 않고 복제를 해서 수정한 후 return) 이어야 함.

      동일한 입력이 오면 동일한 출력이 만들어져야 한다.

      랜덤이나 new Date 또는 네트워크에서 다운로드 받는 작업 등은 일정한 출력을 만들어내지 못하므로 reducer에서 처리하면 안되고 별도의 미들웨어를 만들어서 처리

   4. 설치

      ```bash
      yarn add redux

      // src/exercise.js
      console.log('redux 사용 준비 완료');
      ```

   5. index.js 파일에 생성한 파일을 import하고 콘솔을 확인
   6. exercise.js 파일을 수정하고 데이터 확인

      ```jsx
      import { createStore } from "redux";

      // 사용할 상태 정의
      const initialState = {
        counter: 0,
        text: " ",
        list: [],
      };

      // 액션의 타입 생성
      const INCREASE = "INCREASE";
      const DECREASE = "DECREASE";
      const CHANGE_TEXT = "CHANGE_TEXT";
      const ADD_TO_LIST = "ADD_TO_LIST";

      // 액션 생성 함수
      const increase = () => {
        return { type: INCREASE };
      };
      const decrease = () => {
        return { type: DECREASE };
      };

      const changeText = (text) => {
        return {
          type: CHANGE_TEXT,
          text,
        };
      };

      const addToList = (item) => {
        return {
          type: ADD_TO_LIST,
          item,
        };
      };

      // reducer - 실제 일을 하는 애
      function reducer(state = initialState, action) {
        switch (action.type) {
          case INCREASE:
            return { ...state, counter: state.counter + 1 };
          case DECREASE:
            return { ...state, counter: state.counter - 1 };
          case CHANGE_TEXT:
            return {
              ...state,
              text: action.text,
            };
          case ADD_TO_LIST:
            return {
              ...state,
              list: state.list.concat(action.item),
            };
          default:
            return state;
        }
      }

      // store 만들기
      const store = createStore(reducer);

      // 현재 store의 상태
      console.log(store.getState());

      // listener 설정 - store의 상태가 변경될 때 호출
      const listener = () => {
        const state = store.getState();
        console.log(state);
      };

      // 구독 설정
      const unsubscribe = store.subscribe(listener);

      // 액션 호출
      store.dispatch(increase());
      store.dispatch(decrease());
      store.dispatch(changeText("데이터"));
      store.dispatch(addToList("리스트"));
      ```

4. **Redux Module**

   리덕스 모듈은 액션 타입, 액션 생성 함수, 리듀서를 포함하는 자바스크립트 파일

   리덕스 샘플에서는 액션과 리듀서를 분리해서 정의

   실제 개발 환경에서는 대부분 액션과 리듀서를 하나의 파일에 만드는 경우가 많음

   이렇게 하나의 파일에 만드는 것을 Ducks 패턴이라고 함.

   이전의 예제를 모듈화 시킨다면 counter를 변화를 주는 부분과 text에 변화를 주는 부분 그리고 배열에 변화를 주는 부분을 별도의 파일에 작성하고 다른 파일(index.js)에서 이를 combine한 후 export 하는 형태로 만드는 것이 일반적이다.

   액션을 구분하기 위해서 액션 이름을 만들 때 액션 이름 앞에 접두어를 사용한다.

5. **react 프로젝트에 redux 적용**

   1. 패키지 설치

      ```bash
      yarn add react-redux
      ```

   2. 리덕스 관련 파일들을 모듈화하기 위한 디렉토리를 생성
   3. counter.js 와 todos.js 작성

      ```jsx
      // modules/counter.js

      // 타입 생성 - 매개변수를 받아서 증감, INCREASE, DECREASE
      const SET_DIFF = "counter/SET_DIFF";
      const INCREASE = "counter/INCREASE";
      const DECREASE = "counter/DECREASE";

      // 액션 생성 함수
      export const setDiff = (diff) => ({ type: SET_DIFF, diff });
      export const increase = () => ({ type: INCREASE });
      export const decrease = () => ({ type: DECREASE });

      // 초기 상태 선언
      const initialState = {
        number: 0,
        diff: 1,
      };

      // reducer
      export default function counter(state = initialState, action) {
        switch (action.type) {
          case SET_DIFF:
            return { ...state, diff: action.diff };
          case INCREASE:
            return { ...state, number: state.number + state.diff };
          case DECREASE:
            return { ...state, number: state.number - state.diff };
          default:
            return state;
        }
      }
      ```

      ```jsx
      // modules/todos.js

      const ADD_TODO = "todos/ADD_TODO";
      const TOGGLE_TODO = "todos/TOGGLE_TODO";

      // 액션 생성 함수
      let nextId = 1;

      export const addToDo = (text) => ({
        type: ADD_TODO,
        todo: {
          id: nextId++,
          text,
        },
      });

      export const toggleToDO = (id) => ({
        type: TOGGLE_TODO,
        id,
      });

      const initialState = [];

      export default function todos(state = initialState, action) {
        switch (action.type) {
          case ADD_TODO:
            return state.concat(action.todo);
          case TOGGLE_TODO:
            return state.map((todo) =>
              todo.id === action.id ? { ...todo, done: !todo.none } : todo
            );
          default:
            return state;
        }
      }
      ```

   4. 2개의 리듀서를 하나로 합쳐서 export 해주는 파일을 생성 - index.js

      ```jsx
      import { combineReducers } from "redux";

      import counter from "./counter";
      import todos from "./todos";

      const rootReducer = combineReducers({ counter, todos });

      export default rootReducer;
      ```

   5. src 디렉토리의 index.js 파일에서 reducer를 가져오고 store를 생성한 후 Provider로 넘겨준다.

      ```jsx
      // index.js
      import rootReducer from "./modules";
      import { createStore } from "redux";
      import { Provider } from "react-redux";

      const store = createStore(rootReducer);
      // console.log(store.getState());

      const root = ReactDOM.createRoot(document.getElementById("root"));
      root.render(
        <Provider store={store}>
          <App />
        </Provider>
      );
      ```

   6. 컴포넌트의 분류
      - Presentational Component
        리덕스 스토어에 직접 접근하지 않고 필요한 값이나 함수를 넘겨받아서 사용하는 컴포넌트로 UI를 선언하는 것에만 집중
      - Container Component
        리덕스 스토어에 접근해서 상태를 조회하거나 액션을 디스패치하는 컴포넌트로 HTML 태그를 사용하지 않고 Component를 가져와서 사용만 함.

6. **redux 실습(react-redux)**

   1. 필요한 패키지 설치

      ```bash
      yarn add redux react-redux
      ```

   2. Counter.jsx 작성

      ```jsx
      import React from "react";

      export default function Counter({ number, onIncrease, onDecrease }) {
        return (
          <div>
            <h1>{number}</h1>
            <div>
              <button onClick={onIncrease}> +1</button>
              <button onClick={onDecrease}> -1</button>
            </div>
          </div>
        );
      }
      ```

   3. ToDos.jsx 작성

      ```jsx
      import React from "react";

      // 한 개의 ToDo를 출력하기 위한 컴포넌트
      const ToDoItem = ({ todo, onToggle, onRemove }) => {
        return (
          <>
            <input type="checkbox" />
            <span>todo</span>
            <button>삭제</button>
            <br />
          </>
        );
      };

      const ToDos = ({
        input,
        todos,
        onChangeInput,
        onInsert,
        onToggle,
        onRemove,
      }) => {
        const onSubmit = (e) => {
          e.preventDefault();
        };

        return (
          <div>
            <form onSubmit={onSubmit}>
              <input />
              <button type="submit">등록</button>
            </form>
            <div>
              <ToDoItem />
              <ToDoItem />
              <ToDoItem />
              <ToDoItem />
              <ToDoItem />
            </div>
          </div>
        );
      };

      export default ToDos;
      ```

   4. redux 관련 모듈 생성

      Counter를 위한 모듈 생성

      ```jsx
      // 1. 액션 타입 정의
      const INCREASE = "counter/INCREASE";
      const DECREASE = "counter/DECREASE";

      // 2. 액션 생성 함수 정의
      export const increase = () => ({
        type: INCREASE,
      });

      export const decrease = () => ({
        type: DECREASE,
      });

      // 3. 초기 상태를 정의
      const initialState = {
        number: 0,
      };

      // 4. 리듀서 함수를 생성
      const counter = (state = initialState, action) => {
        switch (action.type) {
          case INCREASE:
            return { number: state.number + 1 };
          case DECREASE:
            return { number: state.number - 1 };
          default:
            return state;
        }
      };

      export default counter;
      ```

      ToDos를 위한 모듈 생성

      ```jsx
      const CHANGE_INPUT = "todos/CHANGE_INPUT";
      const INSERT = "todos/INSERT";
      const TOGGLE = "todos/TOGGLE";
      const REMOVE = "todos/REMOVE";

      export const changeInput = (input) => ({
        type: CHANGE_INPUT,
        input,
      });

      // 샘플 데이터를 2개 삽입할 것이라서 3
      let nextId = 3;
      export const insert = (text) => ({
        type: INSERT,
        todo: {
          id: nextId++,
          text,
          done: false,
        },
      });

      export const toggle = (id) => ({
        type: TOGGLE,
        id,
      });

      export const remove = (id) => ({
        type: REMOVE,
        id,
      });

      const initialState = {
        input: "",
        todos: [
          {
            id: 1,
            text: "Node",
            done: true,
          },
          {
            id: 2,
            text: "React",
            done: false,
          },
        ],
      };

      const todos = (state = initialState, action) => {
        switch (action.type) {
          case CHANGE_INPUT:
            return { ...state, input: action.input };
          case INSERT:
            return { ...state, todos: state.todos.concat(action.todo) };
          case TOGGLE:
            return {
              ...state,
              todos: todos.map((todo) =>
                todo.id === action.id
                  ? {
                      ...todo,
                      done: !todo.done,
                    }
                  : todo
              ),
            };
          case REMOVE:
            return {
              ...state,
              todos: state.todos.filter((todo) => todo.id !== action.id),
            };
          default:
            return state;
        }
      };

      export default todos;
      ```

   5. index.js에서 내보내주기

      ```jsx
      import { combineReducers } from "redux";
      import counter from "./counter";
      import todos from "./todos";

      const rootReducer = combineReducers({
        counter,
        todos,
      });

      export default rootReducer;
      ```

   6. react app에 redux를 적용

      root의 index.js를 수정

      ```jsx
      import { legacy_createStore as createStore } from "redux";
      import { Provider } from "react-redux";
      import rootReducer from "./modules";

      const store = createStore(rootReducer);

      const root = ReactDOM.createRoot(document.getElementById("root"));
      root.render(
        <Provider store={store}>
          <App />
        </Provider>
      );
      ```

   7. 컨테이너 컴포넌트 작업

      컨테이너 컴포넌트는 redux를 사용하는 컴포넌트

      src 디렉토리에 containers 디렉토리 생성

      컨테이너 컴포넌트를 생성

      react-redux의 connect 함수 사용

      ```jsx
      connect(mapStateToProps, mapDispatchToProps)(연동할 컴포넌트)
      ```

      - mapStateToProps
        리덕스 스토어 안의 상태를 컴포넌트의 props로 넘겨주기 위해서 생성하는 함수
      - mapDispatchToProps
        액션 생성 함수를 컴포넌트의 props로 넘겨주기 위해 사용하는 함수

      CounterContianer 작성

      ```jsx
      import { connect } from "react-redux";
      import Counter from "../components/Counter";
      import { increase, decrease } from "../modules/counter";

      const CounterContainger = ({ number, increase, decrease }) => {
        return (
          <Counter
            number={number}
            onIncrease={increase}
            onDecrease={decrease}
          />
        );
      };

      const mapStateProps = (state) => ({
        number: state.counter.number,
      });

      const mapDispatchToProps = (dispatch) => ({
        increase: () => {
          dispatch(increase());
        },
        decrease: () => {
          dispatch(decrease());
        },
      });

      export default connect(
        mapStateProps,
        mapDispatchToProps
      )(CounterContainger);
      ```

      ToDosContainer 작성

      ```jsx
      import { connect } from "react-redux";
      import { changeInput, insert, toggle, remove } from "../modules/todos";
      import ToDos from "..components/ToDos";

      const ToDosContainer = ({
        input,
        todos,
        changeInput,
        insert,
        toggle,
        remove,
      }) => {
        return (
          <ToDos
            input={input}
            todos={todos}
            onChangeInput={changeInput}
            onInsert={insert}
            onToggle={toggle}
            onRemove={remove}
          />
        );
      };

      export default connect(
        ({ todos }) => ({
          input: todos.input,
          todos: todos.todos,
        }),
        { changeInput, insert, toggle, remove }
      )(ToDosContainer);
      ```

   8. App.js 수정

      ```jsx
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
      ```

7. **MiddleWare**

   1. 개요

      액션이 디스패치된 후 리듀서에서 해당 액션을 받아서 작업을 수행하기 전이나 후에 추가적인 작업을 할 수 있도록 해주는 것.

      작업을 수행하기 전에는 유효성 같은 검사 작업을 많이 하고 작업이 수행된 후에는 로그 기록을 많이 한다.

      유사한 용도로 사용되는 것들을 부르는 명칭으로는 `Filter, Interceptor, AOP` 등이 있다.

   2. 직접 생성한 미들웨어 적용

      src 디렉토리에 middlewares 디렉토리 생성하고 미들웨어로 사용할 mymiddleware.js 생성

      ```jsx
      const mymiddleware = (store) => (next) => (action) => {
        // 동작을 로깅
        console.log(action);

        // 다음 미들웨어나 리듀서에게 전달
        const result = next(action);

        // 작업이 끝나고 난 후 확인
        console.log(store.getState());

        return result;
      };

      export default mymiddleware;
      ```

      src/index.js 에서 import 시켜준다.

      ```jsx
      import {
        legacy_createStore as createStore,
        applyMiddleware,
      } from "redux";
      import { Provider } from "react-redux";
      import rootReducer from "./modules";

      import mymiddleware from "./middlewares/mymiddleware";

      const store = createStore(rootReducer, applyMiddleware(mymiddleware));

      const root = ReactDOM.createRoot(document.getElementById("root"));
      root.render(
        <Provider store={store}>
          <App />
        </Provider>
      );
      ```

   3. 외부 라이브러리를 이용한 로그 기록

      ```bash
      yarn add redux-logger
      ```

      src 디렉토리의 index.js 수정
