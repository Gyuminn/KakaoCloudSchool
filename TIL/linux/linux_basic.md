# [Linux] Basic

1. **GNU 프로젝트**
   - 유닉스와 호환되는 자유 소프트웨어를 개발하는 프로젝트
   - GNU is Not Unix: Unix의 상용화 반대
   - GPL 라이선스 반대
2. **Linux**

   Git을 이용해서 커널의 소스 코드를 공개

   리눅스 = 리누스 + 유닉스

   1. 리눅스의 종류
      - Debian → Ubuntu
      - Slackware → Open SUSE
      - Redhat → Fedora, Cent OS
   2. 리눅스 구조

      - Kernel: 핵심 구조, 관리
      - Shell: 명령어 인터페이스
      - Utility: 기타 응용 프로그램

      사용자 ↔ Shell ↔ Kernel ↔ H/W

   3. Ubuntu
      - 데비안 계열에서 가장 성공한 데스크탑 배포판
      - GNOM이라는 GUI 환경을 제공
      - 현재는 구글이 가장 많이 사용
   4. 설치
      - 컴퓨터에 직접 설치: 로컬 컴퓨터나 렌탈 환경(클라우드)에 설치
      - 가상 머신 이용
      - 도커와 같은 컨테이너 이용: 리눅스를 학습할 때는 비추천
   5. 가상 머신
      - 현재 운영체제에 가상의 컴퓨터를 생성한 후 여기에 운영체제를 설치할 수 있도록 해주는 소프트웨어
      - 종류
        - VMWare
        - Virtual PC
        - Virtual Box - Oracle에서 제공
      - Virtual Box 다운로드 - virtualbox.org/wiki/Downloads
   6. 가상 머신에 리눅스 설치
      - 리눅스 이미지가 필요
      - 우분투 리눅스 이미지 다운로드: https://ubuntu.com/#download
        LTS 버전이 안정화된 버전이므로 이를 선택.
        처음에는 Desktop 버전을 많이 다운 받는다.

3. **Editor**
   1. Text Editor - gedit
   2. vi 편집기
      - `vi 파일명`
        파일이 없으면 생성하고 있으면 수정
      - 모드
        - 명령모드: 입력 모드로 전환할 때는 i, I, a, A, o, O를 누르면 가능
        - 입력모드: 명령 모드로 전환할 때는 esc
        - 라인모드
      - 작업을 수행한 후 종료
        - `:q!` → 저장하지 않고 종료
        - `:wq!` → 저장하고 종료
4. **Process**
   1. Process
      - 실행 중인 프로그램
   2. 프로세스 번호와 작업 번호
      - 프로세스 번호는 프로세스를 구분하기 위한 번호
      - 작업 번호는 백그라운드 프로세스가 CPU를 점유해서 실행될 때의 순서에 관련된 번호
   3. Daemon Process
      - 다른 서비스를 제공하기 위해 존재하는 프로세스
      - 평상시에는 대기 상태로 존재하다가 서비스 요청이 오면 해당 서비스를 제공
   4. 부모 자식 관계의 프로세스
      - 부모 프로세스가 종료되면 자식 프로세스도 종료
      - 고아 프로세스
        자식 프로세스가 종료되지 않은 상태에서 부모 프로세스가 종료되는 경우
      - 좀피 프로세스
        자식 프로세스가 정상적인 종료가 되지 않은 상태에서 부모 프로세스가 종료되는 경우
   5. ps 명령어
      - 프로세스 목록 출력
   6. kill 명령
      - 프로세스 종료
      - 비슷한 명령으로 pkill이 있는데 둘의 차이는 kill은 pid로 종료하고 pkill은 프로세스 이름으로 종료
