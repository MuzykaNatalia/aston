package ru.aston.comment.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.comment.dto.RequestCommentDto;
import ru.aston.comment.dto.ResponseCommentDto;
import ru.aston.comment.mapper.CommentMapper;
import ru.aston.comment.entity.Comment;
import ru.aston.comment.repository.CommentRepository;
import ru.aston.exception.NotFoundException;
import ru.aston.exception.ValidationException;
import ru.aston.post.entity.Post;
import ru.aston.post.repository.PostRepository;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public Collection<ResponseCommentDto> getAllCommentsForPost(long postId, long authorId) {
        Post post = getPostById(postId);

        Author author = getAuthorById(authorId);

        if (!post.getUsers().contains(author)) {
            throw new NotFoundException("Post with ID " + postId + " not found");
        }

        Collection<Comment> comments = commentRepository.findCommentsByPostId(postId);
        return commentMapper.toResponseCommentDtoCollection(comments);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseCommentDto getCommentById(long commentId, long userId) {
        getAuthorById(userId);

        Comment comment = commentRepository.findById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment with ID " + commentId + " does not exist.");
        }

        return commentMapper.toResponseCommentDto(comment);
    }

    @Transactional
    @Override
    public ResponseCommentDto createComment(RequestCommentDto commentDto, long postId, long userId) {
        Post post = getPostById(postId);

        Author author = getAuthorById(userId);

        Comment comment = commentMapper.toComment(commentDto, author, post);
        comment = commentRepository.save(comment);

        return commentMapper.toResponseCommentDto(comment);
    }

    @Transactional
    @Override
    public ResponseCommentDto updateComment(RequestCommentDto commentDto, long commentId, long ownerCommentId) {
        Comment existingComment = commentRepository.findById(commentId);
        if (existingComment == null) {
            throw new NotFoundException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getAuthor().getId() != ownerCommentId) {
            throw new ValidationException("Author is not authorized to update this comment.");
        }

        existingComment.setDescription(commentDto.getDescription());
        commentRepository.update(existingComment);
        return commentMapper.toResponseCommentDto(existingComment);
    }

    @Transactional
    @Override
    public void deleteComment(long commentId, long ownerCommentId) {
        Comment existingComment = commentRepository.findById(commentId);
        if (existingComment == null) {
            throw new NotFoundException("Comment with ID " + commentId + " does not exist.");
        }

        if (existingComment.getAuthor().getId() != ownerCommentId) {
            throw new ValidationException("User is not authorized to delete this comment.");
        }

        commentRepository.delete(existingComment);
    }

    private Post getPostById(long postId) {
        Post post = postRepository.getPostById(postId);
        if (post == null) {
            throw new NotFoundException("Post with ID " + postId + " does not exist.");
        }
        return post;
    }

    private Author getAuthorById(long authorId) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new NotFoundException("Author with ID " + authorId + " does not exist.");
        }
        return author;
    }
}
