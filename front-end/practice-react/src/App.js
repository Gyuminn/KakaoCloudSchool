import EventPractice from "./EventPractice";
import MyComponent from "./MyComponent";
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
    </>
  );
}
