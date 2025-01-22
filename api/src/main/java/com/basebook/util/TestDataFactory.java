package com.basebook.util;

import com.basebook.model.*;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestDataFactory {

    private final Faker faker;

    public TestDataFactory(Faker faker) {
        this.faker = faker;
    }

    public Post createFakePost() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();
        String imag = faker.lorem().paragraph();
        LocalDateTime created = faker.timeAndDate().birthday().atStartOfDay();
        LocalDateTime updated = faker.timeAndDate().birthday().atStartOfDay();

        Tag tag = createFakeTag();
        Like like = createFakeLike(id);
        Comment comment = createFakeComment(id);

        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of(tag))
                .createdAt(created)
                .updatedAt(updated)
                .imageUrl(imag)
                .likes(List.of(like))
                .comments(List.of(comment))
                .build();

    }

    public Post createFakePost(Long id) {
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();
        String imag = faker.lorem().paragraph();
        LocalDateTime created = faker.timeAndDate().birthday().atStartOfDay();
        LocalDateTime updated = faker.timeAndDate().birthday().atStartOfDay();

        Tag tag = createFakeTag();
        Like like = createFakeLike(id);
        Comment comment = createFakeComment(id);

        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of(tag))
                .createdAt(created)
                .updatedAt(updated)
                .imageUrl(imag)
                .likes(List.of(like))
                .comments(List.of(comment))
                .build();

    }

    public PostList createFakePostList() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();
        String imag = faker.lorem().paragraph();
        LocalDateTime created = faker.timeAndDate().birthday().atStartOfDay();
        LocalDateTime updated = faker.timeAndDate().birthday().atStartOfDay();

        Tag tag = createFakeTag();
        Like like = createFakeLike(id);
        Comment comment = createFakeComment(id);

        return PostList.PostListBuilder.postListBuilder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of(tag))
                .createdAt(created)
                .updatedAt(updated)
                .imageUrl(imag)
                .likes(List.of(like))
                .comments(List.of(comment))
                .build();

    }

    public List<PostList> createPostListWithQuantity(int quantity) {
        return Stream.generate(() -> createFakePostList()).limit(quantity).collect(Collectors.toList());
    }

    public PostList createFakeDeletedPostList() {
        PostList post = createFakePostList();
        post.setDeleted(true);
        return post;
    }

    public Post createFakeDeletedPost() {
        Post post = createFakePost();
        post.setDeleted(true);
        return post;
    }

    public Comment createFakeComment(Long postId) {
        return Comment.builder()
                .id(faker.number().randomNumber())
                .postId(postId)
                .content(faker.lorem().sentence())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Like createFakeLike(Long postId) {
        return Like.builder()
                .id(faker.number().randomNumber())
                .postId(postId)
                .build();
    }

    public Tag createFakeTag() {
        return Tag.builder()
                .id(faker.number().randomNumber())
                .name(faker.lorem().word())
                .build();
    }

}
