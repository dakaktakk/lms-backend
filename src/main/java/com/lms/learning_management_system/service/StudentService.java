package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.StudentCreateDto;
import com.lms.learning_management_system.domain.dto.StudentDto;
import com.lms.learning_management_system.domain.dto.StudentUpdateDto;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Student;
import com.lms.learning_management_system.domain.mapper.StudentMapper;
import com.lms.learning_management_system.exception.ResourceNotFoundException;
import com.lms.learning_management_system.exception.StudentNotFoundException;
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

        Set<Group> groups = new HashSet<>(groupRepository.findAllById(request.groupIds()));
        if (groups.size() != request.groupIds().size()) {
            throw new ResourceNotFoundException("Одна или несколько групп не найдены");
        }

        Student student = studentMapper.toEntity(request, groups);

        Student savedStudent = studentRepository.save(student);

        return studentMapper.toDto(savedStudent);
    }

    @Transactional(readOnly = true)
    public Page<StudentDto> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public StudentDto getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return studentMapper.toDto(student);
    }

    @Transactional
    public StudentDto update(Long id, StudentUpdateDto dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        Set<Group> groups = new HashSet<>(groupRepository.findAllById(dto.groupIds()));
        if (groups.size() != dto.groupIds().size()) {
            throw new ResourceNotFoundException("Одна или несколько групп не найдены");
        }

        studentMapper.update(dto, groups, student);
        studentRepository.save(student);

        return studentMapper.toDto(student);
    }

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

}
