package ru.aston.user.author.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Data Transfer Object for request author")
public class RequestAuthorDto {
    @NotBlank(groups = Create.class)
    @ApiModelProperty(name = "Name of the author", example = "john_doe")
    private String name;
    @Email(groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class)
    @ApiModelProperty(name = "Email address of the author", example = "john@mail.ru")
    private String email;
}
