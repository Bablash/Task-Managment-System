package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public List<CommentDto> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long id) {
        return commentService.get(id);
    }

    @GetMapping("/task/{id}")
    public List<CommentDto> getByTaskId(@PathVariable Long taskId,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int limit) {
        return commentService.getComments(taskId, PageRequest.of(page, limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.save(commentDto, userDetails.getUsername());
    }

    @PutMapping
    public CommentDto update(@RequestBody CommentDto commentDto, @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.update(commentDto, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        commentService.delete(id, userDetails.getUsername());
    }
}
