package com.kakao.springboot0109;

import com.kakao.springboot0109.dto.ParamDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.tags.Param;

import javax.servlet.http.HttpServletRequest;

@RestController
// 공통된 URL
@RequestMapping("/api/v1/rest-api")
public class JSONController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String getHello() {
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
    public String getPutParam(@RequestBody ParamDTO paramDTO){
        return paramDTO.toString();
    }
}
