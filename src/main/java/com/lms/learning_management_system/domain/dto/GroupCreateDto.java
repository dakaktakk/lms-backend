package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupCreateDto {
    @NotBlank(message = "Название группы обязательно")
    private String name;
}