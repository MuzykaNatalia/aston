package ru.aston.post.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import ru.aston.post.model.Post;
import ru.aston.user.model.User;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;

public class PostRepositoryImpl implements PostRepository {
    @Override
    public Collection<Post> getAllPostsUser(long userId) {
        Collection<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM post p " +
                "JOIN post_user pu ON p.post_id = pu.post_id " +
                "WHERE pu.user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getLong("post_id"));
                post.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                post.setDescription(resultSet.getString("description"));
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post getPostById(long postId) {
        Post post = null;
        String query = "SELECT * FROM post WHERE post_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                post = new Post();
                post.setId(resultSet.getLong("post_id"));
                post.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                post.setDescription(resultSet.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post createPost(Post post) {
        String query = "INSERT INTO post (created_on, description) VALUES (?, ?) RETURNING post_id";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(post.getCreatedOn()));
            preparedStatement.setString(2, post.getDescription());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                post.setId(resultSet.getLong("post_id"));
                addUsersToPost(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Post updatePost(Post post) {
        String query = "UPDATE post SET description = ? WHERE post_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, post.getDescription());
            preparedStatement.setLong(2, post.getId());
            preparedStatement.executeUpdate();

            updateUsersInPost(post);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void deletePost(long postId) {
        String deletePostQuery = "DELETE FROM post WHERE post_id = ?";
        String deleteUsersQuery = "DELETE FROM post_user WHERE post_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement deleteUsersStatement = connection.prepareStatement(deleteUsersQuery);
             PreparedStatement deletePostStatement = connection.prepareStatement(deletePostQuery)) {

            deleteUsersStatement.setLong(1, postId);
            deleteUsersStatement.executeUpdate();

            deletePostStatement.setLong(1, postId);
            deletePostStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUsersToPost(Post post) {
        String query = "INSERT INTO post_user (post_id, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (User user : post.getUsers()) {
                preparedStatement.setLong(1, post.getId());
                preparedStatement.setLong(2, user.getId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUsersInPost(Post post) {
        String deleteQuery = "DELETE FROM post_user WHERE post_id = ?";
        String insertQuery = "INSERT INTO post_user (post_id, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {

            deleteStatement.setLong(1, post.getId());
            deleteStatement.executeUpdate();

            for (User user : post.getUsers()) {
                insertStatement.setLong(1, post.getId());
                insertStatement.setLong(2, user.getId());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
