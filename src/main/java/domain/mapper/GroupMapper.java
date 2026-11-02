package domain.mapper;

import domain.dto.GroupDto;
import domain.entity.Group;
import domain.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(target = "studentIds", expression = "java(mapStudentIds(group.getStudents()))")
    GroupDto toDto(Group group);

    default Set<Long> mapStudentIds(Set<Student> students) {
        if (students == null) return null;
        return students.stream().map(Student::getId).collect(toSet());
    }
}
