package ru.aston.comment.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.aston.comment.entity.Comment;

@Repository
public class CommentRepositoryImpl implements CommentRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    public Collection<Comment> findCommentsByPostId(long postId) {
        String hql = "SELECT c FROM Comment c WHERE c.post.id = :postId";
        TypedQuery<Comment> query = entityManager.createQuery(hql, Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    public Comment findById(long commentId) {
        return entityManager.find(Comment.class, commentId);
    }

    public Comment save(Comment comment) {
        entityManager.persist(comment);
        return comment;
    }

    public Comment update(Comment comment) {
        return entityManager.merge(comment);
    }

    public void delete(Comment comment) {
        entityManager.remove(comment);
    }
}
