import styles from "./Button.scss";
import classNames from "classnames/bind";

const cx = classNames.bind(styles);

// props를 받아오는데 children으로 넘겨준 것은
// children으로 받고 나머지는 rest로 받기

const Button = ({ children, ...rest }) => {
  return (
    <div className={cx("button")} {...rest}>
      {children}
    </div>
  );
};

export default Button;
