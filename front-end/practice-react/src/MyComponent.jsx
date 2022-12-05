import React from "react";

import PropTypes from "prop-types";

const MyComponent = ({ name, year, children }) => {
  return (
    <>
      <div>나의 이름은 {name} 이다.</div>
      <p>나이는 {year}에 태어났다.</p>
      <p>{children}</p>
    </>
  );
};

MyComponent.propTypes = {
  name: PropTypes.string,
  year: PropTypes.number.isRequired,
};

MyComponent.defaultProps = {
  name: "gyumin",
};

export default MyComponent;
