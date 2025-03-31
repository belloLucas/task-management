package com.bellolucas.tasks_management.dto.task;

import com.bellolucas.tasks_management.entities.task.Priority;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
        @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
        String title,

        @Size(max = 1000, message = "A descrição não pode exceder 1000 caracteres")
        String description,

        Priority priority,

        Long assignee,

        String deadline,

        Boolean completed
) {
        public LocalDateTime getDeadlineAsLocalDateTime() {
                if (deadline == null) {
                        return null;
                }

                if (deadline.contains("T") || deadline.contains(":")) {
                        return LocalDateTime.parse(deadline);
                }

                return LocalDateTime.parse(deadline + "T23:59:59");
        }
}