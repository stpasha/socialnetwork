package com.basebook.test.persistence;

import com.basebook.model.Tag;
import com.basebook.persistence.repository.DefaultTagRepository;
import com.basebook.test.persistence.config.TestRepositoryConfig;
import com.basebook.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringJUnitConfig(classes = TestRepositoryConfig.class)
public class TagRepositoryTest {

    @Autowired
    private DefaultTagRepository tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("DELETE FROM post_tags");
    }

    @Test
    void testFindById() {
        Tag tag = testDataFactory.createFakeTag();
        jdbcTemplate.execute("INSERT INTO tags (tag_id, name) VALUES (" + tag.getId() + ", '" + tag.getName() + "')");

        Optional<Tag> foundTag = tagRepository.findById(tag.getId());
        assertTrue(foundTag.isPresent());
        assertEquals(tag.getName(), foundTag.get().getName());
    }

    @Test
    void testFindAll() {
        Tag tag1 = testDataFactory.createFakeTag();
        Tag tag2 = testDataFactory.createFakeTag();
        jdbcTemplate.execute("INSERT INTO tags (tag_id, name) VALUES (" + tag1.getId() + ", '" + tag1.getName() + "')");
        jdbcTemplate.execute("INSERT INTO tags (tag_id, name) VALUES (" + tag2.getId() + ", '" + tag2.getName() + "')");

        List<Tag> tags = tagRepository.findAll();
        assertEquals(2, tags.size());
    }

    @Test
    void testFindTagByPost() {
        Tag tag = testDataFactory.createFakeTag();
        jdbcTemplate.execute("INSERT INTO tags (tag_id, name) VALUES (" + tag.getId() + ", '" + tag.getName() + "')");
        jdbcTemplate.execute("INSERT INTO post_tags (post_id, tag_id) VALUES (1, " + tag.getId() + ")");

        List<Tag> tags = tagRepository.findTagByPost(1L);
        assertEquals(1, tags.size());
        assertEquals(tag.getName(), tags.get(0).getName());
    }
}

