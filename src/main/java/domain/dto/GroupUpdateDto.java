package domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GroupUpdateDto {
    @NotBlank(message = "Название группы обязательно")
    private String name;
}