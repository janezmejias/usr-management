package com.usermanagement.core.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SignUpResponse(
        UUID id, LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin,
        String token, Boolean isActive
) {
}
