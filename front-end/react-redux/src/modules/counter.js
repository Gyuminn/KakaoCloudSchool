// 1. 액션 타입 정의
// 다른 모듈과 액션 타입이 중복되는 것을 방지
const INCREASE = "counter/INCREASE";
const DECREASE = "counter/DECREASE";

// 2. 액션 생성 함수 정의
export const increase = () => ({
  type: INCREASE,
});

export const decrease = () => ({
  type: DECREASE,
});

// 3. 초기 상태를 정의
const initialState = {
  number: 0,
};

// 4. 리듀서 함수를 생성
const counter = (state = initialState, action) => {
  switch (action.type) {
    case INCREASE:
      return { number: state.number + 1 };
    case DECREASE:
      return { number: state.number - 1 };
    default:
      return state;
  }
};

export default counter;
