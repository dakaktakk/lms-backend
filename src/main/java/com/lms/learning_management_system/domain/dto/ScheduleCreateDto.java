package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleCreateDto {
    @NotNull(message = "ID группы обязателен")
    private Long groupId;
    @NotNull(message = "ID преподавателя обязателен")
    private Long teacherId;
    @NotNull(message = "ID курса обязателен")
    private Long courseId;
    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime startDate;
    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime endDate;
}
