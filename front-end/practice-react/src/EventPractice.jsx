import React, { useState } from "react";

const EventPractice = () => {
  const [form, setForm] = useState({
    username: "",
    message: "",
  });

  const { username, message } = form;

  const handleChange = (e) => {
    const nextForm = {
      ...form,
      [e.target.name]: e.target.value,
    };
    setForm(nextForm);
  };
  const handleClick = () => {
    alert(username + ": " + message);
    setForm({
      username: "",
      message: "",
    });
  };
  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleClick();
    }
  };
  return (
    <>
      <h1>useState를 이용한 상태 관리</h1>
      <input
        type="text"
        name="username"
        placeholder="이름을 입력하세요"
        value={username}
        onChange={handleChange}
      />

      <input
        type="text"
        name="message"
        placeholder="메시지를 입력하세요"
        value={message}
        onChange={handleChange}
        onKeyPress={handleKeyPress}
      />
      <button onClick={handleClick}>학인</button>
    </>
  );
};

export default EventPractice;
