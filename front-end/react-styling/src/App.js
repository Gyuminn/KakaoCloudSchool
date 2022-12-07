import "./App.css";
// import StyledComponent from "./components/StyledComponent";
// import CSSModule from "./CSSModule";

// import styles from "./App.scss";
// import classNames from "classnames/bind";
import Button from "./components/Button";
import axios from "axios";

// const cx = classNames.bind(styles);

function App() {
  // const isBlue = true;

  return (
    <>
      {/* <div className={cx("box", { blue: isBlue })}>
        <div className={cx("box-inside")} />
      </div>
      <CSSModule />
      <Button>버튼</Button> */}
      {/* <nav>
        <div className="nav-wrapper">
          <div>리액트</div>
        </div>
      </nav>
      <div>머터리얼 디자인</div> */}
      {/* <StyledComponent /> */}
      <Button
        // onClick={(e) => {
        //   let request = new XMLHttpRequest();
        //   request.open("GET", "https://jsonplaceholder.typicode.com/users");
        //   // POST 방식일 때는 send에 파라미터를 대입
        //   request.send("");
        //   request.addEventListener("load", () => {
        //     let data = JSON.parse(request.responseText);
        //     console.log(data);
        //   });
        //   request.addEventListener("error", (error) => {
        //     console.log(error);
        //   });
        // }}
        // onClick={(e) => {
        //   fetch("https://jsonplaceholder.typicode.com/users")
        //     .then((response) => response.json())
        //     .then((data) => console.log(data))
        //     .catch((error) => console.log(error));
        // }}
        onClick={(e) => {
          axios
            .get("https://jsonplaceholder.typicode.com/users")
            .then((response) => console.log(response.data))
            .catch((error) => console.log(error));
        }}
      >
        다운로드
      </Button>
    </>
  );
}

export default App;
