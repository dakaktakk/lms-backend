package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateDto(
        @NotBlank(message = "Название группы обязательно") String name
) {}
