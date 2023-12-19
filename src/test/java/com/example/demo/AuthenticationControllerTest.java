package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class AuthenticationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void testAuthentication_getAccessAndReturnsStatus() throws Exception {
        var requestBuilder = post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "login" : "user1@gmail.com",
                            "password" : "user1"
                        }
                        """);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk());
    }
}
