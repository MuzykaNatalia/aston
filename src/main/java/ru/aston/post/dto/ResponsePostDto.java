package ru.aston.post.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.aston.user.author.dto.ResponseAuthorDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponsePostDto {
    private Long id;
    private String createdOn;
    private String description;
    private Set<ResponseAuthorDto> user;
}
