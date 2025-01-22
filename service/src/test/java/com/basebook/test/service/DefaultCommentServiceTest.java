package com.basebook.test.service;

import com.basebook.model.Comment;
import com.basebook.repository.CommentRepository;
import com.basebook.service.CommentService;
import com.basebook.service.TagService;
import com.basebook.service.config.ServiceConfig;
import com.basebook.test.service.config.TestServiceConfig;
import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {TestServiceConfig.class, ServiceConfig.class})
@ActiveProfiles("test")
class DefaultCommentServiceTest {

    @Autowired
    private Faker faker;

    @Autowired
    private TestDataFactory testFactory;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository repository;

    @BeforeEach
    void setUp() {
        clearInvocations(repository);
    }

    @Test
    void testInitialization() {
        assertAll("Check dependencies injection",
                () -> assertNotNull(commentService),
                () -> assertNotNull(repository),
                () -> assertNotNull(faker),
                () -> assertNotNull(testFactory)
        );
    }


    @Test
    void shouldCreateCommentSuccessfully() {
        Comment comment = testFactory.createFakeComment(faker.number().randomNumber());
        commentService.create(comment);
        verify(repository, times(1)).save(comment);
    }

    @Test
    void shouldDeleteCommentSuccessfully() {
        Long id = faker.number().randomNumber();
        commentService.delete(id);
        verify(repository, times(1)).delete(id);
    }

    @Test
    void shouldUpdateCommentSuccessfully() {
        Comment comment = testFactory.createFakeComment(faker.number().randomNumber());;
        commentService.update(comment);
        verify(repository, times(1)).update(comment);
    }
}
