package ru.aston.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.user.author.dto.ResponseAuthorDto;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Data Transfer Object for response comment")
public class ResponseCommentDto {
    @ApiModelProperty(value = "The date and time when the comment was created", example = "2024-09-23 10:15:30")
    private String createdOn;
    @ApiModelProperty(name = "Description of the comment", example = "This is a comment description")
    private String description;
    @ApiModelProperty(value = "Details about the authors of the comment")
    private ResponseAuthorDto user;
}
