import React, { Component } from "react";

class StateComponent extends Component {
  constructor(props) {
    // 상위 클래스의 생성자 호출
    super(props);

    // state 생성
    this.state = { number: 0 };
  }

  const;
  render() {
    return (
      <>
        <p>숫자:{this.state.number}</p>
        <button
          onClick={(e) => {
            this.setState({ number: this.state.number + 1 }, () => {
              console.log("state의 값이 변경됨");
              console.log(this.state);
            });
          }}
        >
          증가
        </button>
      </>
    );
  }
}

export default StateComponent;
