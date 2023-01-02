# [Java] Thread

1. **동기(Synchronize)와 비동기(Asynchronize)**
   - 동기: 작업을 순서대로 하나씩 처리
   - 비동기: 하나의 작업이 완료되기 전에 다른 작업을 처리할 수 있는 방식
2. **Thread**

   1. Task

      작업, Process, Thread 모두를 Task라고 한다.

   2. Process

      운영체제에서 자원을 할당받아서 독립적으로 수행되는 현재 실행중인 프로그램

   3. Thread

      Process 내에서 생성되는 작업의 단위로 실행 중 다른 작업으로 제어권을 넘길 수 있다.

      **비동기적으로 작업을 수행하고자 할 때 사용**

      - Single Thread
        현재 실행 중인 스레드가 1개인 경우
      - Multi Thread
        현재 실행중인 스레드가 2개 이상인 경우 하나의 JVM 안에서 동작
        CPU의 사용률을 향상시키는 것이 장점이다.
        **동기화나 교착 상태 등을 방지하기 위한 코드가 삽입되어야 해서 프로그래밍이 어려워짐**
        **너무 많은 스레드를 생성하면 Thrashing(Overhead - 스레드가 교체되는 시기에 현재까지 작업한 내용을 저장하고 수행할 스레드의 Context를 불러오는 시간이 늘어남 - Context Switching) 현상이 발생해서 성능이 저하될 수 있음.**

3. **Thread 생성 및 실행**
   - Thread 클래스나 Runnable 인터페이스를 이용할 수 있음.
   - Thread 클래스의 경우는 상속받아서 `public void run` 이라는 메서드에 스레드로 수행할 내용을 작성한 후 인스턴스를 생성하고 start()를 호출하면 된다.
   - Runnable 인터페이스의 경우는 Runnable 인터페이스를 구현한 클래스를 만들어서 public void run 이라는 메서드에 스레드로 수행할 내용을 작성하고 인스턴스를 만든 후 이 인스턴스를 Thread 클래스의 생성자에 대입해서 인스턴스를 만든 후 start()를 호출해야 한다.
4. **Thread를 만들어서 실행하는 것과 그렇지 않은 경우의 차이**

   ```java
   package kakao.itstudy.thread;

   public class ThreadAndProcess {
       public static void main(String[] args) {
           // 스레드를 사용하지 않은 경우
           /*
           new Thread() {
               public void run() {
                   for (int i = 0; i < 10; i++) {
                       try {
                           Thread.sleep(1000);
                           System.out.println(i);
                       } catch (Exception e) {

                       }
                   }
               }
           }.run();

            */

           // 스레드를 사용하는 경우
           new Thread() {
               public void run() {
                   for (int i = 0; i < 10; i++) {
                       try {
                           Thread.sleep(1000);
                           System.out.println(i);
                       } catch (Exception e) {

                       }
                   }
               }
           }.start();

           new Thread() {
               public void run() {
                   for (int i = 0; i < 10; i++) {
                       try {
                           Thread.sleep(1000);
                           System.out.println(i);
                       } catch (Exception e) {

                       }
                   }
               }
           }.start();

       }
   }
   ```

5. **Thread 클래스**

   1. 생성자
      - Thread()
        name은 jvm이 설정
      - Thread(String name)
        name을 설정해서 생성
      - Thread(Runnable runnable)
        Runnable 인스턴스를 받아서 생성
      - Thread(Runnable runnable, String name)
   2. 메서드
      - static void sleep(long msec) throws InterruptException
        현재 스레드를 msec 밀리초 동안 대기
      - void run()
        스레드가 수행할 내용을 가진 메서드
      - void start()
        스레드를 시작시키는 메서드
   3. 스레드의 생성과 시작

      ```java
      package kakao.itstudy.thread;

      // Thread 클래스로부터 상속받는 클래스
      class ThreadEx extends Thread {
          @Override
          public void run() {
              // 1초마다 스레드 이름을 10번 출력
              for (int i = 0; i < 10; i++) {
                  try {
                      Thread.sleep(1000);
                      System.out.println(getName());
                  } catch (Exception e) {

                  }
              }
          }
      }

      public class ThreadCreateMain {
          public static void main(String[] args) {
              // 클래스를 상속받은 경우
              // 대부분의 경우는 변수를 만들 때는 상위 클래스 이름을 사용
              // ThreadEx th1 = new ThreadEx();
              Thread th1 = new ThreadEx();
              th1.start();

              // class로 만든 ThreadEx는 계속 메모리에 상주하게 된다.
              // 한 번만 사용하고 싶기 때문에 anonymous 클래스를 사용해보자.
              Runnable r = new Runnable() {
                  @Override
                  public void run() {
                      // 1초마다 스레드 이름을 10번 출력
                      for (int i = 0; i < 10; i++) {
                          try {
                              Thread.sleep(1000);
                              System.out.println(i);
                          } catch (Exception e) {

                          }
                      }
                  }
              };
              Thread th2 = new Thread(r);
              th2.start();
          }
      }
      ```

