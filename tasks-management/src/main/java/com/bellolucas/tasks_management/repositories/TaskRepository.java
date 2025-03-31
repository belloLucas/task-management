package com.bellolucas.tasks_management.repositories;

import com.bellolucas.tasks_management.entities.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByCompletedFalse();
}
