package ru.aston.user.author.repository;

import java.util.Collection;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.aston.common.AbstractRepository;
import ru.aston.post.entity.Post;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.model.PostAuthorCount;
import ru.aston.utils.HibernateUtil;

public class AuthorRepositoryImpl extends AbstractRepository<Author, Long> implements AuthorRepository {
    public AuthorRepositoryImpl() {
            super(Author.class);
    }

    @Override
    public Author getAuthorById(long userId) {
        return super.getById(userId);
    }

    @Override
    public Collection<Author> getAllAuthors() {
        return super.getAll();
    }

    @Override
    public Author createAuthor(Author user) {
        return super.save(user);
    }

    @Override
    public Author updateAuthor(Author user) {
        return super.update(user);
    }

    @Override
    public void deleteAuthor(Long userId) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Author author = session.get(Author.class, userId);
            if (author == null) {
                return;
            }

            String postAuthorCountQuery = "SELECT new ru.aston.user.author.model.PostAuthorCount(p.id, COUNT(DISTINCT a.id)) " +
                    "FROM Post p " +
                    "JOIN p.users a " +
                    "WHERE p.id IN (SELECT p2.id FROM Post p2 JOIN p2.users u WHERE u.id = :userId) " +
                    "GROUP BY p.id";

            List<PostAuthorCount> results = session.createQuery(postAuthorCountQuery, PostAuthorCount.class)
                    .setParameter("userId", userId)
                    .getResultList();

            for (PostAuthorCount result : results) {
                Long postId = result.getPostId();
                Long authorCount = result.getAuthorCount();

                Post post = session.get(Post.class, postId);
                if (post != null) {
                    post.getUsers().remove(author);

                    if (authorCount == 1) {
                        session.remove(post);
                    }
                }
            }
            session.flush();
            session.remove(author);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting author", e);
        }
    }
}
