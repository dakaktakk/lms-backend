package domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseUpdateDto {
    @NotBlank(message = "Название курса обязательно")
    private String name;
    private String description;
}
