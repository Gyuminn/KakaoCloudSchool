# [Database] Sub Query & Join

1. **SET OPERATOR**

   동일한 테이블 구조를 가진 2개의 테이블을 가지고 수행하는 연산

   컬럼의 개수가 같아야 하고 컬럼의 자료형이 일치해야 한다.

   컬럼의 이름이나 테이블의 이름은 아무 상관이 없음.

   종류로는 UNION, UNION ALL, INTERSECT, EXCEPT(MINUS인 데이터베이스도 있음.)

   컬럼의 이름은 첫 번째 테이블의 컬럼 이름을 사용

   ORDER BY는 마지막에 한 번 작성

   데이터의 자료형이 BLOB, CLOB, BFILE, LONG(데이터의 사이즈가 너무 커서 일치 여부를 판단하는데 시간이 많이 걸리기 때문 - 이 자료형들은 INDEX를 생성하지 않음)은 안됨.

   데이터가 분산되어 있는 경우 연산의 결과를 합칠 때 사용

   <aside>
   ▪️ 데이터가 분산되어 있을 때 처리 방식
   - 데이터를 모은 후 처리
   - 데이터를  처리한 후 결과를 모으는 것: 속도가 빠른 경우가 많음, Map Reduce

   </aside>

   1. 형식

      ```sql
      SELECT
      FROM
      ...
      SET 연산자
      SELECT
      FROM
      ...
      ```

   2. 실습

      ```sql
      # DEPT 테이블의 DEPTNO와 EMP 테이블의 DEPTNO의 합집합
      SELECT DEPTNO FROM DEPT UNION SELECT DEPTNO FROM EMP;
      SELECT DEPTNO FROM DEPT UNION ALL SELECT DEPTNO FROM EMP;

      # DEPT 테이블의 DEPTNO와 EMP 테이블의 DEPTNO의 교집합
      SELECT DEPTNO FROM DEPT INTERSECT SELECT DEPTNO FROM EMP;

      # DEPT 테이블의 DEPTNO와 EMP 테이블의 DEPTNO의 차집합
      SELECT DEPTNO FROM DEPT EXCEPT SELECT DEPTNO FROM EMP;
      ```

