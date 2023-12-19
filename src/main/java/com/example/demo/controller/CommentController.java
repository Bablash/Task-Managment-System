package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Комментарии")
@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Получить все комментарии"
    )
    @GetMapping()
    public List<CommentDto> getAll() {
        return commentService.getAll();
    }

    @Operation(
            summary = "Получить комментарий по id"
    )
    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long id) {
        return commentService.get(id);
    }

    @Operation(
            summary = "Получить комментарии к задаче по id задачи"
    )
    @GetMapping("/task/{id}")
    public List<CommentDto> getByTaskId(@PathVariable Long id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int limit) {
        return commentService.getComments(id, PageRequest.of(page, limit));
    }

    @Operation(
            summary = "Создать комментарий к задаче. Автором задачи назначится аутентифицированный пользователь"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.save(commentDto, userDetails.getUsername());
    }

    @Operation(
            summary = "Обновить комментарий. Доступно только автору комментария"
    )
    @PutMapping
    public CommentDto update(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.update(commentDto, userDetails.getUsername());
    }

    @Operation(
            summary = "Удалить комментарий. Доступно только автору задачи"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        commentService.delete(id, userDetails.getUsername());
    }
}
