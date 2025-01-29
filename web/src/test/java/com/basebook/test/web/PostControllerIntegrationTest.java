package com.basebook.test.web;

import com.basebook.test.web.config.TestWebConfig;
import com.basebook.web.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {WebConfig.class, TestWebConfig.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.yaml")
public class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM post_tags");
        jdbcTemplate.execute("""
                INSERT INTO posts (post_id, title, content, image_url, created_at, updated_at, is_deleted) VALUES
                (999, 'Почему программисты ненавидят понедельники?', 'Программист пришел в офис в понедельник и увидел, что 
                его коллеги снова добавили десятки новых тасков. Он вздохнул и сказал: -Кажется, это мой код хочет 
                провести реванш.- И начал писать рефакторинг.', '/uploads/images/1.jpeg', CURRENT_TIMESTAMP, 
                CURRENT_TIMESTAMP, FALSE),
                (1000, 'Роутер программиста', 'Программист купил новый роутер и решил настроить его сам. После часа настройки 
                он сказал жене: -Теперь это не просто роутер, это — API для нашего интернета!-',
                 '/uploads/images/2.jpeg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE)""");
        jdbcTemplate.execute("""
                INSERT INTO post_tags (post_id, tag_id, is_deleted) VALUES
                (999, 2, FALSE),
                (1000, 3, FALSE)
                """
        );
    }

    @Test
    void getPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post_list"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("selectedTag"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(content().string(containsString("Почему программисты ненавидят понедельники?")))
                .andExpect(content().string(containsString("Роутер программиста")))
                .andExpect(xpath("//div[@class='post-card mt-4']").nodeCount(2)).andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2,3", "4", "5"})
    void save_shouldAddPostToDatabaseAndRedirect(String tag) throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg",
                "image/jpeg", new byte[]{1, 2, 3});
        mockMvc.perform(multipart("/posts")
                        .file(file)
                        .param("title", "4")
                        .param("content", "Анна")
                        .param("tags", tag))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void delete_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
        Long postId = 999L;
        mockMvc.perform(post("/posts/{postId}/delete", postId))
                        //.param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void save_shouldUpdatePostInDatabaseAndRedirect() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "test.jpg",
                "image/jpeg", new byte[]{1, 2, 3});
        mockMvc.perform(multipart("/posts/edit/")
                        .file(file)
                        .param("id", "1000")
                        .param("title", "4")
                        .param("content", "DSIJISDJIJASDIDJSJASD\n" +
                                "saddsadsdas\n" +
                                "!!!!!!!!!!!!\n")
                        .param("tags", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1000"));
    }
}
