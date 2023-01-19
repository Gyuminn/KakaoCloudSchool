# [React] hooks

1. **컴포넌트 반복**

   동일한 모양의 컴포넌트를 여러 개 배치

   배열 형태의 데이터를 출력할 때 유용하게 사용

   1. 배열.map()

      배열의 데이터를 순회하면서 매개변수로 받은 함수를 요소 단위로 수행한 후 그 결과를 모아서 다시 배열로 리턴하는 함수.

      map에 설정하는 매개변수

      - 첫 번째는 callback함수(필수) - 매개변수는 3개까지 될 수 있고 하나의 데이터를 반드시 리턴해야 한다.
        - 첫 번째 매개변수는 순회하는 각 요소
        - 두 번째 매개변수는 인덱스
        - 세 번째 매개변수는 배열 그 자체
      - 두 번째 callback 함수 내부에서 사용할 this 참조(선택)

   2. 배열을 이용한 컴포넌트 반복

      ```jsx
      import React from "react";

      export const Iteration = () => {
        const names = ["java", "spring", "database", "node", "express"];
        const nameList = names.map((name, idx) => <li key={idx}>{name}</li>);

        return (
          <>
            <ul>{nameList}</ul>
          </>
        );
      };
      ```

      react에서는 Component 배열을 렌더링할 때 어떤 원소에 변동이 있었는지 확인하기 위해서 key 값을 설정해야 한다.

      key를 설정하지 않으면 변동 사항을 확인하기 위해서 배열 전체를 순회해야 하지만 key를 설정하면 더욱 빠르게 변동 사항을 확인할 수 있다.

   3. filter는 매개변수가 1개이고 boolean을 리턴하는 함수를 매개변수로 대입하는데 함수의 결과가 true인 데이터만 모아서 다시 배열로 리턴하는 함수.

2. **Life Cycle**

   인스턴스가 생성되고 소멸될 까지의 과정

   생성은 생성자(Constructor)가 담당하고 소멸은 소멸자가(Destructor)가 담당한다.

   IoC(Inversion of Control - 제어의 역전, 클래스는 개발자가 생성하지만 인스턴스의 생성과 소멸은 개발자가 하지 않고 다른 프레임워크나 컨테이너 등이 하는 형태)에서는 특별한 경우가 아니면 생성자를 이용해서 인스턴스를 생성하지 않음.

   react에서 컴포넌트는 IoC가 적용되는 객체

   `안드로이드나 react는 IoC가 적용된 객체를 Component라고 부른다.`

   `Spring에서는 Spring Bean이라고 부른다`

   IoC가 적용되면 일반적으로 생성자를 직접 호출하지 않기 때문에 수명 주기 관련 메서드를 이용해서 생성과 소멸될 때 작업을 수행

   1. react의 Component의 수명 주기

      Mount(컴포넌트가 메모리 할당을 받아서 출력) → Update(컴포넌트 정보를 업데이트하는 것으로 리렌더링) → Unmount(컴포넌트가 화면에서 제거)

   2. Mount 될 때 호출되는 메서드
      - contructor
        Component를 새로 만들 때 마다 호출되는 클래스 생성자 메서드
        가장 먼저 호출, `state 초기화를 수행`
      - getDerivedStateFromProps
        props에 있는 값을 state에 동기화할 때 호출
      - render
        UI를 렌더링할 때 호출되는 메서드
        `this.props와 this.state를 이용해서 props와 state 접근이 가능`
      - componentDidMount
        Component가 웹 브라우저 상에 나타난 후(화면에 보여지고 난 후) 호출되는 메서드
        `비동기 작업(네트워크 사용이나 타이머 작업) 수행`
   3. update 할 때 호출되는 메서드
      - getDerivedStateFromProps
        props에 있는 값을 state에 동기화할 때 호출
      - shouldComponentUpdate
        리렌더링을 결정하는 메서드로 여기서 false를 리턴하면 리렌더링되지 않음.(아래 메서드드 들을 호출하지 않음)
      - render
        Component를 리렌더링
      - getSnapshotBeforeUpdate
        변경된 내용을 DOM에 적용하기 직전에 호출되는 메서드
      - componentDidUpdate
        업데이트 되고 나면 호출되는 메서드
   4. Unmount될 때 호출되는 메서드
      - componentWillUnmount
        Component가 웹 브라우저 상에서 사라지기 직전에 호출되는 메서드로 `memory leak(타이머)이 발생할 수 있는 객제의 제거 작업을 수행`
   5. 라이프 사이클 이용 시 주의할 점

      `react의 개발 모드가 React.StrictMode로 설정되어 있으면 개발 환경에서는 Mount를 2번씩 한다.`

