package ru.aston.user.admin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.aston.config.Create;
import ru.aston.config.Update;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Data Transfer Object for Admin")
public class AdminDto {
    @NotBlank(groups = Create.class)
    @ApiModelProperty(name = "Name of the admin", example = "john_doe")
    private String name;
    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    @ApiModelProperty(name = "Email address of the admin", example = "john@mail.ru")
    private String email;
    @Positive(groups = Create.class)
    @ApiModelProperty(name = "Level of the admin", example = "1")
    private Integer adminLevel;
}
