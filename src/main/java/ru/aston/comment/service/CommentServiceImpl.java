package ru.aston.comment.service;

import java.util.Collection;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.mapper.CommentMapper;
import ru.aston.comment.entity.Comment;
import ru.aston.comment.repository.CommentRepository;
import ru.aston.post.entity.Post;
import ru.aston.post.repository.PostRepository;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.repository.AuthorRepository;

public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper = new CommentMapper();

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              AuthorRepository authorRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Collection<ResponseCommentDto> getAllCommentsForPost(long postId, long authorId) {
        Post post = postRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist.");
        }

        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new IllegalArgumentException("Author with ID " + authorId + " does not exist.");
        }

        if (!post.getUsers().contains(author)) {
            throw new RuntimeException("Post with ID " + postId + " not found");
        }

        Collection<Comment> comments = commentRepository.findCommentsByPostId(postId);
        return commentMapper.toResponseCommentDtoCollection(comments);
    }

    @Override
    public ResponseCommentDto getCommentById(long commentId, long userId) {
        Author author = authorRepository.getAuthorById(userId);
        if (author == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        Comment comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        return commentMapper.toResponseCommentDto(comment);
    }

    @Override
    public ResponseCommentDto createComment(RequestCommentDto commentDto, long postId, long ownerCommentId) {
        validate(commentDto);
        Post post = postRepository.getPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post with ID " + postId + " does not exist.");
        }

        Author author = authorRepository.getAuthorById(ownerCommentId);
        if (author == null) {
            throw new IllegalArgumentException("Author with ID " + ownerCommentId + " does not exist.");
        }

        Comment comment = commentMapper.toComment(commentDto, author, post);
        comment = commentRepository.save(comment);

        return commentMapper.toResponseCommentDto(comment);
    }

    @Override
    public ResponseCommentDto updateComment(RequestCommentDto commentDto, long commentId, long ownerCommentId) {
        validate(commentDto);

        Comment existingComment = commentRepository.findById(commentId);
        if (existingComment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getAuthor().getId() != ownerCommentId) {
            throw new IllegalArgumentException("Author is not authorized to update this comment.");
        }

        existingComment.setDescription(commentDto.getDescription());
        commentRepository.update(existingComment);
        return commentMapper.toResponseCommentDto(existingComment);
    }

    @Override
    public void deleteComment(long commentId, long ownerCommentId) {
        Comment existingComment = commentRepository.findById(commentId);

        if (existingComment == null) {
            throw new IllegalArgumentException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getAuthor().getId() != ownerCommentId) {
            throw new IllegalArgumentException("User is not authorized to delete this comment.");
        }

        commentRepository.delete(commentId);
    }

    private void validate(RequestCommentDto commentDto) {
        if (commentDto == null || commentDto.getDescription() == null || commentDto.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description comment cannot be null or empty");
        }
    }
}
