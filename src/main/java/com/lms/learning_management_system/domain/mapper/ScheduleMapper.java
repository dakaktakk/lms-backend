package com.lms.learning_management_system.domain.mapper;

import com.lms.learning_management_system.domain.dto.ScheduleCreateDto;
import com.lms.learning_management_system.domain.dto.ScheduleDto;
import com.lms.learning_management_system.domain.dto.ScheduleUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Schedule;
import com.lms.learning_management_system.domain.entity.Teacher;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "courseId", source = "course.id")
    ScheduleDto toDto(Schedule schedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", source = "group")
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "course", source = "course")
    @Mapping(target = "startDate", source = "request.startDate")
    @Mapping(target = "endDate", source = "request.endDate")
    Schedule toEntity(ScheduleCreateDto request, Group group, Teacher teacher, Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "course", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ScheduleUpdateDto request, @MappingTarget Schedule schedule);
}