6. **Thread 상태 - 수명 주기**
   - NEW: 생성만 된 상태
   - RUNNABLE: 스레드가 실행 중인 상태
   - WAITING: 다른 스레드가 notify나 notifyAll을 불러주기를 기다리는 상태
   - TIMED_WAITING: 일정 시간 대기하고 있는 상태
   - BLOCKED: CPU를 사용할 필요가 없는 입출력 작업을 수행 중인 상태
   - TERMINATED: 스레드가 종료된 상태
     Thread는 run 메서드의 내용이 종료되면 종료된다.
     다른 Thread를 강제 종료시킬 수 있다.
     한 번 종료된 스레드는 다시 실행시킬 수 없다.
7. **Daemon Thread**
   - Daemon이 아닌 스레드가 동작 중이지 않으면 종료되는 스레드
   - 다른 스레드의 보조적인 역할(중요하지 않은 작업)을 수행하는 스레드
   - 스레드를 생성하고 시작하기 전에 setDaemon에 true만 설정하면 된다.
   - **대표적인 Daemon Thread가 main 메서드를 실행시켜 만들어지는 main thread 와 garbage collector이다.**
8. **Thread의 Priority(우선 순위)**
   - 모든 스레드의 우선 순위는 동일 - 어떤 스레드를 먼저 수행하고 어떤 스레드가 자주 시행되는지를 알 수 없다.
   - 우선 순위를 설정하면 정확하게 동작하지는 않지만 그래도 우선 순위가 높은 스레드가 먼저 또는 자주 실행될 가능성이 높아진다.
   - 우선 순위 설정은 setPriority라는 메서드로 하게 되는데 메서드의 매개변수는 정수인데 숫자가 높은 것이 높다.
   - 우선 순위 설정을 위해서 JDK 에서는 Thread 클래스에 static final 변수(Field Summary)를 제공
     - MIN_PRIORITY
     - NORM_PRIORITY
     - MAX_PRIORITY
9. **Thread의 강제 종료**
   - run 메서드에서 InterruptException 이 발생할 떄 return 하도록 만들고 강제로 종료시키고자 하는 스레드에서 interrupt()라는 메서드 호출
   - interrupt()는 InterruptedException을 발생시키는 메서드이다.
10. **Thread의 실행 제어 메서드**
    - join 일정 시간동안 작업을 수행하도록 해주는 메서드 - Time Sharing(시분할) System
    - suspend: 스레드를 일정 정지시키는 메서드
    - resume: 일시 정지된 스레드를 다시 실행시키는 메서드
    - yield: 다른 Thread에게 실행 상태를 양보하고 자신은 준비 상태로 변경
