package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherUpdateDto {
    @NotBlank(message = "Имя обязательно")
    private String firstName;
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;
}
