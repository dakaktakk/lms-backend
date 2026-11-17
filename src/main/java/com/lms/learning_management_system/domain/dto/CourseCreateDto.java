package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CourseCreateDto(
    @NotBlank(message = "Название курса обязательно") String name,
    String description
) {}