3. **에러를 화면에 출력**

   1. 에러를 발생시키기 위해서 없는 프로퍼티를 출력

      {this.state.missing.value}

   2. state 관련된 에러가 발생하면 호출되는 메서드를 재정의해서 에러가 발생했다는 사실을 출력해주는 컴포넌트를 생성 - ErrorBoundary.jsx

      ```jsx
      class ErrorBoundary extends Component {
        state = {
          error: false,
        };

        // 컴포넌트에서 예외가 발생하면 호출되는 메서드
        componentDidCatch(error, info) {
          this.setState({
            error: true,
          });
          console.log({ error, info });
        }

        render() {
          if (this.state.error) {
            return <div>에러가 발생했습니다.</div>;
          }

          return this.props.children;
        }
      }
      ```

4. **Hooks**

   React 16.8 버전에 추가

   Class Component의 기능을 Function Component에서 사용할 수 있도록 해주는 기능.

5. **useState**

   state를 함수 컴포넌트 안에서 사용할 수 있도록 해주는 hook

   useState의 매개변수는 state의 초기값이 되고 리턴하는 데이터는 state와 state의 값을 변경할 수 있는 setter 함수의 배열

   1. 클래스형 컴포넌트와 함수형 컴포넌트의 비교

      ```jsx
      import React, { Component, useState } from "react";

      // 클래스 컴포넌트
      class ClassState extends Component {
        /*
        // 생성자를 만들지 않고 이렇게 state를 초기화해도 된다.
        // babel이 알아서 this를 붙여주기 때문!
        state = {
          count:0
        }
        */
        constructor(props) {
          super(props);
          this.state = {
            count: 0,
          };
        }

        render() {
          return (
            <>
              <p>클릭을 {this.state.count}번 수행</p>
              <button
                onClick={(e) => this.setState({ count: this.state.count + 1 })}
              >
                +1
              </button>
            </>
          );
        }
      }

      // 함수형 컴포넌트에서 useState 사용

      const FunctionState = () => {
        const [count, setCount] = useState(0);

        return (
          <>
            <p>클릭을 {count}번 수행</p>
            <button onClick={(e) => setCount((prev) => prev + 1)}>+1</button>
          </>
        );
      };

      function App() {
        return (
          <>
            <ClassState />
            <FunctionState />
          </>
        );
      }

      export default App;
      ```

6. **useRef**

   ref: 컴포넌트를 조작하기 위해서 붙이는 일종의 id와 같은 변수

   useRef로 만들어진 변수는 값이 변경된다고 해서 컴포넌트가 리렌더링 되지는 않음.

   useRef(초기값)으로 변수를 생성하고 컴포넌트나 DOM에 설정할 때는 ref 속성에 생성된 변수를 대입해주면 된다.

   특정 input에 포커스 설정하기

   - InputSample.jsx 파일을 생성하고 작성

     ```jsx
     import React, { useRef } from "react";
     import { useState } from "react";

     export const InputSample = () => {
       // 2개의 속성을 가진 state 생성
       const [inputs, setInputs] = useState({
         name: "",
         nickname: "",
       });

       // state를 편리하게 사용하기 위해서 비 구조화할당
       const { name, nickname } = inputs;

       // react에서 다른 컴포넌트나 DOM을 참조할 수 있는 변수를 생성
       const nameInput = useRef();

       const handleChange = (e) => {
         setInputs({
           [e.target.name]: e.target.value,
         });
       };

       const handleReset = (e) => {
         setInputs({
           name: "",
           nickname: "",
         });
         // 이름 입력 란으로 포커스 옮기기
         nameInput.current.focus();
       };

       return (
         <>
           <input
             name="name"
             value={name}
             onChange={handleChange}
             ref={nameInput}
           />
           <input name="nickname" value={nickname} onChange={handleChange} />
           <button onClick={handleReset}>초기화</button>
         </>
       );
     };
     ```

