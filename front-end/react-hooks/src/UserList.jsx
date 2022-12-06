import React from "react";
import { useEffect } from "react";

function UserList({ users, handleRemove, handleToggle }) {
  return (
    <div>
      {users.map((user) => (
        <User
          user={user}
          key={user.id}
          handleRemove={handleRemove}
          handleToggle={handleToggle}
        />
      ))}
    </div>
  );
}

function User({ user, handleRemove, handleToggle }) {
  // 마운트 될 떄 그리고 state가 변경될 때 모두 호출
  useEffect(() => {
    console.log("컴포넌트가 화면에 나타남");
    // 삽입과 수정 시 호출
    // 이 데이터의 id가 존재하면 수정이고 id가 존재하지 않으면 삽입
    console.log(user);
    // 함수를 리턴하면 컴포넌트가 사라질 때 호출됨.
    return () => {
      // 데이터를 삭제한 경우 호출
      console.log("컴포넌트가 사라짐");
      console.log(user);
    };
  }, [user]);
  return (
    <div>
      <b
        style={{
          cursor: "pointer",
          color: user.active ? "red" : "blue",
        }}
        onClick={(e) => handleToggle(user.id)}
      >
        {user.username}
      </b>
      <br />
      <span>{user.email}</span>
      <button onClick={(e) => handleRemove(user.id)}>삭제</button>
    </div>
  );
}

export default UserList;
