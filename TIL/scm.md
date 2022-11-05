## 소스 코드 버전 관리

1. **형상 관리(Configuration Management)**

   변경 사항 관리

   소스 코드 뿐 아니라 문서 등 개발에 사용된 모든 것들을 관리

1. **소스 코드 버전 관리**

   git: 소스 코드 버전 관리하는 소프트웨어 중 가장 잘 알려져있는 프로그램

   github: 소스 코드 버전 관리를 git을 이용해서 원격 저장소에서 사용할 수 있도록 해주는 서비스

1. **git hub 사용 준비**

   - git 관리 프로그램 설치 - [https://www.git-scm.com에서](https://www.git-scm.com에서) 다운로드 받기
   - cmd에서 명령어를 수행
     - git config —global [user.name](http://user.name) 이름
     - git config —global [user.email](http://user.email) 이메일
     - 확인방법 git config —list

1. **VSCode 프로젝트를 git hub에 업로드**
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
