package com.basebook.persistence.repository;

import com.basebook.model.Tag;
import com.basebook.repository.TagRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultTagRepository implements TagRepository {

    public static final String FIND_BY_POSt = "SELECT t.* FROM tags t INNER JOIN post_tags pt ON t.tag_id = pt.tag_id " +
            " WHERE pt.post_id = ?";
    public static final String QUERY_TAGS = "SELECT * FROM tags WHERE tag_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public DefaultTagRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tag> tagRowMapper = (rs, rowNum) -> Tag.builder()
            .id(rs.getLong("tag_id"))
            .name(rs.getString("name"))
            .build();


    @Override
    public Optional<Tag> findById(final long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(QUERY_TAGS, tagRowMapper, id));
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query("SELECT * FROM tags", tagRowMapper);
    }

    @Override
    public List<Tag> findTagByPost(final Long postId) {
        return jdbcTemplate.query(FIND_BY_POSt, tagRowMapper, postId);
    }
}
