import React from "react";

export default function CreateUser({
  username,
  email,
  handleChange,
  handleCreate,
}) {
  return (
    <div>
      <input
        name="username"
        value={username}
        onChange={handleChange}
        placeholder="이름 입력"
      />
      <input
        name="email"
        value={email}
        onChange={handleChange}
        placeholder="이메일 입력"
      />
      <button onClick={handleCreate}>추가</button>
    </div>
  );
}
