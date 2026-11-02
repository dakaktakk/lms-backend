package domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleUpdateDto {
    @NotNull(message = "Дата начала обязательна")
    private LocalDateTime startDate;
    @NotNull(message = "Дата окончания обязательна")
    private LocalDateTime endDate;
}