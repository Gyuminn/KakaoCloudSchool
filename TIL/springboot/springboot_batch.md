# [Spring Boot] Batch

1. **Batch Processing**
   - 대부분의 애플리케이션은 사용자와 상호 작용하도록 개발
   - 서비스를 개발하다보면 API 기능 만으로 모든 기능을 개발할 수 없는 상황이 발생
     API는 사용자 요청을 받아서 실행되는 특징
   - API로 처리하기 어려운 작업 - 크기가 큰 데이터를 처리
     매일 오후 6시에 내일 예약한 모든 고객에 이메일이나 문자 메시지를 전송
     매주 일주일동안 판매한 데이터를 가공해서 통계를 작성
   - **배치 프로세스는 사용자 개입없이 또는 중단 없이 유한한 양이 데이터를 처리하는 것**
   1. 구성
      - Task
        실행할 작업
      - Scheduling
        주기 또는 시간
      - Trigger
        Task를 실행하는 것
   2. Spring Batch를 사용하는 이유
      - 유지 보수 측면
        테스트 용이성이나 추상화와 같은 이점을 얻을 수 있도록 설계
      - 유연성
        POJO로 업무 로직을 구현
      - 커뮤니티 지원
        도큐먼트 제공
      - 비용
   3. 사용 사례
      - ETL(Extract Transform Load) - 추출,변환,적재 - 빅데이터의 기초개념
        한 형식에서 다른 형식으로 데이터를 전환하는 작업으로 엔터프라이즈 데이터 처리에서 많은 부분을 처리
      - Data Migration
      - 워크로드 조정
      - **병렬 처리**: 동시에 처리
        Apache Spark, YARN, GridGain, Hazlecast 등의 빅데이터 플랫폼이 병렬처리를 수행할 수 있는데 이 플랫폼들은 알고리즘 또는 데이터 구조에 맞게 전처리 작업이 필요
      - **무중단 처리 또는 상시 데이터 처리** - chuck 단위 queue를 이용해서 배치 처리 수행 가능
2. **application.yml 파일에 데이터 암호화 → 잘 수행되지 않았음. 스킵해도 됨!**

   1. 프로젝트 생성 (spring boot 2.7기준 → 3.0에서는 deprecated되어 다른 방법을 수행해야 함.)

      Spring Web, JPA, mariadb, devTools, lombok, Batch 의존성을 설정

   2. 실행하면 에러 발생

      JPA는 애플리케이션이 시작된 후 데이터베이스와 Connection Pool을 생성해야 하므로 데이터베이스 접속 정보가 없으면 에러가 발생

   3. application.properties나 application.yml 파일에 데이터베이스 접속 정보 작성

      ```yaml
      server:
        port: 80

      spring:
        datasource:
          url: jdbc:mariadb://localhost:3306/gyumin
          driver-class-name: org.mariadb.jdbc.Driver
          username: root
          password: root

        jpa:
          hibernate:
            ddl-auto: update
      ```

   4. 실행을 하면 오류는 없지만 주요 정보가 노출
   5. 암호화를 위한 라이브러리 의존성을 설정

      ```java
      implementation ‘com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4’
      ```

   6. 환경 설정 클래스를 생성 - 암호화와 복호화를 위한 클래스 - config.JasyptConfig

      ```java
      package config;

      import org.jasypt.encryption.StringEncryptor;
      import org.jasypt.encryption.pbe.PooledPBEBigDecimalEncryptor;
      import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
      import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;

      @Configuration
      public class JasyptConfig {
          // 키 설정
          private static final String KEY = "kimgyumin";

          // 암호화 알고리즘 설정
          private static final String ALGORITHM = "PDBWithMD5AndDES";

          @Bean
          public StringEncryptor stringEncryptor() {
              PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
              SimpleStringPBEConfig config = new SimpleStringPBEConfig();
              config.setPassword(KEY);
              config.setAlgorithm(ALGORITHM);
              config.setKeyObtentionIterations("1000");
              config.setPoolSize("1");
              config.setProviderName("gyumin");
              config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
              config.setStringOutputType("base64");
              encryptor.setConfig(config);
              return encryptor;
          }
      }
      ```

   7. 온라인 복호화 사이트에서 복호화를 수행
      - https://www.devglan.com/online-tools/jasypt-online-encryption-decryption

