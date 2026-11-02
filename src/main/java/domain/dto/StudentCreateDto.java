package domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class StudentCreateDto {

    @NotNull
    private String firstName;

    @NotNull private String lastName;

    @NotEmpty(message = "Студент должен состоять минимум в одной группе")
    private Set<@NotNull Long> groupIds;
}
