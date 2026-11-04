package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.StudentCreateDto;
import com.lms.learning_management_system.domain.dto.StudentDto;
import com.lms.learning_management_system.domain.dto.StudentUpdateDto;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Student;
import com.lms.learning_management_system.domain.mapper.StudentMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.learning_management_system.repository.GroupRepository;
import com.lms.learning_management_system.repository.StudentRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDto create(StudentCreateDto request) {

        Set<Group> groups = new HashSet<>(groupRepository.findAllById(request.getGroupIds()));
        if (groups.size() != request.getGroupIds().size()) {
            throw new EntityNotFoundException("Одна или несколько групп не найдены");
        }

        Student student = studentMapper.toEntity(request, groups);

        return studentMapper.toDto(student);
    }

    @Transactional(readOnly = true)
    public Page<StudentDto> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public StudentDto getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден: " + id));
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto update(Long id, StudentUpdateDto dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден: " + id));

        Set<Group> groups = new HashSet<>(groupRepository.findAllById(dto.getGroupIds()));
        if (groups.size() != dto.getGroupIds().size()) {
            throw new EntityNotFoundException("Одна или несколько групп не найдены");
        }

        studentMapper.update(dto, groups, student);

        return studentMapper.toDto(student);
    }

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Студент не найден: " + id);
        }
        studentRepository.deleteById(id);
    }

}
