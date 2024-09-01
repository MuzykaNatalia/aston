package ru.aston.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import ru.aston.user.model.User;

public interface UserRepository {
    User getUserById(long id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    int deleteUser(Long idUser);

    List<User> findUsersByIds(Set<Long> userIds);
}
