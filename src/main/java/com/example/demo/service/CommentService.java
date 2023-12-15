package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.dto.CommentDto;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommentService {
    private static final String FIND_ERROR_MESSAGE = "%s entity with id = %d is not found";
    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public CommentDto save(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setTask(getTask(commentDto.getTaskId()));
        comment.setUser(getUser(commentDto.getAuthorId()));
        comment.setText(commentDto.getText());

        comment = commentRepository.save(comment);
        return get(comment.getId());
    }

    public CommentDto update(CommentDto commentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(commentDto.getId());

        if(optionalComment.isPresent()) {
            optionalComment.get().setTask(getTask(commentDto.getTaskId()));
            optionalComment.get().setUser(getUser(commentDto.getAuthorId()));
            optionalComment.get().setText(commentDto.getText());
            return get(commentRepository.save(optionalComment.get()).getId());
        } else {
            throw new NoSuchElementException("Entity with id = " + commentDto.getId() + " not found");
        }
    }

    public Task getTask(Long id) {
        String taskExceptionMessage = String.format(FIND_ERROR_MESSAGE,
                Task.class.getSimpleName(), id);
        return taskRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(taskExceptionMessage));
    }

    public User getUser(Long id) {
        String userExceptionMessage = String.format(FIND_ERROR_MESSAGE,
                User.class.getSimpleName(), id);
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(userExceptionMessage));
    }

    public CommentDto get(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isPresent()) {
            return optionalComment.map(c -> new CommentDto(c.getId(),
                    c.getTask().getId(), c.getUser().getId(), c.getText())).get();
        } else {
            throw new NoSuchElementException("Entity with id = " + id + " not found");
        }
    }

    public List<CommentDto> getAll() {
        List<CommentDto> listCommentDto = commentRepository.findAll().stream()
                .map(c -> new CommentDto(c.getId(), c.getTask().getId(), c.getUser().getId(), c.getText()))
                .toList();
        if(!listCommentDto.isEmpty()) {
            return listCommentDto;
        } else {
            throw new NoSuchElementException("Entity " + Comment.class.getSimpleName() + " is empty");
        }
    }

    public void delete(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);

        if(optionalComment.isEmpty()) {
            throw new NoSuchElementException(String.format(FIND_ERROR_MESSAGE, Comment.class.getSimpleName(), id));
        }

        commentRepository.deleteById(id);
    }

    public List<CommentDto> getComments(Long taskId, PageRequest page) {
        List<CommentDto> listCommentDto = commentRepository.findByTaskId(taskId, page).stream()
                .map(c -> new CommentDto(c.getId(), c.getTask().getId(), c.getUser().getId(), c.getText()))
                .toList();
        if(!listCommentDto.isEmpty()) {
            return listCommentDto;
        } else {
            throw new NoSuchElementException("Comments for tasks with id " + taskId + " not found");
        }
    }
}
