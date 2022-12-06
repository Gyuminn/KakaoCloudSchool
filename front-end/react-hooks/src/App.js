import React, { Component, useEffect, useRef, useState } from "react";
import CreateUser from "./CreateUser";
import { InputSample } from "./InputSample";
import UserList from "./UserList";

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

const ClassEffect = () => {
  const [count, setCount] = useState(0);

  useEffect(() => {
    console.log("마운트와 업데이트가 끝나면 호출");
    document.title = `you clicked ${count} times`;
  }, [count]);

  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={(e) => setCount(count + 1)}>증가</button>
    </div>
  );
};

function App() {
  const [users, setUsers] = useState([
    {
      id: 1,
      username: "gyumin",
      email: "rhkdtlrtm12@gmail.com",
      active: false,
    },
    {
      id: 2,
      username: "Gyuminn",
      email: "rhkdtlrtm12@hanyang.ac.kr",
      active: true,
    },
    {
      id: 3,
      username: "q.min",
      email: "kksv2@naver.com",
      active: false,
    },
  ]);

  const [inputs, setInputs] = useState({
    username: "",
    email: "",
  });

  const handleChange = (e) => {
    // 입력 요소 초기화
    setInputs({
      ...inputs,
      [e.target.name]: e.target.value,
    });
  };

  // 변수를 생성
  const nextId = useRef(4);

  const { username, email } = inputs;
  const handleCreate = () => {
    const user = {
      id: nextId.current,
      username,
      email,
    };

    setUsers([...users, user]);

    setInputs({
      username: "",
      email: "",
    });

    nextId.current += 1;
  };

  const handleRemove = (id) => {
    // users state에서 id가 id인 데이터를 삭제
    // 실제로는 id가 일치하지 않는 데이터만 가지고 배열을 만들어서 수정
    setUsers(users.filter((user) => user.id !== id));
  };

  // id에 해당하는 데이터의 active 속성의 값을 반전
  const handleToggle = (id) => {
    setUsers(
      users.map((user) =>
        user.id === id ? { ...user, active: !user.active } : user
      )
    );
  };

  return (
    <>
      <ClassState />
      <FunctionState />
      <InputSample />
      <ClassEffect />
      <UserList
        users={users}
        handleR
        emove={handleRemove}
        handleToggle={handleToggle}
      />
      <CreateUser
        username={username}
        email={email}
        handleChange={handleChange}
        handleCreate={handleCreate}
      />
    </>
  );
}

export default App;
