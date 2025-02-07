package com.basebook.persistence.repository;

import com.basebook.model.Like;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultLikeRepository implements LikeRepository {

    private final JdbcTemplate jdbcTemplate;

    public DefaultLikeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Like> likeRowMapper = (rs, rowNum) -> Like.builder()
            .id(rs.getLong("like_id"))
            .postId(rs.getLong("post_id"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .isDeleted(rs.getBoolean("is_deleted"))
            .build();

    @Override
    public void save(final Like like) {
        jdbcTemplate.update("INSERT INTO likes (post_id, created_at, is_deleted) VALUES (?, ?, ?)",
                like.getPostId(), like.getCreatedAt(), like.isDeleted());
    }

    @Override
    public long countByPostId(final long postId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes WHERE post_id = ?", Long.class, postId);
    }

}
