package controller;

import domain.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Управление расписанием")
public class ScheduleController {
    private final ScheduleService scheduleService;

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
}