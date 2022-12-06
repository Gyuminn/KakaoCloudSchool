import React, { Component, useState } from "react";
import { InputSample } from "./InputSample";

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
        <button onClick={(e) => this.setState({ count: this.state.count + 1 })}>
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
      <InputSample />
    </>
  );
}

export default App;
