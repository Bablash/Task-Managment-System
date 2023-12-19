package com.example.demo;


import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.dto.TaskDto;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.service.TaskService;
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

    @Test
    void testGetAll_ReturnsValidList() {
        List<TaskDto> expected = List.of(
                new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, 1L),
                new TaskDto(2L, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, 2L, 2L),
                new TaskDto(3L, "task by user with id = 3", Status.IN_PROGRESS, Priority.MEDIUM, 3L, 3L)
        );

        List<TaskDto> actual = List.of(taskService.getAll().get(0),
                taskService.getAll().get(1),
                taskService.getAll().get(2));

        assertEquals(expected, actual);
    }

    @Test
    void testSave_ValidTask() {
        TaskDto expected = new TaskDto(null, "task by user with id = 1", Status.WAITING, Priority.HIGH, null, 1L);
        TaskDto actual = taskService.save(expected, "user1@gmail.com");

        expected.setId(actual.getId());
        expected.setAuthorId(actual.getAuthorId());

        assertEquals(expected, actual);
    }

    @Test
    void testGetById_ReturnsValid() {
        TaskDto expected = new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, 1L);

        TaskDto actual = taskService.get(1L);

        assertEquals(expected, actual);
    }

    @Test
    void testGetById_IdNotFound_ReturnsNoSuchElementException() {
        Long id = 20L;
        assertThrows(NoSuchElementException.class, () -> taskService.get(id));
    }

    @Test
    void testGetTasksByAuthor_ReturnsValid() {
        Long authorId = 2L;
        List<TaskDto> expected = List.of(new TaskDto(2L, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, authorId, 2L));

        List<TaskDto> actual = taskService.getTasksByAuthor(authorId, PageRequest.of(0, 5));

        assertEquals(expected, actual);
    }

    @Test
    void testGetTasksByExecutor_ReturnsValid() {
        Long executorId = 2L;
        List<TaskDto> expected = List.of(new TaskDto(2L, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, 2L, executorId));

        List<TaskDto> actual = taskService.getTasksByExecutor(executorId, PageRequest.of(0, 5));

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ReturnsValid() {
        TaskDto expected = new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, 1L);
        TaskDto actual = taskService.update(expected, "user1@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_ByNotAuthorTask_ReturnsForbiddenExc() {
        TaskDto taskDto = new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, 1L);
        assertThrows(ForbiddenException.class, () -> taskService.update(taskDto, "user2@gmail.com"));
    }

    @Test
    void testUpdateStatusTask_ReturnsValid() {
        Status status = Status.IN_PROGRESS;
        TaskDto expected = new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, 1L);
        TaskDto actual = taskService.updateStatusTask(1L, status, "user1@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    void testUpdateStatusTask_ByNotAuthorAndNotExecutor_ReturnsForbiddenExc() {
        Status status = Status.IN_PROGRESS;
        assertThrows(ForbiddenException.class, () -> taskService.updateStatusTask(1L, status, "user3@gmail.com"));
    }

    @Test
    void testUpdateExecutorTask_ReturnsValid() {
        Long executorId = 1L;
        TaskDto expected = new TaskDto(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1L, executorId);
        TaskDto actual = taskService.updateExecutorTask(1L, executorId, "user1@gmail.com");

        assertEquals(expected, actual);
    }

    @Test
    void testDelete_ByNotAuthor_ReturnsForbiddenExc() {
        Long id = 1L;
        String username = "user2@gmail.com";

        assertThrows(ForbiddenException.class, () -> taskService.delete(id, username));
    }

    @Test
    void testDelete_ValidId() {
        TaskDto taskDto = new TaskDto(null, "task by user with id = 1", Status.WAITING, Priority.HIGH, null, 1L);
        TaskDto actual = taskService.save(taskDto, "user1@gmail.com");
        String username = taskService.getAuthor(actual.getAuthorId()).getUsername();

        Long id = actual.getId();
        assertDoesNotThrow(() -> taskService.get(id));

        taskService.delete(id, username);

        assertThrows(NoSuchElementException.class, () -> taskService.get(id));
    }

}
