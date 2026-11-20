package com.lms.learning_management_system.domain.dto;

import java.util.Set;

public record StudentDto(
        Long id,
        String firstName,
        String lastName,
        Set<Long> groupIds
) {}
