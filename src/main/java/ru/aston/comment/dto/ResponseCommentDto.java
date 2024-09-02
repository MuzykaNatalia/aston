package ru.aston.comment.dto;

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
public class ResponseCommentDto {
    private String createdOn;
    private String description;
    private ResponseAuthorDto user;
}
