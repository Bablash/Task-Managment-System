package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAll();
    List<Task> findByAuthorId(Long id, Pageable page);

    List<Task> findByExecutorId(Long id, Pageable page);
}
