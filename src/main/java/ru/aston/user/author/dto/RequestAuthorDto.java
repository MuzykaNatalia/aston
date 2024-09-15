package ru.aston.user.author.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.aston.config.Create;
import ru.aston.config.Update;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAuthorDto {
    @NotBlank(groups = Create.class)
    private String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    private String email;
}
