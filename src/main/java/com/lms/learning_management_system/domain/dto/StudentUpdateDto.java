package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class StudentUpdateDto {
    @NotBlank(message = "Имя обязательно")
    private String firstName;
    @NotBlank(message = "Фамилия обязательна")
    private String lastName;
    @NotEmpty(message = "Студент должен состоять минимум в одной группе")
    private Set<@NotNull Long> groupIds;
}
