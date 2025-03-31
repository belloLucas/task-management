package com.bellolucas.tasks_management.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateUserDTO(
        @NotNull
        String firstName,

        @NotNull
        String lastName,

        @NotNull @Email
        String email,

        @NotNull
        String password
) {
}
