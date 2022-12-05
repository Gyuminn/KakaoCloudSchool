import React, { Component } from "react";

class EventPractice extends Component {
  //   constructor(props) {
  //     super(props);
  //     this.handleChange = this.handleChange.bind(this);
  //     this.handleClick = this.handleClick.bind(this);
  //   }s
  state = {
    name: "",
    message: "",
  };

  // babel이 인스턴스의 메서드로 변환을 자동으로 수행
  // this.handleChange로 이 베서드를 사용하는 것이 가능.
  handleChange = (e) => {
    this.setState({
      [e.target.name]: e.target.value,
    });
  };

  handleClick = (e) => {
    alert(this.state.name + ":" + this.state.message);
    this.setState({
      name: "",
      message: "",
    });
  };

  handleKeyPress = (e) => {
    if (e.key === "Enter") {
      this.handleClick();
    }
  };
  render() {
    return (
      <>
        <h1>useState를 이용한 상태 관리</h1>
        <input
          type="text"
          name="name"
          placeholder="이름을 입력하세요"
          value={this.state.name}
          onChange={this.handleChange}
        />

        <input
          type="text"
          name="message"
          placeholder="메시지을 입력하세요"
          value={this.state.message}
          onChange={this.handleChange}
          onKeyPress={this.handleKeyPress}
        />
        <button onClick={this.handleClick}>학인</button>
      </>
    );
  }
}

export default EventPractice;
