package com.example.demo.dto;

import com.example.demo.constant.Priority;
import com.example.demo.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private Long id;

    private String description;

    private Status status;

    private Priority priority;

    private Long authorId;

    private Long executorId;

    public TaskDto(Long id, String description, Status status, Priority priority, Long authorId) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.authorId = authorId;
        this.executorId = null;
    }
}
