## CSS

1. **작성법**

   - external: 별도의 파일에 작성하고 가져와서 사용
   - internal: HTML 문서 안에 <style></style>을 만들고 작성
   - inline: 태그 안에 style 속성에 작성(inline)

1. **실행 방식**

   - html 코드나 javascript 코드를 전부 읽어서 구조를 만든 후 적용
   - 스타일 시트는 작성 위치에 상관없이 동작

1. **기본 선택자**

   - -
   - tag
   - .class(여러 요소를 그룹화하기 위해서 사용, 대부분 스타일 시트 적용에 사용)
   - #id(문서 내에서 구별하기 위해서 사용, 대부분 자바스크립트에서 사용)

1. **속성 선택자**

   - 선택자[속성이름]: 선택자 안에 속성이 존재하는 경우만 적용
   - 선택자[속성이름=값]: 선택자 안에 속성의 값까지 일치하는 경우만 적용

   ```css
   /* input 속성의 type이 text인 경우 적용 */
   input[type="text"] {
   }
   ```

   - 부분일치
     - A^ = B: A 속성의 값이 B로 시작하는 경우
     - A& = B: A 속성의 값이 B로 끝나는 경우
     - A\* = B: A 속성의 값에 B가 포함된 경우

1. **복합 선택자**

   - 일치 선택자: 선택자1선택자2…
     공백없이 여러 선택자를 나열하면 모든 선택자가 일치해야 한다.
     ```css
     /* div 태그 중에서 class 속성이 content인 경우에 적용 */
     div.content {
     }
     ```
   - 자기 선택자: 선택자1 > 선택자 2

     선택자1에 포함된 선택자2 인데 선택자2가 바로 아래 레벨에 있어야 함.

   - 하위 선택자: 선택자1 선택자2
     공백을 이용해서 선택자를 나열
     선택자2가 선택자1에 포함되어 있으면 적용
     ```html
     <!-- div > span 으로 작성하면 위의 경우만 적용 -->
     <!-- div span으로 작성하면 위의 모든 경우에 적용 -->
     <div><span></span></div>
     <div>
       <ul>
         <span></span>
       </ul>
     </div>
     ```

- 인접 형제 선택자: 선택자1 + 선택자2

  선택자1 과 동일한 레벨에서 다음에 나오는 선택자2에 적용

- 형제 선택자: 선택자1 ~ 선택자2
- 그룹 선택자: , 로 구분
  , 와 나열하게 되면 나열된 선택자 중에 포함되면 적용

1. **pseudo-class selector**
   - 선택자 뒤에 :을 하고 작성

- a:link - 링크에 모두 적용
- a:visited - 링크를 한번이라도 누른 경우 적용

- 동작 - hover, active, focus
- UI 요소 - checked, enabled, disabled,
- 구조 관련 선택자: root, first-child, last-child, nth-last-child, nth-child(n), only-child, not(선택자), empty, first-line, first-letter, nth-of-type(n), first-of-type, last-of-type, only-of-type
- lang(languagecode): 특정 언어 설정에만 적용
  - 언어 코드 2자리 - 국가 코드 2자리(한국은 ko-kr, 미국은 en-US)

1. **pseudo-element**

   - ::first-letter
   - ::first-line
   - ::before
   - ::after
   - ::selection

1. **상속**

   하위 요소가 상위 요소의 값을 물려받는지 여부

   속성 별로 다르게 적용되는데 폰트 관련된 속성은 대부분 상속됨.

   어떤 속성이 상속되지 않는 경우 상속을 받고자 하면 inherit으로 값을 설정하면 된다.

1. **우선순위**

   하나의 요소에 동일한 속성의 값을 2개 이상 적용하는 경우 충돌이 발생

   - 마지막에 작성된 요소가 우선 순위를 갖음
   - 태그 안에 작성한 style이 우선 순위가 가장 높다
   - 외부 파일에 작성한 것과 style 태그 안에 작성한 것은 나중에 작성한 것이 우선선위가 높다.

동일한 방식으로 작성된 경우는 특정도를 가지고 우선 순위를 적용한다.

- inline - 1000
- id 선택자 - 100
- class 선택자 - 10
- 가상 클래스 - 10
- 가상 요소 - 1
- 태그 선택자 - 1

