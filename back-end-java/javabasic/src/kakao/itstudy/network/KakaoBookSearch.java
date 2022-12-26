package kakao.itstudy.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class KakaoBookSearch {
    public static void main(String[] args) {
        // 카카오 도서 검색 API
        new Thread(new Runnable() {
            @Override
            public void run() {
                // url 만들기
                String address = "https://dapi.kakao.com/v3/search/book?target=title";
                address += "&query=";
                try {
                    // Connection 만들기
                    // GET 방식에서 파라미터는 반드시 인코딩되어야 한다.
                    address += URLEncoder.encode("미움받을 용기", "utf-8");
                    URL url = new URL(address);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(30000);
                    con.setUseCaches(false);
                    con.setRequestMethod("GET");
                    // 키 설정
                    con.setRequestProperty("Authorization", "KakaoAK f5a85f22297e40067fe9036e143eeeb1");

                    // 데이터 읽어오기
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while(true) {
                        String imsi = br.readLine();
                        if(imsi == null) {
                            break;
                        }
                        sb.append(imsi + "\r\n");
                    }
                    String result = sb.toString();
                    System.out.println(result);
                    con.disconnect();
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
        }).start();
    }
}
