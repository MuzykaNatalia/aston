package ru.aston.user.service;

import ru.aston.user.dto.RequestUserDto;
import ru.aston.user.dto.ResponseUserDto;
import java.util.Collection;

public interface UserService {
    ResponseUserDto getUserById(long userId);

    Collection<ResponseUserDto> getAllUsers();

    ResponseUserDto createUser(RequestUserDto user);

    ResponseUserDto updateUser(long userId, RequestUserDto user);

    void deleteUser(long userId);
}
