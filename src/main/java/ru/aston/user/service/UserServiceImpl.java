package ru.aston.user.service;

import ru.aston.user.dto.RequestUserDto;
import ru.aston.user.dto.ResponseUserDto;
import java.util.Collection;
import ru.aston.user.model.User;
import ru.aston.user.repository.UserRepository;
import ru.aston.user.service.mapper.UserMapper;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseUserDto getUserById(long userId) {
        User user = userRepository.getUserById(userId);
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
    public ResponseUserDto updateUser(long userId, RequestUserDto user) {
        validate(user);
        User updatedUser = userRepository.updateUser(userMapper.toUserForUpdate(user, userId));
        if (updatedUser == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        int affectedRows = userRepository.deleteUser(userId);
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
