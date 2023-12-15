package com.example.demo.service;

import com.example.demo.constant.Status;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.dto.TaskDto;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {
    private static final String FIND_ERROR_MESSAGE = "%s entity with id = %d is not found";
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public TaskDto save(TaskDto taskDto) {
        Task task = new Task();
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setPriority(taskDto.getPriority());
        task.setAuthor(getAuthor(taskDto.getAuthorId()));
        task.setExecutor(getExecutor(taskDto.getExecutorId()));

        task = taskRepository.save(task);
        return get(task.getId());
    }

    public TaskDto update(TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(taskDto.getId());
        if (optionalTask.isPresent()) {
            optionalTask.get().setDescription(taskDto.getDescription());
            optionalTask.get().setStatus(taskDto.getStatus());
            optionalTask.get().setPriority(taskDto.getPriority());
            optionalTask.get().setAuthor(getAuthor(taskDto.getAuthorId()));
            optionalTask.get().setExecutor(getExecutor(taskDto.getExecutorId()));
            return get(taskRepository.save(optionalTask.get()).getId());

        } else {
            throw new NoSuchElementException("Entity with id = " + taskDto.getId() + " not found");
        }

    }

    public User getAuthor(Long id) {
        String authorExceptionMessage = String.format(FIND_ERROR_MESSAGE,
                User.class.getSimpleName(), id);
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException(authorExceptionMessage));
    }

    public User getExecutor(Long id) {
        if(id == null) {
            return null;
        } else {
            String executorExceptionMessage = String.format(FIND_ERROR_MESSAGE,
                    User.class.getSimpleName(), id);
            return userRepository.findById(id).orElseThrow(() ->
                    new NoSuchElementException(executorExceptionMessage));
        }
    }

    public TaskDto get(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent() && optionalTask.get().getExecutor() == null) {
            return optionalTask.map(t -> newTaskDto(t)).get();
        } else if (optionalTask.isPresent()) {
            return optionalTask.map(t -> newTaskDto(t)).get();
        } else {
            throw new NoSuchElementException("Entity with id = " + id + " not found");
        }
    }

    public List<TaskDto> getAll() {
        List<TaskDto> listTaskDto = taskRepository.findAll().stream()
                .map(this::newTaskDto)
                .toList();
        if (!listTaskDto.isEmpty()) {
            return listTaskDto;
        } else {
            throw new NoSuchElementException("Entity " + Task.class.getSimpleName() + " is empty");
        }
    }

    public TaskDto newTaskDto(Task task) {
        if(task.getExecutor() == null) {
            return new TaskDto(task.getId(), task.getDescription(), task.getStatus(),
                    task.getPriority(), task.getAuthor().getId());
        } else {
            return new TaskDto(task.getId(), task.getDescription(), task.getStatus(),
                    task.getPriority(), task.getAuthor().getId(), task.getExecutor().getId());
        }
    }

    public void delete(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            throw new NoSuchElementException(String.format(FIND_ERROR_MESSAGE, Task.class.getSimpleName(), id));
        }

        taskRepository.deleteById(id);
    }

    public TaskDto updateStatusTask(Long id, Status status) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new NoSuchElementException(String.format(FIND_ERROR_MESSAGE, Task.class.getSimpleName(), id));
        } else {
            optionalTask.get().setStatus(status);
            taskRepository.save(optionalTask.get());
        }
        return get(id);
    }

    public TaskDto updateExecutorTask(Long id, Long executorId) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            throw new NoSuchElementException(String.format(FIND_ERROR_MESSAGE, Task.class.getSimpleName(), id));
        } else {
            optionalTask.get().setExecutor(getExecutor(executorId));
            taskRepository.save(optionalTask.get());
        }
        return get(id);
    }

    public List<TaskDto> getTasksByAuthor(Long authorId, PageRequest page) {
        List<TaskDto> listTaskDto = taskRepository.findByAuthorId(authorId, page).stream()
                .map(t -> new TaskDto(t.getId(), t.getDescription(), t.getStatus(),
                        t.getPriority(), t.getAuthor().getId(), t.getExecutor().getId()))
                .toList();
        if (!listTaskDto.isEmpty()) {
            return listTaskDto;
        } else {
            throw new NoSuchElementException("Tasks with author id = " + authorId + " not found");
        }
    }

    public List<TaskDto> getTasksByExecutor(Long executorId, PageRequest page) {
        List<TaskDto> listTaskDto = taskRepository.findByExecutorId(executorId, page).stream()
                .map(t -> new TaskDto(t.getId(), t.getDescription(), t.getStatus(),
                        t.getPriority(), t.getAuthor().getId(), t.getExecutor().getId()))
                .toList();
        if (!listTaskDto.isEmpty()) {
            return listTaskDto;
        } else {
            throw new NoSuchElementException("Tasks with executor id = " + executorId + " not found");
        }
    }

}
