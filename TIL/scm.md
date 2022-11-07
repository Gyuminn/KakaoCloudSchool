1. **소스 코드 버전 관리**

   git: 소스 코드 버전 관리하는 소프트웨어 중 가장 잘 알려져있는 프로그램

   github: 소스 코드 버전 관리를 git을 이용해서 원격 저장소에서 사용할 수 있도록 해주는 서비스

2. **git hub 사용 준비**

   - git 관리 프로그램 설치 - [https://www.git-scm.com에서](https://www.git-scm.com에서) 다운로드 받기
   - cmd에서 명령어를 수행
     - git config —global [user.name](http://user.name) 이름
     - git config —global [user.email](http://user.email) 이메일
     - 확인방법 git config —list

3. **VSCode 프로젝트를 git hub에 업로드**
   1. git hub에 접속해서 repository 생성하고 url 복사
   2. VSCode에서 initial repository 아이콘을 클릭 → 소스 코드를 git 명령으로 관리하겠다는 선언
   3. 업로드나 업데이트할 파일을 선택

      파일들을 보면 U가 표시된 파일들이 있고 이 파일들이 수정된 파일이다. 수정된 파일들을 선택하고 +를 누르면 업데이트 할 준비를 한다.

   4. 명령어로 로컬 프로젝트와 저장소를 연결 - 1번만 수행

      git remode add 브랜치 이름 githubURL → branch 저장소 내의 변경점을 의미한다. 하나의 저장소 여러 개의 버전을 저장하고 싶을 때 브랜치 이름을 변경하면 된다. 기본은 origin으로 적는다.

   5. 로컬에 반영

      상자에 메시지를 작성하고 commit을 누르면 된다.

   6. 게시

      다음 화면에서 게시를 누르거나 터미널에서 git push origin main을 수행

   7. 용어
      - repository: 저장소
      - branch: 저장소 내의 스냅샷을 보관하는 작은 저장소
      - commit: 로컬에 반영
      - push: 서버에 반영
      - clone: 복제본을 가져오는 것 - git clone URL을 입력
      - pull: 변경된 내용을 가져오는 것 - git clone 브랜치이름
   8. 에러 해결(Git hub의 기본 branch 이름(main)과 Git의 기본 branch의 이름(master)이 달라서 발생하는 문제를 해결해서 main에 업로드 하기)
      1. Git Hub repository를 생성하고 URL을 복사

         [readme.md](http://readme.md) 파일을 생성한 경우와 그렇지 않은 경우 중간 작업이 조금 다름.

      2. 현재 프로젝트를 로컬 git과 연결
         - git init
      3. 변경된 내용을 로컬 git에 반영
         - git add .: u가 있는 파일을 선택하고 +를 눌러도 된다.
         - git commit -m “메시지”
      4. branch(저장점 - 변경된 내용을 별도로 관리하고자 할 때) 작업

         git hub는 main이라는 기본 branch 이름을 가지고 있고 git은 master라는 저장점을 가지고 있어서 문제가 발생.

         - git branch: 저장점 확인
         - git branch -m 현재브랜치이름 변경할브랜치이름: 저장점 이름 변경
         - git branch checkout 브랜치이름: 저장점 전환
         - git branch -d 브랜치이름: 저장점 삭제

         따라서 `git branch -m master main` 으로 master를 main으로 변경한다.

      5. 원격 저장소 연결
         - git remote add 이름 URL: 이름은 URL 별로 다른 이름을 사용해야 하는데 많이 사용하는 이름이 origin이다.
         - git remote -v: 확인
         - git remote rm 이름: 연결 해제
      6. pull - 서버에서 변경된 내용을 다운로드 받아서 반영.

         `readme.md를 만든 경우에는 다음 작업을 한 번 수행`

         - git config pull.rebase false
         - git config pull.rebase true
         - git config pull.ff only
         - git pull 이름 브랜치이름 - git pull origin main

      7. 업로드
         - git push 이름 브랜치이름: git push origin main
4. **Repository를 복사할 경우**
   1. git clone URL
5. **변경된 내용을 업로드**
   1. git add .
   2. git commit -m “메시지”
   3. git push 이름 브랜치이름
6. **서버에서 변경된 내용을 반영**
   1. git pull 이름 브랜치이름
