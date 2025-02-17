package com.basebook.test.persistence;

import com.basebook.annotations.BasebookTest;
import com.basebook.model.Post;
import com.basebook.model.Tag;
import com.basebook.persistence.repository.DefaultTagRepository;
import com.basebook.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BasebookTest
public class TagRepositoryTest {

    public static final long ID = 1L;

    @Autowired
    private DefaultTagRepository tagRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM appdata.tags");
        jdbcTemplate.execute("DELETE FROM appdata.post_tags");
        jdbcTemplate.execute("DELETE FROM appdata.posts");
        Post post = testDataFactory.createFakePost(ID);
        jdbcTemplate.update("INSERT INTO appdata.posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?)",
                post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt(), post.isDeleted());

    }

    @Test
    void testFindById() {
        Tag tag = testDataFactory.createFakeTag();
        jdbcTemplate.execute("INSERT INTO appdata.tags (tag_id, name) VALUES (" + tag.getId() + ", '" + tag.getName() + "')");

        Optional<Tag> foundTag = tagRepository.findById(tag.getId());
        assertTrue(foundTag.isPresent());
        assertEquals(tag.getName(), foundTag.get().getName());
    }

    @Test
    void testFindAll() {
        Tag tag1 = testDataFactory.createFakeTag();
        Tag tag2 = testDataFactory.createFakeTag();
        jdbcTemplate.execute("INSERT INTO appdata.tags (tag_id, name) VALUES (" + tag1.getId() + ", '" + tag1.getName() + "')");
        jdbcTemplate.execute("INSERT INTO appdata.tags (tag_id, name) VALUES (" + tag2.getId() + ", '" + tag2.getName() + "')");

        List<Tag> tags = tagRepository.findAll();
        assertEquals(2, tags.size());
    }

    @Test
    void testFindTagByPost() {
        Tag tag = testDataFactory.createFakeTag();

        jdbcTemplate.execute("INSERT INTO appdata.tags (tag_id, name) VALUES (" + tag.getId() + ", '" + tag.getName() + "')");
        jdbcTemplate.execute("INSERT INTO appdata.post_tags (post_id, tag_id) VALUES (1, " + tag.getId() + ")");

        List<Tag> tags = tagRepository.findTagByPost(ID);
        assertEquals(1, tags.size());
        assertEquals(tag.getName(), tags.get(0).getName());
    }
}

