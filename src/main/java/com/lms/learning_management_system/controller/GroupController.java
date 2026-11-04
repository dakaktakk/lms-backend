package com.lms.learning_management_system.controller;

import com.lms.learning_management_system.domain.dto.GroupCreateDto;
import com.lms.learning_management_system.domain.dto.GroupDto;
import com.lms.learning_management_system.domain.dto.GroupUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.lms.learning_management_system.service.GroupService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@Tag(name = "Groups", description = "Управление группами")
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Создать группу")
    public GroupDto create(@Valid @RequestBody GroupCreateDto dto) {
        return groupService.create(dto);
    }

    @GetMapping
    @Operation(summary = "Получить все группы")
    public Page<GroupDto> getAll(Pageable pageable) {
        return groupService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить группу по ID")
    public GroupDto getById(@PathVariable Long id) {
        return groupService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить группу")
    public GroupDto update(@PathVariable Long id, @Valid @RequestBody GroupUpdateDto request) {
        return groupService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Удалить группу")
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

    @PostMapping("/{groupId}/students/{studentId}")
    @Operation(summary = "Добавить студента в группу")
    public GroupDto addStudentToGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        return groupService.addStudent(groupId, studentId);
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    @Operation(summary = "Удалить студента из группы")
    public GroupDto removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        return groupService.removeStudent(groupId, studentId);
    }
}