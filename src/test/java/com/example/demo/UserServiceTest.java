package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.mockito.ArgumentMatchers.anyString;


public class UserServiceTest {
    private static UserService userService;

    private static PasswordEncoder passwordEncoderMock;

    @BeforeAll
    public static void setUp() {
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        passwordEncoderMock = Mockito.mock(PasswordEncoder.class);

        Mockito.when(passwordEncoderMock.encode(anyString())).thenReturn(anyString());

        userService = new UserService(userRepositoryMock, passwordEncoderMock);
    }

    @Test
    void passwordAlwaysShouldBeEncoded() {
        userService.save(new User(4L, "user4@gmail.com", "user4"));
        Mockito.verify(passwordEncoderMock, Mockito.times(1)).encode("user4");
    }

}
