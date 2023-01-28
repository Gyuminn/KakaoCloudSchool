# [Docker] basic

1. **Docker**
   1. Docker
      - Container형 가상화 기술을 구현하기 위한 애플리케이션과 이 애플리케이션을 조작하기 위한 명령행 도구로 구성되는 애플리케이션
      - 프로그램과 데이터를 격리시키는 기능을 제공
      - 도커는 리눅스 운영체제를 사용
        윈도우나 MacOS에서도 도커를 구동시킬 수 있지만 이 경우 내부적으로 리눅스가 사용되면 컨테이너 동작시킬 프로그램도 리눅스용 프로그램
      - H/W ↔ Linux ↔ Docker Engine ↔ 컨테이너
   2. 데이터나 프로그램을 독립된 환경에 격리시켜야 하는 이유는 **마이크로 서비스를 구현하는 이유와도 유사**
      - 대부분의 프로그램은 단독으로 동작하지 않고 실행 환경이나 라이브러리 또는 다른 프로그램을 이용해서 동작
        - PHP나 Java는 실행 환경이 별도로 필요하고 Python은 다른 라이브러리를 사용하는 경우가 많고 워드프레스나 레드마인같은 경우는 MySQL이나 Maria DB와 같은 프로그램이 필요
        - 다른 프로그램과 폴더, 디렉토리 또는 환경 설정 파일을 공유하는 경우가 생기게 된다.
        - 프로그램 하나를 수정하는 경우 다른 프로그램에도 영향을 미치는 경우가 발생할 수 있음.
   3. 도커와 가상화 기술 차이
      - Virtual Box나 VMWare, UTM(Mac M1이나 M2 Processor) 같은 가상의 물리 서버를 만드는 것으로 여기서 의미하는 가상화는 물리적인 대상을 소프트웨어로 구현했다는 의미로 운영체제를 설치하고 그 위에 소프트웨어를 구동시키는 것
      - 도커는 리눅스 운영체제의 일부 기능만을 물리 서버에 맡겨서 수행하는 형태로 사용할 수 있는 소프트웨어는 리눅스 용으로 제한됨.
      - 하드웨어 ↔ 리눅스 커널 ↔ 리눅스 쉘 ↔ 소프트웨어 ↔ 사용자
   4. 도커와 AWS EC2 차이
      - AWS EC2는 도커와 비슷하게 인스턴스라는 개념을 사용하는데 EC2도 가상화 기술로 각각의 인스턴스가 완전히 독립된 컴퓨터처럼 동작하는 방식
      - AWS EC2는 AMI라는 이미지로부터 생성되기 때문에 인스턴스를 배포하는 방법이 Docker와 유사
      - 가상 서버를 만들지 않아도 컨테이너 이미지를 바로 실행할 수 있는 호스팅 서비스의 형태
      - 리눅스의 도커
        하드웨어 ↔ 리눅스 커널 ↔ 도커 엔진 ↔ 이미지(셸과 소프트웨어) 또는 컨테이너 ↔ 사용자
      - 윈도우의 도커
        하드웨어 ↔ 윈도우 ↔ Hyper-V, Moby-VM, 리눅스 운영체제(커널) ↔ 도커 엔진
   5. Image와 Container
      - Image - Class
      - Container - Instance
      - Container를 수정해서 Image를 생성할 수 있음.
        Container는 다른 도커 엔진으로 이동이 가능 - 이미지를 이용해서 이동한 것과 같은 효과
        컨테이너를 이용해서 이미지를 만든 후 export를 하고 다른 도커 엔진에서 import하면 된다.
      - 도커 허브: https://hub.docker.com
        도커 이미지를 모아 놓은 곳
      - 이미지의 종류
        - 운영체제
        - 소프트웨어가 포함된 이미지
        - 소프트웨어가 여러 개 포함된 이미지
   6. 데이터를 저장
      - 컨테이너를 삭제하면 컨테이너 안에서 만들어진 데이터도 소멸됨.
      - 도커가 설치된 물리적 서버의 디스크를 마운트해서 저장하는 것이 가능.
   7. 도커의 장점
      - 개발 환경과 운영 환경을 거의 동등하게 재현할 수 있음.
      - 가상화 소프트웨어보다 가벼움.
      - 물리적 환경의 차이나 서버 구성의 차이를 무시할 수 있음.
      - 클라우드 플랫폼을 지원
   8. 도커를 추천하지 않는 경우
      - 리눅스 계열의 운영체제 동작이 요구되는 경우
        실제 리눅스와 도커의 리눅스는 다름
      - 도커는 리눅스 머신에서만 동작
   9. 주 용도
      - 동일한 개발 환경 제공
      - 새로운 버전을 테스트
      - 동일한 구조의 서버가 여러 개 필요한 경우(이미지 복사)
