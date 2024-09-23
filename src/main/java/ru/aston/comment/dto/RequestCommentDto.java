package ru.aston.comment.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Data Transfer Object for request comment")
public class RequestCommentDto {
    @NotBlank(groups = {Create.class, Update.class})
    @ApiModelProperty(name = "Description of the comment", example = "This is a comment description")
    private String description;
}
