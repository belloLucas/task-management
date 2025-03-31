package com.bellolucas.tasks_management.dto.auth;

import java.time.LocalDateTime;

public record AuthResponseDTO(
        String token,
        Long userId,
        String firstName,
        String lastName,
        String email,
        LocalDateTime expirationTime
) {
}
