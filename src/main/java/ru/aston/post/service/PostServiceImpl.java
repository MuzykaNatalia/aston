package ru.aston.post.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.entity.Post;
import ru.aston.post.mapper.PostMapper;
import ru.aston.post.repository.PostRepository;
import ru.aston.user.parent.entity.User;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.repository.AuthorRepository;

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    private final PostMapper postMapper = new PostMapper();

    public PostServiceImpl(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Collection<ResponsePostDto> getAllPostsAuthor(long authorId) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new RuntimeException("Author ID " + authorId + " not found");
        }

        Collection<Post> posts = postRepository.getAllPostsAuthor(authorId);
        return postMapper.toResponsePostDto(posts);
    }

    @Override
    public ResponsePostDto getPostById(long postId, long authorId) {
        Author author = authorRepository.getAuthorById(authorId);
        if (author == null) {
            throw new RuntimeException("Author ID " + authorId + " not found");
        }

        Post post = postRepository.getPostById(postId);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto createPost(RequestPostDto requestPostDto, long authorId) {
        if (requestPostDto == null || requestPostDto.getDescription() == null || requestPostDto.getDescription().isEmpty()
                || requestPostDto.getUserIds() == null || requestPostDto.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("Description, author cannot be null or empty");
        }

        Set<Author> users = setUser(requestPostDto.getUserIds());
        Post post = postMapper.toPost(requestPostDto, users);
        post = postRepository.createPost(post);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto updatePost(RequestPostDto requestPostDto, long postId, long authorId) {
        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, authorId);

        if (requestPostDto != null && !requestPostDto.getDescription().isEmpty()) {
            post.setDescription(requestPostDto.getDescription());
        }
        if (requestPostDto != null && requestPostDto.getUserIds() != null && !requestPostDto.getUserIds().isEmpty()) {
            post.setUsers(setUser(requestPostDto.getUserIds()));
        }

        post = postRepository.updatePost(post);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public void deletePost(long postId, long authorId) {
        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, authorId);
        postRepository.deletePost(postId);
    }

    private Set<Author> setUser(Set<Long> userIds) {
        return userIds.stream()
                .map(authorRepository::getAuthorById)
                .collect(Collectors.toSet());
    }

    private void getExceptionIfPostNoUser(Post post, Long userId) {
        Set<Long> users = post.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        if (!users.contains(userId)) {
            throw new RuntimeException("Post is no user");
        }
    }
}
