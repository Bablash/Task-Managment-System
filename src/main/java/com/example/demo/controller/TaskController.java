package com.example.demo.controller;

import com.example.demo.constant.Status;
import com.example.demo.dto.TaskDto;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Задачи")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Получить все задачи"
    )
    @GetMapping()
    public List<TaskDto> getAll() {
        return taskService.getAll();
    }

    @Operation(
            summary = "Получить задачу по id"
    )
    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return taskService.get(id);
    }

    @Operation(
            summary = "Получить задачи по id автора"
    )
    @GetMapping("/author")
    public List<TaskDto> getTasksByAuthor(@RequestParam Long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int limit) {
        return taskService.getTasksByAuthor(id, PageRequest.of(page, limit));
    }

    @Operation(
            summary = "Получить задачи по id исполнителя"
    )
    @GetMapping("/executor")
    public List<TaskDto> getTasksByExecutor(@RequestParam Long id,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int limit) {
        return taskService.getTasksByExecutor(id, PageRequest.of(page, limit));
    }

    @Operation(
            summary = "Создать задачу. Автором задачи назначится аутентифицированный пользователь"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@RequestBody TaskDto task, @AuthenticationPrincipal UserDetails userDetails) {
         return taskService.save(task, userDetails.getUsername());
    }

    @Operation(
            summary = "Обновить задачу. Доступно только автору задачи"
    )
    @PutMapping
    public TaskDto update(@RequestBody TaskDto task, @AuthenticationPrincipal UserDetails userDetails) {
        return taskService.update(task, userDetails.getUsername());
    }

    @Operation(
            summary = "Обновить статус задачи. Доступно автору и исполнителю задачи"
    )
    @PatchMapping("/{id}/status")
    public TaskDto changeStatus(@PathVariable Long id, @RequestParam Status status,
                                @AuthenticationPrincipal UserDetails userDetails) {
        return taskService.updateStatusTask(id, status, userDetails.getUsername());
    }

    @Operation(
            summary = "Обновить исполнителя задачи. Доступно только автору задачи"
    )
    @PatchMapping("/{id}/executor")
    public TaskDto changeExecutor(@PathVariable Long id, @RequestParam Long executorId,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        return taskService.updateExecutorTask(id, executorId, userDetails.getUsername());
    }

    @Operation(
            summary = "Удалить задачу. Доступно только автору задачи"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        taskService.delete(id, userDetails.getUsername());
    }

}
