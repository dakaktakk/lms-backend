package controller;

import domain.dto.StudentCreateDto;
import domain.dto.StudentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import service.StudentService;

import static org.springframework.http.HttpStatus.CREATED;

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
}
