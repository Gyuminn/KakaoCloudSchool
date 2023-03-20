package com.gyumin.apiclient0320.service;

import com.gyumin.apiclient0320.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class RestTemplateService {
    private final RestTemplate restTemplate;

    // 파라미터가 없는 GET 방식 요청 처리 - 응답 타입은 String
    public String getName() {
        // 요청 URI 생성
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9000")
                .path("/api/v1/crud-api")
                .encode()
                .build()
                .toUri();

        // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        // 내용 리턴
        return responseEntity.getBody();
    }

    // PathVariable이 있는 경우
    // 값을 넣을 떄 expand
    public String getNameWithPathVariable() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://localhost:9000")
                .path("/api/v1/crud-api/{name}")
                .encode()
                .build()
                .expand("규민")
                .toUri();
        // PathVariable이 여러 개라면 expand에 나열하면 된다.

        // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        // 내용 리턴
        return responseEntity.getBody();
    }

    // 파라미터가 있는 경우
    public String getNameWithParameter() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://localhost:9000")
                .path("/api/v1/crud-api/param")
                .queryParam("name", "itstudy")
                .encode()
                .build()
                .toUri();
        // 파라미터가 여러 개라면 queryParam을 연속해서 호출하거나
        // 다른 모양의 queryParam을 이용
        // 요청 객체 생성

        // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        // 내용 리턴
        return responseEntity.getBody();
    }

    // 파라미터와 body가 존재하는 POST 요청
    public ResponseEntity<MemberDTO> postWithParamAndBody() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9000")
                .path("/api/v1/crud-api")
                .queryParam("name", "규민")
                .queryParam("email", "abc@kakao.com")
                .queryParam("organization", "kakao")
                .encode()
                .build()
                .toUri();
        // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

        // Body에 저장해서 보내기
        // Body 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("gyumin");
        memberDTO.setEmail("abc@kakao.com");
        memberDTO.setOrganization("카카오");

        // POST 방식으로 전송할 때 2번쨰 매개변수로 Body를 설정
        ResponseEntity<MemberDTO> responseEntity = restTemplate.postForEntity(uri, memberDTO, MemberDTO.class);
        return responseEntity;
    }

    // body와 header를 모두 전송하는 POST 요청
    public ResponseEntity<MemberDTO> postWithHeader() {
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:9000")
                .path("/api/v1/crud-api")
                .encode()
                .build()
                .toUri();
        // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

        // Body에 저장해서 보내기
        // Body 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("gyumin");
        memberDTO.setEmail("abc@kakao.com");
        memberDTO.setOrganization("카카오");

        // 헤더를 같이 보내기 위한 객체를 생성
        RequestEntity<MemberDTO> requestEntity = RequestEntity
                .post(uri)
                .header("my-header", "gyumin-API")
                .body(memberDTO);


        // POST 방식으로 전송할 때 요청 객체를 전송
        ResponseEntity<MemberDTO> responseEntity = restTemplate.exchange(requestEntity, MemberDTO.class);
        return responseEntity;
    }
}