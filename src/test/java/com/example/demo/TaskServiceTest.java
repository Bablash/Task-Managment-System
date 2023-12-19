package com.example.demo;

import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepositoryMock;

    @InjectMocks
    TaskService taskService;

    @Test
    void findAllWasCalled() {
        User user1 = new User(1L, "user1@gmail.com", "user1");
        Task task1 = new Task(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1, user1, null);

        doReturn(List.of(task1)).when(this.taskRepositoryMock).findAll();
        taskService.getAll();
        Mockito.verify(taskRepositoryMock, Mockito.times(1)).findAll();
    }

}
