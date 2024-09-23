package ru.aston.post.mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.entity.Post;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.mapper.AuthorMapper;
import static ru.aston.constant.Constant.FORMATTER;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final AuthorMapper authorMapper;

    public Post toPost(RequestPostDto postDto, Set<Author> users) {
        return Post.builder()
                .createdOn(LocalDateTime.now())
                .description(postDto.getDescription())
                .users(users)
                .build();
    }

    public ResponsePostDto toResponsePostDto(Post post) {
        return ResponsePostDto.builder()
                .id(post.getId())
                .createdOn(post.getCreatedOn().format(FORMATTER))
                .description(post.getDescription())
                .user(authorMapper.toSetUserDto(post.getUsers()))
                .build();
    }

    public Collection<ResponsePostDto> toResponsePostDto(Collection<Post> posts) {
        return posts.stream()
                .map(this::toResponsePostDto)
                .collect(Collectors.toList());
    }
}
