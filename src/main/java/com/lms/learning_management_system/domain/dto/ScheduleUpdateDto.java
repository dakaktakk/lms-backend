package com.lms.learning_management_system.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ScheduleUpdateDto(
    @NotNull(message = "Дата начала обязательна") LocalDateTime startDate,
    @NotNull(message = "Дата окончания обязательна") LocalDateTime endDate
) {}
