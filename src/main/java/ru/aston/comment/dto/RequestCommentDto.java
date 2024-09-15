package ru.aston.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.config.Create;
import ru.aston.config.Update;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDto {
    @NotBlank(groups = {Create.class, Update.class})
    private String description;
}
