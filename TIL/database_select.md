# [Database] SELECT

1. **SQL 분류**

   1. DDL(구조에 관련된 명령어로 일반적으로 DBA의 명령어) - 취소 안됨.

      - CREATE: 구조 생성
      - ALTER: 구조 변경
      - DROP: 구조 삭제

      - TRUNCATE: 테이블 내의 데이터 삭제
      - RENAME: 구조 이름 변경

   2. DQL - 검색 관련 명령어
      - SELECT
   3. DML - 데이터 관련 명령어 - 취소 가능
      - INSERT
      - UPDATE
      - DELETE
   4. TCL - 트랜잭션 관련 명령어 - 취소 불가능
      - COMMIT: 현재 작업까지 작업 내용을 원본에 반영
      - ROLLBACK: 작업 내용을 취소
      - SAVEPOINT: 취소할 지점을 만드는 명령
   5. DCL - 제어 명령(취소 불가능하고 운영자의 언어)
      - GRANT: 권한 부여
      - REVOKE: 권한 회수
   6. 개발자에게 중요도

      DQL → DML → TCL → DDL → DCL

2. **SELECT**

   데이터 조회 명령어로 원본에 아무런 영향을 주지 않음.

   원본에서 데이터를 복제해서 리턴한다.

   1. 샘플 데이터 구조
      - EMP 테이블
        - EMPNO: 사원번호로 정수 4자리이고 기본 키
        - ENAME: 사원 이름으로 문자
        - JOB: 직무로 문자
        - MGR: 관리자의 사원번호
        - HIREDATE: 입사일로 날짜 형식
        - SAL: 급여로 실수 7자리 소수 2자리
        - COMM: 상여금으로 실수 7자리 소수 2자리
        - DEPT: 부서 번호로 정수 2자리이고 DEPT 테이블의 DEPTNO를 참조
      - DEPT 테이블
        - DEPTNO: 부서 번호로 정수 2자리이고 기본키
        - DNAME: 부서 이름으로 문자
        - LOC: 위치로 문자
      - SALGRADE 테이블
        - GRADE 테이블: 호봉으로 숫자이고 기본키
        - LOSAL: 호봉의 최저 급여로 숫자
        - HISAL: 호봉의 최고 급여로 숫자
      - TCITY 테이블
        - NAME: 도시 이름으로 문자열이고 기본키
        - AREA: 면적으로 정수
        - POPU: 인구수로 정수
        - METRO: 대도시 여부로 문자
        - REGION: 지역으로 문자
      - TSTAFF 테이블
        - NAME: 이름으로 문자열이고 기본키
        - DEPART: 부서 이름으로 문자열
        - GENDER: 성별로 문자열
        - JOINDATE: 입사일로 문자열
        - GRADE: 직무로 문자열
        - SALARY: 급여로 정수
        - SCORE: 고과 점수로 실수
   2. SELECT 용어
      - Selection: 테이블의 행을 선택할 때 사용하는 것.
      - Projection: 테이블의 열을 선택할 때 사용하는 것.
      - Join: 공유 테이블 양쪽의 열에 대해서 링크를 생성해서 다른 테이블의 데이터를 가져와서 합치는 것.
   3. MariaDB에서의 SELECT 구조
      - 5 - SELECT 데이터를 열 단위로 조회하기 위한 계산식을 나열
      - 1 - FROM 데이터를 조회할 테이블을 나열
        FROM을 수행하게 되면 원본 데이터베이스에서 테이블 단위로 복제를 해와서 작업을 수행
      - 2 - [WEHRE 데이터를 행 단위로 분할하기 위한 조건]
      - 3 - [GROUP BY 데이터를 그룹화시키기 위한 열 이름이나 계산식을 나열]
      - 4 - [HAVING 데이터를 행 단위로 분할하기 위한 조건]
      - 6 - [ORDER BY 데이터를 정렬하기 위한 열 이름이나 계산식 또는 SELECT 절의 번호와 정렬방법]
      - 7 - [LIMIT 데이터의 위치와 개수를 지정해서 가져오기 위한 절로 표준은 아님]
   4. SELECT 구문의 기본적인 구조

      - 테이블의 모든 데이터 조회
        컬럼의 순서는 테이블을 만들 때 작성한 순서대로 리턴
        직접 테이블을 생성한 경우가 아니라면 \*은 사용하지 않는 것이 좋다.
        ```sql
        // tCity 테이블의 모든 데이터를 조회
        SELECT * FROM tCity
        ```
      - 특정 컬럼만 추출
        SELECT 절에 필요한 컬럼만 나열

        ```sql
        SELECT 컬럼이름나열 FROM tCity;

        // 예시 - tCity 테이블에서 name과 popu 컬럼을 조회
        SELECT name, popu FROM tCity;

        // 예시 - tStaff 테이블에서 name, depart, grade 컬럼을 조회
        ```

   5. SELECT 절에서의 별명

      SELECT 절에서는 컬럼에 별명을 부여할 수 있다.

      하나의 공백을 두고 별명을 설정하면 되는데 이 때 공백 자리에 AS를 추가해도 된다.

      별명에 공백이나 특수문자 또는 대문자가 있으면 “ “로 묶어야 한다.

      SELECT 절의 별명은 ORDER BY에서 사용 가능하고 프로그래밍 언어에서도 별명을 가지고 데이터를 가져온다.

      계산식이나 그룹 함수의 결과를 조회하고자 할 때는 별명을 부여하는 것이 좋다.

   6. 계산식 출력

      FROM 절을 제외한 모든 곳에서 계산식 사용이 가능

      계산식은 가상의 컬럼이고 FROM은 실제 테이블을 가져오는 것이므로 FROM 절에는 계산식을 사용할 수 없음.

      ```sql
      SELECT name, popu * 10000 AS "인구(명)" FROM tCity;
      // tCity 테이블에서 name과 popu에 10000을 곱한 결과를 조회
      ```

      단순 계산식은 FROM을 생략해도 된다.

      ```sql
      SELECT 60 * 60* 24
      ```

   7. concat 함수

      2개 이상의 문자열을 합쳐주는 함수

      2개 이상의 칼럼이나 연산식을 하나로 합쳐서 출력하기 위해 사용

      MyBatis와 같은 SQL Mapper Framework에서 like를 사용하기 위해서는 알아두어야 한다.

      EMP 테이블에서 ENAME과 JOB을 합쳐서 조회

      ```sql
      SELECT concat(ENAME,' ',JOB);
      ```

   8. DISTINCT

      SELECT 절의 맨 앞에 한번만 기재해서 컬럼의 중복된 값을 제거하는 역할

      컬럼 이름이 하나이면 그 컬럼의 값이 중복된 것만 제거하고 컬럼이 2개 이상이면 모든 값이 일치하는 경우에 제외

      ```sql
      // tCity 테이블에서 region의 중복을 제외하고 조회
      SELECT DISTINCT region FROM tCity;

      // tCity 테이블에서 region과 name 모두가 중복된 경우만 제외
      SELECT DISTINCT region, name FROM tCity;
      ```

   9. ORDER BY

      조회된 데이터를 정렬하기 위한 절

      SELECT 구문의 결과가 2개 이상의 행이 될 것 같은 경우에는 ORDER BY를 이용해서 정렬을 해주는 것이 좋다.

      ```sql
      ORDER BY 컬럼이름 [ASC | DESC] 나열
      // ASC는 오름차순이고 DESC는 내림차순인데 기본이 오름차순
      ```

      오름차순의 기본 순서

      - 숫자는 작은 것에서 큰 것 순으로
      - 날짜는 빠른 것에서 늦은 것으로
      - 문자는 알파벳 순서대로 앞글자부터 비료
      - NULL이 가장 마지막

      컬럼이름 대신에 SELECT 절의 순서로 설정하는 것이 가능

      SELECT 절에서 만든 별명 사용 가능

      2개 이상의 필드 나열 가능한데 첫 번째 필드로 정렬하고 동일한 값이 있는 경우 두 번째 필드의 정렬 조건을 확인

      계산 식을 이용한 정렬 가능

      권장하지는 않지만 정렬 기준 필드를 출력하지 않아도 됨.

      ```sql
      // tCity 테이블의 데이터를
      // region 별로 정렬하고 동일한 값이 있으면 name의 내림차순으로 정렬을 하고
      // region, name, area, popu 컬럼을 조회
      SELECT region, name, area, popu FROM tCity ORDER BY region, name DESC;
      ```

   10. WHERE

       테이블의 데이터를 행 단위로 분할하기 위한 조건을 설정하는 절

       SELECT, UPDATE, DELETE 구문과 함께 사용

       - 비교 연산자

         - =
         - >
         - <
         - > =, NOT 컬럼이름 <
         - <=, NOT 컬럼이름 >
         - <>, !=, ^=, NOT 컬럼이름 =

           ```sql
           // tCity 테이블에서 name이 서울인 데이터의 모든 컬럼을 조회
           SELECT * FROM tCity WHERE name = "서울";

           // tCity 테이블에서 metro가 y인 데이터의 모든 컬럼을 조회
           SELECT * FROM tCity WHERE metro = "y";
           ```

       - Maria DB의 경우 대소문자를 구별하지 않는 경우가 있을 수 있기 때문에 유의해야 한다.
         ```sql
         // 대소문자 구별하기
         // 조회를 할 때 컬럼 이름을 BINARY로 묶어주거나
         // 컬럼을 만들 때 자료형 뒤에 BINARY를 추가해주어야 한다.
         SELECT * FROM tCity WHERE BINARY(metro) = "Y";
         ```

       대소 비교 조건이 있는 경우 테스트를 할 때 경게값과 경계값 양쪽의 데이터를 반드시 테스트해야 한다 - `Boundary Value Analysis(경계값 분석 기법)`

       - NULL(아직 알려지지 않은 값으로 표현) 비교
         - NULL은 일반 연산자로 비교 안됨
         - IS NULL과 IS NOT NULL로 비교
         - 데이터베이스에서 NULL을 저장하는 방법은 공간에 NULL을 대입하는 개념이 아니고 NULL을 저장할 수 있는 컬럼에는 데이터를 저장할 수 있는 공간에 하나의 공간을 추가해서 그 공간에 NULL 여부를 표시하기 때문이다.
           ```sql
           // tStaff 테이블에서 score가 NULL인 데이터 조회
           SELECT * FROM tStaff WHERE score IS NULL;
           ```
       - 논리 연산자 AND와 OR을 제공
         - AND는 두 개의 조건이 모두 일치하는 경우만 조회하는데 앞의 조건이 일치하지 않으면 뒤의 조건은 확인하지 않음. OR는 두 개의 조건 중 하나의 조건만 일치해도 조회되는데 앞의 조건만 일치하면 뒤의 조건은 확인하지 않음.
         - AND가 OR보다 우선 순위가 높다.
           ```sql
           // tCity 테이블에서 popu가 100만 이상이고 area가 700 이상인 데이터의 모든 컬럼을 조회
           SELECT * FROM tCity WHERE popu >= 100 AND area >= 700;
           ```
       - NOT
       - LIKE
         부분 일치하는 데이터를 조회
       - 와일드 카드 문자

         - \_: 하나의 문자와 매칭
         - %: 글자 수 상관없음
         - [ ]: 문자를 나열하면 문자 중 하나와 일치
         - [^]: 문자를 나열하면 문자에 포함되지 않는
           와일드 카드 문자를 검색하고자 하는 경우는 ESCAPE 이용

         ```sql
         // tCity 테이블에서 name에 천이 포함된 데이터를 조회
         SELECT * FROM tCity WHERE name LIKE '%천%';

         // tCity 테이블에서 name에 천이 포함되지 않은 데이터를 조회
         SELECT * FROM tCity WHERE name NOT LIKE '%천%';

         // tCity 테이블에서 name에 천으로 끝나는 데이터를 조회
         SELECT * FROM tCity WHERE name LIKE '%천';

         // tCity 테이블에서 name에 천으로 시작되는 데이터를 조회
         SELECT * FROM tCity WHERE name LIKE '천%';

         // EMP 테이블에서 ENAME이 N으로 끝나는 6자의 이름을 가진 데이터를 조회
         SELECT * FROM EMP WHERE ENAME LIKE '_____N'

         // SALE에 30%가 포함된 데이터 조회
         WHERE SALE LIKE '%30#%%' ESCAPE '#'
         // # 뒤에 한 문자는 있는 그대로 해석
         ```

       - BETWEEN
         BETWEEN A AND B 형태로 작성하는 A 부터 B까지의 데이터 조회
         숫자, 날짜, 문자열 모두 사용이 가능.
         단순 AND로도 가능
         ```sql
         // tCity 데이터에서 popu가 50~100 사이인 데이터 조회
         SELECT * FROM tCity WHERE popu BETWEEN 50 AND 100);
         SELECT * FROM tCity WHERE popu >= 50 AND popu <= 100;
         ```
         문자열 크기 비교는 맨 글자부터 순서대로 하나씩 비교
         ```sql
         SELECT * FROM tCity WHERE name BETWEEN '가' AND '바';
         ```
         ```sql
         SELECT * FROM tCity WHERE name LIKE 'ㅊ%';
         SELECT * FROM tCity WHERE name >= "차" AND name < "카";
         ```
         ```sql
         // tStaff에서 joindate가 2015년 1월 1일 부터 2017년 12월 31일 사이인 데이터를 조회
         SELECT * FROM tStaff WHERE joindate BETWEEN '20150101' AND '20171231';
         ```
       - IN 연산자
         IN(값을 나열): 나열된 값에 포함되는 경우 조회
         ```sql
         // tCity 테이블에서 region이 경상이나 전라인 데이터 조회
         SELECT * FROM tCity WHERE region = '경상' OR region = '전라';
         SELECT * FROM tCity WHERE region IN ('경상', '전라');
         ```

   11. LIMIT

       행의 개수 제한에 이용 - TOP N

       ```sql
       LIMIT [건너뛸 행의 개수], 조회할 개수
       // 최근에는 LIMIT 개수 OFFSET 건너뛸 행의 개수
       // 일반적으로 ORDER BY와 같이 사용되는 경우가 많다.
       ```

       ```sql
       // tCity 테이블에서 popu가 큰 4개의 데이터를 조회
       SELECT * FROM tCity ORDER BY popu DESC LIMIT 4;

       // tCity 테이블에서 popu가 큰 5번째 데이터부터 2개 조회
       SELECT * FROM tCity ORDER BY popu DESC LIMIT 4,2;
       SELECT * FROM tCity ORDER BY popu DESC LIMIT 2 OFFSET 4;
       SELECT * FROM tCity ORDER BY popu DESC OFFSET 4 ROWS FETCH NEXT 2 ROWS ONLY;
       ```

