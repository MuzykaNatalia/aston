package ru.aston.repository;

import java.util.Collection;
import ru.aston.model.User;

public interface UserRepository {
    User getUserById(long id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    int deleteUser(Long idUser);
}
