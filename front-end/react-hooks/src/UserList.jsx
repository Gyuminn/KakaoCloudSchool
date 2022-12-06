import React from "react";

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
