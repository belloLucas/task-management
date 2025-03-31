package com.bellolucas.tasks_management.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank @Email
        String email,

        @NotBlank
        String password
) {
}
