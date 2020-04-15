package com.bskim.security.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(BasicController.class)
public class BasicControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser   // 인증해주는 어노테이션.
    public void hello() throws Exception {
        // 인증
        mockMvc.perform(get("/hello")
                .accept(MediaType.TEXT_HTML))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("hello"))
            ;
    }

    @Test
    // @withMockUser가 없기 때문에 인증을 할 수 없다. -> 테스트 실패...
    public void my() throws Exception {
        mockMvc.perform(get("/my"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("my"))
            ;
    }
}