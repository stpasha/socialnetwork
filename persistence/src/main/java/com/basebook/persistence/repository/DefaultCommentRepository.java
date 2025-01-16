package com.basebook.persistence.repository;

import com.basebook.model.Comment;
import com.basebook.repository.CommentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public DefaultCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> Comment.builder()
            .id(rs.getLong("comment_id"))
            .postId(rs.getLong("post_id"))
            .content(rs.getString("content"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .isDeleted(rs.getBoolean("is_deleted"))
            .build();

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("INSERT INTO comments (post_id, content, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?)",
                comment.getPostId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt(), comment.isDeleted());
    }

    @Override
    public Comment findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM comments WHERE comment_id = ?", commentRowMapper, id);
    }

    @Override
    public List<Comment> findByPostId(long postId) {
        return jdbcTemplate.query("SELECT * FROM comments WHERE post_id = ?", commentRowMapper, postId);
    }

    @Override
    public void update(Comment comment) {
        jdbcTemplate.update("UPDATE comments SET content = ?, updated_at = ?, is_deleted = ? WHERE comment_id = ?",
                comment.getContent(), comment.getUpdatedAt(), comment.isDeleted(), comment.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM comments WHERE comment_id = ?", id);
    }

    @Override
    public long countByPostId(long postId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM comments WHERE post_id = ?", Long.class, postId);
    }
}