3. **Scala Funtion**

   하나의 데이터를 받아서 하나의 데이터를 리턴하는 함수

   컬럼을 데이터로 제공하면 각 컬럼의 데이터 단위로 작업을 수행한 후 결과를 하나의 컬럼으로 만들어서 리턴

   1. 수치 함수

      숫자 연산과 관련된 함수로 올림 버림, 반올림 등의 함수가 제공된다.

      데이터는 숫자 데이터이어야 한다.

      FROM 절을 제외한 곳에서 사용이 가능하다.

      ```sql
      // EMP 테이블에서 EMPNO가 홀수인 데이터를 조회
      MOD(데이터, 나누는 수) // 나머지를 리턴

      SELECT * FROM EMP WHERE MOD(EMPNO, 2) = 1;
      ```

   2. 문자열 함수

      - CONCAT: 문자열 결합
      - UPPER, LOWER
      - LTRIM, RTRIM, TRIM
      - SUBSTRING
      - LENGTH

      ```sql
      // EMP 테이블에서 1982년에 입사한 사원의 ENAME과 SAL을 조회
      SELECT
      	ENAME,
      	SAL
      FROM
      	EMP
      WHERE
      	SUBSTRING(HIREDATE, 1, 4) = '1982'
      ```

   3. 날짜 관련 함수

      - CURRENT_DATE(), CURDATE() - 현재 날짜
      - CURRENT_TIME(), CURTIME() - 현재 시간
      - NOW(), LOCALTIME(), LOCALTIMESTAMP(), CURRUENT_TIMESTAMP() - 현재 날짜 및 시간
      - ADDDATE, SUBDATE, ADDTIME, SUBTIME - 날짜 및 시간 연산 함수
      - STR_TO_DATE(문자열, 서식): 서식에 맞춰서 문자열을 날짜 형태로 변환
        ```sql
        ('1986-05-05 11:00:00', '%Y-%m-%d %H;%i;%S')
        ```

      MySQL이나 MaraiDB는 일반적인 날짜 포맷의 문자열도 날짜로 간주

      ```sql
      STR_TO_DATE('1986-05-05', '%Y-%m-%d')
      '1986-05-05'
      // 날짜에서 많이 사용하는 포맷이므로 날짜로 간주
      ```

   4. 시스템 정보 함수
      - ROW_COUNT()
      - USER()
      - DATABASE()
   5. 타입 변환 함수
      - CAST(데이터 AS 자료형)
        자료형 - DATETIME, DATE, TIME, CHAR(VARCHAR, TEXT), INT, DOUBLE, BINARY…
   6. NULL 관련 함수

      - IFNULL(데이터1, 데이터2)
        데이터1이 NULL이 아니면 데이터1을 리턴하고 데이터 1이 NULL이면 데이터2를 리턴
      - NULLIF(데이터1, 데이터2)
        두 개의 데이터가 같으면 NULL을 리턴하고 그렇지 않으면 데이터1을 리턴
      - COALESCE(데이터나열): 나열된 데이터 중 NULL이 아닌 첫 번째 데이터를 리턴

      `데이터베이스에서는 NULL과 연산을 하면 결과는 NULL`

   7. 분기 관련 함수
      - IF
      - CASE 데이터 WHEN 값 THEN 결과 ELSE 결과

