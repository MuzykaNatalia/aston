package ru.aston.user.author.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.aston.user.author.entity.Author;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Author getAuthorById(long authorId) {
        return entityManager.find(Author.class, authorId);
    }

    @Override
    public Collection<Author> getAllAuthors(Pageable pageable) {
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a", Author.class);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public Author createAuthor(Author author) {
        entityManager.persist(author);
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        return entityManager.merge(author);
    }

    @Override
    public void deleteAuthor(long authorId) {
        Author author = entityManager.find(Author.class, authorId);
        if (author == null) {
            throw new RuntimeException("Author not found with ID: " + authorId);
        }

        String deletePostsQuery = "DELETE FROM post p WHERE p.post_id IN (" +
                "  SELECT up.post_id FROM user_post up " +
                "  GROUP BY up.post_id " +
                "  HAVING COUNT(up.user_id) = 1 AND MAX(up.user_id) = :userId)";
        entityManager.createNativeQuery(deletePostsQuery)
                .setParameter("userId", authorId)
                .executeUpdate();

        entityManager.remove(author);
    }
}
