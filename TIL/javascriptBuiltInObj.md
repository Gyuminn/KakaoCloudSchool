## Javascript Built-In(제공하는) Object

1. **종류**

   1. 일반 객체

      javascript에서 제공하는 일반적인 객체 - 브라우저와 상관없이 동작.

   2. BOM(Browser Object Model)

      브라우저 차원에서 제공하는 객체

   3. DOM(Document Object Model)

      HTML의 body 부분에 만드는 태그들을 사용하기 위한 객체

2. **Document**

   https://www.w3schools.com/jsref/default.asp

   https://devdocs.io/javascript

3. **일반 내장 객체**

   1. Object

      - Javascript의 최상위 객체
      - Javascript에는 모든 객체가 이 객체를 상속받음

      ```jsx
      // 객체 생성 방법
      {
      }
      new Object();
      ```

      - 주요 속성이나 메서드
        - prototype 속성: 이 속성에 데이터를 추가하면 모든 객체가 데이터를 사용할 수 있다.
        - toString 메서드: 객체를 문자열로 변환하는 메서드로 출력하는 메서드에 객체 이름을 대입하면 자동으로 호출되는 메서드.

   2. Number 객체
      - 숫자와 관련된 객체
      - 객체를 생성할 때는 숫자를 직접 대입해도 되고 new Number(숫자로 구성된 문자열)
   3. Math 객체
      - Java의 Math 클래스를 그대로 가져옴.
      - 인스턴스 생성을 하지 않음(new 를 하지 않음). - `모든 멤버가 static이기 때문이다.`
   4. String
      - 문자열 클래스
      - 생성은 큰 따옴표나 작은 따옴표 안에 문자열 리터럴을 대입해서 생성할 수 있고 ‘new String(문자열)’을 이용해서 생성하는 것이 가능.
      - length 속성: 문자열의 길이
      - charAt(인덱스): 인덱스 번째 문자를 리턴
      - 문자열 관련 작업 중 중요한 것은 `좌우 공백 제거`, `영문 대소문자`, `특정 패턴의 문자나 문자열의 존재 여부`, `불용어 사용`, `한글을 사용할 때는 인코딩 문제`,