7. **useEffect**

   state가 변경된 후 수행할 side effect를 설정하는 Hook

   Class의 LifeCycle 중에서 componentDidMount와 componentDidUpdate, ComponentWillUnmount가 합쳐진 형태

   1. Class의 생명 주기 메서드 사용

      ```jsx
      class ClassEffect extends Component {
        // 생성자
        constructor(props) {
          super(props);
          console.log("생성자 - 가장 먼저 호출되는 메서드");
          this.state = {
            count: 0,
          };
        }

        // Component가 Mount된 후 호출되는 메서드
        componentDidMount() {
          console.log("마운트된 후 호출되는 메서드");
          document.title = `You clicked ${this.state.count} times`;
        }

        // Component가 Update된 후 호출되는 메서드
        componentDidUpdate() {
          console.log("업데이트된 후 호출되는 메서드");
          document.title = `Yout clicked ${this.state.count} times`;
        }

        render() {
          return (
            <div>
              <p>You clicked {this.state.count} times</p>
              <button
                onClick={(e) => this.setState({ count: this.state.count + 1 })}
              >
                증가
              </button>
            </div>
          );
        }
      }
      ```

   2. useEffect를 이용해서 함수형 컴포넌트에 위와 유사한 형태 구현

      ```jsx
      const ClassEffect = () => {
        const [count, setCount] = useState(0);

        useEffect(() => {
          console.log("마운트와 업데이트가 끝나면 호출");
          document.title = `you clicked ${count} times`;
        });

        return (
          <div>
            <p>You clicked {count} times</p>
            <button onClick={(e) => setCount(count + 1)}>증가</button>
          </div>
        );
      };
      ```

   3. useEffect 함수

      ```jsx
      // 원형
      useEffect(() => {수행할 내용}, deps배열}
      ```

      deps 배열을 생략하면 Mount된 경우와 모든 state가 변경될 때 마다 호출

      deps 배열을 비워두게 되([ ]) Mount된 후에만 호출한다.

      deps 배열에 state를 설정하게 되면 state 값이 변경될 때도 호출

      `수행할 내용에서 함수를 리턴하면 cleanup 함수가 된다`

      cleanup 함수는 Unmount될 때 호출하는 함수이다.

8. **객체 배열을 함수형 컴포넌트로 출력하고 삽입, 삭제, 갱신을 수행하기**

   1. 하나의 객체(이름과 이메일을 소유하고 기본키로 id를 소유)를 출력할 컴포넌트와 객체 배열을 출력할 컴포넌트를 작성 - UserList.jsx
   2. 데이터 추가 구현

      추가 화면에 해당하는 컴포넌트 생성 - CreateUser.jsx

      실제 데이터 추가는 App.js 파일에 있는 데이터에 추가되어야 한다.

      이런 경우에는 App.js state나 이벤트 핸들러를 만들고 CreateUser에게 props 형태로 전달을 해서 사용하는 것이 편리하다.

   3. 삭제 구현

      데이터를 출력할 때 이메일 뒤에 삭제 버튼을 추가해서 삭제 버튼을 누르면 그 데이터가 삭제되도록 구현

      삭제의 경우 대부분 기본키를 매개변수로 받아서 그 기본키에 해당하는 데이터를 삭제

   4. 배열의 데이터를 수정 - 계정 이름을 클릭하면 active 속성 값을 toggle(반전) 시켜서 글자 색상을 변하도록 하기
   5. useEffect 활용

9. **useMemo**

   연산된 값을 재사용하는 Hook

   성능 최적화를 위해서 사용

   매개변수로 연산을 수행하는 함수와 배열을 대입받는데, 배열에 변화가 생긴 경우에만 연산을 수행하는 함수를 수행하고 그렇지 않은 경우는 함수를 호출해도 결과만 다시 제공한다.

10. **useCallback**

    특정 함수를 새로 만들지 않고 재사용하고자 할 때 사용

    컴포넌트에 구현한 함수들은 컴포넌트가 렌더링될 때마다 다시 생성

    컴포넌트가 많아지고 렌더링이 자주 발생하면 함수들을 다시 만드는 것은 비효율적이 될 수 있다.

    useCallback을 이용하면 데이터가 변경된 경우에만 함수를 다시 만들도록 할 수 있다.

    첫 번째 매개변수는 함수이고 두 번째 매개변수는 데이터의 배열이다.

11. **React.memo**

    컴포넌트의 props가 변경되지 않았다면 리렌더링을 방지해서 리렌더링의 성능 최적화를 해줄 수 있는 함수.

    이 함수를 이용해서 컴포넌트를 감싸주기만 하면 리렌더링이 필요한 상황에서만 리렌더링을 한다.
