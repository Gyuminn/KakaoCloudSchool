# [Java] Lambda

1. **lambda**

   자바에서 함수형 프로그래밍을 지원하기 위해 만든 문법

   메서드의 매개변수로 메서드의 내용을 전달할 수 있도록 하기 위한 문법

   Anonymous Function을 생성하기 위한 문법

   **추상 메서드가 1개인 인터페이스에만 적용 가능**

   `(매개변수) -> {내용}`

   Runnable 인터페이스는 public void run() 메서드 1개만 소유

   ```java
   new Runnable() {
   	@Override
   	public void run() {
   		내용
   	}
   }

   // () -> {내용} 으로 변경 가능
   // 위 식에서는 run이라는 메서드 이름이 있어야 하지만 아래 식에서는 이름이 없음.
   ```

   1. 작성법

      ```java
      (자료형 매개변수이름, 자료형 매개변수이름…) → {내용}
      ```

      - 매개변수의 자료형 생략 가능
      - 매개변수가 1개일 때는 ‘( )’ 생략 가능
      - 내용이 한 줄인 경우 ‘{ }’와 ‘;’ 생략 가능
      - return을 사용할 수 있는데 { } 안에 return 문만 있는 경우에는 return 생략 가능

   2. **자바에서는 람다식을 주로 메서드의 매개변수로 사용하는데 그 이유는 람다식은 직접 호출이 안되고 인터페이스 변수에 담아서 호출해야 하기 때문이다.**
   3. 함수적 인터페이스
      - **@FunctionalInterface** 라는 어노테이션을 Interface 위에 작성하면 이 Interface는 하나의 추상 메서드만을 소유해야 한다.
      - 람다식으로 작성 가능한 인터페이스라는 것을 의미
      - 안드로이드 스튜디오나 Intelli J를 사용하는 경우 함수적 인터페이스를 anonymous class를 이용하는 문법으로 작성하면 lambda로 변경한다.
   4. **Runnable 인터페이스**를 Lambda 식으로 사용

      - Runnable 인터페이스는 public void run이라는 메서드 1개만 소유

        ```java
        package kakao.itstudy.network;

        public class RunnableLambda {
            public static void main(String[] args) {
                new Thread(new Runnable() {
                    // Runnable 인터페이스를 이용한 스레드 생성
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.sleep(1000);
                                System.out.println("일반적인 방식");
                            } catch (Exception e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                        }
                    }
                }) {
                }.start();

                new Thread(() -> {
                    for (int i = 0; i < 10; i++) {
                        try {
                            Thread.sleep(1000);
                            System.out.println("람다를 이용한 방식");
                        } catch (Exception e) {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                }).start();
            }
        }
        ```

   5. **Comparator 인터페이스**를 Lambda 식으로 표현

      - Comparator 인터페이스는 pulbic int compare(T o1, To2) 메서드 1개를 소유한 인터페이스
      - 제네릭을 사용한다.

        ```java
        package kakao.itstudy.network;

        import java.util.Arrays;
        import java.util.Comparator;

        public class ComparatorLambda {
            public static void main(String[] args) {
                // 데이터 정렬을 위해서 문자열 배열 생성
                String[] ar = {"축구", "야구", "배구", "족구", "농구", "당구"};

                Arrays.sort(ar, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }
                });
                System.out.println(Arrays.toString(ar));
                // 코드는 간결해졌지만 가독성이 떨어진다.
                Arrays.sort(ar, (o1, o2) ->
                    o1.compareTo(o2)
                );
                System.out.println(Arrays.toString(ar));
            }
        }
        ```

   6. **메서드 이름을 매개변수로 전달**

      코드를 작성하지 않고 메서드를 직접 넘기고자 하는 경우에 작성 방법

      - 클래스 메서드
        클래스이름 :: 메서드이름
      - 인스턴스 메서드
        인스턴스이름::메서드이름

      List의 forEach 메서드는 List를 순회하면서 매개변수로 대입된 메서드를 실행한다.

      이 때 대입되는 메서드는 매개변수는 1개이고 리턴 타입은 void인 메서드이어야 한다.

   7. 표준 람다 인터페이스
      - Consumer: 매개변수가 1갱이고 리턴이 없는 메서드를 소유한 인터페이스
      - Supplier: 매개변수가 없고 리턴이 있는 메서드를 소유한 인터페이스
      - Function: 매개변수가 1개 있고 리턴이 있는 메서드를 소유한 인터페이스
        데이터 변환에 사용하는 인터페이스
      - Operator: 매개변수가 2개 있고 리턴이 있는 메서드를 소유한 인터페이스
        연산을 수행해서 결과를 리턴하는 인터페이스
      - Predicate: 매개 변수가 1개 있고 리턴이 boolean인 메서드를 소유한 인터페이스
        필터링할 때 사용하는 인터페이스

