# [Database] MariaDB

1. **개요**

   SQL에 기반을 둔 RDBMS(관계형 데이터베이스)로 Open Source 형태로 제공

   My SQL 개발자가 만들어서 MySQL과 거의 유사

   SQL도 거의 차이가 없음.

   작업 단위

   - 데이터베이스 > 테이블
     하나의 데이터베이스는 여러 유저가 공유

   데이터베이스 사용

   - 로컬 데이터베이스 - 임시 저장
   - 외부 데이터베이스 - 서버
     SQLite(브라우저나 스마트폰에 장착되어있음)나 Access는 로컬에 설치해서 사용 - 데이터를 빠르게 또는 Offline 상태에서 사용하기 위한 목적. 그 이외의 데이터베이스는 대부분의 경우 외부에 설치해서 애플리케이션 서버를 통해서 사용하거나 직접 사용(DBA나 Operator)

2. **설치**

   1. OS에 직접 설치
      - windows - https://mariadb.org/download
      - Mac(brew install mariadb)이나 리눅스는 패키지 관리자를 이용해서 설치
   2. 가상화 컨테이너에 설치
      - docker는 회원 가입을 하고 설치
      - docker 사이트에서 회원가입
      - docker 다운로드 및 설치
        Windows에서 Docker를 사용하기 위해서는 리눅스 커널을 설치해야 한다.
   3. mariadb image 다운로드

      ```bash
      docker pull mariadb 버전(버전은 생략하면 최신 버전)
      ```

   4. mairadb 컨테이너 생성

      ```bash
      docker run --name mariadb -d -p 외부에서접속할포트번호:MariaDB포트번호 -e MYSQL_ROOT_PASSWORD=루트비밀번호 컨테이너이름
      ```

   5. 컨테이너 실행 확인

      ```bash
      docker ps
      // 모든 컨테이너를 확인하는 것은 -a 옵션을 추가
      ```

   6. 컨테이너 중지

      ```bash
      docker stop 컨테이너이름이나아이디
      ```

   7. 컨테이너 시작이나 재시작

      ```bash
      docker start 컨테이너이름이나아이디
      docker restart 컨테이너이름이나아이디
      ```

   8. 컨테이너 삭제

      ```bash
      docker rm 컨테이너이름이나아이디
      // 실행중인 컨테이너를 강제삭제할 때는 뒤에 -f 옵션을 추가
      ```

   9. 데이터베이스 접속 도구 설치
      - DBeaver(Open Source 이고 여러 데이터베이스 접속 가능) 설치
        금융 분야로 취업을 생각하면 토드나 오렌지 같은 접속 도구를 사용하는 것도 나쁘지 않다.

3. **데이터베이스 서버 실행 및 접속 확인**

   데이터베이스 접속 도구에서 다음과 같이 입력하고 연결을 시도한다.

   ```bash
   HOST: localhost
   PORT: 3306 // 설치할 때 변경했으면 수정
   DATABASE: mysql(기본 제공)
   USERNAME: root(기본 제공)
   PASSWORD: 설치할 때 사용한 비밀번호
   ```

4. **데이터베이스 외부 접속 허용**

   1. 권한 설정

      ```sql
      GRANT all privileges on 사용할데이터베이스이름 TO '계정'@'접속할IP';
      // 모든 데이터베이스를 사용하게 하고자 하는 경우는 *.*
      // 모든 곳에서 접속하도록 하고자 할 때는 %를 설정하고 로컬에서만 접속하도록 할 때는 localhost

      // 권한 설정 명령은 설정하고 적용 명령을 수행해야 한다.
      FLUSH privileges;

      // root 계정을 모든 곳에서 접속하도록 설정
      GRANT all privileges on *.* TO 'root'@'%';
      FLUSH privileges;
      ```

   2. 서버 설정(Windows에 직접 설치했으면 이 과정은 생략)

      /etc/mysql/mariadb.conf.d/50-server.cnf 파일의 bind-adress 부분을 허용할 IP로 변경을 해주어야 하는데 0.0.0.0 이면 모든 곳에서 접속 가능

      실제 서버 설정이라면 Application Server의 IP만 허용한다.

      `docker는 직접 파일을 수정할 수 없기 때문에 터미널에서 컨테이너의 bash로 접속해야한다.`

      ```bash
      docker exec -it 컨테이너이름 bash

      // 아래 작업은 한 번만 하면 됨.
      apt update
      apt upgarde
      apt install vim

      vim /etc/mysql/mariadb.conf.d/50-server.cnf 명령으로 수정

      // 텍스트가 열리면 i를 눌러서 수정 모드로 진입해서 수정
      // bind address 부분을 주석해제하고, 0.0.0.0으로 수정
      // esc를 눌러서 수정 모드를 빠져나온 후
      // :wq 명령으로 저장하고 나와야 한다.
      // mairadb 재시작(docker에서는 컨테이너 재시작)

      exit
      docker stop mariadb
      docker start mariadb
      docker restart mariadb
      ```

5. **SQL 작성 규칙**

   SQL의 예약어는 대소문자 구분을 하지 않는다.

   테이블 이름이나 컬럼 이름은 대소문자를 구분하는 데이터베이스도 있고 구분하지 않는 데이터베이스도 있다.

   Maria DB나 MYSQL은 구분한다.

   값을 작성할 때는 대소문자 구분을 하는데 MariaDB는 대소문자 구분을 하지 않는 경우도 있다.

   숫자 데이터는 따옴표를 하지 않고 문자는 작은 따옴표를 해서 표현하는데 MariaDB나 MySQL은 큰 따옴표도 허용한다.

   `명령문의 마지막은 ; 인데 접속 도구에서는 해도 되고 하지 않아도 되지만 절차적 프로그래밍을 할 떄는 명확하게 해주어야 하며 프로그래밍 언어에서 SQL을 사용할 때는 ;을 하면 안된다.`

6. **데이터베이스 관련 명령어**

   1. 데이터베이스 생성

      ```sql
      create database 데이터베이스이름; # 이미 존재하는 이름이면 에러
      # 일반적으로 프로젝트를 진행할 때 마다 데이터베이스를 생성
      ```

   2. 데이터베이스 확인

      ```sql
      show databases;
      ```

   3. 데이터베이스 사용 - 항상 MySQL이나 MariaDB에서는 SQL을 사용하기 전에 데이터베이스 사용 설정을 먼저 해야한다. (만들었는데 없다? 데이터베이스 이름이 틀려서 그렇다.)

      ```sql
      use 데이터베이스이름;
      ```

   4. 데이터베이스 삭제

      ```sql
      drop database 데이터베이스이름
      ```

   5. 데이터베이스에 존재하는 테이블 확인

      ```sql
      show tables;
      ```

   6. 샘플 데이터 생성

      pdf 참고