1. **단위**

   요즘은 디바이스 크기가 다양하기 때문에 화면 출력을 할 때는 상대 단위를 사용하는 것을 권장하고, 인쇄를 할 때는 절대 단위를 사용하는 것을 권장한다.

   - 절대 단위: 불변의 단위
     - cm, mm, in
     - px: 1/96 인치로 해상도 같은 것을 표현할 때 사용하는 단위, 화면의 확대 축소에 따라 변하기도 해서 상대 단위라고 하기도 함.
     - pt: 1/72 인치
     - pc: 12pt
   - 상대 단위: 화면 크기나 디바이스 크기에 따라 다르게 적용
     - px, %
     - em: font-size가 기준, font-size가 16px라면 1em은 16px
     - rem: 최상위 요소의 글꼴 크기
     - vw: 디바이스 화면의 가로 크기를 100으로 설정
     - vh: 디바이스 화면의 세로 크기를 100으로 설정
     - vmin: 디바이스 화면의 가로나 세로 중 작은 것을 100
     - vmax: 디바이스 화면의 가로나 세로 중 큰 것을 100
     - ex: 소문자 x의 높이로 em의 절반
     - ch: 숫자 0의 너비
     - deg, rad : 각도를 설정하는 두 가지 방법.

1. **Typography**

   문자나 기호에 적용

   1. font-family

   폰트 나열. 폰트가 없을 때 다른 폰트를 적용하기 위해서 나열한다.

   ```css
   /* 돋움이 없으면 sans-serif를 적용 */
   font-family: 돋움, sans-serif;
   ```

1. font-face

폰트가 없을 때 다운로드 받을 수 있도록 해주는 속성

```css
@font-face{
	font-family: 글꼴 이름
	src:url(글꼴 파일의 경로) format(파일 유형)
}
```

1. font-size

   글꼴 크기

   키워드(xx-small, x-small, small, medium, large, x-large, xx-large, smaller, lager..)로 설정할 수 있고 직접 단위 설정 가능

1. font-weight

   글자 두께

   100부터 900까지 100 단위로 설정 가능하고 normal, bold, bolder, lighter와 같은 키워드 설정 가능

1. font-style

   italic을 설정하면 기울임

1. font-variant

   small-caps를 설정하면 소문자를 작은 대문자로 변형

1. font

   앞의 6가지를 한꺼번에 적용하기 위해서 사용

   font: weight style variant szie line-height font-family (font-family는 맨 뒤에 놓아야 함)

   다른 모든 속성은 생략이 가능하지만 font-size와 font-family는 생략 불가

1. color

   요소의 전경 색상 - 대부분 글자에만 적용됨.

   키워드로 설정할 수 있고 #16진수 6자리, rgb(0-255까지의 숫자 3개 나열), 3개 숫자 대신에 백분율로 설정할 수 있고 rgba를 사용하면 투명도 설정도 가능하며 hsl도 있음.

1. text-decortaion

   밑줄이나 취소선 등의 효과를 설정

   - none, underline, overline, line-through, blick 등으로 설정
   - a 태그를 이용해서 버튼 효과를 나타낼 때 none 으로 설정하는 경우가 있음.

요즘은 a 태그에 밑줄을 긋는 것 보다는 색이나 두께를 변경해서 알려주는 것을 권장.

일반 텍스트에는 underline을 하지 않고 강조를 하고자 하면 italic으로 기울임을 설정하는 것을 권장.

1. text-transform

   영문의 대소문자 변환을 설정

   - none, uppercase, lowercase, capitalize 등을 이용

1. white-space

   공백 문자 설정

   - normal: 여러 개의 공백을 하나로 처리
   - nowrap: 여러 개의 공백을 하나로 처리하고 영역 너비를 넘어가면 줄 바꿈하지 않고 한 줄로 표시
   - pre: 여러 개의 공백을 그대로 처리하고 영역 너비를 넘어가면 줄 바꿈하지 않고 한 줄로 표시
   - pre-wrap: 여러 개의 공백을 그대로 처리하고 영역 너비를 넘어가면 줄 바꿈해서 표시
   - pre-line: 여러 개의 공백을 그대로 처리하고 영역 너비를 넘어가면 줄 바꿈해서 표시