4. **GROUPING**

   그룹화 관련된 기능

   1. 그룹 함수
      - COUNT, SUM, AVG, MAX, MIN, STDDEV, VARIANCE
   2. 특징
      - NULL은 제외하고 연산
      - COUNT를 제외한 모든 함수는 컬럼 이름이나 연산식을 대입해야 하지만 COUNT는 \*이 가능
      - SUM과 AVG, STDDEV, VARIANCE는 문자열에는 사용 못함.
      - GROUP BY 이후부터 사용 가능
        HAVING, SELECT, ORDER(얘는 실제로는 사용할 필요가 거의 없음)에서 사용 가능
      - SELECT 절에 사용할 때는 대부분 별명과 함께 사용
   3. 그룹 함수에서는 NULL을 제외하고 연산

      ```sql
      #18개의 데이터 평균
      SELECT ROUND(AVG(score),0) FROM tStaff;

      # COUNT에 *을 사용했기 때문에 20개 데이터의 평균
      # 데이터가 NULL인 경우는 0으로 간주
      SELECT ROUND(SUM(score)/COUNT(*),0) FROM tStaff;
      ```

   4. GROUPING

      SELECT 구문에서 데이터를 그룹화하고자 할 때 사용하는 절

      WHERE 절 다음에 수행

      이 절이 수행되어야 그룹 함수를 사용하는 것이 가능 - `WHERE 절에서는 그룹 함수를 사용할 수 없음.`

      ```sql
      # EMP 테이블에서 부서별(DEPTNO) 평균 급여(SAL) 조회
      SELECT ROUND(AVG(SAL), 0)
      FROM EMP
      GROUP BY DEPTNO;

      # 그룹화한 항목을 제외하고 출력하면 데이터를 알아보기가 어렵기 때문에
      # 그룹화한 항목과 같이 조회하는 것이 좋다.
      SELECT DEPTNO, ROUND(AVG(SAL), 0)
      FROM EMP
      GROUP BY DEPTNO;
      ```

      그룹화는 여러 개 가능

      ```sql
      SELECT DEPTNO, JOB, ROUND(AVG(SAL), 0)
      FROM EMP
      GROUP BY DEPTNO, JOB;
      ```

      그룹화한 후 SELECT 절에서 그룹화한 항목이나 집계함수가 아닌 데이터의 조회 - ORACLE은 에러이고 MySQL이나 MariaDB는 그룹화한 항목 중 첫 번째 데이터 조회

   5. HAVING

      GROUPY BY 이후의 조건을 설정해서 행 단위로 추출

      `그룹 함수는 WHERE 절에서 사용 불가`하기 때문에 그룹 함수를 이용한 조건을 설정할 때는 HAVING에 작성해야 한다.

      ```sql
      # tStaff 테이블에서 depart 별로 그룹화해서 평균 salary가 340이 넘는 부서의
      # depart와 평균 salary를 조회

      # 에러 - WHERE 절에는 AVG를 쓸 수 없다.
      SELECT depart, AVG(salary)
      FROM tStaff
      WHERE AVG(salary) > 340
      GROUP BY depart;

      # 수정
      SELECT depart, AVG(salary)
      FROM tStaff
      GROUP BY depart
      HAVING AVG(salary) > 340;
      ```

      ```sql
      # tStaff 테이블에서 depart가 인사과와 영업부인 데이터를
      # depart 별로 그룹화해서 depart와 평균 salary를 조회

      # 잘못 쓴 경우
      # depart를 모두 그룹화(총무부 포함)한 후 인사과와 영업부를 추출
      SELECT depart, AVG(salary)
      FROM tStaff
      GROUP BY depart
      HAVING depart IN('인사과','영업부');

      # 올바른 경우
      # 인사과와 영업부 데이터를 추출한 후 그룹화를 수행
      # 데이터를 필터링할 때는 할 수 있으면 빨리하는 것이 좋다.
      SELECT depart, AVG(salary)
      FROM tStaff
      WHERE depart IN('인사과', '영업부')
      GROUP BY depart;
      ```

      `그룹 함수를 이용한 조건이 아니라면 HAVING에 작성하지 말고 WHERE에 작성을 해야 한다.`

   6. SELECT 구문
      - 5 - SELECT
      - 1 - FROM
      - 2 - WHERE
      - 3 - GROUP BY
      - 4 - HAVING
      - 6 - ORDER BY
      - 7 - LIMIT

5. **Window 함수**

   행과 행 사이의 관계를 표현하기 위한 함수

   순위나 누적합 등의 연산을 위한 함수

   순위 함수로는 동등한 값일 때 어떤 식으로 처리하느냐에 따라 여러 함수를 제공하고 N등분한 그룹도 제공해주는 함수가 있다.

   RANK, DENSE_RANK, ROW_NUMBER, NTILE 함수 제공

   함수 뒤에 OVER를 이용해서 순위를 구할 방법을 ORDER BY로 제공해야 함.

   ```sql
   SELECT 순위함수 OVER(순위 구할 방법), 다른컬럼 FROM 테이블이름;

   # EMP 테이블에서 SAL의 오름차순 순위
   SELECT ROW_NUMBER() OVER(ORDER BY SAL ASC), ENAME, SAL FROM EMP;

   # EMP 테이블에서 SAL의 오름차순 정렬해서 4개의 그룹으로 분할
   SELECT NTILE(4) OVER(ORDER BY SAL ASC), ENAME, SAL FROM EMP;
   ```

6. **기타 함수**

   JSON 출력

   잘 안쓰기는 하지만 데이터를 조회할 때 JSON_OBJECT로 감싸면 JSON 문자열을 리턴하는 강력한 기능.
