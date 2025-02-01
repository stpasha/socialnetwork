package com.basebook.test.web;

import com.basebook.annotations.BasebookTest;
import com.basebook.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@BasebookTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerIT {

    public static final long POST_ID = 1001L;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("""
                INSERT INTO posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES
                (1001, 'Почему программисты ненавидят понедельники?', 'Программист пришел в офис в понедельник и увидел, что
                его коллеги снова добавили десятки новых тасков. Он вздохнул и сказал: -Кажется, это мой код хочет
                провести реванш.- И начал писать рефакторинг.', '/uploads/images/1.jpeg', CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP, FALSE)
                """);
        jdbcTemplate.execute("""
                INSERT INTO comments (comment_id, post_id, content, created_at, updated_at, is_deleted) VALUES
                (1, 1001, 'This is a comment 1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE),
                (2, 1001, 'This is a comment 2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE)
                """);
    }

    @ParameterizedTest
    @ValueSource(strings = {"This is a comment 1", "This is a comment 2"})
    public void testAddComment(String content) throws Exception {
        Long postId = POST_ID;

        mockMvc.perform(post("/comments/{postId}", postId)
                        .param("content", content))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));
    }

    @Test
    public void testDeleteComment() throws Exception {
        Long commentId = 1L;
        Long postId = POST_ID;

        mockMvc.perform(post("/comments/{id}/delete", commentId)
                        .param("postId", postId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));
    }

    @Test
    public void testEditComment() throws Exception {
        Long commentId = 2L;
        Long postId = POST_ID;
        String content = "Updated comment";

        Comment comment = Comment.builder()
                .id(commentId)
                .postId(postId)
                .content(content)
                .build();

        mockMvc.perform(post("/comments/{id}/edit", commentId)
                        .flashAttr("comment", comment))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + postId));
    }
}

