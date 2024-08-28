package ru.aston.user.author.repository;

import java.util.Collection;
import ru.aston.user.author.entity.Author;

public interface AuthorRepository {
    Author getUserById(long id);

    Collection<Author> getAllUsers();

    Author createUser(Author user);

    Author updateUser(Author user);

    void deleteUser(Long idUser);
}
