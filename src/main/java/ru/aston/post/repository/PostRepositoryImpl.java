package ru.aston.post.repository;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.aston.post.entity.Post;

@Repository
public class PostRepositoryImpl implements PostRepository {
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Collection<Post> getAllPostsAuthor(long userId) {
        String hql = "SELECT p FROM Post p JOIN p.users u WHERE u.id = :userId";
        TypedQuery<Post> query = entityManager.createQuery(hql, Post.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Post getPostById(long postId) {
        return entityManager.find(Post.class, postId);
    }

    @Override
    public Post createPost(Post post) {
        entityManager.persist(post);
        return post;
    }

    @Override
    public Post updatePost(Post post) {
        return entityManager.merge(post);
    }

    @Override
    public void deletePost(Post post) {
        entityManager.remove(post);
    }
}
