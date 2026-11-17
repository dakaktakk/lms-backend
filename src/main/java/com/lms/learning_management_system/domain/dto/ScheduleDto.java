package com.lms.learning_management_system.domain.dto;

import java.time.LocalDateTime;

public record ScheduleDto(
    Long id,
    Long groupId,
    Long teacherId,
    Long courseId,
    LocalDateTime startDate,
    LocalDateTime endDate
) {}