1. paragraph

   문단 관련 속성

   - text-align: 문단의 가로 정렬, 셀이나 인라인 요소에 적용할 때는 내용보다 너비가 더 커야 설정된다.
   - 셀이나 인라인 요소에 적용할 때는 내용보다 너비가 더 커야 설정된다.
   - start, end, left, center, right, justify (문단의 시작을 왼쪽에 끝을 오른쪽에 맞추고 여백을 조정)
   - text-justify: text-align에 justify를 적용했을 때 공백 조절
     - auto: 웹 브라우저가 조절
     - none: 정렬하지 않음
     - inter-word: 단어 사이의 공백을 조절
     - distribute: 글자 사이의 공백을 조절
   - text-indent: 첫 줄 들여쓰기, 양수를 설정하면 들여쓰기고 음수를 설정하면 내어쓰기
   - letter-spacing: 문자 사이의 간격
   - line-height: 문단의 행 사이의 간격
   - word-break: 줄 바꿈 옵션, keep-all을 설정하면 단어 단위 줄바꿈을 적용
   - direction: rtl을 설정하면 오른쪽에서 왼쪽으로 출력
   - vertial-align: 인라인 요소끼리의 세로 위치를 설정
     - sub, super, top, text-top, middle, bottom, text-bottom
     - 이미지 주위에 텍스트를 배치할 때 많이 이용

1. text-shadow

   글자에 그림자 효과

   css3에서 추가된 속성이라서 구형 브라우저에서는 적용이 안됨.

   수평 오프셋, 수직 오프셋, 흐릿해지는 반경, 색상 순으로 설정

   수평 오프셋과 수직 오프셋이 일치하면 하나만 설정

   속성을 나열한 후 ,를 하고 다시 설정하면 여러 개 적용이 가능.

1. list

- list-style-type: 목록의 마커(머리 부분) 설정
- none, disc, circle, square, decimal, decimal-leading-zero, upper-alpha, lower-alpha, upper-roman, lower-roman, upper-latin, lower-latin, lower-greek, armenian 등
- list-style-image: 이미지 파일을 마커로 사용
  - url(이미지 파일의 경로)
- list-style-position: 마커의 위치
  - inside와 outside를 설정할 수 있음

list-style에 3가지를 동시에 설정 가능한데 이 경우는 type, position, image 순으로 작성

1. background

- background-color
- background-image: 배경 이미지를 설정하는 것으로 url(경로)
- ,를 이용해서 여러 개 적용이 가능한데 순서대로 적용이 된다.
- background-repeat: 이미지의 반복을 설정, 이미지가 배경보다 작을 때 적용
  - repeat, repeat-x, repeat-y, no-repeat 설정 가능
- background-position
  - reft, right, center, top, bottom, 직접 숫자 입력 가능
- background-attachment
  - 스크롤할 때 이미지의 이동 여부
  - fixed를 설정하면 이미지 고정이 되고 scroll을 설정하면 배경 이미지도 스크롤
- background-size: 배경 이미지 크기
  - auto: 원본 이미지 크기 그대로 출력
    숫자 2개: 너비와 높이
    숫자 1개: 너비에 설정되고 높이는 auto가 된다.
  - cover: 너비와 높이 비율을 맞추어서 확대하거나 축소하는데 큰 값을 적용
  - contain: 너비와 높이 비율을 맞추어서 확대하거나 축소하는데 작은 값을 적용
- background-clip: 적용 범위

  - border-box: 테두리까지 적용
  - padding-box: 테두리 제외
  - content-box: content에만 적용

- background
  - color, image, 반복여부, position, attachment를 한꺼번에 적용하기 위한 속성

