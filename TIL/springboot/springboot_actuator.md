# [Spring Boot] Actuator

1. **Actuator**

   Spring의 모니터링이나 메트릭과 같은 기능을 HTTP와 JMX를 통해 제공하는 키

   실행 중인 애플리케이션의 내부를 볼 수 있게 하고 제어를 할 수 있도록 해주는 방법을 제공

   확인 가능한 항목

   - 애플리케이션 환경에서 사용할 수 있는 구성 속성들
   - 애플리케이션에 포함된 다양한 패키지의 로깅 레벨
   - 메모리
   - 요청 횟수
   - 애플리케이션 상태

   1. 사용 설정

      ```java
      implementation 'org.springframework.boot:spring-boot-starter-actuator'
      ```

      actuator라는 EndPoint가 제공됨

      이 경로를 변경하고자 하는 경우 application.properties에 작성(실제 수정하는 일은 거의 하지 않음)

      ```java
      management.endpoints.web.base-path = /커스텀경로
      ```

   2. 기본 제공 EndPoint - /actuator 다음에 추가
      - auditevents
        호출된 Audit 이벤트 정보를 표시 - AuditEventRepository 빈이 필요
      - beans
        스프링 빈 리스트를 표시
      - caches
        사용 가능한 캐시를 표시
      - conditions
        자동 구성 조건 내역을 생성
      - configprops
        @Configurationproperties의 속성 리스트를 표시
      - env
        애플리케이션에서 사용할 수 있는 환경 속성
      - health
        애플리케이션의 상태 정보
      - httptrace
        가장 최근에 이뤄진 100건의 요청 기록을 표시하는데 HttpTraceRepository 빈이 필요
      - info
        개발자가 설정한 정보를 표시
      - intergrationgraph
        스프링 통합 그래프를 표시하는데 spring-intergration-core 모듈의 의존성 필요
      - loggers
        로그 구성을 표시하고 수정
      - metrics
        메트릭 정보
      - mapping
        @RequestMapping의 매핑 정보를 표시
      - quartz
        Quartz 스케쥴러 작업에 대한 정보를 표시
      - scheduledtasks
        예약된 작업
      - sessions
        세션 확인
      - shutdown
        정상적으로 종료할 수 있는지 여부
      - startup
        시작 단계 데이터를 표시
      - threaddump
        스레드 덤프를 수행
   3. Spring MVC나 WebFlux를 사용하는 경우 추가되는 End Point
      - headdump
        파일로 변환하는 VM 종류에 따라 다른 포맷을 사용
      - logfile
        로그 파일의 내용을 확인
      - Prometheus
        Prometheus 서버에서 스크랩할 수 있는 형식으로 메트릭 정보를 표시하는데 micrometer-registry-prometheus 의존성을 설정해야 함.

2. **활용**

   1. application.yml에 EndPoint를 노출시키는 설정

      ```yaml
      server:
        port: 80

      management:
        endpoints:
          web:
            exposure:
              include: "*"
      ```

   2. 노출된 URL을 확인

      /actuator

   3. health는 현재 상태

      - 자세한 정보를 얻고자 하는 경우에는 application.yml에 속성을 추가

        ```yaml
        server:
          port: 80

        management:
          endpoints:
            web:
              exposure:
                include: "*"

          endpoint:
            health:
              show-details: always
        ```

      - 데이터베이스 접속 정보가 있는 경우 외부 데이터베이스 건강 상태도 출력 가능
        JDBC 데이터 소스, redis, ElasticSearch, Cassandra, JMS 메시지 브로커 등

   4. beans는 현재 생성된 모든 Bean의 내역
