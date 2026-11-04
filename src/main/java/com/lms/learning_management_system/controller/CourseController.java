package com.lms.learning_management_system.controller;

import com.lms.learning_management_system.domain.dto.CourseCreateDto;
import com.lms.learning_management_system.domain.dto.CourseDto;
import com.lms.learning_management_system.domain.dto.CourseUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.lms.learning_management_system.service.CourseService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Управление курсами")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Создать курс")
    public CourseDto create(@Valid @RequestBody CourseCreateDto dto) {
        return courseService.create(dto);
    }

    @GetMapping
    @Operation(summary = "Получить все курсы")
    public Page<CourseDto> getAll(Pageable pageable) {
        return courseService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить курс по ID")
    public CourseDto getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить курс")
    public CourseDto update(@PathVariable Long id, @Valid @RequestBody CourseUpdateDto dto) {
        return courseService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить курс")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}