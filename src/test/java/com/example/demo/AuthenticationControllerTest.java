package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
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

    @Autowired
    UserRepository userRepository;

    private User user1;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "user@gmail.com", "user"));
    }

    @Test
    void testAuthentication_getAccessAndReturnsStatus() throws Exception {
        var requestBuilder = post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "login" : "user@gmail.com",
                            "password" : "user"
                        }
                        """);
        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk());


    }

    @AfterEach
    public void clear() {
        userRepository.delete(user1);
    }
}
