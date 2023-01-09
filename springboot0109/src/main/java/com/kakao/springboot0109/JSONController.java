package com.kakao.springboot0109;

import com.kakao.springboot0109.dto.ParamDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.Param;

import javax.servlet.http.HttpServletRequest;

@RestController
// 공통된 URL
@RequestMapping("/api/v1/rest-api")
public class JSONController {
    // 로깅 가능한 객체를 생성
    private final Logger LOGGER = LoggerFactory.getLogger(JSONController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getHello() {
        LOGGER.info("hello 요청이 왔습니다. 2");
        return "GET Hello";
    }

    @GetMapping("/newhello")
    public String getNewHello() {
        return "Get New Hello";
    }

    @GetMapping("/product/{num}")
    public String getNum(@PathVariable("num") int num) {
        return num + "";
    }

    @GetMapping("/param")
    public String getParam(HttpServletRequest request) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String organization = request.getParameter("organization");

        return name + ":" + email + ":" + organization;
    }

    @GetMapping("/param1")
    public String getParam(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("organization") String organization
    ) {
        return name + ":" + email + ":" + organization;
    }

    @GetMapping("/param2")
    public String getParam(
            ParamDTO paramDTO
    ) {
        return paramDTO.getName() + ":" + paramDTO.getEmail() + ":" + paramDTO.getOrganization();
    }

    @PostMapping("/param")
    public String getPostParam(@RequestBody ParamDTO paramDTO) {
        return paramDTO.toString();
    }

    @PutMapping("/param")
    public String getPutParam(@RequestBody ParamDTO paramDTO) {
        return paramDTO.toString();
    }

    @PutMapping("/param1")
    public ParamDTO getPutParam1(@RequestBody ParamDTO paramDTO) {
        return paramDTO;
    }

    @PutMapping("/param2")
    public ResponseEntity<ParamDTO> getPutParam2(@RequestBody ParamDTO paramDTO) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(paramDTO);
    }

    @DeleteMapping("/product/{num}")
    public String deleteNum(@PathVariable("num") int num) {
        return num + "";
    }

    @DeleteMapping("/product")
    public String deleteParamNum(@RequestParam("num") int num) {
        return num + "";
    }
}
