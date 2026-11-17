package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.TeacherCreateDto;
import com.lms.learning_management_system.domain.dto.TeacherDto;
import com.lms.learning_management_system.domain.dto.TeacherUpdateDto;
import com.lms.learning_management_system.domain.entity.Teacher;
import com.lms.learning_management_system.exception.TeacherNotFoundException;
import com.lms.learning_management_system.domain.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.learning_management_system.repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Transactional
    public TeacherDto create(TeacherCreateDto dto) {
        return teacherMapper.toDto(teacherRepository.save(teacherMapper.toEntity(dto)));
    }

    @Transactional(readOnly = true)
    public Page<TeacherDto> getAll(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(teacherMapper::toDto);
    }

    @Transactional(readOnly = true)
    public TeacherDto getById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
        return teacherMapper.toDto(teacher);
    }

    @Transactional
    public TeacherDto update(Long id, TeacherUpdateDto request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
        teacherMapper.updateEntityFromDto(request, teacher);
        return teacherMapper.toDto(teacher);
    }

    @Transactional
    public void delete(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException(id);
        }
        teacherRepository.deleteById(id);
    }
}
