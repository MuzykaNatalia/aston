package ru.aston.post.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import ru.aston.post.dto.RequestPostDto;
import ru.aston.post.dto.ResponsePostDto;
import ru.aston.post.mapper.PostMapper;
import ru.aston.post.model.Post;
import ru.aston.post.repository.PostRepository;
import ru.aston.user.model.User;
import ru.aston.user.repository.UserRepository;

public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper = new PostMapper();

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<ResponsePostDto> getAllPostsUser(long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User ID " + userId + " not found");
        }

        Collection<Post> posts = postRepository.getAllPostsUser(userId);
        return postMapper.toResponsePostDto(posts);
    }

    @Override
    public ResponsePostDto getPostById(long postId, long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User ID " + userId + " not found");
        }

        Post post = postRepository.getPostById(postId);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto createPost(RequestPostDto requestPostDto, long userId) {
        if (requestPostDto == null || requestPostDto.getDescription() == null || requestPostDto.getDescription().isEmpty()
                || requestPostDto.getUserIds() == null || requestPostDto.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("Description, author cannot be null or empty");
        }

        Set<User> users = setUser(requestPostDto.getUserIds());
        Post post = postMapper.toPost(requestPostDto, users);
        post = postRepository.createPost(post);
        return postMapper.toResponsePostDto(post);
    }

    @Override
    public ResponsePostDto updatePost(RequestPostDto requestPostDto, long postId, long userId) {
        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, userId);

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
    public void deletePost(long postId, long userId) {
        Post post = postRepository.getPostById(postId);
        getExceptionIfPostNoUser(post, userId);
        postRepository.deletePost(postId);
    }

    private Set<User> setUser(Set<Long> userIds) {
        return userIds.stream()
                .map(userRepository::getUserById)
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
