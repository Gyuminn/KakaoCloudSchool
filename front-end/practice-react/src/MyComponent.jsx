import React, { Component } from "react";

import PropTypes from "prop-types";
class MyComponent extends Component {
  render() {
    // props 가져오기
    const { name, year, children } = this.props;
    return (
      <>
        <div>안녕하세요 제 이름은 {name} 입니다.</div>
        <p>children 값은 {children}</p>
        <p>제가 태어난 해는 {year} 입니다.</p>
      </>
    );
  }
}

MyComponent.defaultProps = {
  name: "기본이름",
};

MyComponent.propTypes = {
  name: PropTypes.string,
  year: PropTypes.number.isRequired,
};

export default MyComponent;