2. **SubQuery**

   1. 개요

      다른 SQL 구문 안에 포함된 쿼리

      Sub Query는 SELECT 구문

      Sub Query는 반드시 괄호 안에 작성

      Sub Query는 포함하는 Query가 실행되기 전에 한 번만 실행됨.

   2. 분류
      - 위치에 따른 분류
        - FROM 절이 아닌 경우: Sub Query
        - FROM 절에 사용된 경우: Inline View
      - 리턴되는 데이터에 따른 분류
        - 단일 행 Sub Query: 리턴되는 결과가 하나의 행
        - 다중 행 Suq Query: 2개 이상의 행이 리턴되는 경우
   3. 사용하는 경우

      ```sql
      # EMP 테이블에서 ENAME이 MILLER인 사원과 동일한 DENPTNO를 가진 사원의
      # ENAME을 조회하고자 하는 경우
      SELECT
      	ENAME
      FROM
      	EMP
      WHERE
      	DEPTNO = (
      	SELECT
      		DEPTNO
      	FROM
      		EMP
      	WHERE
      		ENAME = 'MILLER');
      ```

   4. 실습

      ```sql
      # tCity 테이블에서 popu가 최대인 도시의 name을 조회

      # 아래와 같은 경우 popu가 최대인 데이터가 2개 이상 존재하는 경우 1개만 조회됨.
      SELECT name, MAX(popu) FROM tCity;

      # Sub Query 이용
      SELECT
      	name
      FROM
      	tCity
      WHERE
      	popu = (
      	SELECT
      		MAX(popu)
      	FROM
      		tCity);
      ```

      ```sql
      # EMP 테이블에서 평균 급여(SAL) 보다 많은 급여를 받는 사원의 이름(ENAME)과 급여(SAL)을 조회
      SELECT
      	ENAME,
      	SAL
      FROM
      	EMP
      WHERE
      	SAL > (
      	SELECT
      		AVG(SAL)
      	FROM
      		EMP);
      ```

   5. 다중 열 Sub Query

      Sub Query의 결과가 1개의 컬럼이 아니고 여러 개의 컬럼인 경우

      ```sql
      # tStaff 테이블에서 name이 안중근인 데이터와 depart와 gender가 일치하는 데이터를 조회
      SELECT
      	*
      FROM
      	tStaff
      WHERE
      	(depart,
      	gender) = (
      	SELECT
      		depart,
      		gender
      	FROM
      		tStaff
      	WHERE
      		name = "안중근");
      ```

   6. 다중 행 Sub Query

      Sub Query에서 리턴되는 결과가 2개 이상의 행인 경우

      이 경우는 =, <>는 사용이 안되고 >, >=, <, <=도 단독으로 사용안됨.

      =, <>는 하나의 데이터와 비교 가능한 단일행 연산자이기 때문이다.

      이런 경우에는 IN이나 NOT IN, 그리고 ANY와 ALL 같은 다중 행 연산자를 사용해야 한다.

      ANY와 ALL은 MAX와 MIN 함수로 대체가 가능하다.

      ```sql
      # EMP 테이블에서 DEPTNO 별로 그룹화해서
      # 각 그룹의 SAL이 최대인 데이터와 일치하는 SAL을 가진
      # 데이터의 ENAME과 SAL을 조회

      # 에러 - 서브 쿼리의 결과는 3개인데 = 로 비교해서 에러
      SELECT ENAME, SAL
      FROM EMP
      WHERE SAL = ( SELECT MAX(SAL) FROM EMP GROUP BY DEPTNO);

      # 서브 쿼리가 2개 이상 리턴하므로 IN 이용
      SELECT
      	ENAME,
      	SAL
      FROM
      	EMP
      WHERE
      	SAL IN (
      	SELECT
      		MAX(SAL)
      	FROM
      		EMP
      	GROUP BY
      		DEPTNO);
      ```

      ```sql
      # EMP 테이블에서 DEPTNO가 30인 데이터의 모든 SAL보다 많은 SAL을 받는
      # 직원의 ENAME과 SAL을 조회
      SELECT
      	ENAME,
      	SAL
      FROM
      	EMP
      WHERE
      	SAL > ALL(
      	SELECT
      		SAL
      	FROM
      		EMP
      	WHERE
      		DEPTNO = 30);

      # MAX를 이용해도 된다.
      SELECT
      	ENAME,
      	SAL
      FROM
      	EMP
      WHERE
      	SAL > (
      	SELECT
      		MAX(SAL)
      	FROM
      		EMP
      	WHERE
      		DEPTNO = 30);
      ```