11. **MultiThread**

    - 2개 이상의 스레드가 동시에 실행 중인 상황
    - 장점
      - 작업이 수행 도중 대기 시간이 있는 경우 대기 시간을 효율적으로 사용 가능
      - 긴 작업과 짧은 작업이 있는 경우 짧은 작업이 긴 작업을 무한정 기다리는 것도 방지.
    - 단점

      - 메모리 사용량이 증가
      - 너무 많은 스레드가 동시에 실행되면 처리 속도가 느려질 수 있음.
      - 공유 자원 사용의 문제점
        - critical section(임계 영역 - 공유 자원을 사용하는 코드 영역)에서의 데이터 수정 문제
        - 생산자와 소비자 문제 - 생산자가 생산을 하지 않은 상태에서 소비자가 공유 자원에 접근하는 문제
        - **Dead Lock(교착 상태) 문제** - 결코 발생할 수 없는 사건을 무한정 기다리는 것
      - 공유 자원 수정 문제

        - 하나의 스레드가 사용 중인 공유 자원을 다른 스레드가 수정하면서 발생할 수 있는 문제
        - 공유 자원 수정 문제 발생

          ```java
          package kakao.itstudy.thread;

          // 스레드 작업을 위한 클래스
          class ShareData implements Runnable {
              private int result; // 합계를 저장할 속성
              private int idx; // 합계를 구할 때 사용할 인덱스

              public int getResult() {
                  return result;
              }

              // idx의 값을 1씩 증가시키면서 result에 더해줄 메서드
              private void sum() {
                  for (int i = 0; i < 1000; i++) {
                      idx++;
                      try {
                          Thread.sleep(1);
                      } catch (Exception e) {
                      }
                      result = result + idx;
                  }
              }

              @Override
              public void run() {
                  sum();
              }
          }

          public class MutexMain {
              public static void main(String[] args) {
                  ShareData data = new ShareData();

                  Thread th1 = new Thread(data);
                  th1.start();
                  Thread th2 = new Thread(data);
                  th2.start();
                  try {
                      Thread.sleep(5000);
                      System.out.println(data.getResult());
                  } catch (Exception e) {
                  }

              }
          }
          ```

          현 시점까지 실행해보면 1 - 2000까지의 합이 나올 것 같은데 그렇지도 않고 실행할 때마다 결과도 달라진다.
          어느 한 쪽이 작업을 마무리하기 전에 다른 작업을 시작하기 때문이다.
          이 문제를 해결하는 방법은 2가지 정도 되는데 첫째는 스레드로 작업할 때 데이터를 복제하지 말고 원본에 작업하라고 하는 것이고 이 예약하고 volatile 이다(권장 x).
          작업을 순차적으로 실행하도록 하는 것이다.
          `synchronized(인스턴스){}` - { } 내에서는 인스턴스를 사용하는 부분이 동시에 실행될 수 없음, 권장.
          메서드에 synchoronized를 추가. 메서드가 동기화가 된다.(권장 x - 자원 공유의 한계)

      - 생산자와 소비자 스레드 문제(producer-consumer problem)

        - 생산자 스레드가 만든 자원을 소비자 스레드가 동시에 사용하는 것은 가능하다.
        - 생산자 스레드가 자원을 아직 생성하지 못했는데 소비자가 자원을 사용하려고 하면 자원이 만들어지지 않아서 예외가 발생하게 됨.
          이런 경우에는 사용할 자원이 없으면 소비자 스레드를 waiting을 해야 하고 생산자 스레드는 자원을 생성하면 소비자 스레드에게 signal(notify)을 주어야 한다.
          java에서는 Object 클래스에서 waiting을 위한 wait 메서드와 notify와 notifyAll 메서드를 제공하는데 이 메서드들은 synchronized 메서드 안에서만 동작

          ```java
          package kakao.itstudy.thread;

          import java.util.ArrayList;
          import java.util.List;

          // 공유 자원 클래스
          class Product {
              // 공유 자원
              List<Character> list;

              // 공유 자원을 넘겨받기 위한 생성자
              public Product(List<Character> list) {
                  this.list = list;
              }

              // 공유 자원에 데이터를 삽입하는 메서드
              public void put(char ch) {
                  list.add(ch);
                  System.out.println(ch + "를 입고");
                  try {
                      Thread.sleep(1000);
                  } catch (Exception e) {
                  }
                  System.out.println("재고 수량:" + list.size());
              }

              // 공유 자원을 소비하는 메서드
              public void get() {
                  // 첫 번째 데이터 꺼내기
                  Character ch = list.remove(0);
                  System.out.println(ch + "를 출고");
                  try {
                      Thread.sleep(1000);
                  } catch (Exception e) {
                  }
                  System.out.println("재고 수량:" + list.size());
              }
          }

          // 생산자 스레드
          class Producer extends Thread {
              private Product product;

              public Producer(Product product) {
                  this.product = product;
              }

              // 스레드로 수행할 내용
              public void run() {
                  for (char ch = 'A'; ch <= 'Z'; ch++) {
                      product.put(ch);
                  }
              }
          }

          // 소비자 스레드
          class Customer extends Thread {
              private Product product;

              public Customer(Product product) {
                  this.product = product;
              }

              public void run() {
                  for (int i = 0; i < 26; i++) {
                      product.get();
                  }
              }
          }

          public class ProducerConsumerMain {
              public static void main(String[] args) {
                  // 공유 자원을 생성
                  List<Character> list = new ArrayList<>();
                  Product product = new Product(list);

                  // 생산자와 소비자 스레드 생성
                  Producer producer = new Producer(product);
                  Customer customer = new Customer(product);

                  // 스레드 시작
                  producer.start();
                  customer.start();
              }
          }
          ```

          실행을 하면 소비자 스레드 예외가 발생해서 중지가 된다. Product의 list에 데이터가 없는데 꺼내려고 해서 발생하는 문제이다.
          데이터를 꺼내서 사용하는 메서드에서 데이터가 없으면 기다리라고 하고(wait) 데이터를 생성하는 메서드에서는 데이터를 생성하면 notify를 붙여서 대기 중인 스레드가 작업을 수행하도록 신호를 보낸다.

      - Dead Lock
        - 결코 발생할 수 없는 사건을 무한정 기다리는 것
        - 동기화된 메서드 안에서 다른 동기화된 메서드를 호출하는 것처럼 동기화된 코드 안에 동기화된 코드가 존재하는 경우에 발생하는 경우가 많음.
      - Semaphore
        - 동시에 수행할 수 있는 스레드의 개수를 설정할 수 있는 클래스
        - 공유 자원이 여러 개인 경우 각각의 Lock을 설정하게 되는데 이런 경우 모두 사용 중이라는 것을 알려주면 모든 스레드를 전부 조사해야 한다.
          사용할 수 있는 Lock을 관리하는 인스턴스를 하나 만들어서 사용하기 전에 사용할 수 있는 Lock의 개수를 확인해서 수행하면 관리하기가 편해진다.
        - 공유 자원을 사용할 때 acquire()를 호출하고 공유 자원이 사용이 끝나면 release 호출
