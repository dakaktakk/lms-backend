package service;

import domain.dto.GroupCreateDto;
import domain.dto.GroupDto;
import domain.dto.GroupUpdateDto;
import domain.entity.Group;
import domain.entity.Student;
import domain.mapper.GroupMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.GroupRepository;
import repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupDto create(GroupCreateDto dto) {

        Group group = new Group();
        group.setName(dto.getName());
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Transactional(readOnly = true)
    public Page<GroupDto> getAll(Pageable pageable) {

        return groupRepository.findAll(pageable).map(groupMapper::toDto);
    }

    public GroupDto getById(Long id) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + id));
        return groupMapper.toDto(group);
    }

    @Transactional
    public GroupDto update(Long id, GroupUpdateDto dto) {

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + id));
        group.setName(dto.getName());
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Transactional
    public void delete(Long id) {

        if (!groupRepository.existsById(id)) {
            throw new EntityNotFoundException("Группа не найдена: " + id);
        }
        groupRepository.deleteById(id);
    }

    @Transactional
    public GroupDto addStudent(Long groupId, Long studentId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + groupId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден: " + studentId));

        group.getStudents().add(student);
        student.getGroups().add(group);

        return groupMapper.toDto(groupRepository.save(group));
    }

    @Transactional
    public GroupDto removeStudent(Long groupId, Long studentId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена: " + groupId));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден: " + studentId));

        group.getStudents().remove(student);
        student.getGroups().remove(group);

        return groupMapper.toDto(groupRepository.save(group));
    }
}
