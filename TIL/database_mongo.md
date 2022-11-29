# [Database] MongoDB

1. **개요**

   도큐먼트 지향 No SQL

   JSON 형식의 BSON이라는 데이터 구조를 사용

   샤딩(데이터를 나누어서 저장하는 기술)과 복제를 지원

2. **설치 및 접속**

   1. Server 설치
      - windows
        mongodb 사이트에서 다운로드 받아서 설치
      - mac(운영체제 업데이트를 한 경우 `xcode-select --install` 수행 후 설치)
        `brew tap mongodb/brew`
        `brew install mongodb-community`
      - docker
        `docker pull mongo`
        `docker run --name mongodb -d -p 27017:27017 mongo`
   2. Server 실행 - 포트 설정을 하지 않으면 27017번 포트를 사용
      - windows
        service에 등록된 경우에는 service에서 실행과 중지할 수 있음.
        cmd 창에서 mongod —dbpath 데이터 저장 경로 명령으로 실행 가능
        mongod 명령은 mongodb 설치 경로에 존재
      - Mac
        brew services start mongodb-community
        brew services stop mongodb-community
      - docker
        `docker run --name 컨테이너이름(ex:mongodb) -d -p 27017:27017 mongo`
   3. 외부 접속 허용

      mongod.conf 파일이나 mongod.cfg 파일을 수정

      - windows
        Mongo DB 설치 경로의 bin 디렉토리에 파일이 존재
      - mac
        /usr/local/var/log/mongodb/mongo.log

      bindIp 부분을 0.0.0.0 으로 설정하면 모든 곳에서 접속이 가능. #으로 시작하면 주석이 된다.

      windows는 설정 파일을 변경하고 서버를 실행할 때 `mongod --dbpath 데이터저장경로 --bind_ip 0.0.0.0` 으로 서버를 실행해야 한다.

   4. 접속 프로그램

      compass: windows에서는 mongodb를 설치할 때 같이 설치

      mac에서는 https://www.mongodb.com/try/download/compass

3. **Mongo DB 구성 요소**

   database > collection > document

   관계형 데이터베이스(RDBMS)와의 비교

   - Database - Database
   - Table(Relation) - Collection
   - Row(Record, Tuple) - Document
   - Column(Attribute) - Field
   - Index - Index
   - Join - Embedding & Linking

   조회의 결과가 관계형 데이터베이스는 Row의 집합인데 Mongo DB는 cursor

