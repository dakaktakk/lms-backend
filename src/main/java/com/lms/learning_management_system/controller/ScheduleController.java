package com.lms.learning_management_system.controller;

import com.lms.learning_management_system.domain.dto.ScheduleCreateDto;
import com.lms.learning_management_system.domain.dto.ScheduleDto;
import com.lms.learning_management_system.domain.dto.ScheduleUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.lms.learning_management_system.service.ScheduleService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Управление расписанием")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Создать расписание (назначить время курса для группы)")
    public ScheduleDto create(@Valid @RequestBody ScheduleCreateDto request) {
        return scheduleService.create(request);
    }

    @GetMapping
    @Operation(summary = "Получить всё расписание")
    public Page<ScheduleDto> getAll(Pageable pageable) {
        return scheduleService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить расписание по ID")
    public ScheduleDto getById(@PathVariable Long id) {
        return scheduleService.getById(id);
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "График курсов для группы")
    public Page<ScheduleDto> getByGroup(@PathVariable Long groupId, Pageable pageable) {
        return scheduleService.getScheduleByGroup(groupId, pageable);
    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "График занятий для преподавателя")
    public Page<ScheduleDto> getByTeacher(@PathVariable Long teacherId, Pageable pageable) {
        return scheduleService.getScheduleByTeacher(teacherId, pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменить время проведения курса")
    public ScheduleDto update(@PathVariable Long id, @Valid @RequestBody ScheduleUpdateDto request) {
        return scheduleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить расписание")
    public void delete(@PathVariable Long id) {
        scheduleService.delete(id);
    }
}