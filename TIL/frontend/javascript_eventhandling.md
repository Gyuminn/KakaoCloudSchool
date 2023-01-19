## Event Handling

1. **Event Handling**

   1. Event & Event Handler

      Event: 상태 변화, 사용자가 발생(click, hover)시키는 것도 있고 시스템이 발생(timer, 데이터를 전부 다운로드 받으면)시키는 것도 있음.

      Event Handling: Event가 발생했을 때 수행할 동작을 설정

      이러한 Event Handling을 수행하는 객체를 Event Handler 또는 Listener라고 한다.

   2. JavaScript 이벤트 처리 방법
      - inline: 태그 안에서 이벤트 속성에 스크립트 코드를 직접 작성하는 것 - 권장하지 않음.
      - 고전적 이벤트 처리 모델: 객체.이벤트이름 = 함수(함수 이름이 될 수도 있고, 함수를 만드는 코드가 될 수도 있다.)
      - `표준 이벤트 모델`: 객체.addEventListener(”이벤트이름”, 함수 이름이나 함수를 만드는 코드)
        - 표준 이벤트 모델에서는 이벤트 이름 앞에 on을 붙이지 않음.
        - 하나의 이벤트에 여러 함수를 연결하는 것이 가능하고 이벤트 처리 코드를 제거할 때는 removeEventListener를 호출하면 된다.
   3. 이벤트 객체

      이벤트를 처리하는 함수에는 event 객체 1개가 전달된다.

      이 객체에 이벤트와 관련된 정보가 전달된다.

      이벤트 객체의 속성은 이벤트마다 다름.

      IE 하위 버전에서는 이벤트 처리 함수에서 이벤트 객체를 받을 수 없고 window.event로 이벤트 객체를 가져온다.

      ```jsx
      (e) => {
        let event = e || window.event; // IE 하위 버전때문에 이렇게 작성한다.
      };
      ```

      이벤트 처리 함수 내에서 this는 이벤트가 발생한 객체이다. 전에는 많이 사용했는데 최근에는 화살표 함수를 사용하기 때문에 잘 사용하지 않는다.

      this 대신에 document 객체를 이용해서 DOM을 찾아와서 사용.

      - 속성
        - data: Drag & Drop을 할 때 Drop된 객체들의 URL을 문자열 배열로 반환
        - height
        - layerX, layerY
        - modifier: 같이 누른 조합키(ALT, CTRL, SHIFT나 마우스 왼, 오른쪽 판별)
        - pageX, pageY
        - screenX, screenY
        - target: 이벤트가 전달된 객체
        - type: 이벤트의 타입
        - which: 마우스 버튼의 ASCII 코드 값이나 키보드의 키값(window.event.keyCode)
        - width:
        - x
        - y

   4. Event Trigger

      이벤트를 강제로 발생시키는 것

      객체.이벤트이름()

   5. Default Event Handler

      특정한 태그들에는 기본적인 이벤트 처리 코드가 포함되어 있다.

      - a 태그는 다른 URL이나 책갈피로 이동
      - input type이 submit이면 form의 action으로 요청을 전송
      - input type이 reset이면 form을 clear
      - button이 form 안에 존재하면 submit 기능을 수행
        - submit이나 reset은 form에 발생하는 이벤트이다.
        - 버튼을 누르지만 실제로는 form의 이벤트이다.
      - keydown이 발생하면 누른 키를 input에 출력

      기본 기능을 수행하지 않도록 할 때는 event 객체의 prevendDefault()를 호출하면 된다.

   6. Event Bubbling

      부모와 자식 태그 관계에서 양쪽에 동일한 이벤트에 대한 핸들러가 존재하면 자식 태그에서 이벤트가 발생하면 자식 태그의 핸들러를 수행하고 부모 태그의 핸들러도 수행한다.

      이 때 이벤트 버블링을 막고자 하면 이벤트 객체의 `stopPropagation` 메서드를 호출하면 된다.

   7. 여러 종류의 이벤트
      - click, dblclick - 누른 좌표는 screenX, screenY
      - keydown, keypress, keyup - which 속성을 이용해서 누른 키의 ASCII 코드값을 찾아올 수 있고 code로 상수 형태로 가져올 수 있고 key로 문자를 가져올 수 있다.
      - mousemove, mouseout, mouseover, mouseup
      - focusin(focus), focusout(blur) - focusout 이벤트에서 유효성 검사를 하기도 한다.
      - load: 메모리에 적재되면
        window에서는 body에 있는 태그를 전부 읽어서 메모리에 적재하면 호출되는 이벤트
        img나 file의 경우는 내용을 전부 읽었을 때 호출되는 이벤트
        ajax에서는 서버에서 응답을 받았을 때
      - beforeunload: 브라우저의 내용이 사라지기 직전
        세션을 이용해서 로그인 처리를 이용하는 경우 로그아웃을 할 때 세션을 초기화하는데 브라우저 창을 그냥 닫아버리면 세션 초기화를 하지 못하는 경우가 있는데 이 이벤트를 이용해서 브라우저가 닫히는 시점을 찾아서 세션을 초기화하면 된다.
      - change: 값이 변경될 때
        비밀번호 같은 것을 변경할 때 메시지를 출력하는 형태로 많이 이용한다.
      - form에는 submit과 reset 이벤트가 있다.
      - scroll
      - 모바일에서는 터치 이벤트와 방향 전환 이벤트가 있다.