2. **도커 사용**
   1. 사용 방법
      - 리눅스 컴퓨터에서 도커 사용
      - 윈도우나 Mac OS에서 Docker Desktop을 설치해서 사용
      - 가상 머신이나 렌탈 환경에서 도커를 설치하고 사용
   2. 제약
      - 운영체제가 64bit 이어야 한다.
   3. 윈도우에서 사용할 때 수행할 내용
      - Hyper-V 기능이 필요
        확인은 작업 관리자에서 CPU 부분을 확인해보면 가상화가 사용으로 되어 있어야 한다.
        사용 안함으로 되어 있다면 BIOS에서 설정을 해야 한다.
      - WSL2를 다운로드 받아서 설치: https://learn.microsoft.com/en-us/windows/install
   4. Docker Desktop 설치
      - 도커 허브에 회원가입 후 설치
3. **Docker의 기본적인 사용**

   1. 명령어의 기본 형식

      - 도커의 명령어는 docker로 시작
      - 기본 형식

        ```bash
        docker 명령어 옵션 대상 인자
        ```

        - 명령어는 단순 명령어와 하위 명령어로 구분되기도 한다.
        - 옵션은 도커에 대한 옵션
        - 인자는 이미지에 대한 옵션
        - 예

          ```bash
          // penguin 이미지를 다운로드
          docker image pull penguin

          // penguin 이미지를 실행
          docker container run penguin

          // penguin 이미지를 background mode에서 사용
          docker container run -d penguin -mode=1
          ```

   2. 도커의 버전 확인

      `docker version`

   3. 상위 커맨드
      - container
      - image
      - network
        여러 개의 컨테이너를 하나로 묶고자 할 때 사용
      - volume
   4. 하위 커맨드
      - container
        - start
          컨테이너 실행
        - stop
          컨테이너 중지
        - create
          컨테이너 생성
        - run
          이미지가 없으면 내려받고 컨테이너를 생성해서 실행(image pull, create, start가 합쳐진 명령)
        - rm
          정지 중인 컨테이너 삭제
        - exec
          컨테이너 안에서 프로그램을 실행
        - ls
          컨테이너 목록 출력
        - cp
          컨테이너와 호스트 간에 파일을 복사
        - commit
          컨테이너를 이미지로 변환
      - image
        - pull
          이미지 다운로드
        - rm
          이미지 삭제
        - ls
          이미지 목록 출력
        - build
          이미지를 생성
      - volume
        - create
          생성
        - inspect
          상세 정보 출력
        - ls
          목록 출력
        - prune
          마운트 되지 않은 볼륨을 모두 삭제
        - rm
          지정한 볼륨 삭제
      - network
        - connect
          컨테이너를 네트워크에 연결
        - disconnect
          컨테이너를 네트워크 연결에서 해제
        - create
          네트워크 생성
        - inspect
          상세 정보 출력
        - ls
          목록 출력
        - prune
          현재 컨테이너가 접속하지 않은 경우 삭제
        - rm
          네트워크 삭제
   5. 기타 상위 커맨드
      - checkpoint
      - node
      - plugin
      - secret
      - service
      - stack
      - swarm
      - system
   6. 단독으로 사용되는 명령어
      - login
      - logout
      - search
      - version
   7. docker run 명령
      - `docker image pull`, `docker container create`, `docker container start` 명령을 합친 것과 같은 명령
      - 기본 형식
        `docker run [옵션] 이미지 [인자]`
      - 옵션
        - --name
          컨테이너
        - -p
          포트 포워딩
        - -v
          볼륨 마운트
        - --net
          네트워크 연결
        - -d
          데몬으로 생성
        - -i
          컨테이너에 터미널을 연결
        - -t
          특수 키를 사용할 수 있도록 설정
          (d, i, t는 같이 사용하는 경우가 많아서 -dit로 설정하는 경우가 많음)
   8. 컨테이너 확인
      - `docker ps`
        실행 중인 컨테이너 확인
      - `docker ps -a`
        모든 컨테이너 확인
      - 출력되는 정보
        - CONTAINER ID
          컨테이너의 식별자로 64글자인데 12글자만 출력
        - IMAGE
          컨테이너 생성할 때 사용한 이미지 이름
        - COMMAND
          컨테이너 실행 시에 실행하도록 설정된 프로그램 이름
        - CREATED
          컨텡너 생성 후 경과한 시간
        - STATUS
          컨테이너의 현재 상테인데 실행 중이면 up, 실행 중이 아니면 exited
        - PORTS
          컨테이너에 할당된 포트 번호로 호스트포트번호 → 컨테이너 포트 번호 형식으로 출력되는데 동일한 포트 번호를 사용하면 → 뒤는 나오지 않음.
        - Names
          컨테이너 이름
   9. 컨테이너 중지
      - `docker stop 컨테이너ID 또는 이름`
      - `docker stop $(docker ps -a -q)`
        모든 컨테이너 중지
   10. 컨테이너 삭제
       - `docker rm 컨테이너ID 또는 이름`
         컨테이너가 실행 중이면 삭제되지 않음.
       - `docker rm $(docker ps -a -q)`
         모든 컨테이너 삭제
   11. Apache 컨테이너를 이용한 실습
       - Apache와 Nginx
         웹 서버를 만들어주는 소프트웨어
       - 이미지 이름은 httpd이고 컨테이너 이름은 apa000ex1
         이미지를 다운로드받고 컨테이너를 생성하고 실행 → 컨테이너 상태 확인 → 컨테이너 종료 → 컨테이너 상태 확인 → 컨테이너 삭제 → 컨테이너 상태 확인
       - 동일한 하위 명령어가 없다면 상위 명령어는 생략 가능
       - 생성하고 실행
         `docker container run --name apa000ex1 -d httpd`
       - 실행 확인
         `docker ps`
       - 컨테이너 중지
         `docker container stop apa000ex1`
       - 실행 확인
         `docker ps`
       - 삭제
         `docker container rm apa000ex1`
         rm의 경우도 container를 생략 가능
       - 확인
         `docker ps -a`
   12. **포트 포워딩**
       - 외부에서 컨테이너에 접근하기 위해서는 외부와 접속하기 위한 설정이 필요한데 이 때 필요한 설정이 포트포워딩이다.
       - apache는 80번 포트를 이용해서 외부와의 접속을 수행한다.
         80번 포트를 외부에서 접속할 수 있도록 외부 포트와 연결을 해주어야 한다.
       - 포트 포워딩 방법
         `-p 호스트포트번호(밖에서 접속할 포트번호):컨테이너 포트번호`
         **컨테이너 포트 번호는 중복될 수 있지만 호스트 포트 번호는 중복되면 안된다.**
   13. Apache 웹 서버를 컨테이너로 생성하고 외부에서 접속을 확인

       - 이미지 이름은 httpd
       - 컨테이너 이름은 apa000ex2
       - 실행

         ```bash
         // 생성하고 실행
         docker container run --name apa000ex2 -d -p 8080:80 httpd

         // 실행 확인
         docker ps

         // 외부에서 접속 localhost:8080

         // 컨테이너 중지
         docker container stop apa000ex2

         docker ps

         // 삭제
         docker conatiner rm apa000ex2

         // 확인
         docker ps -a

         ```

   14. 다양한 컨테이너
       - 리눅스 운영체제가 담긴 컨테이너
         - ubuntu
           -d 없이 it 옵션만으로 사용
         - debian
           -d 없이 it 옵션만으로 사용
         - centos
           -d 없이 it 옵션만으로 사용
         - fedora
           -d 없이 it 옵션만으로 사용
       - 웹 서버 및 데이터베이스 서버용 컨테이너
         - httpd
           apache 웹 서버, -d로 백그라운드 실행을 하고 -p로 포트 설정
         - nginx
           웹 서버로 -d로 백그라운드 실행을 하고 -p로 포트 설정
           여기서 WAS기능을 넣은게 tomcat
         - mysql
           -d를 사용하고 `-e MYSQL_ROOT_PASSWORD` 로 루트 비밀번호를 지정
         - mariadb
           -d를 사용하고 `-e MYSQL_ROOT_PASSWORD` 로 루트 비밀번호를 지정
         - postgres
         - oracle은 이미지 이름이 다양
       - 프로그래밍 언어
         - openjdk, python, php, ruby, perl, gcc, node
         - registry
           도커 레지스트리
         - wordpress
           블로그와 같은 웹 사이트 개발 툴
         - redmine
           todo와 유사한 형태의 일정 관리 툴
         - nextcloud
   15. 이미지 관련 명령
       - 이미지 삭제
         `docker image rm 이미지이름나열`
         컨테이너가 사용 중인 이미지는 삭제가 안됨.
       - 이미지 정보 확인
         `docker image ls`
   16. 우분투 컨테이너를 생성
       - 우분투 이미지를 조회
         `docker search ubuntu`
         `docker search --limit 10 ubuntu`
       - 컨테이너 생성
         `docker run --name ubuntu -it ubuntu`
         바로 shell에 접속이 된다.
   17. 오라클 컨테이너 사용
       - 오라클은 8080 포트와 1521(외부 접속을 위한 포트)번 포트를 사용
       - Mac의 M1 프로세서는 구동이 안됨 - 해결책이 나왔는데 Docker에 추가로 Colima를 설치하면 가능은 함. 근데 저는 맥이라서 안하겠습니다.ㅋㅋ
       - oracle 11g 컨테이너 생성
         `docker run --name oracle11g -d -p 8080:8080 -p 1521:1521 jaspeen/oracle-xe-11g`
       - 오라클은 외부 접속을 위해서 아무것도 하지 않아도 된다.
         다만 서비스 이름이 xe이고 관리자 계정의 비밀번호는 oracle이 된다.

