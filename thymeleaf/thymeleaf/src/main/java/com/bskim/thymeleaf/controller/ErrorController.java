package com.bskim.thymeleaf.controller;

import com.bskim.thymeleaf.Exception.ExceptionModel;
import com.bskim.thymeleaf.Exception.SampleException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * 
 * ExceptionHanlder를 사용시,
 * 경로를 /error로 잡으면 에러 ExceptionHandler가 동작하지 않는다.
 * 다른 경로를 이용할 것.
 * 
 
 * @Controller
 * @RequestMapping("${server.error.path:${error.path}:/error}}")
 * BasicErrorController 소스를 한번 확인해 볼것.
 * 위 RequestMapping은 (참 : (참 : 거짓)) 의 형태.
 * 
 * ErrorController를 상속받아 Bean으로 등록하면 BasicErrorController 대신 상속받은 클래스가 에러 처리를 한다.
 * BasicErrorController를 상속받아 사용해도 됨.
 
 * resources - static - error - (error code).html을 만들어 두면, 해당 에러 페이지가 보여진다.
 * 이 에러처리 방식은 커스터마이징 작업이 아무것도 없을 경우 보여짐(우선순위가 가장 낮음)
 * ex) 404.html or 5xx.html -> 500번대 error
 * 
 * ErrorViewResolver를 커스터마이징하면 동적인??에러를 처리하기 쉽다?
 * 
 * 
 */
@Controller
public class ErrorController {

    // @GetMapping("/error")
    @GetMapping("/errors")
    public String error(){
        throw new SampleException("errors");
    }
    @ExceptionHandler({SampleException.class})
    public @ResponseBody ExceptionModel ResponseEntity(SampleException e){
        return new ExceptionModel(e.getMessage(), 404);
    }
}