3. **스케쥴링**

   1. 스케쥴링 설정

      - 스프링 프레임워크에서는 스케쥴링 기능을 확장하는org.springframework.scheduling.annotation.SchedulingConfigurer 인터페이스를 제공하는데 configureTasks 라는 메서드가 존재하고 이 메서드는 ScheduledTaskRegistry 를 인자로 받는데 TaskScheduler를 설정하는 헬퍼 클래스
      - 스프링 프레임워크는 TaskScheduler의 존재 여부를 확인해서 스케쥴링 기능을 활성화
      - **스케쥴링은 별도의 스레드로 동작**
      - 스케쥴링 설정 클래스 - config.SchedulingConfig

        ```java
        package com.gyumin.springbatch0323.config;

        import org.springframework.context.annotation.Configuration;

        import org.springframework.scheduling.annotation.EnableScheduling;
        import org.springframework.scheduling.annotation.SchedulingConfigurer;
        import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
        import org.springframework.scheduling.config.ScheduledTaskRegistrar;

        @EnableScheduling
        @Configuration
        public class SchedulingConfig implements SchedulingConfigurer {
            @Override
            public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
                ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
                // 스레드 풀의 스레드 개수를 CPU 코어 수로 설정
                // 스레드의 개수를 코어의 수로 설정했을 떄 최대의 효과
                taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors());
                // 스레드의 이름 앞에 태그를 설정
                taskScheduler.setThreadNamePrefix("스케쥴러-");
                // 초기화
                taskScheduler.initialize();
                // 등록
                taskRegistrar.setTaskScheduler(taskScheduler);
            }
        }
        ```

      - 스케쥴링 작업을 생성 - service.ScheduledTask

        ```java
        package com.gyumin.springbatch0323.service;

        import lombok.extern.slf4j.Slf4j;
        import org.springframework.scheduling.annotation.Scheduled;
        import org.springframework.stereotype.Component;

        @Component
        @Slf4j
        public class ScheduledTask {
            // 스케쥴링에 수행될 작업 - 1초마다 수행
            @Scheduled(fixedRate = 1000L)
            public void triggerEvent() {
                log.info("배치 작업");
            }

        }
        ```

        작업이 길거나 이미 다른 메서드로 구현된 경우는 메서드 호출을 해도 된다.
        **작업을 스프링 빈으로 생성하기 때문에 되도록이면 작업을 가진 객체도 빈으로 만들어두고 주입을 받는게 좋다.**
        **스케쥴링에 사용되는 메서드는 스프링 빈의 메서드로 만들어져야 한다.**
        **메서드의 리턴 타입은 void이어야 하고 매개변수는 없어야 한다.**
        **메서드의 접근 지정자는 public이어야 한다.**

   2. @Scheduled 설정

      - Scheduled는 실행 주기를 설정하는 어노테이션 인터페이스
      - fixedDelay를 이용하면 밀리초 단위로 지연 시간을 설정할 수 있는데 long 단위로 설정하는데 문자열로 설정하고자 하는 경우는 fixedDelayString을 이용하면 된다.
        **하나의 태스크가 종료되고 난 후의 Delay 시간을 이용 → 얘를 이용하면 됨. 보통 API 가져올떄**
      - fixedRate를 이용하면 밀리초 단위로 태스크와 태그 사이의 실행 주기를 설정할 수 있는데 long 단위로 설정하는데 문자열로 설정하고자 하는 경우는 fixedRateString을 이용하면 된다.
        하나의 태스크가 시작되고 난 후의 Delay 시간을 이용
      - 시간의 기본 단위는 밀리초이지만 timeUnit 속성을 이용해서 단위를 수정하는 것도 가능하다.
      - 타임존도 설정 가능 - 기본은 서버의 타임존인데 이 경우는 zone 속성으로 변경
      - initialDelay 속성을 이용하면 초기 지연 시간
      - cron
        특정한 타겟을 설정해서 타겟과 일치하면 수행
        cron = “초 분 시 일 월 요일”
        월은 1 ~ 12 라고 해도 되고 JAN ~ DEC로 설정해도 됨.
        요일 0 ~ 7 라고 입력해도 되고 SUN ~ SAT로 설정해도 됨. 0과 7은 둘 다 일요일
        표현식 문자
        - \*: 모든 값
        - ?: 어떤 값이든 상관없음
        - ,: 배열 - 0. 1. 2
        - -: 범위 설정
        - /: 초기값과 증분값(0/10 ⇒ 0 부터 10분마다)
        - #: 요일과 몇 번쨰를 설정할 떄(5#2 ⇒ 두 번째 목요일)
        - L: 마지막
        - W: 스케쥴된 날부터 가장 가까운 평일
          **10분마다 수행: @Scheduled(cron = “_ 0/10 _ \* \* ?”)**
          **매일 새벽 2시에 수행: @Scheduled(cron = “0 0 2 \* \* ?”)**
          **일요일 6시에 수행: @Scheduled(cron = “0 0 6 \* \* SUN”)**
          **월요일 1시와 2시에 수행: @Scheduled(cron = “0 0 1,2 \* \* MON”)**
      - Cron 표현식의 테스트를 위한 클래스 작성 - test.Crontest

        ```java
        package com.gyumin.springbatch0323;

        import org.junit.jupiter.api.Assertions;
        import org.junit.jupiter.api.Test;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.scheduling.support.CronExpression;

        import java.time.LocalDateTime;

        @SpringBootTest
        public class CronTests {
            @Test
            public void testParse() {
                // 5초마다 수행
                CronExpression express = CronExpression.parse("0/5 * * * * ?");
                // 기준 시간 설정을 설정해서 다음 수행 시간을 가져온다.
                LocalDateTime next = express.next(LocalDateTime.of(2023, 1, 1, 0, 0, 0));
                // 확인 - 디버깅
                // System.out.println(next.toString());
                // 테스트
                Assertions.assertEquals("2023-01-01T00:00:05", next.toString());
            }
        }
        ```

   3. 코드 비교

      ```java
      LocalDateTime from = LocalDateTime.now().withSecond(0).minusMinute(5);
      LocalDateTime to = LocalDateTime.now().minusMinute(5);
      userRepository.findByCreatedAtBetween(from, to);
      ```

      현재 시간부터 5분 동안 가입한 유저 정보를 가져오기

      fixedDelay를 이용하게 되면 일정 부분의 데이터를 조회하지 못하는 현상이 벌어질 수 있음.

      10:00 → 09:55 가져옴. 이 작업이 종료되고 5분 후에 동일한 작업 수행.

      보통 이런 작업을 할 때는 flag같은 변수를 만들어서 이전 작업이 어디까지 이루어졌는지 확인하는게 필요하다. 중간에 장애가 발생한 경우 잘못하면 갱신된 데이터를 잃어버릴 수 있기 때문이다.

4. **Batch Server Architecture**
   1. Batch Processing(일괄 처리)
      - 모아서 처리
        실시간 처리하기에는 작업량이 너무 많거나 작업의 크기가 너무 큰 경우 사용
      - 배치 처리를 하게 되면 시스템 리소스가 많이 필요하기 때문에 서버군을 구성해서 처리
   2. 서버군을 이용할 때 주의사항
      - Load Balancing
        작업을 어떻게 서버에 할당할 것인가?
        A 작업을 수행할 수 있는 서버(앱) 1
        A 작업을 수행할 수 있는 서버(앱) 2
        A 작업을 수행할 수 있는 서버(앱) 3
        이렇게 구성된 경우 A 작업 요청이 왔을 떄 어디에서 처리할 것인가?
        **앞에 Router나 Load Balancer를 배치 → 이 고민은 AWS가 해결해줌.**
      - Dead Lock
        동시에 작업을 처리하다가 결코 발생할 수 없는 사건을 무한정 기다리는 것, 공유 자원 사용 문제.
        공유 자원이 2개 이상인 경우 발생
   3. 실제 구현하는 방식
      - 단독 배치 서버
      - Jenkins + Load Balancer + 서버군
        스케쥴링은 Jenkins가 수행
      - Load Balancer + @Scheduled를 이용하는 서버군
   4. 최근 추세
      - Load Balancer의 기능은 상용 Cloud에 위임
      - 오케스트레이션 툴에도 Cron Job 같은 기능이 있어서 이를 이용하기도 한다.
