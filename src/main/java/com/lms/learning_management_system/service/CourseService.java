package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.CourseCreateDto;
import com.lms.learning_management_system.domain.dto.CourseDto;
import com.lms.learning_management_system.domain.dto.CourseUpdateDto;
import com.lms.learning_management_system.domain.entity.Course;
import com.lms.learning_management_system.domain.mapper.CourseMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.learning_management_system.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseDto create(CourseCreateDto request) {
        return courseMapper.toDto(courseRepository.save(courseMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public Page<CourseDto> getAll(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toDto);
    }

    @Transactional(readOnly = true)
    public CourseDto getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + id));
        return courseMapper.toDto(course);
    }

    @Transactional
    public CourseDto update(Long id, CourseUpdateDto request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден" + id));
        courseMapper.updateEntityFromDto(request, course);
        return courseMapper.toDto(course);
    }

    @Transactional
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Курс не найден" + id);
        }
        courseRepository.deleteById(id);
    }
}
