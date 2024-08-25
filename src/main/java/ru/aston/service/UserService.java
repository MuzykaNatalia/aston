package ru.aston.service;

import ru.aston.dto.RequestUserDto;
import ru.aston.dto.ResponseUserDto;
import java.util.Collection;
import javax.validation.constraints.Positive;

public interface UserService {
    ResponseUserDto getUserById(@Positive long idUser);

    Collection<ResponseUserDto> getAllUsers();

    ResponseUserDto createUser(RequestUserDto user);

    ResponseUserDto updateUser(@Positive long idUser, RequestUserDto user);

    void deleteUser(@Positive long idUser);
}
