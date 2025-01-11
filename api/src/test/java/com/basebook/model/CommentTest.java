package com.basebook.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    private static final Faker faker = new Faker();

    @Test
    public void testCommentBuilder() {
        Long id = faker.number().randomNumber();
        Long postId = faker.number().randomNumber();
        String content = faker.lorem().sentence();
        LocalDateTime createdAt = LocalDateTime.now();
        Comment comment = Comment.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .createdAt(createdAt)
                .build();

        assertThat(comment.getId()).isEqualTo(id);
        assertThat(comment.getPostId()).isEqualTo(postId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    public void testToString() {
        Long id = faker.number().randomNumber();
        Long postId = faker.number().randomNumber();
        String content = faker.lorem().sentence();
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = Comment.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .createdAt(createdAt)
                .build();
        String commentString = comment.toString();
        assertThat(commentString).contains(id.toString());
        assertThat(commentString).contains(postId.toString());
        assertThat(commentString).contains(content);
        assertThat(commentString).contains(createdAt.toString());
    }

    @Test
    public void testEquals() {
        Long id = faker.number().randomNumber();
        Long postId = faker.number().randomNumber();
        String content = faker.lorem().sentence();
        LocalDateTime createdAt = LocalDateTime.now();
        Comment firstComment = Comment.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .createdAt(createdAt)
                .build();
        Comment secondComment = Comment.builder()
                .id(id)
                .postId(postId)
                .content(content)
                .createdAt(createdAt)
                .build();
        assertThat(firstComment).isEqualTo(secondComment);
    }

    public static Comment createFakeComment(Long postId) {
        return Comment.builder()
                .id(faker.number().randomNumber())
                .postId(postId)
                .content(faker.lorem().sentence())
                .createdAt(LocalDateTime.now())
                .build();
    }
}