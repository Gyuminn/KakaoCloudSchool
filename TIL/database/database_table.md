# [Dababase] Table

1.  **DDL(Data Definition Language)**

    데이터 구조를 생성하고 변경하고 삭제하는 명령어

    1.  기본 형식

        ```sql
        Create [Temporary] Tabel 테이블이름(
        	컬럼이름 자료형[컬럼 제약 조건],
        	...
        	[테이블 제약 조건]) 조건 나열;
        ```

    2.  자료형

        - 숫자: TINYINT(1바이트 - 참/거짓), INT(INTEGER), FLOAT, DOUBLE
        - 문자: CHAR(길이가 고정), VARCHAR(길이가 가변), TEXT(긴 문자열), BLOB(파일의 내용 저장)
        - 날짜: DATE(날짜), DATETIME(날짜와 시간), TIMESTAMP(날짜와 시간), TIME(시간), YEAR(연도)
        - 기타: JSON, GEOMETRY(공간 정보)

        <aside>
        ▪️ 파일을 데이터베이스에 저장하는 방법
        - 파일의 경로를 저장하는 방법: 파일을 별도로 저장하고 그 경로를 저장
        - 파일의 내용을 저장하는 방법: 파일을 별도로 저장하지 않고 데이터베이스에 저장(BLOB)

        </aside>

    3.  조건 나열
        - ENGINE: MyISAM(Indexed Sequential Access Media - 트리 구조로 조회에 유리)이나 InnoDB(Linked List 구조 - 삽입 삭제 갱신에 유리)를 설정할 수 있음.
        - DEFAULT CHARSET: 한글이 깨지는 경우 한글 설정하는 옵션으로 utf8을 설정해주면 되는데 Maria DB는 기본이 utf8
        - auto_increment=시작숫자: 일련번호를 사용할 때 시작숫자부터 시작한다.
        - Timezone 설정: Mac에서 사용할 때 시간 대역이 안맞아서 설정해주어야 하는 경우가 있다.
    4.  테이블 생성

        연락처 테이블 만들기

        - 테이블 이름: concat
        - num: 정수
        - name: 문자열이고 영문 20자까지 저장하고 자주 변경
        - address: 문자열이고 영문 100자까지 저장하고 자주 변경되지 않음
        - tel: 문자열로 영문 20자까지 저장하고 자주 변경됨
        - email: 문자열로 영문 100자까지 저장하고 자주 변경됨
        - birthday: 날짜
        - 비밀번호를 저장할 때는 대부분이 경우 해시를 수행해서 해시코드를 저장하기 때문에 해시 방법에 따라 64, 128, 256으로 설정해야 한다.

        ```sql
        create table contact(
        	num integer,
        	name char(20),
        	address varchar(100),
        	tel char(20),
        	email char(100),
        	birthday date
        ) ENGINE = MyISAM;
        ```

    5.  테이블 구조 변경

        - 테이블 구조 확인
          ```sql
          DESC 테이블이름
          ```
        - 기본 형식
          ```sql
          ALTER TABLE 테이블이름 작업 매개변수나열
          ```
        - 컬럼 추가

          ```sql
          ALTER TABLE 테이블이름 ADD 컬럼이름 자료형 제약조건

          # contact 테이블에 age라는 컬럼을 정수로 추가
          # 컬럼을 추가하는 경우 기존에 데이터가 존재했다면 모두 null로 삽입된다.
          ALTER TABLE contact ADD age INTEGER;
          ```

        - 컬럼 삭제

          ```sql
          ALTER TABLE 테이블이름 DROP 컬럼이름;

          # contact 테이블에서 age 컬럼 삭제
          ALTER TABLE contact DROP age;
          ```

        - 컬럼 변경

          - 이름과 자료형 변경

            ```sql
            ALTER TABLE 테이블이름 CHANGE 기존컬럼이름 새로운컬럼이름 자료형 제약조건;

            # contact 테이블에서 tel이라는 컬럼을 phone 이라는 컬럼으로 변경
            ALTER TABLE contact CHANGE tel phone varchar(11);
            ```

          - 자료형만 변경하는 경우 - NOT NULL에 대한 설정 포함
            크기가 변경되는 경우 기존의 크기보다 커지는 것은 아무런 문제가 없지만 작아지는 것은 데이터의 손실이 발생할 수 있으므로 주의
            `sql ALTER TABLE 테이블이름 MODIFY 기존컬럼이름 새로운컬럼이름 자료형; `
          - 컬럼을 추가하거나 삭제하는 명령은 관계형 데이터베이스에서 거의 비슷하지만 컬럼을 변경하는 명령은 데이터베이스마다 다름.

        - 컬럼 순서 조정
          새로운 컬럼이 추가되면 맨 뒤에 추가

          ```sql
          # 컬럼을 맨 앞으로 이동
          ALTER TABLE 테이블이름 MODIFY COLUMN 컬럼이름 자료형 FIRST;

          # 컬럼을 특정 컬럼 뒤로 이동
          ALTER TABLE 테이블이름 MODIFY COLUMN 컬럼이름 자료형 AFTER 앞에있을컬럼이름;
          ```

        - 테이블 이름 수정
          ```sql
          ALTER TABLE 원래테이블이름 RENAME 새로운테이블이름;
          ```

    6.  테이블 삭제
        - 기본 형식
          ```sql
          DROP TABLE 테이블이름;
          ```
        - 테이블이 삭제가 되지 않는 경우
          외래키로 참조되는 테이블은 외래키를 소유하고 있는 테이블이 먼저 삭제되어야 한다.
    7.  테이블의 모든 데이터 삭제

        ```sql
        TRUNCATE TABLE 테이블이름;
        ```

    8.  테이블 압축

        CREATE TABLE 다음에 ROW_FORMAT=COMPRESSED 옵션을 추가하면 테이블을 압축해서 생성됨.

        저장 공간을 줄일 수 있지만 작업 속도는 느려짐.

    9.  주석 설정

        ```sql
        COMMENT ON TABLE 테이블이름 IS '주석';
        ```

