package ru.aston.comment.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import ru.aston.comment.model.Comment;
import static ru.aston.constant.Constant.DB_PASSWORD;
import static ru.aston.constant.Constant.DB_URL;
import static ru.aston.constant.Constant.DB_USER;

public class CommentRepositoryImpl implements CommentRepository {
    public Collection<Comment> findCommentsByPostId(long postId) {
        Collection<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comment WHERE post_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getLong("comment_id"));
                comment.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                comment.setDescription(resultSet.getString("description"));
                comment.setUserId(resultSet.getLong("user_id"));
                comment.setPostId(resultSet.getLong("post_id"));
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public Comment findById(long commentId) {
        String query = "SELECT * FROM comment WHERE comment_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, commentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Comment comment = new Comment();
                comment.setId(resultSet.getLong("comment_id"));
                comment.setCreatedOn(resultSet.getTimestamp("created_on").toLocalDateTime());
                comment.setDescription(resultSet.getString("description"));
                comment.setUserId(resultSet.getLong("user_id"));
                comment.setPostId(resultSet.getLong("post_id"));
                return comment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Comment save(Comment comment) {
        String query = "INSERT INTO comment (created_on, description, user_id, post_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(comment.getCreatedOn()));
            preparedStatement.setString(2, comment.getDescription());
            preparedStatement.setLong(3, comment.getUserId());
            preparedStatement.setLong(4, comment.getPostId());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                comment.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comment;
    }

    public void update(Comment comment) {
        String query = "UPDATE comment SET description = ? WHERE comment_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment.getDescription());
            preparedStatement.setLong(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long commentId) {
        String query = "DELETE FROM comment WHERE comment_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, commentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
