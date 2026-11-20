package com.lms.learning_management_system.service;

import com.lms.learning_management_system.domain.dto.GroupCreateDto;
import com.lms.learning_management_system.domain.dto.GroupDto;
import com.lms.learning_management_system.domain.dto.GroupUpdateDto;
import com.lms.learning_management_system.domain.entity.Group;
import com.lms.learning_management_system.domain.entity.Student;
import com.lms.learning_management_system.domain.mapper.GroupMapper;
import com.lms.learning_management_system.exception.GroupNotFoundException;
import com.lms.learning_management_system.exception.ResourceNotFoundException;
import com.lms.learning_management_system.exception.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lms.learning_management_system.repository.GroupRepository;
import com.lms.learning_management_system.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupDto create(GroupCreateDto request) {
        return groupMapper.toDto(groupRepository.save(groupMapper.toEntity(request)));
    }

    @Transactional(readOnly = true)
    public Page<GroupDto> getAll(Pageable pageable) {
        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }

    @Transactional(readOnly = true)
    public GroupDto getById(Long id) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto update(Long id, GroupUpdateDto request) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
        groupMapper.updateEntityFromDto(request, group);
        return groupMapper.toDto(group);
    }

    @Transactional
    public void delete(Long id) {

        if (!groupRepository.existsById(id)) {
            throw new GroupNotFoundException(id);
        }

        groupRepository.deleteById(id);
    }

    @Transactional
    public GroupDto addStudent(Long groupId, Long studentId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        if (group.getStudents().stream().anyMatch(s -> s.getId().equals(studentId))) {
            throw new ResourceNotFoundException("Студент уже состоит в этой группе");
        }

        group.getStudents().add(student);
        student.getGroups().add(group);

        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto removeStudent(Long groupId, Long studentId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }

        boolean removed = group.getStudents().removeIf(s -> s.getId().equals(studentId));
        if (!removed) {
            throw new ResourceNotFoundException("Студент не состоит в этой группе");
        }

        return groupMapper.toDto(group);
    }
}
