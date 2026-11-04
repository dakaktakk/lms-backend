package com.lms.learning_management_system.domain.mapper;

import com.lms.learning_management_system.domain.dto.TeacherCreateDto;
import com.lms.learning_management_system.domain.dto.TeacherDto;
import com.lms.learning_management_system.domain.dto.TeacherUpdateDto;
import com.lms.learning_management_system.domain.entity.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper extends BaseMapper<Teacher, TeacherDto, TeacherCreateDto, TeacherUpdateDto> {
}