3. **JOIN**

   1. 개요

      2개 이상의 테이블을 합쳐서 하나의 테이블을 만드는 것

      2개의 테이블이 동일한 테이블일 수도 있음.

      조회하고자 하는 데이터가 2개 이상의 테이블에 나누어져 있거나 하나의 테이블에서 2번 이상 찾아야 하는 경우 사용.

   2. 종류
      - Cartesian Product
        Cross Join 이라고도 하는데 단순하게 2개 테이블의 모든 조합을 만들어내는 것.
      - Equi Join
        양쪽 테이블에 동일한 의미를 갖는 컬럼이 존재할 때 2개의 컬럼의 값이 일치하는 경우에만 결합하는 것으로 Inner Join이라고도 함.
      - Non Equi Join
        Equi Join과 하는 방법은 유사하지만 2개의 컬럼의 값이 일치하지 않는 조건으로 결합하는 것.
      - Outer Join
        Equi Join과 유사하지만 한 쪽 테이블에만 존재하는 데이터도 Join에 참여하는 것.
      - Self Join
        동일한 테이블끼리 Join하는 것으로 하나의 테이블에 동일한 의미를 갖는 컬럼이 2개 이상 존재할 때 사용
      - Semi Joi
        Sub Query를 이용해서 Join
   3. Cross Join - Cartesian Product

      양쪽 테이블의 모든 데이터 조합을 만들어내는 것

      컬럼의 수는 양쪽 테이블의 컬럼의 수의 합이 되고 행의 수는 양쪽 행의 수의 곱

      ```sql
      # EMP 테이블은 14개의 행과 8개의 컬럼
      # DEPT 테이블은 4개의 행과 3개의 컬럼으로 만들어져 있음.

      # Cross Join의 결과는 11개의 컬럼과 56개의 행으로 만들어진다.
      SELECT * FROM EMP, DEPT;
      ```

   4. Equi Join

      가장 많이 사용함. 양쪽 테이블의 동일한 의미를 갖는 컬럼의 값이 일치하는 경우에만 Join을 수행

      동일한 의미를 갖는 컬럼이 `Foreign Key` 이면 Join의 성능은 좋아짐.

      WHERE 절에 Join 조건을 명시하면 된다.

      양쪽 테이블에 동일한 이름의 컬럼이 존재하는 경우는 컬럼이 중복되기 때문에 앞에 `테이블이름.컬럼이름` 의 형태로 사용해야 한다.

      ```sql
      # EMP 테이블의 DEPTNO는 부서 번호이고 DEPT 테이블의 DEPTNO도 부서번호
      SELECT * FROM EMP, DEPT WHERE EMP.DEPTNO = DEPT.DEPTNO;
      ```

      JOIN을 한 후 조건을 가지고 데이터를 조회하는 경우 JOIN을 먼저 기재

      ```sql
      # ENAME이 MILLER인 사원의 DNAME과 ENAME을 조회
      # 이 경우 DNAME은 DMPT 테이블에 존재하고, ENAME은 EMP 테이블에 존재한다.
      # 이 경우는 JOIN으로 해결해야 한다.
      # 조건이 복잡하지만 조회해야하는 컬럼이 하나의 테이블에 존재한다면 Sub Query로 해결 가능
      # 동일한 문제를 Sub Query로 해결할 수 있다면 Sub Query를 사용해야 한다.
      SELECT DNAME, ENAME
      FROM DEPT, EMP
      WHERE DEPT.DEPTNO = EMP.DEPTNO AND ENAME='MILLER';
      ```

      JOIN에서 테이블의 순서는 선행 테이블에 조건을 설정해서 데이터를 추출한 후 후행 테이블의 데이터를 결합하는 방식이다.

      따라서 조건을 확인해서 데이터의 추출 개수가 적은 테이블을 선행 테이블로 사용해야 한다.

      `한 쪽 테이블에만 적용되는 조건이라면 그 조건이 적용되어야하는 테이블을 먼저 기재한다.`

      굉장히 중요하다!!

      ```sql
      # 앞의 문제에서 조건 중에 ENAME을 가지고 조회하는 조건이 있으므로
      # JOIN을 할 때 ENAME을 포함하고 있는 EMP 테이블을 선행 테이블로 사용하는 것이 효율적.
      SELECT DNAME, ENAME
      FROM EMP, DEPT
      WHERE DEPT.DEPTNO = EMP.DEPTNO AND ENAME='MILLER';
      ```

   5. NON EQUI JOIN

      JOIN 조건이 = 가 아닌 경우

      ```sql
      # EMP 테이블의 SAL은 급여이고 SALGRADE 테이블의 GRADE는 급여 등급이고
      # LOSAL은 등급의 죄저 급여이고 HISAL은 동급의 최고 급여이다.

      # SAL은 LOSAL이나 HISAL과 일치하지 않을 수 있음
      # SAL이 LOSAL과 HISAL 사이의 값일 경우 GRADE를 조회
      # 다른 테이블의 데이터와 비교할 때 - 연산자가 아닌 것으로 비교하면
      # NON EQUI JOIN이라고 한다.
      ```

      ```sql
      # 각 사원의 이름(ENAME - EMP)과 GRADE(SALGRADE)를 조회
      SELECT ENAME, GRADE
      FROM EMP, SALGRADE
      WHERE SAL BETWEEN LOSAL AND HISAL;
      ```

   6. SELF JOIN

      동일한 테이블을 가지고 JOIN

      하나의 테이블에 동일한 의미를 갖는 칼럼이 2개 이상 존재할 때 사용한다.

      SNS나 인력 배치와 같은 인사 관리 시스템에서 많이 이용

      ```sql
      # EMP 테이블은 사원 번호에 해당하는 EMPNO가 있고 관리자 사원 번호에 해당하는 MGR이 있다.
      # 이 경우 SELF JOIN이 가능하다.
      ```

      ```sql
      # 사원 이름과 관리자의 조원 이름을 조회
      # 사원 번호를 가지고 관리자의 사원번호를 찾고 그 찾은 관리자의 사원번호를 가지고
      # 관리자의 이름을 찾아야 한다.
      # 이런 경우 SELF JOIN이 필요
      # 이 경우 JOIN을 할 때 2개의 동일한 이름의 테이블을 사용해야하기 때문에
      # 반드시 테이블 이름을 변경해서 사용해야 한다.
      SELECT
      	e1.ENAME '사원 이름',
      	e2.ENAME '관리자 이름'
      FROM
      	EMP e1,
      	EMP e2
      WHERE
      	e1.MGR = e2.EMPNO;
      ```

      Friend 테이블이 있고 이 테이블에는 ID와 FRIENDID(친구 아이디)로 구성된 경우 친구의 친구 찾기

      ```sql
      SELECT
      	f2.FRIENDID
      FROM
      	FRIEND f1,
      	FRIEND f2
      WHERE
      	f1.FRIENDID = f2.ID
      	AND f1.ID != f2.FRIENDID
      	AND NOT IN(
      	SELECT
      		FRIENDID
      	FROM
      		FRIEND
      	WHERE
      		F1.ID);
      ```

   7. ANSI(미국 표준 협회) JOIN

      - CROSS JOIN
        FROM 절에 CROSS JOIN을 기재한다.
        ```sql
        # SELECT * FROM EMP, DEPT;
        SELECT *
        FROM EMP
        CROSS JOIN DEPT;
        ```
      - INNER JOIN
        Equi Join 대신에 INNER JOIN이라는 표현을 사용하고 WHERE 절에 Join 조건을 적지 않고 WHERE 절에 on을 추가하고 on 절에 JOIN 조건을 기재
        ```sql
        # SELECT * FROM EMP, DEPT WHERE EMP.DEPTNO = DEPT.DEPTNO;
        SELECT *
        FROM EMP
        INNER JOIN DEPT
        ON EMP.DEPTNO = DEPT.DEPTNO;
        ```
        양쪽 테이블에 컬럼 이름이 동일한 경우에는 ON절 대신에 USING(컬럼이름)을 사용하는 것도 가능하다.
        ```sql
        SELECT *
        FROM EMP
        INNER JOIN DEPT
        USING(DEPTNO);
        ```
        양쪽 테이블에 컬럼 이름이 동일하다면 USING 절도 생략하고 INNER JOIN 대신에 NATURAL JOIN이라고 기재
        ```sql
        SELECT *
        FROM EMP
        NATURAL JOIN DEPT;
        ```
      - OUTER JOIN
        한쪽 테이블에만 존재하는 데이터도 JOIN에 참여하는 것
        Maria DB에서는 LEFT OUTER JOIN과 RIGHT OUTER JOIN만 지원
        FULL OUTER JOIN은 지원하지 않기 때문에 UNION 연산을 이용해서 수행
        다른 테이블에 존재하지 않는 데이터는 그 테이블의 모든 컬럼의 값을 NULL로 설정한다.

        ```sql
        # EMP 테이블에는 DEPTNO가 10, 20, 30만 존재하고
        # DEPT 테이블에는 DEPTNO가 10, 20, 30, 40이 존재

        # 다음을 비교
        SELECT * FROM EMP LEFT OUTER JOIN DEPT ON EMP.DEPTNO = DEPT.DEPTNO;
        SELECT * FROM EMP RIGHT OUTER JOIN DEPT ON EMP.DEPTNO = DEPT.DEPTNO;
        # RIGHT OUTER JOIN인 경우 DEPT 테이블에만 존재하는 40번도 JOIN에 참여하게 된다.
        ```

        ```sql
        # FULL OUTER JOIN을 하고 싶을 때는 LEFT OUTER JOIN과 OUTER RIGH JOIN의
        # UNION 연산으로 해결
        SELECT
        	*
        FROM
        	EMP
        LEFT OUTER JOIN DEPT ON
        	EMP.DEPTNO = DEPT.DEPTNO
        UNION
        SELECT
        	*
        FROM
        	EMP
        RIGHT OUTER JOIN DEPT ON
        	EMP.DEPTNO = DEPT.DEPTNO
        ```

   8. 다중 조인

      3개 이상의 테이블도 JOIN 가능

      2개 테이블을 JOIN을 하고 난 후 다른 테이블과 JOIN을 하는 형식

   9. JOIN 시 주의사항

      JOIN은 메모리를 많이 사용하기 때문에 최대한 자제해야 한다.

      관계형 데이터베이스의 최대 단점이 빈번한 JOIN의 문제

      JOIN 없이 해결할 수 있는 문제는 JOIN 없이 해결해야 한다.

      `반드시 JOIN이 필요한 경우는 SELECT 절에 기재하는 조회하고자 하는 컬럼이 2개 이상의 테이블에 존재하는 경우 뿐이다. 그 외의 경우는 대부분 Sub Query로 해결이 가능.`

      단, Sub Query를 사용하면 SQL이 길어질 가능성이 높아지므로 적절하게 사용.
