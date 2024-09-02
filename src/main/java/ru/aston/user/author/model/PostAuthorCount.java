package ru.aston.user.author.model;

public class PostAuthorCount {
    private long postId;
    private long authorCount;

    public PostAuthorCount(long postId, long authorCount) {
        this.postId = postId;
        this.authorCount = authorCount;
    }

    public Long getPostId() {
        return postId;
    }

    public Long getAuthorCount() {
        return authorCount;
    }
}
