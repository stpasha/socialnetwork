package com.basebook.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    private final Faker faker = new Faker();

    @Test
    public void testPostBuilder() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();

        Tag tag = TagTest.createFakeTag();
        Like like = LikeTest.createFakeLike(id);
        Comment comment = CommentTest.createFakeComment(id);

        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of(tag))
                .likes(List.of(like))
                .comments(List.of(comment))
                .build();

        assertThat(post.getId()).isEqualTo(id);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getTags()).containsExactly(tag);
        assertThat(post.getLikes()).containsExactly(like);
        assertThat(post.getComments()).containsExactly(comment);
    }

    @Test
    public void testEmptyListsBuilder() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();

        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of())
                .likes(List.of())
                .comments(List.of())
                .build();

        assertThat(post.getTags()).isEmpty();
        assertThat(post.getLikes()).isEmpty();
        assertThat(post.getComments()).isEmpty();
    }

    @Test
    public void testToString() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();

        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .build();

        assertThat(post.toString()).contains(title);
        assertThat(post.toString()).contains(content);
        assertThat(post.toString()).contains(String.valueOf(id));
    }

    @Test
    public void testEquals() {
        Long id = faker.number().randomNumber();
        String content = faker.lorem().sentence();

        Like like = LikeTest.createFakeLike(id);
        Tag tag = TagTest.createFakeTag();
        Comment comment = CommentTest.createFakeComment(id);

        Post firstPost = Post.builder()
                .id(id)
                .content(content)
                .likes(List.of(like))
                .tags(List.of(tag))
                .comments(List.of(comment))
                .build();

        Post secondPost = Post.builder()
                .id(id)
                .content(content)
                .likes(List.of(like))
                .tags(List.of(tag))
                .comments(List.of(comment))
                .build();

        assertThat(firstPost).isEqualTo(secondPost);
    }

    @Test
    public void testPostWithMultipleElements() {
        Long id = faker.number().randomNumber();
        String title = faker.lorem().sentence();
        String content = faker.lorem().paragraph();

        Tag tag1 = TagTest.createFakeTag();
        Tag tag2 = TagTest.createFakeTag();

        Like like1 = LikeTest.createFakeLike(id);
        Like like2 = LikeTest.createFakeLike(id);

        Comment comment1 = CommentTest.createFakeComment(id);
        Comment comment2 = CommentTest.createFakeComment(id);

        Post post = Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .tags(List.of(tag1, tag2))
                .likes(List.of(like1, like2))
                .comments(List.of(comment1, comment2))
                .build();

        assertThat(post.getTags()).containsExactly(tag1, tag2);
        assertThat(post.getLikes()).containsExactly(like1, like2);
        assertThat(post.getComments()).containsExactly(comment1, comment2);
    }

    @Test
    public void testPostWithNullValues() {
        Post post = Post.builder()
                .id(null)
                .title(null)
                .content(null)
                .tags(null)
                .likes(null)
                .comments(null)
                .build();

        assertThat(post.getId()).isNull();
        assertThat(post.getTitle()).isNull();
        assertThat(post.getContent()).isNull();
        assertThat(post.getTags()).isNull();
        assertThat(post.getLikes()).isNull();
        assertThat(post.getComments()).isNull();
    }
}