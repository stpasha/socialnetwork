package com.basebook.test.service.config;

import com.basebook.repository.CommentRepository;
import com.basebook.repository.LikeRepository;
import com.basebook.repository.PostRepository;
import com.basebook.repository.TagRepository;
import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.basebook.test.service")
public class TestServiceConfig {

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


    @Bean
    public TestDataFactory testDataFactory(Faker faker) {
        return new TestDataFactory(faker );
    }
}
