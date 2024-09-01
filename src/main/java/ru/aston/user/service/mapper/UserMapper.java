package ru.aston.user.service.mapper;

import java.util.Set;
import ru.aston.user.dto.RequestUserDto;
import ru.aston.user.dto.ResponseUserDto;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import ru.aston.user.model.User;

public class UserMapper {
    public ResponseUserDto toUserDto(User user) {
        return ResponseUserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUserForCreate(RequestUserDto user) {
        return User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUserForUpdate(RequestUserDto user, long idUser) {
        return User.builder()
                .id(idUser)
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<ResponseUserDto> toCollectionUserDto(Collection<User> users) {
        return users.stream().map(this::toUserDto).collect(Collectors.toList());
    }

    public Set<ResponseUserDto> toSetUserDto(Set<User> users) {
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toSet());
    }
}
