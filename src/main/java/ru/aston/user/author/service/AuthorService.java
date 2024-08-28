package ru.aston.user.author.service;

import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import java.util.Collection;

public interface AuthorService {
    ResponseAuthorDto getUserById(long userId);

    Collection<ResponseAuthorDto> getAllUsers();

    ResponseAuthorDto createUser(RequestAuthorDto user);

    ResponseAuthorDto updateUser(long userId, RequestAuthorDto user);

    void deleteUser(long userId);
}
