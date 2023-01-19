# [Database] View, Procedure, Trigger

**테이블 이외의 개체**

VIEW나 PROCEDURE, TRIGGER, INDEX가 데이터베이스 사용 성능을 향상시키기 위한 개체인데 최근의 프로그래밍에서는 IN MEMORY DB 개념의 형태를 사용하기 때문에 이 개체들을 사용하는 이점이 별로 없다.

1. **VIEW**

   자주 사용하는 SELECT 구문을 하나의 테이블의 형태로 사용하기 위한 개체

   1. 장점

      SELECT 구문을 메모리에 적재하기 때문에 속도가 향상

      필요한 부분만 노출하면 되기 때문에 보안이 향상

   2. Inline View

      FROM 절에 사용한 SELECT 구문

      SELECT 구문이 리턴하는 결과는 하나의 테이블처럼 사용이 가능하기 때문에 FROM절에 사용하는 것이 가능.

      ORACLE에서 OFFSET FETCH가 적용되기 전에는 Inline View를 이용해서 TOP-N을 구현했기 때문에 오라클에서는 매우 중요.

      ```sql
      SELECT * FROM (SELECT * FROM tStaff WHERE grade='과장' OR grade='부장') IMSI WHERE IMSI.score >= 70;
      ```

   3. VIEW

      ```sql
      CREATE [OR REPLACE] VIEW 뷰이름
      AS
      SELECT 구문
      [WITH CHECK OPTION]
      [WITH READ ONLY]
      ```

      VIEW는 ALTER로 수정이 불가능하기 때문에 수정하고자 할 때 OR REPLACE 사용

      VIEW는 테이블처럼 사용할 수 있기 때문에 읽기와 쓰기 작업이 모두 가능.

      WITH CHECK OPTION은 뷰를 만들 때 사용한 조건과 일치하는 데이터만 수정하거나 삭제 또는 삽입할 수 있도록 해주는 옵션

      WITH READ ONLY는 쓰기를 못하게 하는 옵션

   4. VIEW 삭제

      ```sql
      DROP VIEW 뷰이름;
      ```

   5. 실습

      ```sql
      # DEPT 테이블에서 DEPTNO가 30인 데이터를 자주 사용
      SELECT * FROM DEPT WHERE DEPTNO = 30 # 이렇게 사용해야 함.

      CREATE VIEW DEPTVIEW
      AS
      SELECT *
      FROM DEPT
      WHERE DEPTNO = 30;
      # 이제부터는 DEPTVIEW가 SELECT * FROM DEPT WHERE DEPTNO = 30의 역할을 수행
      # 한 번 컴파일이 되면 SQL이 메모리에 상주하기 때문에 빠름
      # 데이터가 올라가는게 아니라 구문이 올라가는 것임.
      # VIEW는 테이블과 동등한 권한을 가지기 때문에 읽기만 가능하다고 오해하지 말자.

      # DEPT 테이블의 모든 내용을 가지고 DEPTCOPY 테이블을 생성
      CREATE TABLE DEPTCOPY
      AS
      SELECT *
      FROM DEPT;

      # DEPTCOPY 테이블에서 DEPTNO가 20이 넘는 데이터를 가지고 DEPTVIEW라는 VIEW를 생성
      CREATE OR REPLACE VIEW DEPTVIEW
      AS
      SELECT *
      FROM DEPTCOPY
      WHERE DEOPTNO > 20;

      # DEPTVIEW는 테이블처럼 사용이 가능
      SELECT * FROM DEPTVIEW;

      # VIEW는 테이블처럼 사용이 가능
      # VIEW는 SQL을 가지고 있는 것이지 실제 데이터를 가지고 있는 것이 아님.

      # VIEW에 데이터 삽입 - 원본 테이블에 삽입
      INSERT INTO DEPTVIEW(DEPTNO, DNAME, LOC) VALUES(50, '영업', '분당');

      # 원본 테이블을 확인
      SELECT * FROM DEPTCOPY;

      # 뷰 삭제
      DROP VIEW DEPTVIEW;
      ```

      포트폴리오를 만들 때 SQL Mapper를 이용하는 경우라면 VIEW를 사용하는 것이 좋다.