5. **패키지 관련 명령**
   1. apt 명령
      - Advanced Pakage
      - …
   2. apt-cache
      - 패키지와 관련된 …
      - 기본 형식
        apt-cache [옵션] [하위 명령]
      - 옵션
        - `-f` → 전체 정보 출력
        - `-h` → 도움말 출력
      - 하위 명령
        - `stats` → 통계
        - `dump` → 패키지 업그레이드
        - `pkgnames` → 사용 가능한 패키지 이름 출력
        - `search [키워드]` → 검색
        - `show [패키지이름]` → 패키지 정보 출력
        - `show` → …
   3. apt-get
      - 패키지 설치 및 업데이트
      - 기본 형식
        `apt-get [옵션] [하위 명령]`
      - 옵션
        - `-d` → 다운로드
        - `-f` → 깨진 패키지 수정
        - `-h` → 도움
      - 서브 명령
        - `update` → 새로운 패키지 정보를 가져오기
        - `upgrade` → 모든 패키지를 최신 버전으로 업그레이드
        - `install 패키지이름` → 설치
        - `remove 패키지이름` → 제거
        - `purge 패키지이름` → 패키지와 설정 파일 모두 제거
        - `autoremove` → 시스템에 설치된 패키지를 자동으로 정리 및 삭제
        - `autoclean` → 오래된 패키지와 불완전한 다운로드 패키지 제거
        - `check` → 의존성이 깨진 패키지 확인
        - `clean` → 캐싱되어있는 패키지 삭제
6. **gcc 컴파일러**
   - C 컴파일러로 리눅스나 유닉스에서는 제공
   - C 프로그래머
     Visual C++(MS-C)
   - ANSI-C 프로그래머
     gcc 컴파일러를 가지고 프로그래밍
