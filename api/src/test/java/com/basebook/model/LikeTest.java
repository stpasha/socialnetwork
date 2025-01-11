package com.basebook.model;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LikeTest {

    static private Faker faker;

    @BeforeAll
    public static void populateFaker() {
         faker = new Faker();
    }


    @Test
    public void testLikeBuilder() {
        Long id = faker.number().randomNumber();
        Long postId = faker.number().randomNumber();
        Like like = Like.builder().id(id).postId(postId).build();
        assertThat(like.getId()).isEqualTo(id);
        assertThat(like.getPostId()).isEqualTo(postId);
    }

    @Test
    public void testLikeWithNulls() {
        Like like = Like.builder().id(null).postId(null).build();
        assertThat(like.getId()).isNull();
        assertThat(like.getPostId()).isNull();
    }

    @Test
    public void testEquals() {
        Long id = faker.number().randomNumber();
        Long postId = faker.number().randomNumber();
        Like firstLike = Like.builder().id(id).postId(postId).build();
        Like secondLike = Like.builder().id(id).postId(postId).build();
        assertThat(firstLike).isEqualTo(secondLike);
    }

    public static Like createFakeLike(Long postId) {
        return Like.builder()
                .id(faker.number().randomNumber())
                .postId(postId)
                .build();
    }
}
