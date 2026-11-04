package com.lms.learning_management_system.controller;

import com.lms.learning_management_system.domain.dto.StudentCreateDto;
import com.lms.learning_management_system.domain.dto.StudentDto;
import com.lms.learning_management_system.domain.dto.StudentUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.lms.learning_management_system.service.StudentService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Управление студентами")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Создать студента")
    public StudentDto create(@Valid @RequestBody StudentCreateDto request) {
        return studentService.create(request);
    }

    @GetMapping
    @Operation(summary = "Получить всех студентов")
    public Page<StudentDto> getAll(Pageable pageable) {
        return studentService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID")
    public StudentDto getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить студента")
    public StudentDto update(@PathVariable Long id, @Valid @RequestBody StudentUpdateDto request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить студента")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}