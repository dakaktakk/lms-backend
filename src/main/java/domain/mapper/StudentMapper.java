package domain.mapper;

import domain.dto.StudentCreateDto;
import domain.dto.StudentDto;
import domain.dto.StudentUpdateDto;
import domain.entity.Group;
import domain.entity.Student;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", source = "groups")
    Student toEntity(StudentCreateDto dto, @Context Set<Group> groups);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groups", source = "groups")
    void update(StudentUpdateDto dto, Set<Group> groups, @MappingTarget Student student);

    @Mapping(target = "groupIds", expression = "java(mapGroupIds(student.getGroups()))")
    StudentDto toDto(Student student);

    default Set<Long> mapGroupIds(Set<Group> groups) {
        if (groups == null) return null;
        return groups.stream().map(Group::getId).collect(toSet());
    }
}