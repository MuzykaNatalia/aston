package ru.aston.post.mapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.model.Post;
import ru.aston.user.model.User;
import ru.aston.user.service.mapper.UserMapper;
import static ru.aston.constant.Constant.FORMATTER;

public class PostMapper {
    private final UserMapper userMapper = new UserMapper();

    public Post toPost(RequestPostDto postDto, Set<User> users) {
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
                .user(userMapper.toSetUserDto(post.getUsers()))
                .build();
    }

    public Collection<ResponsePostDto> toResponsePostDto(Collection<Post> posts) {
        return posts.stream()
                .map(this::toResponsePostDto)
                .collect(Collectors.toList());
    }
}