2.  **제약 조건**

    1. 무결성 제약 조건
       - Entity Integrity(개체 무결성): 기본키는 NULL이거나 중복될 수 없다.
       - Referential Integrity(참조 무결성): 외래키는 참조할 수 있는 값이나 NULL을 가져야 한다.
       - Domain Integrity(도메인 무결성): 속성의 값은 정해진 도메인의 값을 가져야 한다.
    2. NOT NULL
       - NULL일 수 없다라는 제약조건
       - 필수 입력
       - 컬럼의 크기와 관련이 있기 때문에 컬럼을 만들 때 제약조건을 설정해야 한다.
       - 테이블 제약조건으로 만들 수 없다.
         컬럼이름 자료형 NOT NULL의 형태로 설정
       - 기본은 NULL을 허용하는 것이다.
    3. DEFAULT
       - 데이터베이스 이론에서는 DEFAULT는 제약조건이 아님.
       - 입력하지 않았을 때 기본으로 삽입되는 데이터
       - DEFAULT 값의 형태로 지정
       - 숫자는 0 문자열의 경우는 ‘’ 이나 ‘N/A’ 등을 많이 설정하고 날짜의 경우는 현재 시간(CURRENT_TIMESTAMP나 NOW 등)을 많이 사용
    4. CHECK

       - 값의 종류나 범위를 제한하기 위한 조건
       - 설정방법

         ```sql
         CHECK(컬럼이름 조건);

         # GENDER 컬럼은 문자 3자 인데 남 또는 여만 가져야 하는 경우
         GENDER CHAR(3) CHECK(GENDER IN ('남','여'));

         # SCORE 컬럼은 정수인데 0 ~ 100까지의 값만 가져야 하는 경우
         SCORE INTEGER CHECK(SCORE BETWEEN 0 AND 100);
         ```

    5. PRIMARY KEY(기본키)

       - 테이블에서 PRIMARY KEY는 한 번만 설정 가능
       - 2개 이상의 컬럼으로 PRIMARY KEY(복합키)를 설정하는 경우는 테이블 제약조건으로 설정해야 함. 학습을 할 때는 복합키를 거의 사용하지 않지만 실무에서 복합키를 사용하는 경우가 종종 발생한다.

       ```sql
       # MEMBER 테이블에서 ID를 PRIMARY KEY로 설정
       # 컬럼 제약 조건으로 설정
       CREATE TABLE MEMBER(
       	ID VARCHAR(50) PRIMARY KEY
       );

       # 테이블 제약 조건으로 설정
       CREATE TABLE MEMBER(
       	ID VARCHAR(50),
       	PRIMARY KEY(ID)
       );

       # MEMBER 테이블에서 ID와 NAME을 합쳐서 PRIMARY KEY로 설정
       # PRIMARY KEY는 한 번만 나와야 하므로 테이블 제약 조건으로 설정
       CREATE TABLE MEMBER(
       	ID VARCHAR(50),
       	NAME VARCHAR(50),
       	...
       	PRIMARY KEY(ID, NAME)
       );

       ```

       - PRIMARY KEY는 자동으로 클러스터 인덱스(저장 순서대로 만들어지는 인덱스 - 하나만 생성)를 생성한다.
         PRIMARY KEY를 이용해서 조회할 때 가장 빠른 성능을 나타낸다.
         PRIMARY KEY를 설정하는 것은 중요한 작업 중의 하나이다.
       - PRIMARY KEY는 NOT NULL이고 UNIQUE

    6. UNIQUE
       - 중복 값을 가질 수 없도록 하는 제약조건
       - NULL을 허용
       - 인덱스를 생성하는데 PRIMARY KEY가 없으면 UNIQUE가 클러스터 인덱스가 되고 PRIMARY KEY가 있으면 보조 인덱스가 된다.
       - PRIMARY KEY와 더불어 다른 테이블에서의 FOREIGN KEY가 될 수 있다.
       - 시험에서는 PRIMARY KEY만 FOREIGN KEY가 될 수 있다고 한다.
    7. 제약조건 이름 설정
       - 제약조건을 설정할 때 앞에 `CONSTRAINT 제약조건이름`을 추가하면 제약조건 이름이 만들어진다.
       - 일반적으로 테이블 이름과 제약조건의 약자를 조합해서 만드는 경우가 많다.
         - PRIMARY KEY - pk
         - NOT NULL - nn
         - UNIQUE - uk
         - CHECK - ck
         - FOREIGN KEY - fk
         ```sql
         CREATE TABLE MEMBER(
         	ID VARCHAR(50),
         	NAME VARCHAR(50),
         	...
         	CONSTRAINT member_pk PRIMARY KEY(ID, NAME)
         );
         ```
    8. 제약 조건 수정
       - 제약 조건 수정
         ```sql
         ALTER TABLE 테이블이름 MODIFY 컬럼이름 자료형 [CONSTRAINT 이름] 제약조건;
         ```
       - 제약 조건 추가
         NOT NULL을 추가로 설정하는 경우는 제약조건을 추가하는 것이 아니고 컬럼의 자료형을 수정하는 것이다.
         ```sql
         ALTER TABLE 테이블이름 ADD [CONSTRAINT 이름] 제약조건(컬럼이름);
         ```
       - 제약 조건 삭제
         ```sql
         ALTER TABLE 테이블이름 DROP CONSTRAINT 제약조건이름;
         ```
    9. Sequence(일련 번호)
       - 컬럼 이름 뒤에 `AUTO_INCREMENT` 를 설정하면 일련번호가 만들어진다.
         AUTO_INCREMENT가 설정된 컬럼은 값을 대입하지 않아도 된다.
       - 테이블을 생성할 때 초기 값을 설정할 수 있다.
       - 일련번호 초기값 수정
         ```sql
         ALTER TABLE 테이블이름 AUTO_INCREMENT = 값;
         ```
       - AUTO_INCREMENT는 PK나 UK를 설정해야 하고 테이블에서 한 번만 설정 가능
    10. 참조 무결성

        tEmployee 테이블과 tProject 라는 테이블을 생성

        ```sql
        # tEmplyee 테이블은 직원에 대한 정보를 가진 테이블이고
        # tProject 직원이 수행한 프로젝트에 대한 정보를 가진 테이블이다.

        # FOREIGN KEY를 설정하지 않은 경우
        CREATE TABLE tEmployee(
        	name VARCHAR(20) PRIMARY KEY,
        	salary INTEGER NOT NULL,
        	addr CHAR(100) NOT NULL
        );

        INSERT INTO tEmployee(name, salary, addr) VALUES('아이린', 5000, '대구');
        INSERT INTO tEmployee(name, salary, addr) VALUES('수지', 10000, '광주');
        INSERT INTO tEmployee(name, salary, addr) VALUES('조이', 2000, '경기');

        CREATE TABLE tProject(
        	projectid INTEGER AUTO_INCREMENT PRIMARY KEY,
        	name VARCHAR(20),
        	project VARCHAR(20),
        	cost INTEGER
        );

        INSERT INTO tProject(name, project, cost) VALUES('아이린', 'KB', 5000);
        INSERT INTO tProject(name, project, cost) VALUES('수지', 'KB', 5000);
        INSERT INTO tProject(name, project, cost) VALUES(NULL, 'KB', 5000);
        INSERT INTO tProject(name, project, cost) VALUES('아이유', 'KB', 5000);
        # 아이유는 tEmployee 테이블에 존재하지 않지만 외래키가 설정되지 않았으므로 삽입 가능
        # 이런 구조로 테이블을 만들게 되면 tProject 테이블에 데이터를 삽입할 때 마다 name을
        # 확인해야 하기 떄문에 번거롭다.
        ```

        tProject를 삭제하고 외래키를 설정해서 다시 해보자.

        외래키는 상대방 테이블에서 PRIMARY KEY나 UNIQUE 제약 조건이 설정되어 있어야 함.

        - 컬럼 제약 조건으로 설정
          ```sql
          컬럼이름 자료형 [CONSTRAINT 제약조건이름] REFERENCES 참조하는테이블이름(컬럼이름) 옵션
          ```
        - 테이블 제약 조건으로 설정
          ```sql
          [CONSTRAINT 제약조건이름] FOREIGN KEY(컬럼이름) REFERENCES 참조하는테이블이름(컬럼이름) 옵션
          ```

        ```sql
        CREATE TABLE tProject(
        	projectid INTEGER AUTO_INCREMENT PRIMARY KEY,
        	name VARCHAR(20) REFERENCES tEmployee(name),
        	project VARCHAR(50),
        	cost INTEGER
        );

        INSERT INTO tProject(name, project, cost) VALUES('아이린', 'KB', 5000);
        INSERT INTO tProject(name, project, cost) VALUES('수지', 'KB', 5000);
        INSERT INTO tProject(name, project, cost) VALUES(NULL, 'KB', 5000);
        # 외래키는 NULL이 가능하기 때문에 에러 발생하지 않음.
        INSERT INTO tProject(name, project, cost) VALUES('아이유', 'KB', 5000);
        # 아이유는 tEmployee 테이블에 존재하지 않는 데이터이기 때문에 에러 발생
        ```

    11. 외래키 옵션

        - 옵션없이 FOREIGN KEY를 설정하게 되면 외래키로 참조되는 데이터는 삭제할 수 없다.
          참조되지 않는 데이터는 삭제가 가능하다.
        - 외래키에 의해서 참조되는 테이블은 먼저 삭제할 수 없고 외래키를 소유하고 있는 테이블을 삭제한 후 삭제를 해야 한다.

        ```sql
        # 조이는 참조되지 않고 있기 때문에 삭제가 가능
        DELETE FROM tEmployee WHERE name = '조이';

        # 에러 - 수지는 참조되고 있기 때문에 삭제가 불가능
        DELETE FROM tEmployee WHERE name = '수지';
        ```

        - 외래키 설정할 때 옵션

          - ON DELETE [NO ACTION | CASCADE | SET NULL | SET DEFAULT]
          - ON UPDATE [NO ACTION | CASCADE | SET NULL | SET DEFAULT]
            NO ACTION은 아무것도 하지 않음
            CASCADE는 같이 삭제되거나 수정
            SET NULL은 NULL로 변경
            SET DEFAULT는 DEFAULT 값으로 변경
            ON UPDATE는 잘 사용하지 않는데 이유는 일반적으로 PRIMARY KEY는 불변의 성격을 갖기 때문.

          ```sql
          CREATE TABLE tProject(
          	projectid INTEGER AUTO_INCREMENT PRIMARY KEY,
          	name VARCHAR(20) REFERENCES tEmployee(name) ON DELETE SET NULL,
          	project VARCHAR(50),
          	cost INTEGER
          );

          DELETE FROM tEmployee WHERE name = '조이';
          DELETE FROM tEmployee WHERE name = '수지';
          # 이제는 수지도 삭제가 가능해지고 tProject의 name, 수지 부분에 NULL이 들어감.
          ```