5. **Spring Batch**

   1. 용어
      - **Job(Transaction)**
        실제 배치 작업이 이루어지는 단위
        하나의 Job 안에 1개 이상의 Step으로 구성
      - **Step**
        Job 내의 작은 단위 작업
        Tasklet 기반과 Chuck 기반
        Tasklet 은 모든 작업이 순차적으로 1번에 이루어지는 것이고 Chuck는 작업을 나누어서 처리
        **데이터가 많은 경우 Chunk를 이용해서 처리(빅데이터)**
        Spring Framework 에서는 이들을 위한 Annotaion을 제공
        Chunk는 3가지 인터페이스로 구분 - ItemReader, ItemProcessor, ItemWriter로 세분화
      - Job은 실행될 때마다 이름과 기타 정보를 데이터베이스에 저장한다.
        이전에 수행된 Job은 다시 수행되지 않는다.
        **동일한 Job을 여러 번 수행하고자 하면 Job의 식별자를 변경하면서 수행해야 한다.**
   2. main 클래스를 변경

      ```java
      @EnableBatchProcessing
      @SpringBootApplication
      public class BatchWebApplication {
          //Job 과 Step을 생성할 수 있는 팩토리 클래스를 주입
          @Autowired
          private JobBuilderFactory jobBuilderFactory;

          @Autowired
          private StepBuilderFactory stepBuilderFactory;

          @Bean
          public Step step(){
              return this.stepBuilderFactory.get("step1")
                      .tasklet(new Tasklet() {
                          @Override
                          public RepeatStatus execute(
                                  StepContribution contribution,
                                  ChunkContext chunkContext) throws Exception {
                              System.out.println("Hello Spring Batch");
                              return RepeatStatus.FINISHED;
                          }
                      }).build();
          }

          @Bean
          public Job job(){
              return jobBuilderFactory.get("job").start(step()).build();
          }

          public static void main(String[] args) {
              SpringApplication.run(BatchWebApplication.class, args);
          }

      }
      ```

   3. 스케쥴링이나 배치는 외부의 특별한 이벤트 호출없이 실행이 가능
      - 배치는 외부의 특별한 이벤트 호출없이 실행이 가능
      - 배치는 특별한 진입점을 설정하지 않아도 수행이 가능
      - 배치는 작업한 내역을 데이터베이스에 기록을 하기 때문에 데이터베이스 사용이 필수
      - 테이블이 없다고 에러가 발생하면 야믈 파일에 추가
        ```yaml
        spring:
          batch:
            jdbc:
              initialize-schema: always
        ```
      - Job을 한 번 수행한 후 다시 실행하면 Job이 다시 실행되지 않는다.
        Spring Batch는 한 번 수행한 Job을 다시 실행하지는 않음.
        동일한 Step을 여러 번 수행하고자 하면 Job의 이름을 동적으로 변경해주거나 **Sequence**를 사용해야 한다.
   4. 실행 과정

      Spring Boot 에는 JobLauncherCommandLineRunner라는 컴포넌트가 존재하는데 이 컴포넌트는 스프링 배치가 클래스 경로에 있다면 실행 시에 로딩돼서 JobLauncher 클래스의 객체를 이용해서 ApplicationContext(스프링의 모든 Bean과 설정 정보를 갖는 객체)에서 찾아낸 JOB을 수행

      **모든 Job은 Spring Bean 안에서 작성되어야 함.**

   5. Spring Batch가 생성하는 메타 테이블
      - BATCH_JOB_EXECUTION
        시작 시간, 종료 시간, 종료 코드를 저장 - 언제 시작하고 언제 끝났는지 일일이 기억할 필요 x
      - SEQUENCE
        BATCH_JOB_INSTANCE, BATCH_JOB_EXECUTION에서 사용할 Sequence
        일련 번호를 만들어서 제공해줌.
      - BATCH_STEP_EXECUTION
        STEP의 시작 시간, 종료 시간, 종료 코드를 저장
      - BATCH_STEP_EXCUTION_CONTEXT
        실행 관련 정보를 가지고 있는데 이 정보에서 가장 중요한 것은 **실패했을 때의 정보인데 이 정보를 이용하면 실패한 작업을 이어서 다시 수행할 수 있다.**
   6. JOB을 여러 번 수행하고자 하는 경우

      JOB을 생성할 때 **Increment(new RunIdIncrementer())**을 추가 설정하면 된다.

