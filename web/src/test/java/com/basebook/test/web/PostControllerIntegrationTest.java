package com.basebook.test.web;

import com.basebook.test.web.config.TestWebConfig;
import com.basebook.web.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {WebConfig.class, TestWebConfig.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
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
        jdbcTemplate.execute("""
                INSERT INTO posts (title, content, image_url, created_at, updated_at, is_deleted) VALUES
                ('Почему программисты ненавидят понедельники?', 'Программист пришел в офис в понедельник и увидел, что 
                его коллеги снова добавили десятки новых тасков. Он вздохнул и сказал: -Кажется, это мой код хочет 
                провести реванш.- И начал писать рефакторинг.', '/uploads/images/1.jpeg', CURRENT_TIMESTAMP, 
                CURRENT_TIMESTAMP, FALSE),
                ('Роутер программиста', 'Программист купил новый роутер и решил настроить его сам. После часа настройки 
                он сказал жене: -Теперь это не просто роутер, это — API для нашего интернета!-',
                 '/uploads/images/2.jpeg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, FALSE)""");
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

//    @Test
//    void save_shouldAddUserToDatabaseAndRedirect() throws Exception {
//        mockMvc.perform(post("/posts")
//                        .param("id", "4")
//                        .param("firstName", "Анна")
//                        .param("lastName", "Смирнова")
//                        .param("age", "28")
//                        .param("active", "true"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/posts"));
//    }
//
//    @Test
//    void delete_shouldRemoveUserFromDatabaseAndRedirect() throws Exception {
//        mockMvc.perform(post("/posts/1")
//                        .param("_method", "delete"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/posts"));
//    }
}
