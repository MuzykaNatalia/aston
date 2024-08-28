package ru.aston.user.author.service;

import java.util.Collection;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.mapper.AuthorMapper;
import ru.aston.user.author.repository.AuthorRepository;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper = new AuthorMapper();

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseAuthorDto getUserById(long userId) {
        validateId(userId);
        Author user = authorRepository.getUserById(userId);
        return authorMapper.toUserDto(user);
    }

    @Override
    public Collection<ResponseAuthorDto> getAllUsers() {
        return authorMapper.toCollectionUserDto(authorRepository.getAllUsers());
    }

    @Override
    public ResponseAuthorDto createUser(RequestAuthorDto user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Name, email cannot be null or empty");
        }
        validateNameAndEmail(user);
        Author createdUser = authorRepository.createUser(authorMapper.toUserForCreate(user));
        return authorMapper.toUserDto(createdUser);
    }

    @Override
    public ResponseAuthorDto updateUser(long userId, RequestAuthorDto user) {
        validateId(userId);
        validateNameAndEmail(user);

        Author existingUser = authorRepository.getUserById(userId);
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        Author updatedUser = authorRepository.updateUser(existingUser);
        return authorMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        validateId(userId);
        authorRepository.deleteUser(userId);
    }

    private void validateNameAndEmail(RequestAuthorDto user) {
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

    private void validateId(long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be greater than zero");
        }
    }
}
