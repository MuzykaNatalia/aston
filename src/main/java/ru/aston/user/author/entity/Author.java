package ru.aston.user.author.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aston.post.entity.Post;
import ru.aston.user.parent.entity.User;

@Entity
@Table(name = "author")
@NoArgsConstructor
@Getter
@Setter
public class Author extends User {
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonBackReference
    private Set<Post> posts;
}
