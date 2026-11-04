package com.lms.learning_management_system.domain.mapper;

import com.lms.learning_management_system.domain.dto.CourseCreateDto;
import com.lms.learning_management_system.domain.dto.CourseDto;
import com.lms.learning_management_system.domain.dto.CourseUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper extends BaseMapper<Course, CourseDto, CourseCreateDto, CourseUpdateDto>{
}
