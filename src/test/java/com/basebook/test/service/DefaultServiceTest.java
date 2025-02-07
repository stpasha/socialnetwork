package com.basebook.test.service;

import com.basebook.annotations.BasebookTest;
import com.basebook.model.Comment;
import com.basebook.model.Like;
import com.basebook.model.Post;
import com.basebook.model.PostList;
import com.basebook.persistence.repository.CommentRepository;
import com.basebook.persistence.repository.LikeRepository;
import com.basebook.persistence.repository.PostRepository;
import com.basebook.persistence.repository.TagRepository;
import com.basebook.service.CommentService;
import com.basebook.service.ImageService;
import com.basebook.service.LikeService;
import com.basebook.service.PostService;
import com.basebook.util.TestDataFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@BasebookTest
public class DefaultServiceTest {

    @Autowired
    private PostService service;

    @MockitoBean
    private PostRepository repository;

    @Autowired
    private Faker faker;

    @Autowired
    private TestDataFactory testFactory;

    @MockitoBean
    private TagRepository tagRepository;

    @Autowired
    private LikeService likeService;

    @MockitoBean
    private LikeRepository likeRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CommentService commentService;

    @MockitoBean
    private CommentRepository commentRepository;


    @Nested
    class PostRetrievalTests {

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
                    () -> assertNotNull(tagRepository)
            );
        }

        @Test
        void shouldReturnPostWhenExists() {
            Long id = faker.number().randomNumber();
            Post post = testFactory.createFakePost(id);
            when(repository.findById(id)).thenReturn(Optional.of(post));
            when(tagRepository.findTagByPost(id)).thenReturn(post.getTags());

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

            assertEquals(postLists.size(), service.listPosts(limit, offset, filter).size(),
                    "Size of returned posts should match");
        }
    }

    @Nested
    class PostActionTests {

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
                    () -> assertNotNull(tagRepository)
            );
        }

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

    @Nested
    class LikeServiceTests {

        @BeforeEach
        void setUp() {
            clearInvocations(likeRepository);
        }

        @Test
        void testInitialization() {
            assertAll("Check dependencies injection",
                    () -> assertNotNull(likeService),
                    () -> assertNotNull(likeRepository),
                    () -> assertNotNull(faker),
                    () -> assertNotNull(testFactory)
            );
        }

        @Test
        void shouldCreateCommentSuccessfully() {
            Like like = testFactory.createFakeLike(faker.number().randomNumber());
            likeService.create(like);
            verify(likeRepository, times(1)).save(like);
        }

        @Test
        void shouldReturnCount() {
            Like like = testFactory.createFakeLike(faker.number().randomNumber());
            long randomLikes = faker.number().randomNumber();
            when(likeRepository.countByPostId(like.getPostId())).thenReturn(randomLikes);
            assertEquals(randomLikes, likeService.countLikesByPost(like.getPostId()),
                    "Should be equal num of likes");
        }
    }

    @Nested
    class ImageServiceTests {

        private final String uploadDir = "/test/uploads";


        @Test
        void shouldSaveImageSuccessfully() throws IOException {
            String fileName = "test-image.jpg";
            byte[] data = new byte[]{1, 2, 3, 4};
            Path expectedPath = Paths.get(uploadDir, fileName);
            try (MockedStatic<Files> mockedStatic = mockStatic(Files.class)) {
                mockedStatic.when(() -> Files.createDirectories(expectedPath.getParent()))
                        .thenReturn(expectedPath);
                mockedStatic.when(() -> Files.write(expectedPath, data)).thenReturn(expectedPath);
                String result = imageService.saveImage(fileName, data);
                assertEquals("/uploads/images/" + fileName, result);
            }
        }

        @Test
        void shouldThrowExceptionWhenSaveFails() {
            String fileName = "/";
            byte[] data = new byte[]{1, 2, 3, 4};
            Exception exception = assertThrows(RuntimeException.class, () -> imageService.saveImage(fileName, data));
            assertEquals("Failed to save image", exception.getMessage());
        }
    }

    @Nested
    class CommentServiceTests {

        @BeforeEach
        void setUp() {
            clearInvocations(commentRepository);
        }

        @Test
        void testInitialization() {
            assertAll("Check dependencies injection",
                    () -> assertNotNull(commentService),
                    () -> assertNotNull(commentRepository),
                    () -> assertNotNull(faker),
                    () -> assertNotNull(testFactory)
            );
        }


        @Test
        void shouldCreateCommentSuccessfully() {
            Comment comment = testFactory.createFakeComment(faker.number().randomNumber());
            commentService.create(comment);
            verify(commentRepository, times(1)).save(comment);
        }

        @Test
        void shouldDeleteCommentSuccessfully() {
            long id = faker.number().randomNumber();
            commentService.delete(id);
            verify(commentRepository, times(1)).delete(id);
        }

        @Test
        void shouldUpdateCommentSuccessfully() {
            Comment comment = testFactory.createFakeComment(faker.number().randomNumber());
            commentService.update(comment);
            verify(commentRepository, times(1)).update(comment);
        }
    }
}