package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseCreateDto {
    @NotBlank(message = "Название курса обязательно")
    private String name;
    private String description;
}
