package ru.aston.comment.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.aston.post.entity.Post;
import ru.aston.user.author.entity.Author;

@Entity
@Table(name = "comment")
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Author author;
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;
}
