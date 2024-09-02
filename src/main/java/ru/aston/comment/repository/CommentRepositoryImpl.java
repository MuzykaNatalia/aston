package ru.aston.comment.repository;

import java.util.Collection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.aston.comment.entity.Comment;
import ru.aston.common.AbstractRepository;
import ru.aston.utils.HibernateUtil;

public class CommentRepositoryImpl extends AbstractRepository<Comment, Long> implements CommentRepository {
    public CommentRepositoryImpl() {
        super(Comment.class);
    }

    public Collection<Comment> findCommentsByPostId(long postId) {
        Transaction transaction = null;
        Collection<Comment> comments = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<Comment> query = session.createQuery("FROM Comment c WHERE c.post.id = :postId", Comment.class);
            query.setParameter("postId", postId);

            comments = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return comments;
    }

    public Comment findById(long commentId) {
        return super.getById(commentId);
    }

    public Comment save(Comment comment) {
        return super.save(comment);
    }

    public Comment update(Comment comment) {
        return super.update(comment);
    }

    public void delete(long commentId) {
        super.delete(commentId);
    }
}