7. **Shell Script**

   1. Shell의 기능

      사용자와 커널 사이에서 중계자 역할을 수행하는 프로그램

   2. 종류
      - bourne shell
        처리 속도가 빨라서 초창기에 많이 이용하고 sh 명령을 사용하는 경우가 bourne shell
      - C shell
        bourne shell의 기능이 확장된 것으로 작성 형식이 C 언어와 유사해서 붙여진 이름이며 여기서는 csh라는 명령을 사용하는데 처리 속도가 느린 편
      - Korn shell
        horune shell과 호환성을 제공하고 처리속도가 빠른 편이며 우분투에서는 별도로 설치해서 사용하면 ksh 명령을 사용
      - **bash shell**
        앞에 나온 3가지 shell의 호환성을 유지하면서 기능을 강화한 shell로 우분투 리눅스의 기본 셸이면서 bash 명령을 사용하는데 우분투 6.10 이상은 dash shell이 기본 shell
      - dash shell
        bash shell의 크기를 줄여서 빠르게 실행하도록 만든 shell
   3. 자신의 기본 셀 확인
      - `ls -l /bin/sh`
   4. 셸에서 환경 변수
      - 변수명 앞에 $를 붙여야 한다.
      - 환경 변수의 이름은 대문자로 하는 것이 관례
      - HOME(사용자의 홈 디렉토리) 이라는 환경 변수의 값을 확인: `echo $HOME`
      - bash의 버전 확인이라는 환경 변수의 값을 확인: `echo $BASH_VERSION`
   5. 변수 생성
      - `이름=문자열` 의 형태로 작성, 공백을 포함하면 안됨.
      - 출력할 때는 echo 명령 이용
   6. 셸 변수와 환경 변수 사이의 전환
      - `export [옵션] [셸 변수]`
      - 옵션으로 `-n` 을 설정하면 환경 변수를 셸 변수로 전환
      - `export 셸변수=문자열` 의 형태로 설정하는 것도 가능
      - 환경 변수 확인 명령은 `env`
   7. 셸 변수 삭제
      - `unset 변수이름`
   8. Shell Script
      - 셸이나 커맨드라인 인터프리터에서 수행하도록 작성된 운영체제를 위한 스크립트
      - 장점은 다른 프로그래밍 언어에서 작성된 코드보다 빠르게 처리된다는 것
      - 단점은 고품질의 코드와 확장을 기록하기는 힘들다는 것.
   9. 셸 스크립트 작성 및 실행

      - 현재 디렉토리에 셸 스크립트 파일을 생성하고 작성 - myname.sh

        ```bash
        #! /bin/sh

        echo "Name Print"
        echo ">> Connect Name:" $USERNAME

        exit 0
        ```

      - 실행은 `sh myname.sh`
      - 실행을 할 때 다른 디렉토리에 존재하면 실행 오류가 발생
        이런 경우에는 sh 파일을 현재 디렉토리로 옮기거나 `chmod +x 스크립트파일명` 명령을 이용해서 실행 가능 속성을 추가해주면 됨.

   10. 키보드 입력

       - `read 변수명`
       - 스크립트 파일 생성
         ```bash
         sudo gedit inout.sh
         ```
       - 작성

         ```bash
         echo "Input: "
         read input_string
         echo "Output: $input_string "

         exit 0
         ```

   11. 산술 연산
       - 백틱 다음에 expr을 입력하고 산술 여행을 수행하고 백틱으로 막으면 된다.
   12. 조건문

       ```bash
       if [조건식]
       then
       	조건식이 참일 경우 수행할 내용
       fi
       ```

       - 같다는 `=`
       - `[ ]`에서는 뒤와 앞에 공백이 있어야 하고 `=`도 좌우에 공백이 있어야 한다.

       ```bash
       if [조건식]
       then
       	조건식이 참일 경우 수행할 내용
       else
       	조건식이 거짓일 경우 수행할 내용
       fi
       ```

   13. 관계 연산자
       - 문자열 비교
         =, !=, -n(NULL이 아닌 값), -z(NULL 값)
       - 산술 비교
         -eq, -ne, -gt, -ge, -lt, -le, !
   14. case ~ esac

       - if 구문은 조건이 많아지면 구문이 복잡해지는데 case ~ esac 구문은 여러 개의 조건을 펼쳐놓고 어느 조건에 해당하는지를 판별해서 명령을 수행하기 때문에 if ~ else에 비해서 간결하고 이해하기 쉬움
         ```bash
         case 파라미터 또는 키보드입력값 in
         	조건1)
         	조건1에 해당할 경우 실행할 명령
         	조건2)
         	조건2에 해당할 경우 실행할 명령
         	...
         	*)
         	앞에서 주어진 조건 이외의 모든 경우 실행할 명령
         esac
         ```
       - 작성

         ```bash
         #! /bin/sh
         echo " >> season choice: Spring / Summer / Fall / Window"
         case "$1" in
         	Spring)
         	echo ">> choice: Spring ";;
         	Summer)
         	echo ">> choice: Summer ";;
         	Fall)
         	echo ">> choice: Fall ";;
         	Winter)
         	echo ">> choice: Winter ";;
         	*)
         	echo "No Choice ";;
         esac

         exit 0
         ```

       - 실행
         `sh 파일명 Spring(파라미터)`

   15. and와 or
       - and: `-a` 또는 `&&`
       - or: `-o` 또는 `||`
   16. 반복문

       - for
         ```bash
         for 임시변수 in [범위](리스트 또는 배열, 묶음 등)
         do
         	반복 수행할 내용
         done
         ```
       - 작성

         ```bash
         #! /bin/sh
         cnt=0
         for num in 1 2 3 4 5
         do
         	echo " >> No.$cnt : $num "
         	cnt = `expr $cnt + 1`
         done

         exit 0
         ```

       - seq
         범위를 만들 때 사용하는 것으로 `seq [시작값][종료값]`
         범위 대신에 디렉토리 이름도 가능
       - while
         ```bash
         while [조건식]
         do
         	조건식이 true이면 반복 수행할 내용
         done
         ```
       - until
         ```bash
         until [조건식]
         do
         	조건식이 false이면 반복 수행할 내용
         done
         ```

   17. 기타 명령어
       - break
         반복문 중단
       - continue
         조건식으로 이동
       - exit
         프로그램 종료 명령어로 정수를 하나 같이 전달한다. 이 정수는 운영체제에게 종료된 이유를 설명하기 위한 숫자이다.
       - return
         함수의 수행을 종료하고 호출한 곳으로 돌아가는 명령어
   18. 함수와 파라미터

       - function
         자주 사용되는 코드를 하나의 이름으로 묶어서 사용하기 위한 개념
       - 함수의 사용 이유
         - 한 번에 수행되어야 하는 코드가 너무 길어서 분할 - 모듈화
         - 동일한 코드를 자주 호출하기 때문
       - 작성

         ```bash
         // 선언
         함수이름(){
         	함수내용
         	$1 // 첫 번쨰 파라미터
         	$2 // 두 번쨰 파라미터
         }

         // 함수 호출
         함수이름 파라미터1 파라미터2...
         ```

