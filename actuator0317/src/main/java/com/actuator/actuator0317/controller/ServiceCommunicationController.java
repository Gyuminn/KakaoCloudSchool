package com.actuator.actuator0317.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceCommunicationController {
    @GetMapping("/kakao")
    public ResponseEntity<Map<String, Object>> kakao() {
        // 클라이언트에게 응답할 데이터
        Map<String, Object> map = new HashMap<>();
        map.put("result", false);
        // 실제 데이터 배열
        ArrayList<Map> data = new ArrayList<>();
        try {
            // 다운로드받을 URL
            String addr = "https://dapi.kakao.com/v3/search/book?query=";
            String title = URLEncoder.encode("삼국지");
            URL url = new URL(addr + title);

            // 연결 객체 생성
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 옵션 설정
            // 접속 시도 시간 - 이 시간을 설정하지 않으면
            // 서버가 응답하지 않으면 무한대기
            con.setConnectTimeout(30000);
            // 이전에 다운로드 받은 데이터를 사용하지 않고 새로 다운로드
            // 데이터가 자주 변경되면 false
            // 자주 변경되지 않으면 true
            con.setUseCaches(false);

            // 헤더 설정
            con.setRequestProperty("Authorization", "KakaoAK f5a85f22297e40067fe9036e143eeeb1");

            // 스트림 생성
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream()
            ));

            // 문자열이 긴 경우
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            System.out.println(sb.toString());

            String json = sb.toString();
            // 첫 번쨰 파싱
            JSONObject root = new JSONObject(json);
            // documents 키의 값을 배열로 가져오기
            JSONArray documents = root.getJSONArray("documents");
            // 배열은 순회
            for (int i = 0; i < documents.length(); i++) {
                // 배열 안의 객체 가져오기
                JSONObject obj = documents.getJSONObject(i);

                // DTO 안만들고 하기 - 새로운 객체에 데이터를 저장
                Map<String, Object> m = new HashMap<>();
                m.put("title", obj.getString("title"));
                m.put("price", obj.getInt("price"));

                data.add(m);
            }
            map.put("result", true);
            map.put("books", data);

            br.close();
            con.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
