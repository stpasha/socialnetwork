package com.basebook.persistence.repository;

import com.basebook.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DefaultTagRepository implements TagRepository {

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
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM appdata.tags WHERE tag_id = ?",
                tagRowMapper, id));
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query("SELECT * FROM appdata.tags", tagRowMapper);
    }

    @Override
    public List<Tag> findTagByPost(final Long postId) {
        return jdbcTemplate.query(
                        """
                            SELECT
                                t.*
                            FROM appdata.tags t
                            INNER JOIN
                                appdata.post_tags pt
                            ON t.tag_id = pt.tag_id WHERE pt.post_id = ?
                            """,
                tagRowMapper, postId);
    }
}