6. **Job의 종류**

   1. Single Step

      ```java
      @Configuration //모든 JOB은 Spring Bean으로 등록되어야 합니다.
      @EnableBatchProcessing //이 클래스의 Bean이 배치 작업으로 동작할 수 있는 Bean이라는 설정
      @Log4j2
      @RequiredArgsConstructor
      public class SingleStep {
          private final JobBuilderFactory jobBuilderFactory;
          private final StepBuilderFactory stepBuilderFactory;

          @Bean
          //수행할 작업을 작성
          public Step Step(){
              return stepBuilderFactory.get("step")
                      .tasklet((contribution, chunkContext) -> {
                          log.info("Single Step!");
                          return RepeatStatus.FINISHED;
                      })
                      .build();
          }

          @Bean
          public Job Job(){
              return jobBuilderFactory.get("singlestepjob")
                      .incrementer(new RunIdIncrementer())
                      .start(Step())
                      .build();
          }
      }
      ```

   2. Multi Step: Step이 여러개

      Job에 Step을 등록할 때 start 다음에 next를 이용해서 step을 등록

      ```java
      @Configuration //모든 JOB은 Spring Bean으로 등록되어야 합니다.
      @EnableBatchProcessing //이 클래스의 Bean이 배치 작업으로 동작할 수 있는 Bean이라는 설정
      @Log4j2
      @RequiredArgsConstructor
      public class FlowStep {
          private final JobBuilderFactory jobBuilderFactory;
          private final StepBuilderFactory stepBuilderFactory;

          @Bean
          //수행할 작업을 작성
          public Step StartStep(){
              return stepBuilderFactory.get("StartStep")
                      .tasklet((contribution, chunkContext) -> {
                          log.info("시작 스텝");
                          return RepeatStatus.FINISHED;
                      })
                      .build();
          }

          @Bean
          //수행할 작업을 작성
          public Step NextStep(){
              return stepBuilderFactory.get("NextStep")
                      .tasklet((contribution, chunkContext) -> {
                          log.info("두번째 스텝");
                          return RepeatStatus.FINISHED;
                      })
                      .build();
          }

          @Bean
          //수행할 작업을 작성
          public Step LastStep(){
              return stepBuilderFactory.get("LastStep")
                      .tasklet((contribution, chunkContext) -> {
                          log.info("마지막 스텝");
                          return RepeatStatus.FINISHED;
                      })
                      .build();
          }

          @Bean
          public Job MultiStepJob(){
              return jobBuilderFactory.get("multistepjob")
                      .incrementer(new RunIdIncrementer())
                      .start(StartStep())
                      .next(NextStep())
                      .next(LastStep())
                      .build();
          }
      }
      ```

   3. Flow Step: Step을 순차적으로 수행

      스텝의 상태에 따라 다음 작업을 결정하는 방식

      ```java
      .start(스텝1)
      .on("FAILED") // 이 값은 Step이 return하는 값
      .to(FAILED일 때 수행할 스텝)
      .on("*") // Step의 성공 여부와 상관없이
      .to(수행할 스텝)
      .end()
      ```

   4. Step의 기타 설정
      - Step을 생성할 때 startLimit(횟수)를 호출하면 실패했을 때 재시작 횟수를 설정
      - skip(예외클래스.class)
        예외 클래스에 해당하는 예외가 발생하면 작업을 진행
      - noskip(예외클래스.class)
        예외 클래스에 해당하는 예외가 발생하면 중지
      - skipLimit(skip의 최대 허용 횟수)
      - retry(예외클래스.class)
        예외가 발생하면 재시도
      - retryLimit(retry의 최대 허용 횟수)
   5. 참고

      **배치 작업은 대규모 작업에 사용하는 경우**가 많기 때문에 **예외가 발생하면 재시도하는 것을 step에 설정**해야 한다. API Server에서 데이터를 가지고 와서 작업을 수행하는 경우 데이터를 가져오지 못하면 재시도 횟수를 설정해서 재시도를 해보는게 좋고 반드시 예외 처리를 하자.

      클라이언트 서버 시스템에서는 클라이언트에 데이터를 저장하고 출력하는 형태로 만들어주면 서버에서 데이터를 제공하지 못할 때 클라이언트가 로컬의 데이터를 보여주는 형태로 구현하는 경우가 있다.

      서버에서는 이전에 가져온 데이터를 서버의 어딘가에 저장해두고 다른 API에서 데이터를 가져오지 못한 경우 그 데이터를 제공하는 형태로 구현하기도 한다.

7. **STEP을 구성하는 처리 방식**
   1. Tasklet
      - 하나의 메서드로 구성되어 있는 간단한 인터페이스
      - 실패를 알리기 위해서 예외를 반환하거나 throw를 할 때까지 execute를 반복적으로 호출
   2. Chunk
      - 작업을 나누어서 처리
      - Read → Processing → Write 작업으로 나누어서 처리
        Read를 할 때 적절한 데이터 크기를 설정해서 작업을 수행
        **대규모 업무 처리에서는 대부분 이 방식을 사용**
