package com.example.demo;

import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.dto.TaskDto;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testFindAll() {
        List<Task> tasks = List.of(taskRepository.findAll().get(0),
                taskRepository.findAll().get(1),
                taskRepository.findAll().get(2));
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setComments(null);
        }

        User user1 = new User(1l, "user1@gmail.com", "user1");
        User user2 = new User(2l, "user2@gmail.com", "user2");
        User user3 = new User(3l, "user3@gmail.com", "user3");

        Task task1 = new Task(1l, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1, user1, null);
        Task task2 = new Task(2l, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, user2, user2, null);
        Task task3 = new Task(3l, "task by user with id = 3", Status.IN_PROGRESS, Priority.MEDIUM, user3, user3, null);

        List<Task> expected = List.of(task1, task2, task3);
        assertEquals(expected, tasks, "Списки задач не совпадают!");
    }
}
