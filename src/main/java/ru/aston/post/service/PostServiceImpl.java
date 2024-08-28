package ru.aston.post.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.entity.Post;
import ru.aston.post.mapper.PostMapper;
import ru.aston.post.repository.PostRepository;
import ru.aston.post.repository.PostRepositoryImpl;
import ru.aston.user.parent.entity.User;
import ru.aston.user.author.entity.Author;
import ru.aston.user.author.repository.AuthorRepository;
import ru.aston.user.author.repository.AuthorRepositoryImpl;

public class PostServiceImpl implements PostService {
    private final AuthorRepository authorRepository = new AuthorRepositoryImpl();
    private final PostMapper postMapper = new PostMapper();
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepositoryImpl postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Collection<ResponsePostDto> getAllPostsAuthor(long userId) {
        validateId(userId);
        authorRepository.getUserById(userId);
        Collection<Post> posts = postRepository.getAllPostsAuthor(userId);
        return postMapper.toResponsePostDto(posts);
    }

    @Override
    public ResponsePostDto getPostById(long postId, long userId) {
        validateId(userId);
        validateId(postId);

        authorRepository.getUserById(userId);
        Post post = postRepository.getPostById(postId);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto createPost(RequestPostDto requestPostDto, long userId) {
        validateId(userId);
        if (requestPostDto == null || requestPostDto.getDescription() == null || requestPostDto.getDescription().isEmpty()
                || requestPostDto.getUserIds() == null || requestPostDto.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("Description, author cannot be null or empty");
        }

        Set<Author> users = setRegularUser(requestPostDto.getUserIds());
        Post post = postMapper.toPost(requestPostDto, users);
        post = postRepository.createPost(post);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto updatePost(RequestPostDto requestPostDto, long postId, long userId) {
        validateId(userId);
        validateId(postId);

        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, userId);

        if (requestPostDto != null && !requestPostDto.getDescription().isEmpty()) {
            post.setDescription(requestPostDto.getDescription());
        }
        if (requestPostDto != null && requestPostDto.getUserIds() != null && !requestPostDto.getUserIds().isEmpty()) {
            post.setUsers(setRegularUser(requestPostDto.getUserIds()));
        }

        post = postRepository.updatePost(post);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public void deletePost(long postId, long userId) {
        validateId(userId);
        validateId(postId);

        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, userId);
        postRepository.deletePost(postId);
    }

    private Set<Author> setRegularUser(Set<Long> userIds) {
        return userIds.stream()
                .map(authorRepository::getUserById)
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

    private void validateId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be greater than zero");
        }
    }
}
