package service;

import domain.dto.CourseCreateDto;
import domain.dto.CourseDto;
import domain.dto.CourseUpdateDto;
import domain.entity.Course;
import domain.mapper.SimpleMappers;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepo;
    private final SimpleMappers mapper;

    @Transactional
    public CourseDto create(CourseCreateDto dto) {
        Course course = new Course();
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return mapper.toDto(courseRepo.save(course));
    }

    @Transactional(readOnly = true)
    public Page<CourseDto> getAll(Pageable pageable) {
        return courseRepo.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public CourseDto getById(Long id) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден: " + id));
        return mapper.toDto(course);
    }

    @Transactional
    public CourseDto update(Long id, CourseUpdateDto dto) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Курс не найден" + id));
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        return mapper.toDto(courseRepo.save(course));
    }

    @Transactional
    public void delete(Long id) {
        if (!courseRepo.existsById(id)) {
            throw new EntityNotFoundException("Курс не найден" + id);
        }
        courseRepo.deleteById(id);
    }
}
