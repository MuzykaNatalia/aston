package ru.aston.user.author.repository;

import java.util.Collection;
import org.springframework.data.domain.Pageable;
import ru.aston.user.author.entity.Author;

public interface AuthorRepository {
    Author getAuthorById(long authorId);

    Collection<Author> getAllAuthors(Pageable pageable);

    Author createAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteAuthor(long authorId);
}
