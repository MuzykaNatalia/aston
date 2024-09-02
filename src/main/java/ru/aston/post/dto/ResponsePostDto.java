package ru.aston.post.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.user.dto.ResponseUserDto;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePostDto {
    private Long id;
    private String createdOn;
    private String description;
    private Set<ResponseUserDto> user;
}
