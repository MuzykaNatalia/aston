package ru.aston.user.author.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.user.author.dto.RequestAuthorDto;
import ru.aston.user.author.dto.ResponseAuthorDto;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.mapper.AuthorMapper;
import ru.aston.user.author.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseAuthorDto getAuthorById(long authorId) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new RuntimeException("Author not found ID " + authorId);
        }

        return authorMapper.toAuthorDto(author);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ResponseAuthorDto> getAllAuthors(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.asc("id")));
        Collection<Author> allAuthors = authorRepository.getAllAuthors(pageable);
        return authorMapper.toCollectionAuthorDto(allAuthors);
    }

    @Transactional
    @Override
    public ResponseAuthorDto createAuthor(RequestAuthorDto requestAuthorDto) {
        Author author = authorMapper.toAuthorForCreate(requestAuthorDto);
        author = authorRepository.createAuthor(author);
        log.info("Author created={}", author);
        return authorMapper.toAuthorDto(author);
    }

    @Transactional
    @Override
    public ResponseAuthorDto updateAuthor(long authorId, RequestAuthorDto requestAuthorDto) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new RuntimeException("Author not found ID " + authorId);
        }

        if (requestAuthorDto.getName() != null && !requestAuthorDto.getName().isBlank()) {
            author.setName(requestAuthorDto.getName());
        }

        if (requestAuthorDto.getEmail() != null) {
            author.setEmail(requestAuthorDto.getEmail());
        }

        Author updatedAuthor = authorRepository.updateAuthor(author);
        log.info("Author with ID {} updated", authorId);
        return authorMapper.toAuthorDto(updatedAuthor);
    }

    @Transactional
    @Override
    public void deleteAuthor(long authorId) {
        authorRepository.deleteAuthor(authorId);
        log.info("Author with ID {} deleted", authorId);
    }
}