2. **절차적 프로그래밍**

   SQL은 비절차적(작성한 순서대로 동작하지 않는다 - FROM 이후에 SELECT가 수행됨.)

   관계형 데이터베이스에서도 절차적 프로그래밍이 가능

   문법은 관계형 데이터베이스 종류마다 다름(Oracle은 이 문법을 PL/SQL 이라고 하고 MS SQL Server는 T SQL이라고 부름.)

   1. (Stored) Procedure

      자주 사용하는 SQL 구문을 함수처럼 미리 만들어두고 이름만으로 실행하도록 해주는 개체

      함수와 다른 점은 함수는 리턴을 하지만 리턴을 하지 않는다.

      한 번 실행하고 나면 메모리에 상주를 하기 때문에 성능이 향상. 테이블의 구조를 몰라도 작업이 가능하기 때문에 보안이 향상

      - 생성방법
        ```sql
        DELIMITER 기호2개
        CREATE [OR REPLACE] PROCEDURE 이름(매개변수)
        BEGIN
        	필요한 SQL 프로그래밍
        END 기호2개
        DELIMITER;
        ```
        생성을 할 때는 하나의 SQL 구문이 아니기 떄문에 스크립트 실행의 형태로 수행
      - 호출
        ```sql
        CALL 프로시저이름(매개변수 나열);
        ```
      - 삭제
        ```sql
        DROP PROCEDURE 이름;
        ```

      ORM이 아닌 SQL Mapper를 이용하는 경우 필수적으로 한 개 이상 사용

      실습

      ```sql
      DELIMITER $$
      CREATE OF REPLACE PROCEDURE myproc(
      	vdeptno int(2),
      	vdname varchar(14),
      	vloc varchar(13))

      	BEGIN
      		INSERT INTO DEPT(DEPTNO, DNAME, LOC)
      		VALUES(vdeptno, vdname, vloc);
      	END $$
      DELIMITER ;
      # 하나의 문장이 아니기 때문에 스크롤 스크립트 실행을 해야 한다.

      # 프로시저 호출
      CALL myproc(77, '회계', '서울');

      # 프로시저 호출 결과 확인
      SELECT * FROM DEPT;

      # 프로시저 삭제
      DROP PROCEDURE myproc;
      ```

   2. Trigger

      어떤 사건(INSERT, UPDATE, DELETE)이 발생했을 때 절차적 프로그래밍 부분을 자동으로 수행하기 위한 개체

      유효성 검사를 해서 SQL을 실행하지 않도록 하거나 DML 작업을 수행했을 때 로그를 기록하거나 다른 DML 작업을 연쇄적으로 실행하는 등의 작업을 주로 수행

      애플리케이션 개발자 입장에서는 프로그래밍으로 처리하려고 하기 때문에 잘 사용하지 않지만 보안을 위해서는 사용하는 것도 나쁘지 않다.

      ```sql
      # 삽입 트리거 - 하나의 테이블에 데이터를 삽입하면 다른 테이블에 데이터가
      # 동으로 삽입되도록 하는 트리거(블로그 같은 곳에서 회원 가입을 하면
      # 가입한 회원의 데이터를 관리할 수 있는 테이블을 별도로 생성하는 경우가 있다.)

      # 데이터를 삽입할 테이블
      CREATE TABLE EMP01(
      	EMPNO INT PRIMARY KEY,
      	ENAME VARCHAR(10),
      	JOB VARCHAR(30));

      # 트리거로 데이터를 삽입할 테이블
      CREATE TABLE SAL01(
      	SALNO INT PRIMARY KEY AUTO_INCREMENT,
      	SAL FLOAT(7,2),
      	EMPNO INT REFERENCES EMP01(EMPNO) ON DELETE CASCADE);

      # 삽입 트리거
      DELIMITER $$
      CREATE OR REPLACE TRIGGER TRG_01
      AFTER INSERT
      ON EMP01
      FOR EACH ROW
      BEGIN
      	INSERT INTO SAL01(SAL, EMPNO) VALUES(100, NEW.EMPNO);
      END $$
      DELIMITER ;

      # 데이터를 삽입하고 확인
      INSERT INTO EMP01 VALUES(1, 'adam', 'singer');

      SELECT * FROM EMP01;

      # 트리거로 인해서 데이터가 삽입이 됨.
      SELECT * FROM SAL01;
      ```

      <aside>
      ▪️ 중요도
      일반적인 개발자, SI
      SELECT → DML → TCL → DDL → 백업 및 복구 → 테이블 이외의 객체 → 최적화 → DCL

      ORM 사용하는 경우, 중견 기업에서 컨텐츠 개발
      모델링

      </aside>
