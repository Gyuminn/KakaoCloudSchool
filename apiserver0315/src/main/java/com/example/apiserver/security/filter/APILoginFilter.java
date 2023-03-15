package com.example.apiserver.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }

    //클라이언트의 요청을 받아서 JSON 문자열을 파싱해서 Map으로 만들어주는 사용자 정의 메서드
    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        //try-resources: try() 안에서 만든 객체는 close를 호출할 필요가 없음
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            //JSON 문자열을 DTO 클래스의 데이터로 변경
            Gson gson = new Gson();
            //속성의 이름이 Key 가 되고 값이 Value가 됩니다.
            return gson.fromJson(reader, Map.class);
        }catch(Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
    throws AuthenticationException, IOException {
        log.info("API LoginFilter----------------------");

        //GET 방식인 경우 처리할 필요가 없음
        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET Method Not Support");
            return null;
        }

        //토큰 생성 요청을 했을 때 아이디 와 비밀번호를 가져와서 Map으로 생성
        Map<String, String> jsonData = parseRequestJSON(request);
        log.info("jsonData:" + jsonData);

        //아이디 와 비밀번호를 다음 필터에 전송해서 사용하도록 설정
        //APIUserDetailsService 가 동작
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jsonData.get("mid"), jsonData.get("mpw"));

        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
