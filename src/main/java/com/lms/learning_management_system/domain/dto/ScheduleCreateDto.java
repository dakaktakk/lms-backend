package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleCreateDto(
    @NotNull(message = "ID группы обязателен") Long groupId,
    @NotNull(message = "ID преподавателя обязателен") Long teacherId,
    @NotNull(message = "ID курса обязателен") Long courseId,
    @NotNull(message = "Дата начала обязательна") LocalDateTime startDate,
    @NotNull(message = "Дата окончания обязательна") LocalDateTime endDate
) {}
