package com.lms.learning_management_system.domain.mapper;

import com.lms.learning_management_system.domain.dto.StudentCreateDto;
import com.lms.learning_management_system.domain.dto.StudentDto;
import com.lms.learning_management_system.domain.dto.StudentUpdateDto;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StudentMapper extends BaseMapper<Student, StudentDto, StudentCreateDto, StudentUpdateDto> {

    @Mapping(target = "groupIds", source = "groups")
    StudentDto toDto(Student entity);

    @Mapping(target = "groups", source = "groups")
    Student toEntity(StudentCreateDto dto, Set<Group> groups);

    @Mapping(target = "groups", source = "groups")
    void update(StudentUpdateDto dto, Set<Group> groups, @MappingTarget Student student);

    default Set<Long> mapGroupsToGroupIds(Set<Group> groups) {
        if (groups == null || groups.isEmpty()) {
            return Collections.emptySet();
        }
        return groups.stream()
                .map(Group::getId)
                .collect(Collectors.toSet());
    }
}