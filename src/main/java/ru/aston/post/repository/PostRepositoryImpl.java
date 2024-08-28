package ru.aston.post.repository;

import java.util.Collection;
import java.util.Collections;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.aston.common.AbstractRepository;
import ru.aston.post.entity.Post;
import ru.aston.utils.HibernateUtil;

public class PostRepositoryImpl extends AbstractRepository<Post, Long> implements PostRepository {
    public PostRepositoryImpl() {
        super(Post.class);
    }

    @Override
    public Collection<Post> getAllPostsAuthor(long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT p FROM Post p JOIN p.users u WHERE u.id = :userId";
            Query<Post> query = session.createQuery(hql, Post.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Post getPostById(long postId) {
        return super.getById(postId);
    }

    @Override
    public Post createPost(Post post) {
        return super.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return super.update(post);
    }

    @Override
    public void deletePost(long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            String sql = "DELETE FROM user_post WHERE post_id = :postId";
            jakarta.persistence.Query query = session.createNativeQuery(sql, Post.class);
            query.setParameter("postId", postId);
            query.executeUpdate();

            Post post = session.get(Post.class, postId);
            if (post != null) {
                session.remove(post);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting post", e);
        }
    }
}
