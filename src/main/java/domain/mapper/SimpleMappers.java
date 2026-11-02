package domain.mapper;

import domain.dto.CourseDto;
import domain.dto.TeacherDto;
import domain.entity.Course;
import domain.entity.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleMappers {
    TeacherDto toDto(Teacher teacher);
    CourseDto toDto(Course course);
}
