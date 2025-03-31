package com.bellolucas.tasks_management.dto.task;

import com.bellolucas.tasks_management.dto.user.UserResponseDTO;
import com.bellolucas.tasks_management.entities.task.Priority;
import com.bellolucas.tasks_management.entities.task.Task;

import java.time.LocalDateTime;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        Priority priority,
        UserResponseDTO assignee,
        LocalDateTime deadline,
        Boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public TaskResponseDTO(Task task) {
        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getAssignee() != null ? new UserResponseDTO(task.getAssignee()) : null,
                task.getDeadline(),
                task.getCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
