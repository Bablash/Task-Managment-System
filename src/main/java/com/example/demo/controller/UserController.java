package com.example.demo.controller;

import com.example.demo.dto.TaskDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Пользователи")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получить всех пользователей"
    )
    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @Operation(
            summary = "Зарегистрироваться по email и паролю"
    )
    @PostMapping("/signUp")
    public void signIn(@RequestBody @Valid User user) {
        userService.save(user);
    }
}
