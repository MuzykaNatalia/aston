package ru.aston.user.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.post.model.Post;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
    @ToString.Exclude
    private Set<Post> posts;
}
