package com.lms.learning_management_system.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseMapper<E, D, C, U> {

    D toDto(E entity);

    E toEntity(C createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(U updateDto, @MappingTarget E entity);
}