8. **네트워크**

   1. 네트워크 인터페이스 설정 확인

      `ifconfig 인터페이스이름 옵션 값`

      - 옵션 정도만 씀: `-a`

   2. 주소 체제
      - IPv4: 32비트 주소 체제 - 8bit씩 묶어서 표현
        **127.0.0.1** → \*\*\*\*Loopback 주소
      - IPv6: 128비트 주소 체제 - 4bit씩 묶고 다시 4개씩 묶어서 표현
        **0::0::0::0::0::0::0::1** → Loopback 주소
   3. 용어
      - netmask
        하나의 그룹을 만들기 위한 영역을 설정하는 주소
        1과 0으로 구성되는데 1인 부분이 같으면 동일한 네트워크로 간주한다.
      - broadcast
        방송을 위한 주소
        네트워크의 모든 구성 요소에게 데이터를 전달할 때 사용
      - **gateway**
        다른 통신망과 원활한 접속을 유지하기 위해 사용되는 네트워크의 포인트
        밖으로 나갈 때 여기를 통해 나간다.
   4. **ping**
      - `ping IP주소나 URL`
        메시지를 전송하고 돌려받음. 연결 상태를 확인하는 것.
   5. DNS(Domain Name Service)
      - IP 주소와 도메인 사이의 변환을 위한 서비스
   6. nslookup
      - `nslookup 호스트네임 또는 DNS`
      - 호스트 네임이나 DNS 서버가 제대로 동작중인지 확인

9. **Telnet**

   1. 텔넷

      원격지에 있는 서버에 접속하는 프로그램

   2. 텔넷 서버 생성
      - 텔넷 서버 설정을 위한 패키지를 설치
        `sudo apt-get install xinetd`
        `sudo apt-get install telnetd`
      - 환경 설정 파일을 열어서 추가
        `gedit /etc/xinedt.conf`
        ```bash
        service telnet
        {
        	disable = no
        	flags = REUSE
        	socket_type = stream
        	wait = no
        	user = root
        	server = /usr/sbin/in.telnetd
        	log_on_failure += USERID
        }
        ```
      - 데몬으로 동작
        `sudo /etc/init.d/xinetd restart`
   3. 텔넷 서버 접속
      - 자신의 컴퓨터에 접속
        `telnet 0`
      - 다른 컴퓨터에 접속
        ```bash
        telnet
        open IP주소
        ```
   4. **Open SSH**
      - 텔넷은 서버에 접속할 수 있는 대표적인 방법이기는 하지만 데이터가 암호화되지 않은 상태로 전송되기 때문에 보안에 취약해서 패킷 캡쳐 프로그램을 이용하면 데이터를 전부 확인할 수 있다.
      - SSH(Secure Shell)
        - 공개키 방식의 암호화 방식을 이용해서 원격지 시스템에 접근해서 암호화된 메시지를 전송할 수 있는 시스템
        - 가장 많이 사용되는 SSH 프로그램이 PuTTY 이다.
        - PuTTY를 이용해서 리눅스 서버에 접근하기 위해서는 23번 포트를 열어주어야 한다.
          ```bash
          sudo iptables -A INPUT -p tcp --dport23 -j ACCEPT
          sudo iptables -save
          // 데몬 재실행
          sudo /etc/init.d/xinetd restart
          ```

10. **MariaDB 사용**

    1. 설치

       `sudo apt-get update`

       `sudo apt-get install mariadb-server`

    2. maria db 재시작

       `systemctl status mariadb.service` 혹은 `sudo systemctl start mysql`

11. **Web Server 생성**

    1. apache2 패키지 설치

       `sudo apt-get install apache2`

    2. 서비스 실행

       `systemctl status apache2`

       `sudo service apache2 start`

    3. 서비스 확인

       `ps -ef | grep apache`

    4. 브라우저에서 확인

       http:://ip 주소나 localhost

    5. 방화벽 설정
       - 현재 컴퓨터가 아닌 다른 컴퓨터에서 접속이 안되는 경우에는 방화벽에서 80번 포트를 제외를 해 주어야 한다.
       - 방화벽 실행
         `sudo ufw enable`
       - 포트 허용
         `sudo ufw allow 80/tcp`
       - 확인
         `sudo ufw status`
       - 서비스 재시작
         `systemctl status apache2`
         `sudo service apache2 start`
