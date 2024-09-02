package ru.aston.user.author.service;

import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import java.util.Collection;

public interface AuthorService {
    ResponseAuthorDto getAuthorById(long userId);

    Collection<ResponseAuthorDto> getAllAuthors();

    ResponseAuthorDto createAuthor(RequestAuthorDto user);

    ResponseAuthorDto updateAuthor(long userId, RequestAuthorDto user);

    void deleteAuthor(long userId);
}
