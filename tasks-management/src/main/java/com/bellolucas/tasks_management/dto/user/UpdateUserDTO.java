package com.bellolucas.tasks_management.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String firstName,

        @Size(min = 3, max = 100, message = "O sobrenome deve ter entre 3 e 100 caracteres")
        String lastName,

        @Email
        String email,

        @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres")
        String password
) {
}
