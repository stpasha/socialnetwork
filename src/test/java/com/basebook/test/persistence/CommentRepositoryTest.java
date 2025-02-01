package com.basebook.test.persistence;

import com.basebook.annotations.BasebookTest;
import com.basebook.model.Comment;
import com.basebook.model.Post;
import com.basebook.persistence.repository.DefaultCommentRepository;
import com.basebook.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BasebookTest
public class CommentRepositoryTest {

    public static final long ID = 1L;
    public static final long DEL_ID = 4L;

    @Autowired
    private DefaultCommentRepository commentRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        Post post = testDataFactory.createFakePost(ID);
        jdbcTemplate.update("INSERT INTO posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)",
                post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());
    }

    @Test
    void testSaveComment() {
        Comment comment = testDataFactory.createFakeComment(ID);
        commentRepository.save(comment);
        long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE post_id = ?", Long.class, ID);
        assertEquals(1, count);
    }

    @Test
    void testUpdateComment() {
        Comment comment = testDataFactory.createFakeComment(ID);
        commentRepository.save(comment);
        comment.setContent("Updated comment");
        comment.setId(jdbcTemplate.queryForObject("SELECT comment_id FROM comments WHERE post_id = ?", Long.class, ID));
        commentRepository.update(comment);
        String updatedContent = jdbcTemplate.queryForObject("SELECT content FROM comments WHERE comment_id = ?", String.class, comment.getId());
        assertEquals("Updated comment", updatedContent);
    }

    @Test
    void testDeleteComment() {
        Comment comment = testDataFactory.createFakeComment(ID);
        commentRepository.save(comment);
        commentRepository.delete(jdbcTemplate.queryForObject("SELECT comment_id FROM comments WHERE post_id = ?", Long.class, ID));
        boolean isDeleted = jdbcTemplate.queryForObject("SELECT is_deleted FROM comments WHERE post_id = ?", Boolean.class, ID);
        assertEquals(true, isDeleted);
    }

    @Test
    void testCountByPostId() {
        List<Comment> comments = List.of(testDataFactory.createFakeComment(ID), testDataFactory.createFakeComment(ID), testDataFactory.createFakeComment(ID));
        comments.forEach(comment -> commentRepository.save(comment));
        long count = commentRepository.countByPostId(ID);
        assertEquals(comments.size(), count);
    }
}
