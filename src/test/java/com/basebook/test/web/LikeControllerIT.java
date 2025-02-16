package com.basebook.test.web;

import com.basebook.annotations.BasebookTest;
import com.basebook.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@BasebookTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LikeControllerIT {
    public static final Long POST_ID = 999L;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LikeService likeService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM appdata.posts WHERE post_id = " + POST_ID);
        jdbcTemplate.execute("""
                INSERT INTO appdata.posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES
                (999, 'Почему программисты ненавидят понедельники?', 'Программист пришел в офис в понедельник и увидел, что 
                его коллеги снова добавили десятки новых тасков. Он вздохнул и сказал: -Кажется, это мой код хочет 
                провести реванш.- И начал писать рефакторинг.', '/uploads/images/1.jpeg', CURRENT_TIMESTAMP, 
                CURRENT_TIMESTAMP, FALSE),
                (1000, 'Роутер программиста', 'Программист купил новый роутер и решил настроить его сам. После часа настройки 
                он сказал жене: -Теперь это не просто роутер, это — API для нашего интернета!-',
                 '/uploads/images/2.jpeg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE)""");
        jdbcTemplate.execute("""
                INSERT INTO appdata.post_tags (post_id, tag_id, is_deleted) VALUES
                (999, 2, FALSE),
                (1000, 3, FALSE)
                """
        );
    }

    @Test
    void save_shouldChangeLikeCounterAndReturn() throws Exception {
        mockMvc.perform(post("/posts/{postId}/like", POST_ID))
                .andExpect(status().isCreated())
                .andExpect(content().json("1"));
    }
}
