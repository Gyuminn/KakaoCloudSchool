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
