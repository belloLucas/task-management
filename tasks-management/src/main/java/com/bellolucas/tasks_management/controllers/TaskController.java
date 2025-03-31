package com.bellolucas.tasks_management.controllers;

import com.bellolucas.tasks_management.dto.task.CreateTaskDTO;
import com.bellolucas.tasks_management.dto.task.TaskResponseDTO;
import com.bellolucas.tasks_management.dto.task.UpdateTaskDTO;
import com.bellolucas.tasks_management.entities.task.Task;
import com.bellolucas.tasks_management.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks()
                .stream()
                .map(TaskResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(new TaskResponseDTO(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid CreateTaskDTO data) {
        Task task = taskService.createTask(data);
        return ResponseEntity.status(201).body(new TaskResponseDTO(task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody @Valid UpdateTaskDTO data) {
        Task updatedTask = taskService.updateTask(id, data);
        return ResponseEntity.ok(new TaskResponseDTO(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
