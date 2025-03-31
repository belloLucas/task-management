package com.bellolucas.tasks_management.dto.task;

import com.bellolucas.tasks_management.entities.task.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateTaskDTO(
        @NotBlank(message = "O título é obrigatório")
        @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
        String title,

        @Size(max = 1000, message = "A descrição não pode exceder 1000 caracteres")
        String description,

        @NotNull(message = "A prioridade é obrigatória")
        Priority priority,

        @NotNull(message = "O responsável é obrigatório")
        Long assignee,

        @NotNull(message = "A data limite é obrigatória")
        String deadline
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