8. **Chunk Batch**

   1. 설정 클래스 안에 직접 메서드를 구현하는 방식

      - **이 경우 대부분 람다를 사용**

        ```java
        @Log4j2
        //배치를 만들기 위한 어노테이션
        @EnableBatchProcessing
        @Configuration
        @RequiredArgsConstructor
        public class TaskJobConfig {
            //Job을 생성하기 위한 Builder 객체
            private final JobBuilderFactory jobBuilderFactory;
            //Step을 생성하기 위한 Builder 객체
            private final StepBuilderFactory stepBuilderFactory;

            //스텝 생성
            @Bean
            public Step TaskStep(){
                return stepBuilderFactory.get("LambdaStep")
                        .tasklet((contribution, chunkContext) -> {
                            //비지니스 로직 호출
                            for(int idx = 0; idx<10; idx++){
                                log.info("[idx]:" + idx);
                            }
                            return RepeatStatus.FINISHED;
                        }).build();
            }

            //Job 생성
            @Bean
            public Job TaskletJob(){
                Job customJob = jobBuilderFactory.get("taskletjob")
                        .start(TaskStep())
                        .build();
                return customJob;
            }
        }
        ```

      - 애플리케이션을 실행하면 작업이 수행됨
        일반적인 Web Application이나 PC Application 들은 사용자의 이벤트를 받아서 작업을 수행
        구독과 게시 - 애플리케이션을 실행해두고 구독자가 메시지를 보내면 게시자가 일을 하는 구조
        스케쥴링 - 특정한 시점을 등록해두고 그 시점에 작업을 수행하는 구조
        Batch - 사용자의 이벤트와는 상관없이 작업을 수행하는데 스케쥴링과 같이 사용하는 경우가 많음. 모아서 한꺼번에 처리하는 구조라서 시간이 오래 걸리거나 작업이 빈번하게 발생하는 경우에 사용하기 때문에 다른 애플리케이션과 같이 동작시키지 않음.

   2. 비즈니스 로직이 복잡하거나 누군가에게 보여자야 한다면 내용을 별도로 구성

      - Spring Application Context가 작업을 기억하고 있다가 실행하는 개념이므로 작업의 내용이 Bean 객체 안에 존재해야 한다.
      - 비즈니스 로직을 소유한 클래스 생성

        ```java
        @Service
        @Log4j2
        public class CustomService {
            public void businessLogic(){
                for(int idx=0; idx < 10; idx++){
                    log.info("Method Invoke를 이용한 Step 구성");
                }
            }
        }

        =>Job 생성 - 이전에 만든 클래스에서 작업
            //Step으로 수행할 객체
            private final CustomService customService;

            @Bean
            public Job makeJob(){
                return jobBuilderFactory.get("methodInvokingJob")
                        .start(makeStep())
                        .build();
            }

            @Bean
            public Step makeStep(){
                return stepBuilderFactory.get("makeMethodInvokingTasklet")
                        .tasklet(makeMethodInvokingTasklet())
                        .build();
            }

            //Step에 사용할 메서드를 등록해주는 Bean
            @Bean
            @StepScope
            public MethodInvokingTaskletAdapter makeMethodInvokingTasklet(){
                MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
                        new MethodInvokingTaskletAdapter();
                methodInvokingTaskletAdapter.setTargetObject(customService);
                methodInvokingTaskletAdapter.setTargetMethod("businessLogic");
                //매개변수 대입
                //methodInvokingTaskletAdapter.setArguments(//매개변수를 배열로 등록);
                return methodInvokingTaskletAdapter;
            }
        ```

   3. Chunk

      - Spring Batch에서는 처리되는 커밋 row 수를 의미
      - Spring Batch에서는 Chunk 단위로 Transaction을 수행하기 때문에 실패시 Chunk 단위만큼 Rollback
        이 트랜잭션의 크기가 커지면 작업 효율은 높아질 수 있지만 Rollback되는 데이터의 크기가 커지게 됨.
        트랜잭션의 크기가 작으면 Rollback 되는 데이터의 크기는 작아지지만 효율이 떨어질 수 있음.
      - 3가지 시나리오 이용
        - Read
          Database(Data Store)에서 배치 처리를 할 Data를 읽어오는 과정
        - Processing
          읽어온 데이터를 가공하고 처리하는 과정(옵션)
        - Write
          가공하고 처리한 데이터를 저장
      - JobLauncher(잡을 실행) ↔ Job ↔ Step(ItemReader, ItemProcessor, ItemWriter)

        ```java
        // 에시
        List Items = new ArrayList(); // 데이터가 저장될 공간

        for(int idx = 0; idx < interval; idx++)  {
        	Object item = itemReader.read(); // 읽고
        	Object processedItem = itemProcessor.process(item); // 가공
        	items.add(processedItem)
        };

        itemWrtier.write(items);
        ```

      - Paging(나눠서 처리했기 때문에 읽는 부분을 고민)
        Spring Batch에서는 Paging 처리와 Chunk Size를 설정해서 효율적인 배치 처리를 할 수 있도록 기능을 제공
        만약 Paging Size를 5로 설정하고 Chunk Size를 10으로 설정하면 read를 2번해서 1번의 Transaction을 처리
        **Spring Batch 에서는 Paging Size와 Chunk Size를 일치시키는 것을 권장 - Spring Batch는 출력이 목적이 아님.**
        Paging 처리할 때 Offset과 Limit을 지정해 주어야하는데 페이징 처리를 할 때마다 새로운 쿼리를 수행

   4. Flat File
      - 1개 또는 그 이상의 레코드가 기록된 파일
      - 데이터의 포맷이나 의미를 정의하는 메타 데이터가 없음. **(비정형 데이터)**
      - XML과 JSON 데이터는 플랫 파일이라고 하지 않음. **(반정형 데이터)**
      - 플랫 파일도 일정한 간격을 가지고 작성된 fwt와 구분자를 가진 csv 형태로 분류를 한다.
   5. 일정한 간격을 갖는 텍스트 파일을 읽어서 출력

      - 일정한 간격을 갖는 텍스트 파일을 **resources 디렉토리(읽기 전용의 데이터를 배치)**에 복사
      - 한 줄의 데이터를 읽어서 저장할 DTO 클래스 생성 dto.CustomerDTO

        ```java
        package com.example.batch_web.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class CustomerDTO {
            private String firstName;
            private String middleInitial;
            private String lastName;
            private String addressNumber;
            private String street;
            private String city;
            private String state;
            private String zipCode;
        }
        ```

      - Batch를 위한 서비스 클래스를 만들고 작성한 후 실행 - service.FWTFileService

        ```java
        package com.example.batch_web.service;

        import com.example.batch_web.dto.CustomerDTO;
        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.Job;
        import org.springframework.batch.core.Step;
        import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
        import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepScope;
        import org.springframework.batch.item.ItemWriter;
        import org.springframework.batch.item.file.FlatFileItemReader;
        import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
        import org.springframework.batch.item.file.transform.Range;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.core.io.ClassPathResource;

        @Configuration
        @EnableBatchProcessing
        @RequiredArgsConstructor
        public class FWTFileService {
            private final JobBuilderFactory jobBuilderFactory;
            private final StepBuilderFactory stepBuilderFactory;

            // Chunk는 3가지로 나누어짐.
            // 1. 데이터 읽기 작업
            @Bean
            @StepScope
            public FlatFileItemReader<CustomerDTO> customerDTOFlatFileItemReader() {
                // resources 디렉토리의 input/customerFixedWidth 파일을 리소스로 변환
                ClassPathResource resource = new ClassPathResource("input/customerFixedWidth.txt");
                return new FlatFileItemReaderBuilder<CustomerDTO>()
                        .name("customerItemReader")
                        .resource(resource)
                        .fixedLength()
                        .columns(new Range[]{new Range(1, 11), new Range(12, 12), new Range(13, 22),
                                new Range(23, 26), new Range(27, 46), new Range(47, 62),
                                new Range(63, 64), new Range(65, 69)})
                        .names(new String[]{"firstName", "middleInitial", "lastName",
                                "addressNumber", "street", "city", "state", "zipCode"})
                        .targetType(CustomerDTO.class)
                        .build();
            }

            // 3. 데이터 저장 작업
            @Bean
            public ItemWriter<CustomerDTO> itemWriter() {
                return (items) -> items.forEach(System.out::println);
            }

            // 2. 데이터 가공 작업 - 필수가 아님
            // 스텝 생성
            @Bean
            public Step copyFileStep() {
                // Reader가 데이터를 10개씩 읽어서 Writer에 전달
                return stepBuilderFactory.get("copyFileStep")
                        .<CustomerDTO, CustomerDTO>chunk(10)
                        .reader(customerDTOFlatFileItemReader())
                        .writer(itemWriter())
                        .build();
            }

            // 잡생성
            @Bean
            public Job job1() {
                return jobBuilderFactory.get("flatfilejob")
                        .start(copyFileStep())
                        .build();
            }
        }
        ```

        **주의할 점은 이어서 작업하는 경우 @Bean이 붙은 메서드의 이름을 bean으로 등록하기 때문에 이전에 사용한 이름을 이용하면 등록이 안되어서 에러!!!!**

   6. **csv 파일을 읽어서 배치 작업**

      - Mapper를 이용
        Mapper는 변환하는 작업을 수행해주는 객체
        JDBC에서 RDMBS의 데이터를 읽어내면 ResultSet 객체가 만들어져서 리턴되는데 이를 DTO 객체로 변환하는 작업을 하는데 이러한 작업을 수행해주는 객체
        Spring JDBC에서도 이러한 기능을 수행해주는 RowMapper가 제공됨.
        읽어야 하는 데이터 포맷, 그리고 변환해야 하는 데이터 포맷에 따라 별도로 작성
        이러한 역할을 하는 클래스를 Wrapper라고 부르기도 한다.
      - Chunk 단위 작업의 구성 요소
        Reader → Processor(옵션) → Writer → Step(Reader, Processor, Writer를 연결) → Job
        보통 개발자가 할 때 이렇게 한다.

        ```java
        method(매개변수) {
        	읽기
        	변환
        	쓰기
        }
        ```

        차라리 이렇게라도 하면 좋다.

        ```java
        method(매개변수) {
        	읽기 메서드 호출
        	변환 메서드 호출
        	쓰기 메서드 호출
        }

        읽기(){}

        변환(){}

        쓰기(){}
        ```

      - service 생성 - service.CSVFileService

        ```java
        package com.example.batch_web.service;

        import com.example.batch_web.dto.CustomerDTO;
        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
        import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepScope;
        import org.springframework.batch.item.file.FlatFileItemReader;
        import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.core.io.ClassPathResource;

        @Configuration
        @RequiredArgsConstructor
        @EnableBatchProcessing
        public class CSVFileService {
            private final JobBuilderFactory jobBuilderFactory;
            private final StepBuilderFactory stepBuilderFactory;

            // 읽기
            @Bean
            @StepScope
            public FlatFileItemReader<CustomerDTO> customerDTOFlatFileItemReader() {
                // Resource 생성
                ClassPathResource resource = new ClassPathResource("input/customer.csv");
                // delimited는 구분자인데 , 가 기본이기 때문에 , 일 경우에는 아무것도 안적어도 됨.
                return new FlatFileItemReaderBuilder<CustomerDTO>()
                        .name("customerItemReader")
                        .delimited()
                        .names(new String[]{"firstName", "middleInitial", "lastName", "addressNumber", "street", "city", "state", "zipCode"})
                        .fieldSetMapper(new CustomerFieldSetMapper())
                        .resource(resource)
                        .build();
            }
            // 처리

            // step

            // job
        }
        ```

      - mapper 생성 - service.CustomerFieldSetMapper

        ```java
        package com.example.batch_web.service;

        import com.example.batch_web.dto.CustomerDTO;
        import org.springframework.batch.item.file.mapping.FieldSetMapper;
        import org.springframework.batch.item.file.transform.FieldSet;
        import org.springframework.validation.BindException;

        public class CustomerFieldSetMapper implements FieldSetMapper<CustomerDTO> {
            // csv 파일을 읽어서 가져온 Field와 DTO의 필드를 매핑하는 메서드
            @Override
            public CustomerDTO mapFieldSet(FieldSet fieldSet) throws BindException {
                CustomerDTO customerDTO = new CustomerDTO();

                customerDTO.setFirstName(fieldSet.readString("firstName"));
                customerDTO.setLastName(fieldSet.readString("lastName"));
                customerDTO.setMiddleInitial(fieldSet.readString("middleInitial"));
                customerDTO.setCity(fieldSet.readString("city"));
                customerDTO.setStreet(fieldSet.readString("street"));
                customerDTO.setAddressNumber(fieldSet.readString("addressNumber"));
                customerDTO.setState(fieldSet.readString("state"));
                customerDTO.setZipCode(fieldSet.readString("zipCode"));

                return customerDTO;
            }
        }
        ```

      - CSVFileService 수정
        **주의점 → 이전에 만들었던 Job의 이름이나 @Bean으로 등록된 메서드들의 이름이 겹치지 않도록 조심해야 한다!!!!!!!!**

        ```java
        package com.example.batch_web.service;

        import com.example.batch_web.dto.CustomerDTO;
        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.Job;
        import org.springframework.batch.core.Step;
        import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
        import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepScope;
        import org.springframework.batch.item.ItemWriter;
        import org.springframework.batch.item.file.FlatFileItemReader;
        import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.core.io.ClassPathResource;

        @Configuration
        @RequiredArgsConstructor
        @EnableBatchProcessing
        public class CSVFileService {
            private final JobBuilderFactory jobBuilderFactory;
            private final StepBuilderFactory stepBuilderFactory;

            // 읽기
            @Bean
            @StepScope
            public FlatFileItemReader<CustomerDTO> customerDTOCSVItemReader() {
                // Resource 생성
                ClassPathResource resource = new ClassPathResource("input/customer.csv");
                // delimited는 구분자인데 , 가 기본이기 때문에 , 일 경우에는 아무것도 안적어도 됨.
                return new FlatFileItemReaderBuilder<CustomerDTO>()
                        .name("customerItemReader")
                        .delimited()
                        .names(new String[]{"firstName", "middleInitial", "lastName", "addressNumber", "street", "city", "state", "zipCode"})
                        .fieldSetMapper(new CustomerFieldSetMapper())
                        .resource(resource)
                        .build();
            }
            // 처리

            // 쓰기
            // Reader가 데이터를 읽으면 Processor에게 DTO의 Collection이 전달되고
            // Processor가 작업을 하고 나면 Writer에게 DTO의 Collection이 자동으로 전달됨.
            // Processor는 생략될 수 있음.
            @Bean
            public ItemWriter<CustomerDTO> CSVItemWriter() {
                return (items) -> items.forEach(System.out::println);
            }

            // step
            @Bean
            public Step copyCSVStep() {
                // Bean 들어가는 애들은 조심해야 했다. 다 들어가기 때문에!
                // 근데 여기 보면 .get("copyFileStep")은 job(아래의 .get("csvjob"))의 메서드로 들어가기 때문에 괜찮다.
                // job 안에서 안겹치면 된다. -> 즉 csvjob.copyFileStep이 여러 개가 아니면 됨.
                return stepBuilderFactory.get("copyFileStep")
                        .<CustomerDTO, CustomerDTO>chunk(10)
                        .reader(customerDTOCSVItemReader())
                        .writer(CSVItemWriter())
                        .build();
            }

            // job - 단독으로 ApplicationContext에 등록
            // job을 만드는 메서드 이름이 ApplicationContext에 bean의 이름으로 등록
            // job을 만드는 메서드 이름은 중복되면 안된다.
            // step은 관계가 없는데 step은 Job 내부에 등록되기 때문에 Job 안에서 중복되지 않으면 된다.
            @Bean
            public Job csvJob() {
                return jobBuilderFactory.get("csvjob")
                        .start(copyCSVStep())
                        .build();
            }
        }
        ```

   7. **JSON 읽기**

      - JSON은 일반적으로 파일로 제공되지 않고 URL의 형태로 제공되는 경우가 많음.
        배치에서 Mapper가 제공되므로 별도로 Mapper를 만들어서 작업할 필요가 없음.
        실습 JSON 파일
        ```json
        [
          {
            "firstName": "Laura",
            "middleInitial": "O",
            "lastName": "Minella",
            "address": "2039 Wall Street",
            "city": "Omaha",
            "state": "IL",
            "zipCode": "35446",
            "transactions": [
              {
                "accountNumber": 829433,
                "transactionDate": "2010-10-14 05:49:58",
                "amount": 26.08
              }
            ]
          },
          {
            "firstName": "Michael",
            "middleInitial": "T",
            "lastName": "Buffett",
            "address": "8192 Wall Street",
            "city": "Omaha",
            "state": "NE",
            "zipCode": "25372",
            "transactions": [
              {
                "accountNumber": 8179238,
                "transactionDate": "2010-10-27 05:56:59",
                "amount": -91.76
              },
              {
                "accountNumber": 8179238,
                "transactionDate": "2010-10-06 21:51:05",
                "amount": -25.99
              }
            ]
          }
        ]
        ```
      - transcationDTO를 생성

        ```java
        package com.example.batch_web.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.Date;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class TranscationDTO {
            private String accountNumber;
            // 외부 시스템에서 만들어진 텍스트를 날짜 형식으로 바로 변환해서 사용하고자 하는 경우
            // SimpleDateFormat을 알아야 한다.
            // Mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
            private Date transactionDate;
            private Double amount;
        }
        ```

      - CustomerDTO를 수정

        ```json
        package com.example.batch_web.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.List;
        import java.util.Optional;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class CustomerDTO {
            private String firstName;
            private String middleInitial;
            private String lastName;
            private String addressNumber;
            private String street;
            private String city;
            private String state;
            private String zipCode;

            // DTO안에 배열이나 List가 존재하는 경우
            // 배열이나 List의 데이터 1개를 수정하거나 가져오는 메서드를 같이 생성
            // Map이 존재하는 경우 Map에 저장하거나 하나의 key의 값을 가져오는 것을 같이 생성한다.
            private List<TranscationDTO> transactions;

            public void setTransaction(int idx, TranscationDTO transcationDTO) {
                if (idx >= 0 && idx < transactions.size()) {
                    transactions.set(idx, transcationDTO);
                }
            }

            public TranscationDTO getTransaction(int idx) {
                if (idx < 0 || idx >= transactions.size()) {
                    return null;
                }

                return transactions.get(idx);
            }
        }
        ```

   8. 관계형 데이터 베이스 읽기 - JDBC(프레임워크를 사용하지 않고 드라이버만 이용하는 방식)

      - Reader에서 사용할 수 있는 샘플 데이터베이스 생성
        2개의 sql 파일을 실행
      - Customer 테이블의 데이터를 사용하기 위한 DTO 클래스 생성 - Reader에서 사용
        **DTO를 생성할 떄 반드시 적용하는 규칙은 속성은 private으로 만들고 접근하기 위한 메서드를 필요에 따라 public으로 생성하고 다른 계층에서 사용한다면 Serializable을 implements**
        Java Server간의 통신 - Socket 통신이 효율이 좋다. Serializable을 이용하면 객체 단위 통신이 가능
        Java Server와 Web Client간의 통신 - Http 통신이 구현하기가 좋다.
        **equals method와 hashcode 메서드 재정의에 대해서 고민**

        ```java
        // 예시
        public class Data{
        	private String id;
        	...

        	public boolen equals(Data other){
        		if(this.id.equals(other.id)) {
        			return true;
        		} else {
        				return false;
        		}
        	}
        }

        // Test
        data1.equals(data2)
        ```

        clone 메서드를 재정의하기도 함. Comparable 인터페이스의 compareTo를 재정의하기도 함.
        디버깅을 빠르게 하기 위해서 **toString을 재정의하기도 함. - JPA를 사용하는 경우 exclude도 같이 알아야 함.** (지연 로딩의 경우 데이터를 나중에 가져오기 때문에 toString을 하는 경우 에러가 난다 - 제외를 시키거나 아니면 Transcaction을 처리해야 함.)

      - DTO 생성 - JDBCCustomerDTO

        ```java
        package com.example.batch_web.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.io.Serializable;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class JDBCCustomerDTO implements Serializable {
            private Long id;
            private String firstName;
            private String middleInitial;
            private String lastName;
            private String address;
            private String city;
            private String state;
            private String zipCode;
        }
        ```

      - JDBC에서는 Select의 구문의 결과가 ResultSet 타입으로 전송되기 때문에 ResultSet의 결과를 DTO 타입으로 변환해주는 메서드나 클래스를 생성 - CustomerRowMapper

        ```java
        package com.example.batch_web.dto;

        import org.springframework.jdbc.core.RowMapper;

        import java.sql.ResultSet;
        import java.sql.SQLException;

        public class JDBCCustomerRowMapper implements RowMapper<JDBCCustomerDTO> {
            @Override
            public JDBCCustomerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                JDBCCustomerDTO customer = new JDBCCustomerDTO();
                customer.setId(rs.getLong("id"));
                customer.setAddress(rs.getString("address"));
                customer.setCity(rs.getString("city"));
                customer.setFirstName(rs.getString("firstName"));
                customer.setLastName(rs.getString("lastName"));
                customer.setMiddleInitial(rs.getString("middleInitial"));
                customer.setState(rs.getString("state"));
                customer.setZipCode(rs.getString("zipCode"));

                return customer;
            }
        }
        ```

   9. **관계형 데이터베이스 읽기 - JPA**

      JPA와 JDBC를 이용하는 방법이 가장 크게 다른 점은 매핑 부분

      JDBC를 사용할 때는 자동 매핑을 이용할 수 없어서 RowMapper를 직접 구현해야 하지만 SQL을 작성해서 데이터를 읽어오지만 JPA를 이용할 때는 RowMapper를 만들 필요가 없고 SQL이나 JPQL을 이용해서 데이터를 읽어온다.

      JPQL을 사용하며 거의 모든 관계형 데이터베이스에 동일한 방법으로 쿼리가 가능하다.

      GroupBY 에서는 문제가 발생할 수 있음.

      - Entity 생성 - domain.JPACustomerEntity

        ```java
        package com.example.batch_web.domain;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import javax.persistence.Entity;
        import javax.persistence.Id;
        import javax.persistence.Table;

        @Entity
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Table(name="customer")
        public class JPACustomerEntity {
            @Id
            private Long id;
            private String firstName;
            private String middleInitial;
            private String lastName;
            private String address;
            private String city;
            private String state;
            private String zipCode;
        }
        ```

      - 서비스 생성 - service.JPAReaderService

        ```java
        package com.example.batch_web.service;

        import com.example.batch_web.domain.JPACustomerEntity;
        import lombok.RequiredArgsConstructor;
        import org.springframework.batch.core.Job;
        import org.springframework.batch.core.Step;
        import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
        import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
        import org.springframework.batch.core.configuration.annotation.StepScope;
        import org.springframework.batch.item.ItemWriter;
        import org.springframework.batch.item.database.JpaPagingItemReader;
        import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        import javax.persistence.EntityManagerFactory;
        import java.util.HashMap;
        import java.util.Map;

        @Configuration
        @EnableBatchProcessing
        @RequiredArgsConstructor
        public class JPAReaderService {
            private final JobBuilderFactory jobBuilderFactory;
            private final StepBuilderFactory stepBuilderFactory;

            @Bean
            @StepScope
            public JpaPagingItemReader<JPACustomerEntity> jpaCustomerItemReader(EntityManagerFactory entityManagerFactory) {
                Map<String, Object> parameterValues = new HashMap<>();
                parameterValues.put("city", "Chicago");

                return new JpaPagingItemReaderBuilder<JPACustomerEntity>()
                        .name("jpaCustomerItemReader")
                        .entityManagerFactory(entityManagerFactory)
                        .parameterValues(parameterValues)
                        .queryString("select c from JPACustomerEntity c where c.city = :city")
                        .pageSize(3)
                        .build();
            }

            @Bean
            public ItemWriter<JPACustomerEntity> jpaItemWriter() {
                return (items) -> items.forEach(System.out::println);
            }

            @Bean
            public Step jpaStep() {
                return this.stepBuilderFactory.get("jpaStep")
                        .<JPACustomerEntity, JPACustomerEntity>chunk(10)
                        .reader(jpaCustomerItemReader(null))
                        .writer(jpaItemWriter())
                        .build();
            }

            @Bean
            public Job jpaJob() {
                return this.jobBuilderFactory.get("jpajob1")
                        .start(jpaStep())
                        .build();
            }
        }
        ```

      - Entity 클래스를 만들 때 데이터베이스 종류마다 테이블 이름이나 컬럼 이름 생성 방법이 다르므로 이를 고려해서 네이밍을 하고 JPA에서는 native SQL이 아니면 테이블 이름 대신에 Entity 이름과 속성을 사용해서 질의를 만들어야 한다.

   10. 기타 Reader

       NoSQL이나 Web 요청 등도 Reader로 사용 가능

