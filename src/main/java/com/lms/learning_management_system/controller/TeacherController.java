package com.lms.learning_management_system.controller;

import com.lms.learning_management_system.domain.dto.TeacherCreateDto;
import com.lms.learning_management_system.domain.dto.TeacherDto;
import com.lms.learning_management_system.domain.dto.TeacherUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.lms.learning_management_system.service.TeacherService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Управление преподавателями")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Создать преподавателя")
    public TeacherDto create(@Valid @RequestBody TeacherCreateDto request) {
        return teacherService.create(request);
    }

    @GetMapping
    @Operation(summary = "Получить всех преподавателей")
    public Page<TeacherDto> getAll(Pageable pageable) {
        return teacherService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить преподавателя по ID")
    public TeacherDto getById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить преподавателя")
    public TeacherDto update(@PathVariable Long id, @Valid @RequestBody TeacherUpdateDto dto) {
        return teacherService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить преподавателя")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}