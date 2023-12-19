package com.example.demo;

import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "user01@gmail.com", "user1");
        user2 = new User(2L, "user02@gmail.com", "user2");
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

        task1 = new Task(1L, "task by user with id = 1", Status.IN_PROGRESS, Priority.MEDIUM, user1, user1, null);
        task2 = new Task(2L, "task by user with id = 2", Status.IN_PROGRESS, Priority.MEDIUM, user2, user2, null);

        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);
    }

    @Test
    void testFindAll() {
        List<Task> tasks = List.of(taskRepository.findAll().get(0),
                taskRepository.findAll().get(1));
        for (Task task : tasks) {
            task.setComments(null);
        }

        List<Task> expected = List.of(task1, task2);
        assertEquals(expected, tasks, "Списки задач не совпадают!");
    }

    @AfterEach
    public void clear() {
        taskRepository.delete(task1);
        taskRepository.delete(task2);
        userRepository.delete(user1);
        userRepository.delete(user2);
    }
}
