package com.example.demo;

import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.controller.TaskController;
import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {
    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController controller;

    @Test
    void getAll_ReturnsValidResponseEntity() {

        TaskDto task1 = new TaskDto(1l, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, 1l, 1l);
        TaskDto task2 = new TaskDto(2l, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, 2l, 2l);
        TaskDto task3 = new TaskDto(3l, "task by user with id = 3", Status.IN_PROGRESS, Priority.MEDIUM, 3l, 3l);

        List<TaskDto> tasks = List.of(task1, task2, task3);

        doReturn(tasks).when(this.taskService).getAll();

        var responseEntity = this.controller.getAll();

        assertNotNull(responseEntity);
        assertEquals(tasks, responseEntity);

    }
}
