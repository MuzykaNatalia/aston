package ru.aston.service;

import ru.aston.dto.RequestUserDto;
import ru.aston.dto.ResponseUserDto;
import java.util.Collection;
import ru.aston.model.User;
import ru.aston.repository.UserRepository;
import ru.aston.service.mapper.UserMapper;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseUserDto getUserById(long idUser) {
        User user = userRepository.getUserById(idUser);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.toUserDto(user);
    }

    @Override
    public Collection<ResponseUserDto> getAllUsers() {
        return userMapper.toCollectionUserDto(userRepository.getAllUsers());
    }

    @Override
    public ResponseUserDto createUser(RequestUserDto user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Name or email cannot be null or empty");
        }
        validate(user);
        User createdUser = userRepository.createUser(userMapper.toUserForCreate(user));
        return userMapper.toUserDto(createdUser);
    }

    @Override
    public ResponseUserDto updateUser(long idUser, RequestUserDto user) {
        validate(user);
        User updatedUser = userRepository.updateUser(userMapper.toUserForUpdate(user, idUser));
        if (updatedUser == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long idUser) {
        int affectedRows = userRepository.deleteUser(idUser);
        if (affectedRows < 1) {
            throw new RuntimeException("User not found");
        }
    }

    private void validate(RequestUserDto user) {
        if (user.getName() != null && user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (user.getEmail() != null && user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (user.getEmail() != null && !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }
}
