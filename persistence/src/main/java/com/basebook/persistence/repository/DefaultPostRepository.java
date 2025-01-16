package com.basebook.persistence.repository;

import com.basebook.model.Post;
import com.basebook.repository.PostRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public DefaultPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> Post.builder()
            .id(rs.getLong("post_id"))
            .title(rs.getString("title"))
            .content(rs.getString("content"))
            .imageUrl(rs.getString("image_url"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
            .isDeleted(rs.getBoolean("is_deleted"))
            .build();

    @Override
    public void save(Post post) {
        jdbcTemplate.update("INSERT INTO posts (title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?)",
                post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());
    }

    @Override
    public Post findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM posts WHERE post_id = ?", postRowMapper, id);
    }

    @Override
    public List<Post> findAll(int limit, int offset) {
        String sql = "SELECT * FROM posts ORDER BY created_at DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, postRowMapper, limit, offset);
    }

    @Override
    public List<Post> findByTag(String tag) {
        return jdbcTemplate.query("SELECT p.* FROM posts p INNER JOIN post_tags pt ON p.post_id = pt.post_id INNER JOIN tags t ON pt.tag_id = t.tag_id WHERE t.name = ?",
                postRowMapper, tag);
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("UPDATE posts SET title = ?, content = ?, image_url = ?, updated_at = ?, is_deleted = ? WHERE post_id = ?",
                post.getTitle(), post.getContent(), post.getImageUrl(), post.getUpdatedAt(), post.isDeleted(), post.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM posts WHERE post_id = ?", id);
    }
}
