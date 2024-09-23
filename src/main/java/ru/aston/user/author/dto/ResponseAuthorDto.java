package ru.aston.user.author.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel(description = "Data Transfer Object for response author")
public class ResponseAuthorDto {
    @ApiModelProperty(name = "Name of the author", example = "john_doe")
    private String name;
    @ApiModelProperty(name = "Email address of the author", example = "john@mail.ru")
    private String email;
}
