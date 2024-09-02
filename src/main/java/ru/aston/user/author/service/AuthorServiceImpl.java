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
    public ResponseAuthorDto getAuthorById(long authorId) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new RuntimeException("Author not found");
        }

        return authorMapper.toAuthorDto(author);
    }

    @Override
    public Collection<ResponseAuthorDto> getAllAuthors() {
        return authorMapper.toCollectionAuthorDto(authorRepository.getAllAuthors());
    }

    @Override
    public ResponseAuthorDto createAuthor(RequestAuthorDto author) {
        validateNameAndEmail(author);
        Author createdAuthor = authorRepository.createAuthor(authorMapper.toAuthorForCreate(author));
        return authorMapper.toAuthorDto(createdAuthor);
    }

    @Override
    public ResponseAuthorDto updateAuthor(long userId, RequestAuthorDto author) {
        validateNameAndEmail(author);
        Author updatedAuthor = authorRepository.updateAuthor(authorMapper.toAuthorForUpdate(author, userId));
        if (updatedAuthor == null) {
            throw new RuntimeException("User not found");
        }
        return authorMapper.toAuthorDto(updatedAuthor);
    }

    @Override
    public void deleteAuthor(long userId) {
        authorRepository.deleteAuthor(userId);
    }

    private void validateNameAndEmail(RequestAuthorDto author) {
        if (author == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (author.getName() == null || (author.getName() != null && author.getName().isBlank())) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (author.getEmail() == null || (author.getEmail() != null && author.getEmail().isBlank())) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (author.getEmail() != null && !author.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
    }
}
