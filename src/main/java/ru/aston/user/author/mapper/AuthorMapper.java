package ru.aston.user.author.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.entity.Author;

public class AuthorMapper {
    public ResponseAuthorDto toAuthorDto(Author author) {
        return ResponseAuthorDto.builder()
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }

    public Author toAuthorForCreate(RequestAuthorDto requestAuthorDto) {
        Author author = new Author();
        author.setName(requestAuthorDto.getName());
        author.setEmail(requestAuthorDto.getEmail());
        return author;
    }

    public Author toAuthorForUpdate(RequestAuthorDto requestAuthorDto, long authorId) {
        Author author = new Author();
        author.setId(authorId);
        author.setName(requestAuthorDto.getName());
        author.setEmail(requestAuthorDto.getEmail());
        return author;
    }

    public Collection<ResponseAuthorDto> toCollectionAuthorDto(Collection<Author> authors) {
        return authors.stream()
                .map(this::toAuthorDto)
                .collect(Collectors.toSet());
    }

    public Set<ResponseAuthorDto> toSetUserDto(Set<Author> authors) {
        return authors.stream()
                .map(this::toAuthorDto)
                .collect(Collectors.toSet());
    }
}