1. gradation

   css3에서 지원하는 것으로 여러 색상을 혼합해서 사용하는 속성

   그라데이션을 직접 생성하는 것은 쉽지 않아서 [http://www.colorzilla.com/gradient-editor](http://www.colorzilla.com/gradient-editor) 에서 모양을 만든 후 코드를 복사해서 사용하는 것이 편리

   - vendor-prefix

   css3의 기능 중에는 표준으로 채택되지 않아서 브라우저별로 별도 기능을 제공할 때 사용하는 기호

   - -webkit- : 사파리나 크롬
   - -moz-: MS의 브라우저
   - -o- : 오페라 브라우저

   css3 는 현재도 표준을 계속 추가하고 있기 때문에 vendor prefix를 이용하는 기능 중에는 최신 브라우저를 사용하면 vendor-prefix를 생략해도 되는 경우가 있음

- linear-gradtion

  선형 그라데이션

  - linear-gradient(각도, 색상값 나열)
  - vendor prefix 이용

- radial-gradation
  원형 그라데이션
  - radial-gradient(시작점의 위치, circle의 모양, 색상 나열)

1. **Box Model**

   **영역에 대한 설정**

   - width, height, border, padding, magin

   IE의 호환 모드(IE의 하위 버전들은 pading을 width와 height에 포함시켜서 계산

   ```css
   /* 일반 브라우저들은 160px을 확보해서 표시하지만 IE 하위 버전은 100px 만을 확보해서 표시 */
   {width:100px height: 100px, padding: 30px;}
   ```

**margin collapsing(마진 겹침)**

연속 배치된 요소들에 마진을 전부 설정하면 마진은 중복되지 않고 큰 값 1개만 적용

**box-size**

box의 크기를 설정할 때 기분을 정하는 것

content-box를 설정하면 내용을 기준으로 box의 크기를 설정하고 border-box를 설정하면 경계선을 기준으로 함.

**크기 설정**

width와 height을 가지고 설정

max-height, min-height, max-width, min-width를 이용해서 최대 및 최소 크기를 설정하는 것도 가능

**여백 설정**

padding과 margin으로 설정

**보기 설정**

요소의 내용을 숨기거나 표시하고자할 때 사용하는 속성

visible, hidden, collpase로 설정

- overflow
  내용이영억보다 큰 경우의 옵션 설정으로 visibel, hidden, scroll, auto가 있음
- text-overflow
  - 텍스트가 영역보다 긴 영우의 옵션 설정으로 clip(내용을 잘라서 보이지 않음)과 ellipse(l…을 표시)를 설정할 수 있음.

최근에는 텍스트가 영역보다 큰 경우 더보기 버튼 같은 것들을 만들어서 출력하는 경우가 많다.페이스북 앱은 내용이 많은 경우 …과 더보기 기능을 제공하여 더보기를 누르면 아래로 펼쳐지는 형태로 디자인을 하는데 이러한 다지안을 카드형 레이아웃이라고 부름

- floating
  블록 요소 주위에는 다른 요소가 배치될 수 없다.
  블록 요소 주위에 다른 요소를 배치하고자 할 때는 블록 요소를 inline 요소로 변경하거나 float 속성을 이용해서 영역은 차지하지만 공중에 떠 있는 형태로 만들어서 할 수도 있다.
  - left, right, none
  인라인 요소에도 float 속성을 설정할 수 있는데 인라인 요소에 float을 적용할 때는 width와 height를 설정하는 것이 좋다. 그렇지 않으면 콘텐츠를 표시하는 영역이 최대한 확장이 되버림.
  → float 속성이 적용된 상태에서 해제
  - clear 속성에 none, left, right, both를 설정해서 해제 - 블록 요소에서만 가능
  - overflow 속성에 auto나 hidden을 설정해서 해제 - 부모 요소에 설정
  - float 속성을 부모 요소에 설정해서 해제

**크기 조절**

resize 속성에 horizontal이나 vertical, both를 설정해서 크기 조절이 가능하도록 할 수 있음.

**그림자 설정**

box-shadow: 수평오프셋, 수직오프셋, 흐릿함, 확산정도, 색상

수평오프셋 앞에 inset을 설정하면 그림자가 내용 안으로 출력된다.

**경계선**

- border-style

  경계선의 모양으로 none, dotted, dashed, solid, double, groove, ridge, inset, outset을 설정. 그냥 사용하면 상화자우 경계선이 모두 동일한 모양이 되고 방향 별로 다르게 설정하고자 하면 border-left-style, border-right-style 등.

- border-width

  경계선의 두께로 thin, medium, thick으로 설정할 수 있고 값과 단위를 직접 지정할 수 있습니다. 방향 별로 설정할 수 있음.

- border-color

  경계선의 색상

- border: 두께 종류 색상을 한꺼번에 설정

- border-radius: 경계선에 라운드 효과를 설정하는 속성, css3에서 추가된 속성. 값을 하나만 설정해서 상하좌우 모두 동일한 값으로 적용할 수 있고 숫자 4개를 설정해서 상하좌우를 다르게 설정하는 것도 가능하고 ,로 구분하고 다시 설정해서 여러 개의 값을 적용하는 것도 가능.

- border-image-source: url(이미지 파일 경로)
  보더에 이미지 설정 가능.
- border-image-slice: 값 4개로 잘라내기 설정
- border-image-repeat: stretch, repeat, round, space 중 하나의 값을 설정해서 반복 여부를 설정
- border-image-width: 숫자로 두께 설정
- border-image-outset: 숫자로 경계선과의 거리를 조정

**display**

박스의 보기 모드를 변경

- block: 블록 요소로 만들어 짐
- inline: 인라인 요소로 만들어지는데 height가 무시됨.
- inline-block: 인라인 요소로 만드는데 주위에 콘텐츠가 배치되지 않고 height가 적용됨.

**opacity**

불투명도 설정으로 0과 1 사이의 소수로 설정하는데 1이면 100% 불투명이 되고 0이면 투명

**position**

요소의 배치 방법을 설정하는 것으로 기본값은 static

- static: 위치 기준이 없는 것으로 순서대로 배치하는 것인데 left나 top을 사용할 수 없음.
- relative: 이전에 출력된 내용과의 관계를 이용해서 출력되는데 이 경우에는 left나 top을 이용하여 이전 내용과의 거리를 설정
- absolute: 부모의 왼쪽 위를 기준으로 해서 배치되는 것으로 left나 top을 이용해서 부모에서의 위치를 설정
- fixed: 웹 브라우저의 스크린 기준(뷰포트 기준)으로 배치되는 방식
- sticky: 스크롤 영역을 기준으로 배치

위치 설정

`abolute나 fixed로 설정하면 그 요소의 display는 block으로 자동 변경`

left, right, top, bottom을 이용해서 위치를 설정

겹쳐서 출력할 때 순서 설정

z-index 속성을 이용하는데 숫자가 클수록 위에 배치가 된다.

1. **display**

   하나의 컨테이너를 생성해서 요소들을 가로나 세로 방향으로 배치하는 레이아웃.

   모바일 웹에서 가로 방향으로 요소들을 배치해서 가득 채우고자 할 때 사용.

   정렬 방법이나 크기 방법 등을 설정해서 사용

1. **다단**

   가로 화면을 여러 개로 분할해서 콘텐츠를 배치하는 것으로 모바일에서는 가독성 때문에 잘 사용하지 않는다. 모바일은 가로는 가득 채우고 세로로 스크롤 할 수 있도록 만들기 때문에 내용이 많으면 세로 방향으로 스크롤 하도록 해서 출력한다.

1. **Table**

   1. Table 관련 display 속성

   - table, inline-table, table-row, table-row-group, table-header-group, table-footer-group, table-column, table-column-group, table-cell, table-caption, list-item 등이 있다.
   - 예전에 서버가 데이터를 xml로 주는 경우가 많았는데 xml로 전송된 데이터를 표 형태로 출력을 할려고 하면 데이터를 파싱해서 객체로 변환한 후 table 태그를 이용해서 출력을 했는데 이를 편리하게 하고자 할 때 display 속성을 이용하였다.

   2. table-layout

   fixed를 설정하면 셀의 너비가 고정되고 auto를 설정하면 셀의 너비가 셀의 내용에 따라 변경됨.

   3. caption-size

   캡션의 위치를 설정하는 것으로 top과 bottom을 설정할 수 있다.

   4. border-collapse

   테두리 관련 속성으로 기본값은 separate 인데 이 값은 표와 셀의 테두리가 구분이 되는 것이다. collapse로 설정하면 표의 테두리와 셀의 테두리가 한 묶음으로 만들어짐.

   5. border-spacing

   테두리의 간격으로 border-collapse가 seperate일 때 적용

   6. 셀 안에서의 정렬

   상하 정렬은 vertical-align 속성에 baseline, top, middle, bottom 으로 설정

   좌우 정렬은 text-align 속성에 left, right, center, justify, 로 설정

   7. empty-cells

   빈 셀의 표시 여부로 show 로 설정하면 빈 셀이 보이고 hide로 설정하면 빈 셀이 보이지 않음

1. **cursor**

   커서 모양을 변경하는 것으로 url(이미지 파일 경로)로 설정할 수 있고 keyword를 이용해서 설정하는 것도 가능

1. **outline**

   외곽선 속성으로 border와 동일한 방식으로 설정 가능한데 color에 invert를 이용해서 반전 효과를 만들 수 있음.

1. **변환(transform - 행렬을 이용)과 애니메이션**

   1. 2D 변환(이동은 덧셈, 크기 변환은 곱셈)

   - 이동: translate(x축 이동값, y축 이동값), translatex(x축 이동값), translatey(y축 이동값)
   - 크기 변환: scal(x축 이동값, y축 이동값), scalex(가로 비율), scaley(세로 비율)
   - 회전: rotate(각도 deg)
   - 비틀기: skew(x축, y축), skewx(x축), skewy(y축)
   - 한꺼번에 적용: matrix(scaleX, scaleY, skewX, skewY, scaleY, translateX, translateY)
   - 변환 기준점 설정: transform-origin

1. **반응형 웹**

   화면의 크기에 따라 콘텐츠의 배치를 다르게 해서 동일한 컨텐츠를 사용할 수 있도록 디자인 하는 것

   css의 media query와 met 태그의 viewport 속성을 직접 설정해서 구현하거나 bootstrap 같은 외부 라이브러리를 이용해서 구현
