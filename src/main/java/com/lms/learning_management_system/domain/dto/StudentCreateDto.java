package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record StudentCreateDto(
    @NotNull String firstName,
    @NotNull String lastName,
    @NotEmpty(message = "Студент должен состоять минимум в одной группе") Set<@NotNull Long> groupIds
) {}
