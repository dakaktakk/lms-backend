package com.lms.learning_management_system.domain.dto;

import java.util.Set;

public record GroupDto(
        Long id,
        String name,
        Set<Long> studentIds
) {}
