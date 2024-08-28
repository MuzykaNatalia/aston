package ru.aston.user.author.repository;

import jakarta.persistence.Query;
import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.aston.common.AbstractRepository;
import ru.aston.user.author.entity.Author;
import ru.aston.utils.HibernateUtil;

public class AuthorRepositoryImpl extends AbstractRepository<Author, Long> implements AuthorRepository {
    public AuthorRepositoryImpl() {
            super(Author.class);
    }

    @Override
    public Author getUserById(long userId) {
        return super.getById(userId);
    }

    @Override
    public Collection<Author> getAllUsers() {
        return super.getAll();
    }

    @Override
    public Author createUser(Author user) {
        return super.save(user);
    }

    @Override
    public Author updateUser(Author user) {
        return super.update(user);
    }

    @Override
    public void deleteUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = "DELETE FROM user_post WHERE user_id = :userId";
            Query query = session.createNativeQuery(sql, Author.class);
            query.setParameter("userId", userId);
            query.executeUpdate();

            Author user = session.get(Author.class, userId);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting user", e);
        }
    }
}