3.  **DML과 Transaction**

    1. DML(Data Manipolation Language)

       데이터를 테이블에 삽입, 삭제, 갱신하는 SQL

       개발자가 사용하는 언어

    2. 데이터 삽입

       ```sql
       INSERT INTO 테이블이름(컬럼 이름 나열) VALUES (값을 나열);
       ```

       컬럼 이름을 생각하면 모든 컬럼의 값을 테이블을 만들 때 사용했던 순서대로 대입해야 한다.

       ```sql
       # tCity 테이블에 삽입
       # 테이블 구조 확인
       DESC tCity;

       # 컬럼 이름과 함께 삽입
       INSERT INTO tCity(name, area, popu, metro, region) VALUES('강릉',100,22,'n','강원');

       # 컬럼 이름을 생략하고 삽입
       INSERT INTO tCity VALUES('마산', 200, 45, 'n', '경상');
       ```

       NULL 삽입

       - 기본값이 없는 경우에는 컬럼 이름을 생략하고 삽입
       - 명시적으로 값을 NULL이라고 설정
       - 문자열의 경우는 ‘’ 형태로 입력해도 NULL로 간주하는 데이터베이스가 있다.

       ```sql
       # 컬럼 이름을 생략하여 NULL 삽입
       INSERT INTO DEPT(DEPTNO, DNAME) VALUES(60, '영업');

       # 명시적으로 값을 NULL이라고 설정
       INSERT INTO DEPT(DEPTNO, DNAME, LOC) VALUES(80, '비서', NULL);

       # Maria DB에서는 ''은 NULL이 아니고 공백으로 들어간다.
       INSERT INTO DEPT(DEPTNO, DNAME, LOC) VALUES(90, '기획', '');
       ```

       여러 개의 데이터를 한꺼번에 삽입

       ```sql
       INSERT INTO 테이블이름(컬럼 이름 나열) VALUES(값을 나열) (값을 나열)...
       ```

       다른 테이블로부터 조회해서 삽입

       ```sql
       INSERT INTO 테이블이름(컬럼 이름 나열) SELECT 구문;
       ```

       조회한 결과를 가지고 테이블 생성

       ```sql
       CREATE TABLE 테이블이름 AS SELECT 구문;
       ```

       에러 무시하고 삽입

       스크립트를 이용할 때 중간에 에러가 발생해도 데이터를 삽입하고자 하는 경우에는 INSERT IGNORE INTO 구문을 이용하면 된다.

    3. 데이터 삭제

       ```sql
       DELETE FROM 테이블이름 [WHERE 조건]
       ```

       WHERE 절을 생략하면 테이블의 모든 데이터가 삭제 - TRUNCATE와 유사한데 다른 점은 DELETE는 트랜잭션을 설정하면 복구가 가능

       ```sql
       # DEPT 테이블에서 DEPTNO가 40보다 큰 데이터를 전부 삭제
       DELETE FROM DEPT WHERE DEPTNO > 40;
       ```

       INSERT는 성공을 하면 반드시 1개 이상의 행이 영향을 받지만 DELETE나 UPDATE는 0개 이상의 행이 영향을 받는다.

       WHERE 절이 있기 때문에 조건에 맞는 데이터가 없으면 영향받은 행의 개수는 0이다.

       외래키 옵션 없이 생성되면 삭제가 되지 않을 수도 있다.

    4. 데이터 수정

       ```sql
       UPDATE 테이블이름 SET 수정할컬럼=값, ... [WHERE 조건]
       ```

       WHERE 절을 생략하면 테이블의 모든 데이터가 수정된다.

       ```sql
       # tCity 테이블의 name이 서울인 데이터의 popu는 1000으로 region은 전라로 수정
       UPDATE tCity SET popu = 1000, region = '전라' WHERE name='서울'
       ```

    5. Transaction

       한 번에 수행되어야 하는 논리적인 작업 단위

       1개 이상의 DML 문장으로 구성이 된다.

       ```sql
       A와 B가 SWORD 라는 아이템을 10000원에 거래
       데이터베이스에서는 하나씩 작업을 수행

       # 아래 4개의 작업은 모두 수행되던지 수행되지 말아야 한다.
       A 유저에게서 SWORD를 삭제
       B 유저에게서 SWORD를 추가
       A 유저에게 10000원 추가
       B 유저에게 10000원 삭감
       ```

       - 트랜잭션이 가져야 하는 성질
         - Atomicity(원자성): ALL OR NOTHING - 전부 아니면 전무
         - Consistency(일관성): 트랜잭션 수행 전과 수행 후의 결과가 일관성이 있어야 한다.
         - Isolation(격리성, 독립성): 하나의 트랜잭션은 다른 트랜잭션의 영향을 받으면 안되고 독립적으로 수행되어야 한다.
         - Durability(영속성, 지속성): 한 번 완료된 트랜잭션은 영원히 반영되어야 한다.(수정할 수 없다.)
       - 트랜잭션 구현의 원리
         - DML 작업을 수행할 때는 원본 데이터에 작업을 수행하는 것이 아니고 임시 작업 영역을 만들어서 그 영역에 데이터를 복제해서 작업을 수행한다.
         - 작업을 전부 완료하면 원본에 변경 내역을 반영하는데 이 작업을 `COMMIT` 이라고 한다.
         - 작업을 수행하는 도중 실패를 했다면 변경 내역을 원본에 반영하지 않는데 이를 `ROLLBACK` 이라고 한다.
       - 트랜잭션 명령어
         - COMMIT: 원본에 반영
         - ROLLBACK: 원본에 반영하지 않음
         - SAVEPOINT: ROLLBACK할 위치를 설정
       - 트랜잭션 모드
         - Manual: 사용자가 직접 COMMIT과 ROLLBACK을 하도록 하는 모드
         - Auto: 하나의 명령어가 성공적으로 수행되면 자동으로 COMMIT을 하는 모드
           프로그래밍 언어에서 데이터베이스를 연결하거나 접속 도구 등에서 데이터베이스 서버에 접속해서 작업을 수행하는 경우 Auto로 설정되는 경우가 있다.
       - 트랜잭션 생성 시점
         DML 문장이 성공적으로 완료되면 생성
       - 트랜잭션 종료 시점
         COMMIT이나 ROLLBACK을 수행한 경우
       - AUTO COMMIT
         자동적으로 COMMIT 되는 경우
         DDL(CREATE, ALTER, DROP, TRUNCATE)이나 DCL(GRANT, REVOKE) 문장을 수행
         접속 프로그램을 정상적으로 종료한 경우
       - AUTO ROLLBACK
         자동으로 ROLLBACK 되는 경우
         접속이 비정상적으로 종료된 경우

       실습

       ```sql
       # DBeaver auto설정을 Manual로 바꾸고 진행
       # DEPT 테이블의 모든 내용을 가지고 DEPT01 테이블 생성
       CREATE TABLE DEPT01 AS SELECT * FROM DEPT;

       # DEPT01 테이블의 모든 테이블 삭제
       # DELETE 문장을 성공적으로 수행ㅇ하므로 TRANSACTION이 생성됨.
       DELETE FROM DEPT01;

       # TRANSACTION 시작점으로 이동
       ROLLBACK;

       # 데이터 확인 - 지우는 작업이 취소
       SELECT * FROM DEPT01;

       # DEPTNO가 20인 데이터를 삭제
       DELETE FROM DEPT01 WHERE DEPTNO = 20;

       # 현재까지 내역을 원본에 반영 - 트랜잭션 종료
       COMMIT;

       # 트랜잭션이 이미 종료되어서 ROLLBACK을 해도 데이터가 복구되지 않는다.
       ROLLBACK;
       SELECT * FROM DEPT01;
       ```

       ```sql
       # DEPT01 테이블에서 DEPTNO가 10인 데이터를 삭제
       DELETE FROM DEPT01 WHERE DEPTNO = 10;

       # DDL 문장을 수행하면 트랜잭션은 종료
       CREATE TABLE DEPT03(DEPTNO INT);

       # CREATE 구문이 성공적으로 수행되면 트랜잭션은 종료된다.
       # ROLLBACK을 해도 돌아가지 못한다.
       ROLLBACK;

       SELECT * FROM DEPT01;
       ```

       ```sql
       # DEPT01 테이블을 삭제
       DROP TABLE DEPT01;

       # DEPT 테이블을 그대로 복제해서 DEPT01 테이블 생성
       CREATE TABLE DEPT01 AS SELECT * FROM DEPT;

       SELECT * FROM DEPT01;

       # DEPTNO가 40인 데이터 삭제
       DELETE FROM DEPT01 WHERE DEPTNO = 40;

       # SAVEPOINT 생성
       SAVEPOINT s1;

       # DEPTNO가 30인 데이터 삭제
       DELETE FROM DEPT01 WHERE DEPTNO = 30;

       # SAVEPOINT 생성
       SAVEPOINT s2;

       # DEPTNO가 20인 데이터 삭제
       DELETE FROM DEPT01 WHERE DEPTNO = 20;

       # ROLLBACK할 방법이 3가지
       ROLLBACK; # 테이블을 생성한 지점으로 롤백 - 데이터가 4개, 만든 SAVEPOINT는 모두 제거

       ROLLBACK TO s1; # 40인 데이터를 지운 지점까지 롤백 - 데이터가 3개
       								# ROLLBACK을 또 하면 데이터가 4개가 되지만 s2라는 SAVEPOIN는 제거

       ROLLBACK TO s2; # 30인 데이터를 지운 지점까지 롤백 - 데이터가 2개
       								# ROLLBACK을 또 하면 데이터가 4개가 되지만 ROLLBACK TO s1도 가능

       ```

    6. LOCK
       - 2가지 LOCK이 존재
       - Shared LOCK, Exclusive LOCK 2가지로 Shared LOCK은 공유 가능한 LOCK이고 Exclusive LOCK은 공유가 불가능한 LOCK이다.
       - 읽기 작업을 할 때는 Shared LOCK이 설정되고 그 외의 작업을 할 때는 Exclusive LOCK이 걸리게 되는데 이 경우는 트랜잭션이 종료되어야만 LOCK이 해제된다.
       - TRANSACTION MODE를 Manual로 사용하는 경우 하나의 컴퓨터에서 DML 작업을 수행하고 COMMIT이나 ROLLBACK을 하지 않은 상태에서 다른 컴퓨터에 SELECT를 하는 것은 아무런 문제가 없지만 DML 작업이나 DDL 작업을 수행하는 것은 안되는데 무한 루프에 빠지게 된다.
       - LOCK의 기본 단위는 테이블
