import "./App.css";
import CSSModule from "./CSSModule";

import styles from "./App.scss";
import classNames from "classnames/bind";
import Button from "./components/Button";

const cx = classNames.bind(styles);

function App() {
  const isBlue = true;

  return (
    <>
      <div className={cx("box", { blue: isBlue })}>
        <div className={cx("box-inside")} />
      </div>
      <CSSModule />
      <Button>버튼</Button>
    </>
  );
}

export default App;
