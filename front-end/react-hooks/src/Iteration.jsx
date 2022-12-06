import React, { Component } from "react";

class Iteration extends Component {
  // 내용이 변경되면 Component를 리렌더링하는 state를 생성
  state = {
    names: ["java", "spring", "database", "node", "express"],
    name: "",
  };

  // Input에 입력하면 name state의 값을 변경하는
  // 이벤트 처리 함수
  handleChange = (e) => {
    this.setState({
      name: e.target.value,
    });
  };

  // name의 값을 name에 추가하는 함수 - 버튼을 누르면 동작
  handleInsert = (e) => {
    this.setState({
      names: this.state.names.concat(this.state.name),
      name: "",
    });
  };

  // 데이터 삭제 함수
  // index를 매개변수로 받아서 삭제
  handleRemove = (index) => {
    // 확인 대화상자를 출력해서 아니오를 누르면 리턴
    let result = window.confirm("정말로 삭제하시겠습니까?");
    if (result === false) {
      return;
    }

    const { names } = this.state;

    // slice(매개변수 2개를 받아 복제하는 메서드)
    // 배개변수는 시작 위치와 마지막 위치를 대입
    this.setState({
      names: names.filter((item, idx) => idx !== index),
    });
  };
  render() {
    const nameList = this.state.names.map((name, idx) => (
      <li key={idx} onDoubleClick={() => this.handleRemove(idx)}>
        {name}
        <button onClick={() => this.handleRemove(idx)}>삭제</button>
      </li>
    ));
    return (
      <>
        {/* {this.state.missing.value} */}
        <input onChange={this.handleChange} value={this.state.name} />
        <button onClick={this.handleInsert}>추가</button>
        <ul>{nameList}</ul>
      </>
    );
  }
}

export default Iteration;
