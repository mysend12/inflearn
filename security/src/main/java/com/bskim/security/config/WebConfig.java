// package com.bskim.security.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer{

//     // @GetMapping("/hello")와 같은 코드.(다만, 모델 등을 추가할 수 없다.), 컨트롤러를 만들지 않아도 된다.
//     @Override
//     public void addViewControllers(ViewControllerRegistry registry){
//         registry.addViewController("/hello").setViewName("hello");
//     }
// }