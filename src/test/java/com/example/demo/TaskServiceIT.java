package com.example.demo;


import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskServiceIT {
    @Autowired
    TaskService taskService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    private User user1;
    private User user2;

    private User user3;

    private Task task1;
    private Task task2;

    private Task task3;

    @BeforeEach
    void save() {
        user1 = new User(1L, "user1@gmail.com", "user1");
        user2 = new User(2L, "user2@gmail.com", "user2");
        user3 = new User(3L, "user3@gmail.com", "user3");
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        task1 = new Task(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1, user1, null);
        task2 = new Task(2L, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, user2, user2, null);
        task3 = new Task(3L, "task by user with id = 3", Status.IN_PROGRESS, Priority.MEDIUM, user3, user3, null);

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);
        task3 = taskRepository.save(task3);
    }

    @Test
    void testGetAll_ReturnsValidList() {
        List<TaskDto> expected = List.of(
                new TaskDto(task1.getId(), "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), user1.getId()),
                new TaskDto(task2.getId(), "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, user2.getId(), user2.getId()),
                new TaskDto(task3.getId(), "task by user with id = 3", Status.IN_PROGRESS, Priority.MEDIUM, user3.getId(), user3.getId())
        );

        List<TaskDto> actual = List.of(taskService.getAll().get(0),
                taskService.getAll().get(1),
                taskService.getAll().get(2));

        assertEquals(expected, actual);
    }

    @Test
    void testSave_ValidTask() {
        TaskDto expected = new TaskDto(null, "task by user with id = 1", Status.WAITING, Priority.HIGH, null, user1.getId());
        TaskDto actual = taskService.save(expected, user1.getUsername());

        expected.setId(actual.getId());
        expected.setAuthorId(actual.getAuthorId());

        assertEquals(expected, actual);

        taskService.delete(actual.getId(), user1.getUsername());
    }

    @Test
    void testGetById_ReturnsValid() {
        TaskDto expected = taskService.save(new TaskDto(1L, "task by user with id = 1",
                Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), user1.getId()), user1.getUsername());

        TaskDto actual = taskService.get(expected.getId());

        assertEquals(expected, actual);

        taskService.delete(actual.getId(), user1.getUsername());
    }

    @Test
    void testGetById_IdNotFound_ReturnsNoSuchElementException() {
        Long id = 1000L;
        assertThrows(NoSuchElementException.class, () -> taskService.get(id));
    }

    @Test
    void testGetTasksByAuthor_ReturnsValid() {
        Long authorId = user2.getId();
        List<TaskDto> expected = List.of(new TaskDto(task2.getId(), "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, authorId, user2.getId()));

        List<TaskDto> actual = taskService.getTasksByAuthor(authorId, PageRequest.of(0, 5));

        assertEquals(expected, actual);
    }

    @Test
    void testGetTasksByExecutor_ReturnsValid() {
        Long executorId = user2.getId();
        List<TaskDto> expected = List.of(new TaskDto(task2.getId(), "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, user2.getId(), executorId));

        List<TaskDto> actual = taskService.getTasksByExecutor(executorId, PageRequest.of(0, 5));

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ReturnsValid() {
        TaskDto expected = new TaskDto(task1.getId(), "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), user1.getId());
        TaskDto actual = taskService.update(expected, "user1@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ByNotAuthorTask_ReturnsForbiddenExc() {
        TaskDto taskDto = new TaskDto(task1.getId(), "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), user1.getId());
        assertThrows(ForbiddenException.class, () -> taskService.update(taskDto, "user2@gmail.com"));
    }

    @Test
    void testUpdateStatusTask_ReturnsValid() {
        Status status = Status.IN_PROGRESS;
        TaskDto expected = new TaskDto(task1.getId(), "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), user1.getId());
        TaskDto actual = taskService.updateStatusTask(task1.getId(), status, user1.getUsername());

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateStatusTask_ByNotAuthorAndNotExecutor_ReturnsForbiddenExc() {
        Status status = Status.IN_PROGRESS;
        assertThrows(ForbiddenException.class, () -> taskService.updateStatusTask(task1.getId(), status, user3.getUsername()));
    }

    @Test
    void testUpdateExecutorTask_ReturnsValid() {
        Long executorId = user1.getId();
        TaskDto expected = new TaskDto(task1.getId(), "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1.getId(), executorId);
        TaskDto actual = taskService.updateExecutorTask(task1.getId(), executorId, user1.getUsername());

        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ByNotAuthor_ReturnsForbiddenExc() {
        Long id = task1.getId();
        String username = user2.getUsername();

        assertThrows(ForbiddenException.class, () -> taskService.delete(id, username));
    }

    @Test
    void testDelete_ValidId() {
        assertDoesNotThrow(() -> taskService.get(task1.getId()));

        taskService.delete(task1.getId(), user1.getUsername());

        assertThrows(NoSuchElementException.class, () -> taskService.get(task1.getId()));
    }

    @AfterEach
    void delete() {
        taskRepository.delete(task1);
        taskRepository.delete(task2);
        taskRepository.delete(task3);
        userRepository.delete(user1);
        userRepository.delete(user2);
        userRepository.delete(user3);
    }

}
