import EventPractice from "./EventPractice";
import MyComponent from "./MyComponent";
import ScrollBox from "./ScrollBox";
import StateComponent from "./StateComponent";
import ValidationSample from "./ValidationSample";

export default function App() {
  return (
    <>
      <MyComponent name="gyumin" year={97}>
        어쩌구 저쩌구
      </MyComponent>
      <StateComponent />
      <EventPractice />
      <br />
      <ValidationSample />
      <ScrollBox ref={(ref) => (this.scrollBox = ref)} />
      <button
        onClick={(e) => {
          this.scrollBox.scrollToBottom();
        }}
      >
        맨 아래로
      </button>
    </>
  );
}
