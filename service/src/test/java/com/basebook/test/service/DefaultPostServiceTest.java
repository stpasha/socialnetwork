package com.basebook.test.service;

import com.basebook.model.*;
import com.basebook.repository.PostRepository;
import com.basebook.service.PostService;
import com.basebook.service.TagService;
import com.basebook.service.config.ServiceConfig;
import com.basebook.test.service.config.TestServiceConfig;
import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = {TestServiceConfig.class, ServiceConfig.class})
public class DefaultPostServiceTest {

    @Autowired
    private PostService service;

    @Autowired
    private PostRepository repository;

    @Autowired
    private Faker faker;

    @Autowired
    private TestDataFactory testFactory;

    @Autowired
    private TagService tagService;

    @BeforeEach
    void setUp() {
        clearInvocations(repository);
    }

    @Test
    void testInitialization() {
        assertAll("Check dependencies injection",
                () -> assertNotNull(service),
                () -> assertNotNull(repository),
                () -> assertNotNull(faker),
                () -> assertNotNull(testFactory),
                () -> assertNotNull(tagService)
        );
    }

    @Nested
    class PostRetrievalTests {

        @Test
        void shouldReturnPostWhenExists() {
            Long id = faker.number().randomNumber();
            Post post = testFactory.createFakePost(id);
            when(repository.findById(id)).thenReturn(Optional.of(post));
            when(tagService.getTagsByPost(id)).thenReturn(post.getTags());

            Optional<Post> result = service.get(id);

            assertAll("Check post retrieval",
                    () -> assertTrue(result.isPresent(), "Post found"),
                    () -> assertEquals(id, result.get().getId(), "Post ID match")
            );
        }

        @Test
        void shouldReturnEmptyWhenPostNotFound() {
            Long id = faker.number().randomNumber();
            when(repository.findById(id)).thenReturn(Optional.empty());

            Optional<Post> result = service.get(id);

            if (result.isEmpty()) {
                System.out.println("Post not found with id: " + id);
            }

            assertTrue(result.isEmpty(), "Post not be found");
        }
    }

    @Nested
    class PostListingTests {

        @Test
        void shouldReturnCorrectPostListSize() {
            int limit = 10;
            int offset = 0;
            List<PostList> postLists = testFactory.createPostListWithQuantity(limit);
            String filter = "-";

            when(repository.findAll(limit, offset, filter)).thenReturn(postLists);

            assertEquals(postLists.size(), service.listPosts(limit, offset, filter).size(), "Size of returned posts should match");
        }
    }

    @Nested
    class PostActionTests {
        @Test
        void shouldCreatePostSuccessfully() {
            Post post = testFactory.createFakePost(faker.number().randomNumber());
            service.create(post);
            verify(repository, times(1)).save(post);
        }

        @Test
        void shouldDeletePostSuccessfully() {
            long id = faker.number().randomNumber();
            service.delete(id);
            verify(repository, times(1)).delete(id);
        }

        @Test
        void shouldUpdatePostSuccessfully() {
            Post post = testFactory.createFakePost(faker.number().randomNumber());
            service.update(post);
            verify(repository, times(1)).update(post);
        }
    }
}