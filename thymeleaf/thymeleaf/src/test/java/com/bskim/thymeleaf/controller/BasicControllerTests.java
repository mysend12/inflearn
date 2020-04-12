package com.bskim.thymeleaf.controller;

import static org.hamcrest.Matchers.is;
import static org.assertj.core.api.Assertions.assertThat;
// import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(BasicController.class)
public class BasicControllerTests {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebClient webClient;

    // 요청 "/"
    // 응답
    // -model: bosung
    // -뷰 이름: hello
    @Test
    public void helloTest() throws Exception{
        mockMvc.perform(get("/hello"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(view().name("hello"))
            .andExpect(model().attribute("name", is("bosung")))
            // .andExpect(content().string(containsString("bosung"))) deprecated containsString...
            ;
    }
    
    @Test
    // 의존성 추가 필요.
    public void helloTest2() throws Exception{
        HtmlPage page = webClient.getPage("/hello");
        HtmlHeading1 h1 = page.getFirstByXPath("//h1");
        assertThat(h1.getTextContent()).isEqualToIgnoringCase("bosung");
    }
}