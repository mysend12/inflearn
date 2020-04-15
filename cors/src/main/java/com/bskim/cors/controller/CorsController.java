package com.bskim.cors.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Single-origin-policy Cross-Origin Resource Sharing
 * 
 * Origin: URI 스키마(http, https) + hostname(localhost, 10.10.101.151 등...) + port(8080) 하나의 origin이 다른 origin을 호출 할 수 없음. -
 * Single-Origin Policy 정책 위반
 * 
 * 
 * 방법1. 컨트럴로에 @CrossOrigin(origins = "http://URL:PORT")
 * 방법2. WebMvcConfigurer의 addCorsMappings 커스터 마이징()
 * 
 */
@RestController
public class CorsController {

    @GetMapping("/hello")
    // @CrossOrigin(origins = "*")
    public String hello(){
        return "Hello";
    }
}