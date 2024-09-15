package ru.aston.post.dto;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aston.config.Create;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostDto {
    @NotBlank(groups = Create.class)
    private String description;
    @NotEmpty(groups = Create.class)
    private Set<Long> userIds;
}