4. **여러 개의 컨테이너를 하나의 네트워크로 묶어 연동**

   1. 워드프레스와 MySQL을 연동

      - 워드프레스
        - 웹 사이트를 만들기 위한 소프트웨어로 서버에 설치해서 사용
        - 워드프레스는 워드프레스 이외에 아파치, php 런타임 그리고 데이터베이스를 필요로 한다.
        - MariaDB와 MySQL을 지원
        - 워드프레스 이미지는 워드프레스 프로그램 본체와 아파치 그리고 php 런타임을 내장
        - 워드프레스를 구동시키기 위해서는 외부에 접속할 수 있는 데이터베이스가 있거나 데이터베이스 컨테이너가 존재해야 한다.
      - 도커 네트워크
        - 가상의 네트워크로 네트워크에 속한 컨테이너끼리 연결을 시켜서 서로 접속이 가능하도록 해주는 것
        - 생성
          `docker network create 이름`
        - 삭제
          `docker network rm 이름`
        - 목록
          `docker network ls 이름`
      - MySQL
        - 컨테이너 생성
          ```bash
          docker run
          --name 컨테이너이름 -dit
          --net=접속할네트워크이름
          -e MYSQL_ROOT_PASSWORD=관리자비밀번호
          -e MYSQL_DATABASE=데이터베이스이름
          -e MYSQL_USER=사용자이름
          -e MYSQL_PASSWORD=사용자비밀번호
          -p 호스트포트번호:3306 이미지이름
          --character-set-server=문자인코딩
          --collation-server=정렬순서
          --default-authentication-plugin=인증방식
          ```
        - -e 옵션은 환경변수
        - 관리자 비밀번호는 필수
        - 관리자만 있어도 모든 작업을 수행할 수 있지만 관리자 계정으로 데이터베이스를 사용하는 것은 보안상 문제가 발생할 소지가 있어서 사용자를 생성해서 사용하는 것을 권장
        - MySQL과 MariaDB에서는 `authentication-plugin` 이 다른 의미를 갖는데 MySQL 8.0 버전부터 보안을 위해서 인증 방식을 변경했는데 비밀번호를 해싱을 해서 보관을 하는 형태로 변경했다.
        - 이전 방식으로 데이터베이스 접속이 가능하도록 하고자 하는 경우 `--default-authentication-plugin` 에 `mysql_native_password` 를 설정해주면 된다.
          이 작업은 데이터베이스에서 관리자로 접속해서 변경하는 것도 가능하다.
      - Wordpress
        - 컨테이너 생성
          ```bash
          docker run
          --name 컨테이너이름 -dit
          --net=접속할네트워크이름
          -p 포트번호
          -e WORDPRESS_DB_HOST=데이터베이스컨테이너이름
          -e WORDPRESS_DB_NAME=데이터베이스이름
          -e WORDPRESS_DB_USER=데이터베이스사용자계정
          -e WORDPRESS_DB_PASSWORD=데이터베이스사용자비밀번호
          wordpress
          ```
        - 실행 확인
          http://localhost:포트번호
          데이터베이스 연결이 안되면 컨테이너는 생성되지만 실제 구동은 안됨.
      - 연동 수행

        - 네트워크 수행
          `docker network create wordpress000net1`
        - 컨테이너 생성
          ```bash
          docker run --name mysql000ex1 -dit
          --net=wordpress000net1
          -e MYSQL_ROOT_PASSWORD=myrootpass
          -e MYSQL_DATABASE=wordpress000db
          -e MYSQL_USER=wordpress000kun
          -e MYSQL_PASSWORD=wkunpass mysql
          --character-set-server=utf8mb4
          --collation-server=utf8mb4_unicode_ci
          --default-authentication-plugin=mysql_native_password
          ```
          ```bash
          docker run --name wordpress000ex2 -dit
          --net=wordpress000net1
          -p 8085:80
          -e WORDPRESS_DB_HOST=mysql000ex1
          -e WORDPRESS_DB_NAME=wordpress000db
          -e WORDPRESS_DB_USER=wordpress000kun
          -e WORDPRESS_DB_PASSWORD=wkunpass wordpress
          ```
        - 확인
          `localhost:8085`
        - 뒷정리

          ```bash
          // 정리는 역순으로 가야한다.
          docker stop wordpress000ex2
          docker stop mysql000ex1

          // 정리가 됐기 때문에 삭제 순서는 큰 상관은 없다.
          docker rm mysql000ex1
          docker rm wordpress000ex2
          docker network rm wordpress000net1
          ```

        - 확인
          `docker ps -a`
          `docker network ls`

   2. MySQL 컨테이너에서 유저 생성 및 접속 권한 부여

      - 외부에서 사용할 수 있도록 컨테이너 생성
        ```bash
        docker run --name mysql
        -e MYSQL_ROOT_PASSWORD=비밀번호
        -d -p 접속할포트번호:3306 mysql
        ```
      - 컨테이너 내부 접속
        `docker exec -it mysql bash`
      - bash에서 관리자로 접속
        `mysql -u root -p` 후 비밀번호 입력
      - 데이터베이스 사용

        ```sql
        create database gyumin;

        // 모든 곳에서 접속하게 하고자 할 때는 ip 대신에 % 사용
        // create user '유저아이디'@'접속할IP' identified by '비밀번호';
        create user 'user00'@'%' identified by 'user00';

        // 모든 데이터베이스에 접속 가능하도록 하고자 하는 경우는 이름 대신에 *.*을 설정
        // grant all privileges on 데이터베이스이름 to '유저아이디'@'접속할IP';
        grant all privileges on *.* to 'user00'@'%';

        // MYSQL 8.0은 인증 방식을 예전 방식으로 되돌려야만 외부에서 접속이 가능
        alter user 'user00'@'%' identified with mysql_native_password by 'user00';

        // user에 관한 것은 이 구문이 들어가야 commit이 된다.
        flush privileges;
        ```

        여기서 ‘%’를 쓰는 것은 공부할 때 뿐이고 실제로는 보통 IP 주소를 사용한다. 관리자(root)일 경우 local을 사용한다. 아무나 접근할 수 없도록 하기 때문.

   3. 레드 마인 및 Maria DB 컨테이너 결합

      - 레드마인
        웹 기반의 프로젝트 관리와 버그 추적 기능을 제공하는 도구
        프로젝트 관리에 도움이 되는 달력과 Gantt Chart를 제공하고 일정 관리 기능을 제공
      - Maria DB 컨테이너 생성
        이미지 이름이 mariadb일 뿐이고 컨테이너 생성 방법은 동일
      - 연동 수행

        - 네트워크 수행
          ```bash
          docker network create redmine000net1
          ```
        - MariaDB 컨테이너 생성
          ```bash
          docker run --name mariadb000ex1 -dit --net redmine000net1
          -e MYSQL_ROOT_PASSWORD=root
          -e MYSQL_DATABASE=redmine000db
          -e MYSQL_USER=redmine000kun
          -e MYSQL_PASSWORD=rkunpass mariadb
          --charcter-set-server=utf8mb4
          --collation-server=utf8mb4_unicode_ci
          --default-authentication-plugin=mysql_native_password
          ```
        - 레드마인 컨테이너 생성
          ```bash
          docker run -dit --name redmine000ex1 --net redmine000net1
          -p 8087:3000
          -e REDMINE_DB_MYSQL=mariadb000ex1
          -e REDMINE_DB_DATABASE=redminde000db
          -e REDMINE_DB_USERNAME=redmine000kun
          -e REDMINE_DB_PASSWORD=rkunpass redmine
          ```
        - 확인
          localhost:8087
        - 뒷정리

          ```bash
          docker stop redmine000ex1
          docker stop maraidb000ex1

          docker container rm redmine000ex1
          docker container rm mariadb000ex1

          docker network rm redmine000net1
          ```

