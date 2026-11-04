package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.ScheduleCreateDto;
import com.lms.learning_management_system.domain.dto.ScheduleDto;
import com.lms.learning_management_system.domain.dto.ScheduleUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Schedule;
import com.lms.learning_management_system.domain.entity.Teacher;
import com.lms.learning_management_system.domain.mapper.ScheduleMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.learning_management_system.repository.CourseRepository;
import com.lms.learning_management_system.repository.GroupRepository;
import com.lms.learning_management_system.repository.ScheduleRepository;
import com.lms.learning_management_system.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleDto create(ScheduleCreateDto request) {

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + request.getGroupId()));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new EntityNotFoundException("Учитель не найден: " + request.getTeacherId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + request.getCourseId()));

        Schedule schedule = scheduleMapper.toEntity(request, group, teacher, course);
        return scheduleMapper.toDto(schedule);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ScheduleDto getById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Расписание не найдено: " + id));
        return scheduleMapper.toDto(schedule);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByGroup(Long groupId, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + groupId));
        return scheduleRepository.findByGroup(group, pageable).map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByTeacher(Long teacherId, Pageable pageable) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("Учитель не найден: " + teacherId));
        return scheduleRepository.findByTeacher(teacher, pageable).map(scheduleMapper::toDto);
    }

    @Transactional
    public ScheduleDto update(Long id, ScheduleUpdateDto request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Расписание не найдено: " + id));

        scheduleMapper.updateEntityFromDto(request, schedule);

        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Transactional
    public void delete(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Расписание не найдно: " + id);
        }
        scheduleRepository.deleteById(id);
    }
}