9. **ItemProcessor**

   입력받은 데이터를 가공하기 위한 컴포넌트

   필수 요소는 아님

   입력 데이터를 다시 쓰는 작업(Migration)만 수행할 때는 생략

   파일에 기록된 데이터를 데이터베이스로 옮기는 작업이나 기존 데이터베이스의 데이터를 다른 데이터베이스로 옮기는 작업

   1. ItemProcessor를 이용하는 작업
      - 입력의 유효성 검사
      - 기존 서비스의 재사용
      - 스크립트 실행
      - 단일 아이템을 여러 서비스에 사용하는 경우 Chain을 구성해서 사용

10. **ItemWriter**

    Spring Batch의 출력을 담당

    1. 종류
       - File에 출력
       - RDBMS에 출력
       - NoSQL에 출력
       - 기타 출력 방식
         메일을 전송한다던가 메시지 전송에 이용
    2. csv 파일에 기록 - ItemWriter 수정: Step 부분을 수정

       ```java
       package com.example.batch_web.service;

       import com.example.batch_web.dto.JDBCCustomerDTO;
       import com.example.batch_web.dto.JDBCCustomerRowMapper;
       import lombok.RequiredArgsConstructor;
       import org.springframework.batch.core.Job;
       import org.springframework.batch.core.Step;
       import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
       import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
       import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
       import org.springframework.batch.core.configuration.annotation.StepScope;
       import org.springframework.batch.item.ItemWriter;
       import org.springframework.batch.item.database.JdbcCursorItemReader;
       import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
       import org.springframework.context.annotation.Bean;
       import org.springframework.context.annotation.Configuration;
       import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

       import javax.sql.DataSource;

       @Configuration
       @EnableBatchProcessing
       @RequiredArgsConstructor
       public class JDBCCustomerCursorService {
           private final JobBuilderFactory jobBuilderFactory;
           private final StepBuilderFactory stepBuilderFactory;

           // Reader 생성
           @Bean
           public JdbcCursorItemReader<JDBCCustomerDTO> JdbcCustomerDTOItemReader(DataSource dataSource) {
               return new JdbcCursorItemReaderBuilder<JDBCCustomerDTO>()
                       .name("customerItemReader")
                       .dataSource(dataSource)
                       .sql("select * from customer where city=?")
                       .rowMapper(new JDBCCustomerRowMapper())
                       .preparedStatementSetter(citySetter())
                       .build();
           }

           @Bean
           @StepScope
           public ArgumentPreparedStatementSetter citySetter() {
               return new ArgumentPreparedStatementSetter(new Object[]{"StamFord"});
           }

           // Writer 객체
           @Bean
           public ItemWriter<JDBCCustomerDTO> JdbcCustomerDTOItemWriter() {
               return (items) -> items.forEach(System.out::println);
           }

           // Step 생성
           @Bean
           public Step jdbcCopyFileStep() {
               return this.stepBuilderFactory.get("jdbcCopyFileStep")
                       .<JDBCCustomerDTO, JDBCCustomerDTO>chunk(10)
                       .reader(JdbcCustomerDTOItemReader(null))
                       .writer(JdbcCustomerDTOItemWriter())
                       .build();
           }

           @Bean
           public Job JdbcJob() {
               return this.jobBuilderFactory.get("jdbcJob")
                       .start(jdbcCopyFileStep())
                       .build();
           }
       }
       ```
