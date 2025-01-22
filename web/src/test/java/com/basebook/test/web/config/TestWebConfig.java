package com.basebook.test.web.config;

import com.basebook.repository.CommentRepository;
import com.basebook.repository.LikeRepository;
import com.basebook.repository.PostRepository;
import com.basebook.repository.TagRepository;
import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.basebook.test.service")
public class TestWebConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }

    @Bean
    public TestDataFactory testDataFactory() {
        return new TestDataFactory(faker());
    }

    @Bean
    @Primary
    public PostRepository mockPostRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    @Primary
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    @Primary
    public LikeRepository mockLikeRepository() {
        return Mockito.mock(LikeRepository.class);
    }

    @Bean
    @Primary
    public TagRepository mockTagRepository() {
        return Mockito.mock(TagRepository.class);
    }
}
