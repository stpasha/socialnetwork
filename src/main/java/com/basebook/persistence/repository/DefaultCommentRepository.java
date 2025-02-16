package com.basebook.persistence.repository;

import com.basebook.model.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public DefaultCommentRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(final Comment comment) {
        jdbcTemplate.update("INSERT INTO appdata.comments (post_id, content, created_at, updated_at, is_deleted) "
                        + "VALUES (?, ?, ?, ?, ?)",
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.isDeleted());
    }

    @Override
    public void update(final Comment comment) {
        jdbcTemplate.update("UPDATE appdata.comments SET content = ?, updated_at = ?, is_deleted = ? WHERE comment_id = ?",
                comment.getContent(), comment.getUpdatedAt(), comment.isDeleted(), comment.getId());
    }

    @Override
    public void delete(final long id) {
        jdbcTemplate.update("UPDATE appdata.comments SET  is_deleted = TRUE WHERE comment_id = ?", id);
    }

    @Override
    public long countByPostId(final long postId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE post_id = ? AND is_deleted = FALSE",
                Long.class, postId);
    }
}