2. **Stream**

   - 배열이나 List의 작업을 빠르게 수행하기 위해 추가된 API
   - 작업 처리 과정은 `생성 → 중간 처리 → 최종 처리` 형태로 동작
     중간 처리 과정은 여러 번 수행할 수 있다.
   - 데이터를 복제해서 수행하므로 원본에는 아무런 영향이 없음.

     ```java
     package kakao.itstudy.network;

     import java.util.ArrayList;
     import java.util.Iterator;
     import java.util.List;
     import java.util.stream.Stream;

     public class CollectionAccess {
         public static void main(String[] args) {
             List<String> list = new ArrayList<>();
             list.add("Java");
             list.add("Java Web Programming");
             list.add("Spring Framework");

             // 반복문을 이용하는 방밥
             int len = list.size();
             for (int i = 0; i < len; i++) {
                 System.out.print(list.get(i) + "\t");
             }
             System.out.println();

             // 이터레이터 이용
             Iterator<String> iterator = list.iterator();
             while (iterator.hasNext()) {
                 System.out.print(iterator.next() + "\t");
             }
             System.out.println();

             // 빠른 열거
             for (String temp : list) {
                 System.out.print(temp + "\t");
             }
             System.out.println();

             // 스트림 API 활용
             // 내부 반복자를 이용하기 때문에 가장 빠름
             Stream<String> stream = list.stream();
             stream.forEach(temp -> System.out.print(temp + "\t"));
         }
     }
     ```

   1. Stream API
      - [java.util.stream](http://java.util.stream) 패지키에서 제공
      - BaseStream이라는 상위 스트림 아래 Stream, IntStream, LongSrea, DoubleStream 클래스가 존재
      - 생성
        컬렉션이나 배열의 메서드를 이용해서 기존 데이터의 스트림을 생성해서 사용한다.
        Collection 인터페이스의 stream이나 parallelStream 메서드를 이용해서 생성가능하고 Arrays.stream 메서드를 이용해서 배열에서 생성 가능.
   2. 중간 연산

      생성된 스트림을 가지고 필터링, 매핑(변환), 정렬, 그룹화, 집계(합계, 평균, 최대, 최소 등) 작업을 수행할 수 있으며 여러가지를 조합해서 연산하는 것도 가능.

      - skip(long n): 건너뛰기
      - limit(long n): 개수 제한
      - distinct(): equals 메서드로 비교
      - filter(매개변수가 1개이고 Boolean을 리턴하는 메서드): true인 데이터만 골라서 리턴
      - map(매개변수가 1개이고 리턴이 있는 메서드를 이용해서 데이터의 자료형이나 값을 변경하는 중간 연산
      - sorted: 정렬을 해주는데 매개변수가 없으면 데이터의 compareTo 메서드를 이용해서 오름차순 정렬하고 Comparaotr<T> 인터페이스를 대입하면 인스턴스의 메서드를 이용해서 오름차순 정렬을 수행

   3. 최종 연산
      - 매칭: allMatch, anyMatch, noneMatch
      - 집계: count, findFirst, max, min, average, reduce, sum
        Optional이 붙는 자료형으로 리턴되는 경우는 null일 수 있으므로 null 여부를 확인하고 사용하라고 하는 것이다.
      - 루핑: forEach
      - 수집: collect - Collectors.to자료형()을 대입하면 중간 연산의 결과를 자료형(List, Set, Map)으로 변환
        Collectors.groupingBy(그룹화할 함수, 집계함수)를 대입하면 그룹화해서 결과를 Map으로 리턴을 한다.
        Map의 키는 자동으로 그룹화할 함수의 값이다.
   4. 집계 실습

      VO 역할을 수행할 클래스 - Student

      ```java
      package kakao.itstudy.stream;

      public class Student {
          private int num;
          private String name;
          private String gender;
          private String subject;
          private int score;

          public Student() {
              super();
          }

          public Student(int num, String name, String gender, String subject, int score) {
              super();
              this.num = num;
              this.name = name;
              this.gender = gender;
              this.subject = subject;
              this.score = score;
          }

          public int getNum() {
              return num;
          }

          public void setNum(int num) {
              this.num = num;
          }

          public String getName() {
              return name;
          }

          public void setName(String name) {
              this.name = name;
          }

          public String getGender() {
              return gender;
          }

          public void setGender(String gender) {
              this.gender = gender;
          }

          public String getSubject() {
              return subject;
          }

          public void setSubject(String subject) {
              this.subject = subject;
          }

          public int getScore() {
              return score;
          }

          public void setScore(int score) {
              this.score = score;
          }

          @Override
          public String toString() {
              return "Student{" +
                      "num=" + num +
                      ", name='" + name + '\'' +
                      ", gender='" + gender + '\'' +
                      ", subject='" + subject + '\'' +
                      ", score=" + score +
                      '}';
          }
      }
      ```

      Stream을 사용할 클래스 - StreamMain

      ```java
      package kakao.itstudy.stream;

      import java.util.ArrayList;
      import java.util.List;
      import java.util.Map;
      import java.util.OptionalDouble;
      import java.util.stream.Collectors;

      public class StreamMain {
          public static void main(String[] args) {
              // Student 클래스의 List 생성
              List<Student> list = new ArrayList<>();
              list.add(new Student(1, "김규민", "남자", "컴공", 97));
              list.add(new Student(2, "천규민", "여자", "기계", 93));
              list.add(new Student(3, "이규민", "남자", "전자", 98));
              list.add(new Student(4, "남규민", "남자", "화학", 99));
              list.add(new Student(5, "박규민", "여자", "생명", 87));

              // score의 합계
              // Student를 Student.getScore 메서드의 결과를 이용해서 정수로 변환
              int sum = list.stream().mapToInt(Student::getScore).sum();
              System.out.println("점수의 합계: " + sum);

              // 평균 구하기
              OptionalDouble avg = list.stream().mapToInt(Student::getScore).average();
              // Optional이 붙는 자료형은 null 여부를 확인 후 수행
              if (avg.isPresent() == true) {
                  System.out.println("평균: " + avg.getAsDouble());
              } else {
                  System.out.println("평균을 구할 수 없음");
              }

              // reduce - 집계
              // 매개변수가 2개이고 리턴이 있는 메서드를 제공
              // 처음 2개의 데이터를 가지고 메서드를 호출해서 결과를 만들고
              // 그 다음부터는 결과와 다음 데이터를 가지고 메서드를 호출한다.
              sum = list.stream().mapToInt(Student::getScore).reduce(0, (n1, n2) -> n1 + n2);

              // 남자만 추출해서 List로 변환
              List<Student> manList = list.stream().filter(student -> student.getGender().equals("남자")).collect(Collectors.toList());
              System.out.println(manList);

              // gender 별로 그룹화해서 score의 평균 구하기
              Map<String, Double> genderMap = list.stream().collect(Collectors.groupingBy(Student::getGender, Collectors.averagingDouble(Student::getScore)));
              System.out.println(genderMap.get("남자"));
              System.out.println(genderMap.get("여자"));
          }
      }
      ```

3. **병렬 처리**

   1. 용어

      - Multi Processing
        2개 이상의 프로세스가 동작 중인 것 - 프로세서가 여러 개
      - Multi Threading
        2개 이상의 스레드가 동작 중인 것 - 하나의 프로세서로 가능
      - Parellel Processing
        동시에 작업을 처리

        - 데이터 병렬성
          많은 양의 데이터를 나누어서 처리
        - 작업 병렬성
          동시에 발생하는 작업을 각각의 스레드에서 병렬로 처리 - **Web Server**
          스트림을 만들 때 parallelStream을 호출하거나 스트림을 만든 후 parallel()을 호출하면 데이터를 나누어서 병렬로 처리를 한다.

        ```java
        package kakao.itstudy.stream;

        import java.util.Arrays;
        import java.util.List;

        public class ParallelMain {
            public static void main(String[] args) {
                List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

                // 일반 스트림으로 1초씩 쉬기
                long start = System.currentTimeMillis();
                list.stream().forEach(imsi -> {
                    try{
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                });
                long end = System.currentTimeMillis();
                System.out.println("걸린 시간: " + (end - start));

                // 병렬 스트림으로 1초씩 쉬기
                start = System.currentTimeMillis();
                list.stream().parallel().forEach(imsi -> {
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                });
                end = System.currentTimeMillis();
                System.out.println("걸린 시간: " + (end - start));
                System.out.println(Runtime.getRuntime().availableProcessors());
            }
        }
        ```
