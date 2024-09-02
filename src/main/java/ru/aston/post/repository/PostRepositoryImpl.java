package ru.aston.post.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ru.aston.post.model.Post;
import ru.aston.user.model.User;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;

public class PostRepositoryImpl implements PostRepository {
    @Override
    public Collection<Post> getAllPostsUser(long userId) {
        Collection<Post> posts = new ArrayList<>();
        String query = "SELECT p.post_id, p.created_on, p.description, " +
                "u.user_id, u.name, u.email " +
                "FROM post p " +
                "JOIN post_user pu ON p.post_id = pu.post_id " +
                "LEFT JOIN users u ON pu.user_id = u.user_id " +
                "WHERE pu.user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<Long, Post> postMap = new HashMap<>();

            while (resultSet.next()) {
                long postId = resultSet.getLong("post_id");

                Post post = postMap.get(postId);
                if (post == null) {
                    post = new Post();
                    post.setId(postId);
                    post.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                    post.setDescription(resultSet.getString("description"));
                    post.setUsers(new HashSet<>());
                    postMap.put(postId, post);
                }

                if (resultSet.getObject("user_id") != null) {
                    User user = new User();
                    user.setId(resultSet.getLong("user_id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    post.getUsers().add(user);
                }
            }

            posts.addAll(postMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post getPostById(long postId) {
        Post post = null;
        String query = "SELECT p.post_id, p.created_on, p.description, " +
                "u.user_id, u.name, u.email " +
                "FROM post p " +
                "LEFT JOIN post_user pu ON p.post_id = pu.post_id " +
                "LEFT JOIN users u ON pu.user_id = u.user_id " +
                "WHERE p.post_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                post = new Post();
                post.setId(resultSet.getLong("post_id"));
                post.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                post.setDescription(resultSet.getString("description"));

                Set<User> users = new HashSet<>();
                do {
                    if (resultSet.getObject("user_id") != null) {
                        User user = new User();
                        user.setId(resultSet.getLong("user_id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                        users.add(user);
                    }
                } while (resultSet.next());

                post.setUsers(users);
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
                addUsersToPost(post, connection);
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

    private void addUsersToPost(Post post, Connection connection) {
        String query = "INSERT INTO post_user (post_id, user_id) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
