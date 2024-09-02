package ru.aston.comment.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.mapper.CommentMapper;
import ru.aston.comment.model.Comment;
import ru.aston.comment.repository.CommentRepository;
import ru.aston.post.model.Post;
import ru.aston.post.repository.PostRepository;
import ru.aston.user.model.User;
import ru.aston.user.repository.UserRepository;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper = new CommentMapper();

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<ResponseCommentDto> getAllCommentsForPost(long postId, long userId) {
        Post post = postRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist.");
        }

        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        Collection<Comment> comments = commentRepository.findCommentsByPostId(postId);

        Set<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        Map<Long, User> userMap = new HashMap<>();
        List<User> users = userRepository.findUsersByIds(userIds);
        for (User u : users) {
            userMap.put(u.getId(), u);
        }

        List<ResponseCommentDto> responseComments = new ArrayList<>();
        for (Comment comment : comments) {
            User commentUser = userMap.get(comment.getUserId());
            ResponseCommentDto responseComment = commentMapper.toResponseCommentDto(comment, commentUser);
            responseComments.add(responseComment);
        }

        return responseComments;
    }

    @Override
    public ResponseCommentDto getCommentById(long commentId, long userId) {
        Comment comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        User user = userRepository.getUserById(comment.getUserId());

        return commentMapper.toResponseCommentDto(comment, user);
    }

    @Override
    public ResponseCommentDto createComment(RequestCommentDto commentDto, long postId, long ownerCommentId) {
        if (commentDto == null || commentDto.getDescription() == null || commentDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description comment cannot be null or empty");
        }

        Post post = postRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist.");
        }

        User user = userRepository.getUserById(ownerCommentId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + ownerCommentId + " does not exist.");
        }

        Comment comment = commentMapper.toComment(commentDto, user.getId(), post.getId());
        comment = commentRepository.save(comment);

        return commentMapper.toResponseCommentDto(comment, user);
    }

    @Override
    public ResponseCommentDto updateComment(RequestCommentDto commentDto, long commentId, long ownerCommentId) {
        if (commentDto == null || commentDto.getDescription() == null || commentDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description comment cannot be null or empty");
        }

        Comment existingComment = commentRepository.findById(commentId);

        if (existingComment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getUserId() != ownerCommentId) {
            throw new IllegalArgumentException("User is not authorized to update this comment.");
        }

        existingComment.setDescription(commentDto.getDescription());
        commentRepository.update(existingComment);
        User user = userRepository.getUserById(ownerCommentId);

        return commentMapper.toResponseCommentDto(existingComment, user);
    }

    @Override
    public void deleteComment(long commentId, long ownerCommentId) {
        Comment existingComment = commentRepository.findById(commentId);

        if (existingComment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getUserId() != ownerCommentId) {
            throw new IllegalArgumentException("User is not authorized to delete this comment.");
        }

        commentRepository.delete(commentId);
    }
}
