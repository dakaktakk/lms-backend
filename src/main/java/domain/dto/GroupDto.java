package domain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private Set<Long> studentIds;
}
