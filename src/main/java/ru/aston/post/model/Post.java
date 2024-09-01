package ru.aston.post.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.comment.model.Comment;
import ru.aston.user.model.User;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private long id;
    private LocalDateTime createdOn;
    private String description;
    @ToString.Exclude
    private Set<User> users;
    @ToString.Exclude
    private Set<Comment> comments;
}