5. **컨테이너와 호스트 간에 파일 복사**

   - 프로그램만으로 구성된 시스템은 그리 많지 않음.
   - 실제 시스템은 프로그램, 프로그래밍 언어의 런타임, 웹 서버, 데이터베이스 등이 함께 시스템을 구성하고 그 이외도 파일 데이터가 포함되기도 한다.
     apache의 경우 내부적으로 index.html을 소유하고 있다가 웹에서 호출을 하면 index.html을 출력한다.
     데이터베이스들도 백업을 하면 자신의 특정한 디렉토리 백업 파일을 보관한다. 이런 파일들을 호스트에게 전송할 수 있고 호스트에게 받아서 사용할 수도 있다.

   1. 명령
      - 호스트에서 컨테이너로 복사
        `docker cp 호스트경로 컨테이너이름:컨테이너 경로`
      - 컨테이너에서 호스트로 복사
        `docker ps 컨테이너이름:컨네이너경로 호스트경로`
   2. apache의 index.html 파일의 위치
      - /usr/local/apache2/htdoc
   3. 호스트 컴퓨터의 index.html 파일을 apache 컨테이너의 index.html 파일로 사용
      - apache 컨테이너를 생성
        `docker run --name apa000ex1 -d -p 8089:80 httpd`
      - 웹 브라우저에서 확인
        localhost:8080
      - index.html 파일을 로컬에 생성
      - 호스트의 파일을 컨테이너에 복사
        `docker cp 파일경로 apa000ex1:/usr/local/apache2/htdocs`
      - 브라우저에서 다시 접속 - 새로고침
   4. 컨테이너의 파일을 호스트로 복사

      `docker cp apa000ex1:/usr/local/apache2/index.html 호스트의디렉토리경로`

