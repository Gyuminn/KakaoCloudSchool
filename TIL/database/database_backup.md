# [Database] MariaDB & MySQL 백업 및 복원

1. **백업**

   docker에 있는 데이터베이스 백업 - 컨테이너 이름 확인(mariadb)

   컨네이너의 bash로 진입

   ```bash
   docker exec -it 컨테이너이름(mariadb) bash

   // mysqldump -h127.0.0.1 -p(루트비밀번호) 데이터베이스이름 > /tmp/파일명.sql
   mysqldump -h127.0.0.1 -proot gyumin > /tmp/gyumin.sql

   // 확인
   ls -al /tmp

   // 빠져나가기
   exit

   // 파일을 로컬에 복사
   // docker cp 컨테이너ID(mariadb):/tmp/파일명.sql 복사할디렉토리경로
   docker cp mariadb:/tmp/gyumin.sql /Users/gimgyumin/Downloads
   ```

2. **복원**

   ```bash
   // 컨테이너 만들기
   docker run --name mariadb -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mariadb:latest

   // 실행 중인 컨테이너의 로컬 파일을 복사
   // docker cp 복사할파일경로 컨테이너ID:/tmp
   docker cp /Users/gimgyumin/Downloads/gyumin.sql mariadb:/tmp

   // docker에서 bash shell로 진입
   docker exec -it mariadb bash

   // 복사된 파일 확인
   ls -al /tmp

   // 백업
   // mysql -h127.0.0.1 -p루트비밀번호 데이터베이스이름 < /tmp/파일명
   mysql -h127.0.0.1 -proot gyummin < /tmp/gyumin.sql

   // gyumin이라는 데이터베이스가 존재하지 않을 수 있으므로 아래와 같이 해보기도 한다.
   mysql -h127.0.0.1 -proot mysql < /tmp/gyumin.sql
   ```
