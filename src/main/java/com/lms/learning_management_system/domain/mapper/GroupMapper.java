package com.lms.learning_management_system.domain.mapper;

import com.lms.learning_management_system.domain.dto.GroupCreateDto;
import com.lms.learning_management_system.domain.dto.GroupDto;
import com.lms.learning_management_system.domain.dto.GroupUpdateDto;
import com.lms.learning_management_system.domain.entity.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper extends BaseMapper<Group, GroupDto, GroupCreateDto, GroupUpdateDto> {
}
