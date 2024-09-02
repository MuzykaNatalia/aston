package ru.aston.user.author.repository;

import java.util.Collection;
import ru.aston.user.author.entity.Author;

public interface AuthorRepository {
    Author getAuthorById(long id);

    Collection<Author> getAllAuthors();

    Author createAuthor(Author user);

    Author updateAuthor(Author user);

    void deleteAuthor(Long idUser);
}
