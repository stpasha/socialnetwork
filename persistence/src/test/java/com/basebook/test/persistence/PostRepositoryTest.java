package com.basebook.test.persistence;


import com.basebook.model.Post;
import com.basebook.persistence.repository.DefaultPostRepository;
import com.basebook.test.persistence.config.TestRepositoryConfig;
import com.basebook.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = TestRepositoryConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostRepositoryTest {

    @Autowired
    private DefaultPostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDataFactory testDataFactory;


    @Test
    @Order(1)
    void testSavePost() {
        jdbcTemplate.execute("DELETE FROM posts");
        Stream.of(testDataFactory.createFakePost(), testDataFactory.createFakePost(), testDataFactory.createFakePost()).forEach(post -> postRepository.save(post));
        long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Long.class);
        assertEquals(3, count);
    }

    @Test
    @Order(2)
    void testFindById() {
        Post post = testDataFactory.createFakePost(999L);
        jdbcTemplate.update("INSERT INTO posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)",
                post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());

        Optional<Post> foundPost = postRepository.findById(post.getId());
        assertTrue(foundPost.isPresent());
        assertEquals(post.getTitle(), foundPost.get().getTitle());
    }

    @Test
    @Order(3)
    void testUpdatePost() {
        Post post = testDataFactory.createFakePost(1L);
        post.setTitle("Updated post");

        postRepository.update(post);

        String updatedTitle = jdbcTemplate.queryForObject("SELECT title FROM posts WHERE post_id = ?", String.class, post.getId());
        assertEquals("Updated post", updatedTitle);
    }

    @Test
    @Order(4)
    void testDeletePost() {
        postRepository.delete(3L);

        boolean isDeleted = jdbcTemplate.queryForObject("SELECT is_deleted FROM posts WHERE post_id = ?", Boolean.class, 3L);
        assertEquals(true, isDeleted);
    }
}

