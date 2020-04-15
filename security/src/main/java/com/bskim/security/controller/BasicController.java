package com.bskim.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {

    /**
     
     * 시큐리티 관련 설정은 WebSecurityConfigurerAdapter를 상속받아서 커스터마이징.
     * 유저 관련 설정은 UserDetailsService를 등록해서 커스터마이징.
     
     * 테스트 시에, @withMockUser를 이용해서 mock test 시에 인증정보를 추가할 수 있다.
     
     * 1. WebSecurityConfigurerAdatpter를 상속받아서 커스터마이징.
     * 2. UserDetailsService 구현
     * 3. PasswordEncoder 설정 및 사용
     
     */

    @GetMapping("/hello")
    public String hello(){

        return "hello";
    }

    @GetMapping("/my")
    public String my(){

        return "my";
    }

}