# [Java] Open Source 활용

1. **Open Source**

   Source Code를 누구나 열람할 수 있고 사용할 수 있도록 만든 것.

   1. 장점
      - 편리성 - 직접 만들려고 하면 시간과 비용이 많이 소모
      - 성능이 우수할 가능성이 높음.
      - 버전이 높다면 신뢰성이 높음.
   2. Java Open Source Project

      - Hadoop: 아파치 재단에서 만든 오픈 소스 분산 처리 시스템
      - Cassandra Database: NoSQL
      - Lucene: 검색 엔진 플랫폼

      Gradle, Jenkins 등의 도구도 있음.

      - Apache Web Server
      - Log4j
      - Commons Project: 자바의 다양한 유틸

2. **Maven**

   1. build
      - source code를 compile하고 최종적인 실행 파일로 만드는 작업
      - 일반 Application은 jar 파일로 생성하고 Web Applicationdms war 그리고 android는 apk 파일을 생성
      - jdk/bin 디렉토리에 있는 명령어로 작업을 수행할 수 있음.
      - 외부 라이브러리를 이용해서 build script에 빌드할 내용을 기술하고 수행 - **Maven(pom.xml)이나 Gradle(build.gradle)**
      - CI 도구에서 자동으로 빌드를 하고 정기적으로 빌드를 할 수 있는지 확인 한 후 Deploy까지 자동으로 수행하는 경우도 있음.
        Jenkins가 많이 사용됨.
   2. build tool
      - Ant
      - Maven
      - Gradle
      - Jenkins
   3. Maven

      POM(Project Object Model - pom.xml) 이라는 것에 기초를 두고 빌드, 테스트, 도큐먼테이션, 성과물의 배치 등의 라이프 사이클 전체를 관리하는 도구

   4. pom.xml 파일
      - maven의 설정 파일
      - **repositories**: 다운로드 받을 저장소 설정 - Oracle을 사용하는 경우 설정
      - **dependencies**: 외부 라이브러리의 의존성 설정
   5. Maven 프로젝트 생성
      - Maven 프로젝트를 생성
      - 기존 프로젝트를 Maven 프로젝트로 변환
   6. 의존성 설정 - 외부 라이브러리 사용
      - Java Application
        Build Path에 사용하고자 하는 라이브러리를 추가해주어야 한다.
      - Java Web Application
        WEB-INF/lib 디렉토리에 라이브러리를 추가해주어야 한다.
      - build tool을 사용하는 경우
        설정 파일에 작성만 하면 다운로드를 받아서 로컬에 저장을 하고 build를 할 때 그 라이브러리를 프로젝트에 포함시켜 준다.
   7. Maven 프로젝트를 이용한 MySQL 라이브러리 사용
      - Java Application을 사용
      - Java Opensource Repository: **[www.mvnrepository.com](http://www.mvnrepository.com)**
        위 링크에서 mysql 선택하고 jar 선택하고 다운로드
      - 프로젝트를 선택하고 마우스 오른쪽을 눌러서 [Build path] - [Configure
      - 일반 프로젝트에서 Intellij 기준 프로젝트 우클릭을 하고 [add Framework suppor] 에서 Maven 선택
      - 프로그램 다시 실행
      - 의존성 설정 후 동작
        pom.xml 파일에 작성도니 dependencies를 확인한 후 현재 사용자의 .m2라는 디렉토리에서 라이브러리의 존재 여부를 확인한 후 없으면 중앙 저장소에서 다운로드 받아서 .m2에 저장하고 빌드를 할 때 다운로드 받은 라이브러리를 프로젝트에 복사한 후 실행
      - 라이브러리를 다시 다운로드
        마우스 오른쪽을 클릭하고 [Run AS] 메뉴에서 claen을 선택한 후 install 선택

3. **Junit**

   1. 개요
      - 단위테스트(전체 프로그램을 구성하고 있는 기본 단위 프로그램이 정상적으로 동작하는지 확인 - 클래스나 메서드)를 수행해주는 프레임워크
      - TDD(테스트 주도 개발)에서 가장 많이 사용되는 테스트 라이브러리
      - Eclipse에 내장
   2. 테스트 방법
      - TestClass로부터 상속받는 클래스를 생성 - 클래스 안에 만들어진 모든 메서드를 테스트
      - Object 클래스로부터 상속받는 클래스를 만들고 테스트하고자 하는 메서드 위에 @Test를 추가
      - 결과를 테스트 할 때 asssertThan 메서드나 assertEquals 메서드의 첫 번째 인수로 실제 값을 그리고 두 번째 인수에 기대하는 값을 기재해서 호출하는데 이 떄 양쪽의 값이 다르면 AssertionError 예외가 발생
   3. TestCase 클래스를 이용한 테스트

      - 테스트를 수행하기 위한 클래스 생성

        ```java
        package test.java;

        import junit.framework.TestCase;
        import main.java.Source;

        public class SourceTest extends TestCase {
            public void testmethod() {
                System.out.println(new Source().add(100, 200));
            }
        }
        ```

      - @Test를 이용

   4. 실제 테스트를 수행할 때는 별도의 패키지에서 수행
      - 배포를 제외하기 위해서
      - 최근의 프레임워크들은 프로젝트를 생성할 때 test를 위한 디렉토리를 제공하고 그 디렉토리에 작성한 내용은 빌드를 제외시킴.

4. **CSV 활용**

   1. csv
      - Comma Seperated Values의 약자로 항목을 쉼표로 구분해서 만든 텍스트 파일로 확장자는 csv
        최근에는 분할할 수 있는 텍스트는 전부 csv로 간주
      - 대부분의 경우 첫 번째 라인의 데이터는 헤더 항목(컬럼의 이름)이고 두 번째 라인부터가 실제 데이터인 경우가 많음.
      - 변하지 않는 고정적인 데이터를 제공하고자 할 때 주로 이용.
      - 외부 라이브러리를 이용해서 사용하는 경우가 많음.
      - super-csv라는 외부 라이브러리가 csv 읽고 쓰기를 편리하게 해준다.
   2. 사용 설정
      - 일반 프로젝트의 경우는 라이브러리를 다운로드 받아서 build-path에 추가
      - Maven이나 Gradle 프로젝트의 경우는 의존성을 설정(pom.xml 파일에 dependencies 추가)
   3. 샘플로 사용할 csv 파일을 루트에 추가 - 파일의 확장자는 의미 없음.

      ```java
      name, age, birth, email, nickname
      김규민, 26, 1997-01-29, gyumin@gmail.com, "마라무쌍"
      나규민, 30, 1992-03-04, ggg@gmail.com, "탕화쿵푸"
      ```

   4. VO 클래스 생성 - Player

      ```java
      package main.java;

      import java.util.Date;

      public class Player {
          private String name;
          private int age;
          private Date birth;
          private String email;
          private String nickname;

          public Player() {
              super();
          }

          public Player(String name, int age, Date birth, String email, String nickname) {
              super();
              this.name = name;
              this.age = age;
              this.birth = birth;
              this.email = email;
              this.nickname = nickname;
          }

          public String getName() {
              return name;
          }

          public void setName(String name) {
              this.name = name;
          }

          public int getAge() {
              return age;
          }

          public void setAge(int age) {
              this.age = age;
          }

          public Date getBirth() {
              return birth;
          }

          public void setBirth(Date birth) {
              this.birth = birth;
          }

          public String getEmail() {
              return email;
          }

          public void setEmail(String email) {
              this.email = email;
          }

          public String getNickname() {
              return nickname;
          }

          public void setNickname(String nickname) {
              this.nickname = nickname;
          }

          @Override
          public String toString() {
              return "Player{" +
                      "name='" + name + '\'' +
                      ", age=" + age +
                      ", birth=" + birth +
                      ", email='" + email + '\'' +
                      ", nickname='" + nickname + '\'' +
                      '}';
          }
      }
      ```

   5. 라이브러리를 사용하지 않고 csv 파싱하기 - NoLibraryCSVMain

      ```java
      public class NoLibraryCSVMain {

      	public static void main(String[] args) {
      		//문자열 파일을 읽기 위한 스트림 생성
      		try(BufferedReader br =
      			new BufferedReader(
      				new InputStreamReader(
      					new FileInputStream(
      						"./volley.csv")))) {
      			//파일의 경로 확인을 위해서 작성
      			//System.out.println(br);

      			//첫줄은 데이터가 아니므로 첫줄을 배제하기 위한 변수
      			boolean flag = false;

      			//파싱한 결과를 저장하기 위한 List
      			List<Player> list = new ArrayList<>();

      			while(true) {
      				String line = br.readLine();
      				if(line == null) {
      					break;
      				}
      				//첫 줄을 읽지 않기 위해서 작성
      				if(flag == false) {
      					flag = true;
      					continue;
      				}
      				//System.out.println(line);
      				//, 단위로 분할
      				String [] ar = line.split(",");
      				//System.out.println(ar[0]);

      				Player player = new Player();
      				player.setName(ar[0]);
      				player.setAge(Integer.parseInt(ar[1]));

      				String birth = ar[2];
      				//위의 문자열을 Date 타입으로 변환해서 대입
      				SimpleDateFormat sdf =
      					new SimpleDateFormat("yyyy-MM-dd");
      				Date date = sdf.parse(birth);
      				player.setBirth(date);

      				player.setEmail(ar[3]);
      				player.setNickname(ar[4]);

      				//리스트에 추가
      				list.add(player);
      			}

      			for(Player player : list) {
      				System.out.println(player);
      			}

      		}catch(Exception e) {
      			System.out.println(e.getLocalizedMessage());
      			e.printStackTrace();
      		}

      	}

      }
      ```

   6. super-csv 라이브러리를 사용하여 csv 파싱하기

      ```java
      package main.java;

      import java.nio.file.Files;
      import java.nio.file.Path;
      import java.nio.file.Paths;
      import java.util.ArrayList;
      import java.util.List;

      public class SuperCSVMain {
          public static void main(String[] args) {
              // 저장할 List 생성
              List<Player> list = new ArrayList<>();
              // 읽을 파일의 경로 생성
              Path path = Paths.get("./volley.csv");
              // 제약조건 설정
              // super csv 라이브러리를 직접 추가해야 함. 다운받지 않은 상태.

              CellProcessor[] processors = new CellProcessor[]{
                      new NotNull(),
                      new ParseInt(new NotNull()),
                      new ParseDate("yyyy-MM-dd"),
                      new Optional(),
                      new Optional()
              };

              // Csv 읽기 위한 경로 생성
              try (ICsvBeanReader beanReader = new CsvBeanReader(Files.newBufferedReader(path), CsvPreference.STANDARD_PREFERENCE)) {
                  // 첫줄 읽어오기
                  String[] header = beanReader.getHeader(true);
                  System.out.println(Arrays.toString(header));

                  // 데이터 읽어서 list에 추가
                  Player player1 - null;
                  // 한 줄씩 읽어서 header에 맞추어 Player 클래스 타입의
                  // 인스턴스를 생성
                  while((player1 = beanReader.read(Player.class, header, processors)) != null) {
                      list.add(player1);
                  }
                  System.out.println(list);
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
              }
          }
      }
      ```

5. **JSON Parsing**

   1. JSON(JavaScript Object Notation)
      - 자바스크립트의 데이터 표현볍을 이용해서 데이터를 표현하는 문자열 서식
      - { } 안에 Key와 Value 형태로 객체(Object)를 생성하고 배열(Array)은 [ ] 안에 데이터를 나열
      - 문자열은 큰 따옴표를 해야 하고 key는 무조건 문자열
   2. Java에서 JSON 파싱
      - org.json.json이라는 라이브러리 사용 가능
      - JSONObject라는 클래스와 JSONArray라는 클래스가 존재하는데 json 문자열을 파악해서 위 2개 중 하나의 클래스 생성자에 문자열을 대입해서 인스턴스를 생성한다.
        데이터를 꺼내올 때는 `get자료형(키 또는 인덱스)` 를 이용해서 데이터를 추출하면 된다.
   3. JSON 파싱

      - 샘플 데이터: https://jsonplaceholder.typicode.com/todos
        userId(정수), id(정수), title(문자열), completed(boolean)로 구성된 객체의 배열
      - 프로젝트에 json 라이브러리의 의존성 설정
        dependency 추가(org.json)
      - VO 클래스 생성

        ```java
        package main.java;

        import java.util.Date;

        public class Player {
            private String name;
            private int age;
            private Date birth;
            private String email;
            private String nickname;

            public Player() {
                super();
            }

            public Player(String name, int age, Date birth, String email, String nickname) {
                super();
                this.name = name;
                this.age = age;
                this.birth = birth;
                this.email = email;
                this.nickname = nickname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }

            public Date getBirth() {
                return birth;
            }

            public void setBirth(Date birth) {
                this.birth = birth;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            @Override
            public String toString() {
                return "Player{" +
                        "name='" + name + '\'' +
                        ", age=" + age +
                        ", birth=" + birth +
                        ", email='" + email + '\'' +
                        ", nickname='" + nickname + '\'' +
                        '}';
            }
        }
        ```

      - main 메서드를 소유한 클래스를 생성 - JSONParsingMain

        ```java
        import org.json.JSONArray;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;

        public class JSONParsingMain {
            public static void main(String[] args) {
                // 1. 데이터 다운로드
                // 다운로드 받은 문자열을 저장하기 위한 변수
                String json = null;
                try {
                    // 다운로드 받기 위한 URL을 생성
                    // 한글이 포함되어 있으면 그 부분은
                    // URLEncoder.encode 메서드를 이용해서 인코딩한 후 생성
                    URL url = new URL("https://jsonplaceholder.typicode.com/todos");
                    // URL에 연결
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    // 옵션 설정
                    con.setRequestMethod("GET"); // 요청 방식
                    con.setConnectTimeout(30000); // 접속 요청 제한 시간
                    con.setUseCaches(false); // 캐싱된 데이터 사용 여부

                    // Kakao와 같은 Open API들은 key를 요구하는 경우가 있는데 그 경우에는
                    // con.setRequestProperty(키이름, 실제 키)

                    // 문자열을 읽기 위한 스트림 생성
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    // 많은 양의 문자열을 읽기
                    StringBuilder sb = new StringBuilder();
                    while(true) {
                        String line = br.readLine();
                        if(line == null) {
                            break;
                        }
                        sb.append(line + "\n");
                    }
                    json = sb.toString();
                    // System.out.println(json);
                } catch(Exception e) {
                    System.out.println("데이터 다운로드 실패");
                    // 프로그램 종료
                    // System.exit(0);
                    // main 종료
                    return;
                }

                // 2. 다운로드 받은 데이터를 파싱
                List<ToDoVo> list = new ArrayList<>();
                try {
                    if(json != null) {
                        // 전체 문자열을 배열로 변환
                        JSONArray ar = new JSONArray(json);
                        System.out.println(ar);

                        // 배열을 순회
                        for(int i = 0; i< ar.length(); i++) {
                            // 배열의 요소를 JSON 객체로 가져오기
                            JSONObject obj = ar.getJSONObject(i);
                            // System.out.println(obj);
                            ToDoVo vo = new ToDoVo();

                            // 객체는 key를 이용해서 가져온다.
                            vo.setUserid(obj.getInt("userId"));
                            vo.setId(obj.getInt("id"));
                            vo.setTitle(obj.getString("title"));
                            vo.setCompleted(obj.getBoolean("completed"));

                            list.add(vo);
                        }
                        for(ToDoVo vo: list) {
                            System.out.println(vo);
                        }
                    }
                } catch(Exception e) {
                    System.out.println("파싱 실패");
                    System.out.println(e.getLocalizedMessage());
                }

                // 3. 파싱한 결과를 사용 - 출력
            }
        }
        ```

   4. JSON 생성

      - Spring Framework에서는 이 기능을 자동으로 수행
      - 직접 수행하고자 하는 경우의 의존성 - **jackson-core**, **jackson-databind**
      - JSON 생성을 위한 클래스 추가

      ```java
      import com.fasterxml.jackson.databind.ObjectMapper;

      import java.io.File;
      import java.util.Arrays;

      public class JSONCreate {
          public static void main(String[] args) {
              ToDoVo vo1 = new ToDoVo(1, 1, "한글", true);
              ToDoVo vo2 = new ToDoVo(2, 11, "영어", false);

              // 저장할 JSON 파일 생성
              File file = new File("./todo.json");
              // JSON 파일 기록을 위한 인스턴스 생성
              ObjectMapper mapper = new ObjectMapper();
              try {
                  // 기록
                  mapper.writeValue(file, Arrays.asList(vo1, vo2));
              } catch (Exception e) {
                  System.out.println("기록 실패");
              }
          }
      }
      ```

      ```json
      // todo.json
      [
        { "userid": 1, "id": 1, "title": "한글", "completed": true },
        { "userid": 2, "id": 11, "title": "영어", "completed": false }
      ]
      ```

6. **기타**
   - XML Parsing - 내장 클래스 이용
   - HTML Parsing - jsoup 라이브러리 이용
   - ajax나 fraem으로 만들어진 데이터를 가져오기 위해서는 selenium을 이용
7. **HTML Parsing**

   Web 보여지는 것 중에서 데이터로 활용하고자 하는 것이 있는데 API로 제공되지 않는 경우

   1. 사용되는 라이브러리(의존성): **jsoup**
   2. main 메서드를 만들어서 작성(**import에 주의 : org.jsoup 클래스 이용)**

      ```java
      import org.jsoup.Jsoup;
      import org.jsoup.nodes.Document;
      import org.jsoup.nodes.Element;
      import org.jsoup.select.Elements;

      import java.io.BufferedReader;
      import java.io.InputStreamReader;
      import java.net.HttpURLConnection;
      import java.net.URL;

      public class HTMLPaarsing {
          public static void main(String[] args) {
              // 웹에서 필요한 문자열 가져오기
              // API Server에서 데이터를 받아오는 부분은
              // 전송방식, 파라미터 부분을 제외하면 동일

              String html = null;
              try {
                  // URL 생성
                  URL url = new URL("https://www.hani.co.kr/");

                  // 다운로드 옵션 설정 - 전송 방식이나 파라미터 설정
                  HttpURLConnection con = (HttpURLConnection) url.openConnection();
                  con.setRequestMethod("GET");
                  // 필요하면 파라미터 설정

                  // 결과 다운로드
                  BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                  StringBuilder sb = new StringBuilder();
                  while(true) {
                      String line = br.readLine();
                      if(line == null) {
                          break;
                      }
                      // 파싱할 떄는 \n은 의미가 없다.
                      // 데이터를 확인하고자 할 때 보기좋게 출력하기 위해서 삽입.
                      sb.append(line + "\n");
                  }
                  html = sb.toString();
                  // System.out.println(html);
              } catch (Exception e) {
                  System.out.println(e.getMessage());
                  return;
              }

              // 문자열에서 원하는 데이터를 추출 - Parsing
              // 데이터 형식에 따라 json, xml, csv, html에 따라 다름.
              if(html != null) {
                  try {
                      // html 문자열을 메모리에 DOM 형태로 펼쳐내기
                      Document document = Jsoup.parse(html);

                      /*
                      // 태그를 이용하여 찾기
                      Elements elements = document.getElementsByTag("a");
                      // 태그는 여러 개 이므로 순회
                      for(int i = 0; i< elements.size(); i++) {
                          // 한 개 찾아오기
                          Element element = elements.get(i);

                          // 텍스트 확인
                          // System.out.println(element.text());

                          // 속성 확인
                          System.out.println(element.attr("href"));
                      }

                       */

                      // 선택자 이용하여 찾기
                      Elements elements = document.select("#main-top > div.main-top > div.main-top-article > div > h4 > a");
                      for(int i = 0; i< elements.size(); i++) {
                          Element element = elements.get(i);
                          System.out.println(element.text());
                      }
                  } catch(Exception e) {
                      System.out.println(e.getLocalizedMessage());
                  }
              }

              // 데이터를 사용
          }
      }
      ```

   3. URL을 이용한 html 다운로드 한계
      - 동적인 데이터(ajax로 가져오는 데이터)나 iframe의 데이터를 가져올 수 없음.
      - **따라서 브라우저를 직접 조작할 수 있는 selenium을 많이 이용**
        selenium을 이용하면 브라우저 조작이 가능

8. **Selenium**

   웹 앱을 테스트하기 위한 도구

   1. 준비

      브라우저

      브라우저 버전에 맞는 드라이버

   2. 브라우저 버전에 맞는 드라이버 다운로드
   3. main 메서드 생성

      ```java
      import org.openqa.selenium.By;
      import org.openqa.selenium.JavascriptExecutor;
      import org.openqa.selenium.WebDriver;
      import org.openqa.selenium.chrome.ChromeDriver;
      import org.openqa.selenium.chrome.ChromeOptions;

      public class SeleniumMain {
          public static void main(String[] args) {
              // 드라이버 위치 설정
              System.setProperty("webdriver.chrome.driver", "/Users/gimgyumin/Downloads/chromedriver");

              // 브라우저를 출력하지 않고 가져오기  options를 ChromeDriver(options) <- 여기에 넣어준다.
              // ChromeOptions options = new ChromeOptions();
              // options.addArguments("headless");

              // 드라이버 로드
              WebDriver driver = new ChromeDriver();

              // 사이트 접속
              driver.get("https://nid.naver.com/nidlogin.login");

              // 자바스크립트 실행
              JavascriptExecutor js = (JavascriptExecutor) driver;
              js.executeScript("document.getElementsByName('id')[0].value=\'" + "아이디" + "\'");
              js.executeScript("document.getElementsByName('pw')[0].value=\'" + "비밀번호" + "\'");

              // 로그인 버튼 클릭
              driver.findElement(By.xpath("//*[@id=\"log.login\"]")).click();
              driver.get("https://cafe.naver.com/joonggonara");
              // 소스 코드 가져오기
              System.out.println(driver.getPageSource());
          }
      }
      ```

9. **암호화**
   1. 암호화
      - 데이터 원본을 알아볼 수 없도록 다른 형태로 만드는 것이 암호화이고 암호화된 문장을 가지고 원래 문장(평문)을 복원하는 작업을 복호화라고 한다.
      - 암호화 기법
        암호화한 문장을 비교는 할 수 있지만 복호화는 못하는 형식(해싱을 주로 이용하는데 해싱은 원문을 모두 같은 길이의 문자열로 만드는 것)과 암호화를 하고 복호화가 가능한 방식 2가지가 있다.
   2. jBCrypt 라이브러리
      - 복호화가 안되는 암호화를 위한 라이브러리 - Spring Security에는 이 기능이 내장되어 있음.
      - 암호화
        `BCrypt.hashpw(원문, BCrypt.getsalt())`
      - 비교
        `BCrypt.checkpw(비교할 문장, 암호화된 문장)`
   3. 실습
      - pom.xml 에서 의존성 설정 - **jbcrypt**
