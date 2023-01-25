package com.kakao.securityapp0119.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    // 카카오 로그인 성공 후 넘어오는 데이터를 이용해서 email을 추출해서 리턴하는 메서드
    private String getKakaoEmail(Map<String, Object> paramMap) {
        // 카카오 계정 정보가 있는 Map을 추출
        Object value = paramMap.get("kakao_account");
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");
        log.info("카카오 계정 이메일: " + email);
        return email;
    }

    // 로그인 성공했을 때 호출되는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 로그인에 성공한 서비스의 정보 가져오기
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("Service Name: " + clientName);

        // 계정에 대한 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;
        switch (clientName.toLowerCase()) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }
        log.info("email: " + email);
        return oAuth2User;
    }
}
