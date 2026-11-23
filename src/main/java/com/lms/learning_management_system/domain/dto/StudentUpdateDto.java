package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record StudentUpdateDto(
    @NotBlank(message = "Имя обязательно") String firstName,
    @NotBlank(message = "Фамилия обязательна") String lastName,
    @NotEmpty(message = "Студент должен состоять минимум в одной группе") Set<Long> groupIds
) {}
