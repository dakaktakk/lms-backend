package service;

import domain.dto.ScheduleDto;
import domain.entity.Course;
import domain.entity.Group;
import domain.entity.Schedule;
import domain.entity.Teacher;
import domain.mapper.ScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CourseRepository;
import repository.GroupRepository;
import repository.ScheduleRepository;
import repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepo;
    private final GroupRepository groupRepo;
    private final TeacherRepository teacherRepo;
    private final CourseRepository courseRepo;
    private final ScheduleMapper mapper;

    @Transactional
    public ScheduleDto create(ScheduleDto dto) {

        Group group = groupRepo.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена"));

        Teacher teacher = teacherRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Учитель не найден"));

        Course course = courseRepo.findById(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));

        Schedule schedule = mapper.toEntity(dto, group, teacher, course);
        return mapper.toDto(scheduleRepo.save(schedule));
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByGroup(Long groupId, Pageable pageable) {

        Group group = groupRepo.findById(groupId).orElseThrow(EntityNotFoundException::new);
        return scheduleRepo.findByGroup(group, pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByTeacher(Long teacherId, Pageable pageable) {

        Teacher teacher = teacherRepo.findById(teacherId).orElseThrow(EntityNotFoundException::new);
        return scheduleRepo.findByTeacher(teacher, pageable).map(mapper::toDto);
    }
}