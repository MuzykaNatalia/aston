package ru.aston.user.author.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.entity.Author;
import ru.aston.user.parent.entity.User;

public class AuthorMapper {
    public ResponseAuthorDto toUserDto(User user) {
        return ResponseAuthorDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public Author toUserForCreate(RequestAuthorDto user) {
        Author author = new Author();
        author.setName(user.getName());
        author.setEmail(user.getEmail());
        return author;
    }

    public Collection<ResponseAuthorDto> toCollectionUserDto(Collection<Author> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toSet());
    }

    public Set<ResponseAuthorDto> toSetUserDto(Set<Author> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toSet());
    }
}
