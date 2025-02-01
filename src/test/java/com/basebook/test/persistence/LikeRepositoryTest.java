package com.basebook.test.persistence;

import com.basebook.annotations.BasebookTest;
import com.basebook.model.Like;
import com.basebook.model.Post;
import com.basebook.persistence.repository.DefaultLikeRepository;
import com.basebook.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BasebookTest
public class LikeRepositoryTest {

    public static final long ID = 1L;
    @Autowired
    private DefaultLikeRepository likeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM likes");
        jdbcTemplate.execute("DELETE FROM posts");
        Post post = testDataFactory.createFakePost(ID);
        jdbcTemplate.update("INSERT INTO posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)",
                post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());
    }

    @Test
    void testSaveLike() {
        Like like = testDataFactory.createFakeLike(ID);

        likeRepository.save(like);

        long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes WHERE post_id = ?", Long.class, ID);
        assertEquals(1, count);
    }

    @Test
    void testCountByPostId() {
        List<Like> likes = List.of(testDataFactory.createFakeLike(ID), testDataFactory.createFakeLike(ID), testDataFactory.createFakeLike(ID));
        likes.forEach(like -> likeRepository.save(like));
        long count = likeRepository.countByPostId(ID);
        assertEquals(likes.size(), count);
    }
}
