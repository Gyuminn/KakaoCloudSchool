import "./App.css";
import CSSModule from "./CSSModule";

import styles from "./App.scss";
import classNames from "classnames/bind";

const cx = classNames.bind(styles);

function App() {
  const isBlue = true;

  return (
    <>
      <div className={cx("box", { blue: isBlue })}>
        <div className={cx("box-inside")} />
      </div>
      <CSSModule />
    </>
  );
}

export default App;
