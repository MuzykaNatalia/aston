package ru.aston.post.dto;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Data Transfer Object for request post")
public class RequestPostDto {
    @NotBlank(groups = Create.class)
    @ApiModelProperty(name = "Description of the post", example = "This is a post description")
    private String description;
    @NotEmpty(groups = Create.class)
    @ApiModelProperty(name = "Authors IDs of the post", example = "[1, 2, 3]")
    private Set<Long> userIds;
}
