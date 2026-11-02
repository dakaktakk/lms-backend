package domain.mapper;

import domain.dto.ScheduleDto;
import domain.entity.Course;
import domain.entity.Group;
import domain.entity.Schedule;
import domain.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "courseId", source = "course.id")
    ScheduleDto toDto(Schedule schedule);

    @Mapping(target = "group", source = "group")
    @Mapping(target = "teacher", source = "teacher")
    @Mapping(target = "course", source = "course")
    Schedule toEntity(ScheduleDto dto, Group group, Teacher teacher, Course course);
}
