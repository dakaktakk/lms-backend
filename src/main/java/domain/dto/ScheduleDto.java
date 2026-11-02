package domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {
    private Long id;
    private Long groupId;
    private Long teacherId;
    private Long courseId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}