4. **Mongo DB CRUD**

   JSON 형식

   객체: {”속성 이름” : 값, “속성 이름” : 값…}

   배열: [데이터, 데이터, …]

   값에 문자열, 숫자, boolean, 날짜, null이 올 수 있고 다른 객체나 배열이 올 수 있다.

   데이터도 모든 종류의 값이 모두 올 수 있다.

   1. 데이터베이스 작업

      Mongo DB에서 가장 큰 저장소의 개념

      확인: show dbs;

      생성은 create로 할 수 있지만 `use 데이터베이스이름` 을 사용하면 가능.

      데이터베이스를 생성하더라도 데이터가 없으면 show dbs에서 출력되지 않는다.

      데이터 삽입을 한 후 show dbs를 수행

      ```sql
      db.mycollection.insertOne({name: "gyumin"});
      ```

   2. collection

      데이터의 집합

      관계형 데이터베이스의 테이블과 유사하지만 모든 종류의 데이터를 저장할 수 있다는 측면에서 보면 테이블과는 다름

      그렇지만 실제로는 대부분 동일한 모양의 데이터만 저장한다.

      - collection을 생성
        `db.createCollection('이름')`
        컬렉션을 만들지 않고 처음 사용하면 자동 생성
      - 현재 존재하는 컬렉션을 확인
        `show collections`
      - 컬렉션 제거
        `db.컬렉션이름.drop()`
      - 컬렉션 이름 변경
        `db.컬렉션이름.renameCollection('변경이름')`

      Capped Collection은 크기를 제한해서 생성할 수 있는 컬렉션으로 크기보다 많은 양의 데이터를 저장하고자 하면 오래된 데이터가 삭제되면서 저장이 된다.

      - 생성
        `db.createCollection(컬렉션이름, {capped:true, size: 크기})`
      - 크기 단위는 byte
      - 예시
        ```sql
        db.createCollection("noCapCollection")
        for(i=0; i<1000; i++){db.noCapCollection.insertOne({x:i})}
        db.noCapCollection.find()
        ```
        ```sql
        db.createCollection("cappedCollection", {capped:true, size:1000})
        for(i=0; i<1000; i++){db.cappedCollection.insertOne({x:i})}
        db.cappedCollection.find()
        ```

   3. Document 생성 - 데이터 삽입

      - 데이터는 객체 형태로 삽입을 해야 한다.
      - \_id라는 속성을 설정하지 않으면 Mongo DB가 Objectid 타입으로 \_id 값을 생성해서 삽입한다. 이 값이 Primary Key 이면서 Index
      - 삽입을 할 때는 insert, insertOne, insertMany, save 함수를 이용하는 것이 가능

        - insert는 현재 버전에서는 deprecated
        - insertOne은 하나의 데이터를 대입하지만 insertMany는 배열의 형태로 대입하면 된다.
        - insert를 이용한 삽입
          `db.users.insert({name:'gyumin', age:26, gender:"man"})`
          `db.users.find()`

          ```sql
          db.inventory.insert({
          	item:”ABC1”,
          	details:{
          		model:”123”,
          		manufacture:”xyz”
          	},
          	stock:[{size:”s”, qty:25}, {size:”m”, qty:20}],
          	category:”clothing”
          })

          # 관계형 데이터베이스는 테이블의 컬럼 안에 하나의 값만 입력할 수 있다.
          # 다른 테이블의 데이터나 배열을 대입할 수 없다.
          # 이런 이유로 여러 개의 테이블을 만들어야 하고 여러 정보를 가져오기 위해서는
          # join을 수행해야 한다.
          ```

      - root가 배열인 경우 데이터를 분할해서 삽입
        ```sql
        db.users.insert([{name:"matt"}, {name:"lara"}])
        db.users.find()
        ```
      - 데이터를 삽입할 때 두 번째 매개변수로 ordered 옵션에 true를 설정하면 single thread 형태로 데이터를 삽입하고 false를 설정하면 멀티 스레드 형태로 삽입한다. 스레드는 다른 스레드에 영향을 주지 않는다. ordered가 true이면 중간에 삽입 실패를 하면 다음 작업을 수행하지 않지만 oredered를 false로 설정하면 중간에 작업이 실패하더라도 나머지 작업을 수행한다.

        ```sql
        # sample 컬렉션에 name 속성의 값은 중복되지 않도록 인덱스를 생성
        db.sample.createIndex({name:1}, {unique:true})

        db.sample.insert({name:"gyumin"})

        # 3개의 데이터를 삽입하는데 두 번째 데이터는 이미 존재하는 gyumin을 삽입 - 싱글스레드
        db.sample.insert([{name:"jack"},{name:"gyumin"},{name:"park"}],{ordered:true)

        # gyumin에서 에러가 발생해서 park은 삽입이 안됨.
        db.sample.find()

        # sample을 삭제
        db.sample.drop()
        ```

        ```sql
        # sample 컬렉션에 name 속성의 값은 중복되지 않도록 인덱스를 생성
        db.sample.createIndex({name:1}, {unique:true})

        db.sample.insert({name:"gyumin"})

        # 3개의 데이터를 삽입하는데 두 번째 데이터는 이미 존재하는 gyumin을 삽입 - 멀티스레드
        db.sample.insert([{name:"jack"},{name:"gyumin"},{name:"park"}],{ordered:false)

        # gyumin에서 에러가 발생했지만 다른 작업은 별도의 스레드로 수행되므로 park도 삽입이 된다.
        db.sample.find()

        # sample을 삭제
        db.sample.drop()
        ```

      - ObjectId: MongoDB의 자료형으로 12byte로 구성
        컬렉션에 기본키를 생성하기 위해서 제공되는 자료형
        삽입할 때 \_id 라는 컬럼에 자동으로 대입된다.
        직접 설정하는 것도 가능한데 이 경우에는 new ObjectId()로 인스턴스를 생성해서 대입하면 된다.

   4. 데이터 조회

      ```sql
      db.컬렉션이름.find(<query>, <projection>)

      # selection
      # select 구문에서 where 절에 해당하는 것으로 조건을 가지고 테이블을 수평 분할하기 위한 연산

      # projection
      # select 구문에서 select 절에 해당하는 것으로 컬럼 이름을 가지고 테이블을 수직 분할하기 위한 연산
      ```

      - 컬렉션의 전체 데이터 조회
        `db.컬렉션이름.find()`
      - 조건을 이용한 조회
        `db.컬렉션이름.find({조건})`

        ```sql
        # users 컬렉션의 name이 gyumin인 데이터 조회
        db.users.find({name:"gyumin"})

        # 하나의 객체에 여러 속성의 값을 나열하면 and
        # containerBox 컬렉션에서 category는 animal이고 name은 bear인 데이터 조회
        db.containerBox.find({category:'animal', name:'bear'})
        ```

      - 컬럼 추출
        ```sql
        # true를 설정하면 출력되고 false이면 출력되지 않음.
        db.컬렉션이름.find({}, {속성이름:true 또는 false, ...})
        ```
      - 비교 연산자
        - $eq: =
        - $ne: !=
        - $gt: >
        - $gte: >=
        - $lt: <
        - $lte: <=
        - $in: in
        - $nin: not in
        ```sql
        db.users.find({name:{$eq:"gyumin"}})
        db.users.find({score:{$ge:90}})
        de.users.find({name:{$in:["gyumin", "jack"]}})
        ```
      - 조회를 할 때 문자열 자리에 정규 표현식을 사용하는 것이 가능.(#b로 시작하는)
        ```sql
        db.users.find({name:{$in:[/^b/]}})
        ```
      - 논리 연산자
        $not, $or, $and, $nor
        not을 제외하고는 조건을 배열의 형태로 설정
        ```sql
        # inventory에서 qty가 100보다 크거나 qty가 10보다 작은 데이터 조회
        db.inventory.find({$or:[{qty:{$gt:100}}, {qty:{$lt:10}}]]})
        ```
      - 문자열 검색 - 값에 정규식 사용 가능

        ```sql
        # 샘플 데이터 삽입
        db.users.insert({name:'paulo'})
        db.users.insert({name:'patirc'})
        db.users.insert({name:'pedro'})

        # a가 포함된 데이터 조회
        db.users.find({name:/a/})

        # pa로 시작하는 데이터 조회
        db.users.find({name:/^pa/})

        # ro로 끝나는 데이터 조회
        db.users.find({name:/ro$/})
        ```

      - 데이터 개수 제한은 limit 함수

        ```sql
        # users 테이블에서 데이터 2개만 조회
        db.users.find().limit(2)

        # 1개만 조회시에는 findOne 함수를 이용해도 됨.
        ```

      - 건너뛰기는 skip 함수
      - 데이터 정렬은 sort 함수
        ```sql
        sort({속성이름: 1이나 -1, ...})
        // 1은 오름차순이고 -1은 내림차순.
        ```
        속성 이름 대신에 natural을 이용하면 삽입한 순서대로 조회가 가능하다.
      - Cursor
        find 함수의 결과로 리턴되는 자료형으로 도큐먼트를 순서대로 접근할 수 있도록 해주는 일종의 포인터이다. 프로그래밍 언어의 Enumeration(Enumerator)나 Iterator와 유사한 개념
        커서에는 hasNext() 함수와 next()가 존재하는데 다음 데이터의 존재 여부를 리턴하고 뒤의 함수는 다음 데이터를 리턴한다.

   5. 데이터 수정

      함수

      update, updateOne, updateMany, replaceOne

      형식

      ```sql
      update({조건}, {수정할 형식})
      # 수정할 형식은 {$set:{속성이름: 수정할 데이터, ...}}

      db.users.update({name:"gyumin"}. {$set:{score:100}})
      ```

   6. 데이터 삭제

      함수

      remove, deleteOne, deleteMany

      ```sql
      db.users.deleteOne({name:"gyumin"})
      ```

   7. 트랜잭션 처리

      Mongo DB는 느슨한 트랜잭션을 지원

      트랜잭션 처리가 중요한 업무에서는 Mongo DB를 잘 사용하지 않음.

      ```sql
      # 트랜잭션 시작
      session = db.getMongo().startSession()
      session.startTransaction()

      // 작업 수행
      session.commitTransaction()
      session.abortTransaction()
      # 둘 중 하나 호출
      ```
