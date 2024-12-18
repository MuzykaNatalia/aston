package ru.aston.user.author.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.post.entity.Post;
import ru.aston.user.parent.entity.User;

@Entity
@Table(name = "author")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author extends User {
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonBackReference
    private Set<Post> posts;
}