6. **스토리지 마운트 - 볼륨 마운트**

   1. 용어
      - Volume
        스토리지의 한 영역을 분할한 것
      - Mount
        연결을 해서 운영체제나 소프트웨어가 관리를 할 수 있도록 하는 것.
   2. 스토리지 마운트 이유
      - 컨테이너 안에 만들어진 데이터는 컨테이너가 소멸되면 같이 사라지게 되는데 이 떄 데이터는 남겨서 다른 컨테이너나 호스트에서 사용하고자 하는 경우에 직접 복사를 해도 되지만 이런 복사 작업은 수정을 할 때마다 수행을 해야하기 때문에 번거롭다.
      - 데이터를 다른 외부 장치와 연결해서 컨테이너와 독립적으로 유지시키는 것을 Data Persistency라고 하는데 이를 위해서 스토리지 마운트를 수행한다.
   3. 마운트 방식
      - 볼륨 마운트
        도커 엔진이 관리하는 영역 내에 만들어진 볼륨을 컨테이너에 디스크 형태로 제공하는 것
      - 바인드 마운트
        도커 엔진이 설치된 컴퓨터의 디렉토리와 연결하는 방식
        | | 볼륨 마운트 | 바인드 마운트 |
        | ------------- | ----------------------- | ---------------------- |
        | 스토리지 영역 | 볼륨 | 디렉토리 |
        | 물리적 위치 | 도커 엔진의 관리 영역 | 어디든지 가능 |
        | 마운트 절차 | 볼륨을 생성한 후 마운트 | 기존 파일이나 디렉토리 |
        | 내용 편집 | 도커 컨테이너 이용 | 연관된 프로그램 |
        | 백업 | 복잡 | 일반 파일과 동일 방식 |
   4. 마운트 명령어
      - docker container run을 할 때 옵션의 형태로 제공
      - 스토리지의 경로가 컨테이너 속 특정 경로와 연결되도록 설정
      - 컨테이너의 경로는 도커 이미지의 문서를 참조해서 확인
        apache의 경우는 /usr/local/apache2/htdocs 이고 mysql의 경우는 /var/lib/mysql
   5. 바인드 마운트 실습
      - 바인드할 디렉토리를 결정 - /Users/gimgyumin/Desktop
      - apache 컨테이너 생성
        ```bash
        docker run --name apa000ex2 -d -p 8090:80
        -v /Users/gimgyumin/Desktop:/usr/local/apache2/htdocs httpd
        ```
      - 웹 브라우저에서 [localhost:8090](http://localhost:8090) 으로 접속을 하면 ‘It Works’가 출력되지 않고 ‘Index of/’ 가 출력됨.
      - 바인드된 디렉토리에 index.html을 복사하고 웹 브라우저에서 새로고침 수행
   6. 볼륨 마운트(잘 사용하지는 않음)

      - 기본 명령
        - 볼륨 생성
          `docker volume create 볼륨이름`
        - 볼륨 상세 정보 확인
          `docker volume inspect 볼륨이름`
        - 볼륨 삭제
          `docker volume rm 볼륨이름`
      - 실습

        ```bash
        docker volume create apa000vol1

        // 볼륨 확인
        docker volume ls

        docker volume insepct apa000vol1

        // apache 컨테이너 생성 및 볼륨연결
        docker run --name apa000ex3 -d -p 8091:80
        -v apa000vol1:/usr/local/apache2/htdocs httpd

        docker container inspect apa000ex3
        // 혹은 docker volume inspect apa000vo1
        ```

7. **컨테이너로 이미지 만들기**

   다른 개발자와 동일한 개발 환경을 만들기 위해서 주로 이용

   1. 이미지를 만드는 방법
      - commit 커맨드로 컨테이너를 이미지로 변환하는 방법
      - Dockerfile 스크립트로 이미지를 만드는 방법
   2. commit 명령으로 이미지 만들기
      - 기본 형식
        `docker commit 컨테이너이름 이미지이름`
   3. Dockerfile 스크립트로 이미지 만들기
      - 스크립트 파일에 명령어를 기재하고 이 파일을 빌드해서 만드는 방식
      - 빌드 명령어
        `docker build -t 이미지이름 스크립트파일의디렉토리경로`
      - 스크립트 명령어
        - FROM
          토대가 되는 이미지를 지정
        - ADD
          이미지에 파일이나 폴더를 추가
        - COPY
          이미지에 파일이나 폴더를 추가
        - RUN
          이미지를 빌드할 때 실행할 명령어를 지정
        - CMD
          컨테이너를 실행할 때 실행할 명령어를 지정
   4. commit 명령으로 apache 컨테이너를 이미지로 생성
      - 현재 이미지를 확인
        `docker image ls`
      - apache 컨테이너 생성
        ```bash
        docker run --name apa000ex4 -d -p 8091:80 httpd
        ```
      - index.html을 생성된 컨테이너에 복사
        ```bash
        // docker cp 파일경로 apa000ex4:/usr/local/apache2/htdocs/
        docker cp /Users/gimgyumin/Desktop apa000ex4:/usr/local/apache2/htdocs/
        ```
      - 컨테이너를 이미지로 변환
        ```bash
        // docker commit apa000ex4 이미지이름
        docker commit apa000ex4 ex4_copy
        ```
      - 이미지 목록 확인
        ```bash
        docker image ls
        ```
      - 새로만든 이미지를 기반으로 컨테이너 생성
        ```bash
        docker run --name apa000ex5 -d -p 8093:80 ex5_copy
        ```
   5. Dockerfile 스크립트로 이미지 만들기
      - 컨테이너에 추가할 파일과 스크립트가 저장될 파일을 위한 디렉토리를 결정
      - 컨테이너와 함께 저장될 파일을 위의 디렉토리에 복사
      - Dockerfile 파일을 작성 - 메모장에서 작성하고 확장자를 제거
        ```
        // Dockerfile.txt가 아닌 Dockerfile로 저장
        FROM httpd
        COPY index.html /usr/local/apache2/htdocs
        ```
      - Dockerfile 파일을 빌드해서 이미지를 생성
        `docker build -t 이미지이름 Dockerfile경로`
      - 이미지 생성 여부 확인
        `docker image ls`

8. **컨테이너 개조**

   도커를 실제 운용하는 현업에서는 사내에서 개발한 시스템을 사용하는 경우가 많음.

   기본 이미지를 커스터마이징한 이미지를 많이 사용

   1. 컨테이너 개조
      - 파일 복사와 마운트를 이용하는 방법
      - 컨테이너에서 리눅스 명령어를 실행해서 소프트웨어를 설치하거나 설정을 변경하는 방법
        리눅스 명령어를 실행하려면 셸에서 작업을 수행해야 한다.
        셸 중에서는 bash 셸을 많이 사용한다.
      - 컨테이너를 만들 때 옵션 없이 생성하면 셸에 들어갈 수 없는 경우가 발생
      - bash 셸을 사용할 수 있는 상태를 만들고자 하는 경우
        `docker exex 옵션 컨테이너이름 /bin/bash`
        `docker exex 옵션 이미지이름 /bin/bash` (이 방법은 잘 안쓰는데 컨테이너가 실행되지 않고 bash가 실행됨)
   2. bash 명령
      - 대부분의 경우 debian 계열의 리눅스에서 이미지를 생성

9. **도커 허브**

   1. 도커 허브
      - 이미지를 내려받고 올려받을 수 있는 도커 제작사에서 운영하는 공식 도커 레지스트리
      - 오픈 소스 재단의 많은 개발자들이 도커 허브에 참여
   2. 레지스트리와 레포지토리
      - 레지스트리는 배포하는 장소
      - 레포지토리는 레지스트리를 구성하는 소프트웨어
   3. 이미지 이름과 태그
      - 도커 허브에 공개로 이미지를 업로드하는 경우는 이미지에 태그를 부여해야 함
      - 태그 형식
        `레지트스리주소/레포지토리이름:버전`
      - 이미지에 태그를 부여하기
        처음부터 이미지 이름을 태그 형식으로 만들어서 사용
        `docker tag 원본이미지이름 태그`
   4. 도커 허브에 업로드

      `docker push 태그`
