package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.ScheduleCreateDto;
import com.lms.learning_management_system.domain.dto.ScheduleDto;
import com.lms.learning_management_system.domain.dto.ScheduleUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Schedule;
import com.lms.learning_management_system.domain.entity.Teacher;
import com.lms.learning_management_system.domain.mapper.ScheduleMapper;
import com.lms.learning_management_system.exception.CourseNotFoundException;
import com.lms.learning_management_system.exception.GroupNotFoundException;
import com.lms.learning_management_system.exception.ScheduleNotFoundException;
import com.lms.learning_management_system.exception.TeacherNotFoundException;
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

        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new GroupNotFoundException(request.groupId()));

        Teacher teacher = teacherRepository.findById(request.teacherId())
                .orElseThrow(() -> new TeacherNotFoundException(request.teacherId()));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new CourseNotFoundException(request.courseId()));

        Schedule schedule = scheduleMapper.toEntity(request, group, teacher, course);

        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getAll(Pageable pageable) {
        return scheduleRepository.findAll(pageable).map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ScheduleDto getById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));
        return scheduleMapper.toDto(schedule);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByGroup(Long groupId, Pageable pageable) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        return scheduleRepository.findByGroup(group, pageable).map(scheduleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ScheduleDto> getScheduleByTeacher(Long teacherId, Pageable pageable) {

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException(teacherId));
        return scheduleRepository.findByTeacher(teacher, pageable).map(scheduleMapper::toDto);
    }

    @Transactional
    public ScheduleDto update(Long id, ScheduleUpdateDto request) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ScheduleNotFoundException(id));

        scheduleMapper.updateEntityFromDto(request, schedule);

        return scheduleMapper.toDto(schedule);
    }

    @Transactional
    public void delete(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ScheduleNotFoundException(id);
        }
        scheduleRepository.deleteById(id);
    }
}
