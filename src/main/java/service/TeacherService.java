package service;

import domain.dto.TeacherCreateDto;
import domain.dto.TeacherDto;
import domain.dto.TeacherUpdateDto;
import domain.entity.Teacher;
import domain.mapper.SimpleMappers;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.TeacherRepository;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepo;
    private final SimpleMappers mapper;

    @Transactional
    public TeacherDto create(TeacherCreateDto dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        return mapper.toDto(teacherRepo.save(teacher));
    }

    @Transactional(readOnly = true)
    public Page<TeacherDto> getAll(Pageable pageable) {
        return teacherRepo.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public TeacherDto getById(Long id) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Преподаватель не найден: " + id));
        return mapper.toDto(teacher);
    }

    @Transactional
    public TeacherDto update(Long id, TeacherUpdateDto dto) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Преподаватель не найден: " + id));
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        return mapper.toDto(teacherRepo.save(teacher));
    }

    @Transactional
    public void delete(Long id) {
        if (!teacherRepo.existsById(id)) {
            throw new EntityNotFoundException("Преподаватель не найден: " + id);
        }
        teacherRepo.deleteById(id);
    }
}
