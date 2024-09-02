package ru.aston.comment.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private long id;
    private LocalDateTime createdOn;
    private String description;
    private Long userId;
    private Long postId;
}
