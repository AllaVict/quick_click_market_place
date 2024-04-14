package quick.click.core.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import quick.click.config.factory.UserFactory;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.CommentFactory.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {


    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AdvertRepository advertRepository;

    @Autowired
    UserRepository userRepository;


    private static final long COMMENT_ONE_ID = 101L;

    private static final long USER_ID = 101L;

    private List<Comment> commentList;

    private Comment comment;

    private Advert advert;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.save(UserFactory.createUser());
        advert = advertRepository.save(createAdvertOne(user));
        comment = createCommentOneWithUserAndAdvert(user, advert);
        commentList = Arrays.asList(comment,comment);
    }
        @Nested
        @DisplayName("When Find Comment By Advert")
        class FindAllByAdvertTests {
            @Test
            void testFindAllByAdvert_shouldReturnExtraFeeSuccessfully() {
                commentRepository.save(comment);
                List<Comment> result = commentRepository.findAllByAdvert(advert);

                assertNotNull(result);
                assertEquals("John Johnson", result.get(0).getUsername());
            }

            @Test
            void testFFindAllByAdvert_shouldReturnNoExtraFee() {
                commentRepository.deleteAll();
                List<Comment> result = commentRepository.findAllByAdvert(advert);

                assertTrue(result.isEmpty());
                assertEquals(0, result.size());
            }

        }

        @Nested
        @DisplayName("When Find Comment By Id and UserId")
        class FindByIdAndUserIdTests {
            @Test
            void testFindByIdAndUserId_shouldReturnCommentListSuccessfully() {
                Comment savedComment = commentRepository.save(comment);
                Comment result = commentRepository.findByIdAndUserId(savedComment.getId(), user.getId());

                assertNotNull(result);
                assertEquals("John Johnson", result.getUsername());
            }

            @Test
            void testFindByIdAndUserId_shouldReturnNoComment() {
                commentRepository.deleteAll();
                Comment result = commentRepository.findByIdAndUserId(COMMENT_ONE_ID, USER_ID);

                assertNull(result);
            }

        }

        @Nested
        @DisplayName("When Find Comment by Id")
        class FindCommentByIdTests {

            @Test
            void testFindCommentById_shouldReturnExistingCommentWithGivenId() {
                Comment existingComment = commentRepository.save(comment);
                Optional<Comment> result = commentRepository.findById(existingComment.getId());
                assertEquals(existingComment.getId(), result.get().getId());
                assertTrue(result.isPresent());

            }

            @Test
            void testFindCommentById_shouldReturnNoCommentWithGivenId() {
                Optional<Comment> foundComment = commentRepository.findById(COMMENT_ONE_ID);

                assertFalse(foundComment.isPresent());
                assertThat(foundComment).isEmpty();

            }
        }

        @Nested
        @DisplayName("When Find All Comment")
        class FindALlExtraFeeTests {

            @Test
            void testFindAllComment_shouldReturnCommentListSuccessfully() {
                commentRepository.deleteAll();
                commentRepository.save(comment);
                List<Comment> result = commentRepository.findAll();

                assertEquals(1, result.size());
                assertFalse(result.isEmpty());
            }

            @Test
            void testFindAllComment_shouldReturnEmptyCommentList() {
                commentRepository.deleteAll();
                List<Comment> result = commentRepository.findAll();

                assertEquals(0, result.size());
                assertTrue(result.isEmpty());
            }
        }

        @Nested
        @DisplayName("When Save Comment")
        class SaveExtraFeeTests {

            @Test
            void testSaveComment_shouldSaveCommentSuccessfully() {
                Comment savedComment = commentRepository.save(comment);

                assertNotNull(savedComment);
                assertNotNull(savedComment);
                assertThat(savedComment.getId()).isNotNull();
                assertThat(savedComment.getMessage()).isEqualTo(comment.getMessage());
                assertEquals(comment.getUsername(), savedComment.getUsername());
            }

            @Test
            void testSaveComment_shouldThrowException() {
                assertThrows(InvalidDataAccessApiUsageException.class,
                        () -> commentRepository.save(null));
            }

        }

        @Nested
        @DisplayName("When Delete Comment By Id")
        class DeleteCommentByIdTests {
            @Test
            void testDeleteCommentById_shouldDeleteCommentByIdSuccessfully() {

                Comment commentForDelete = commentRepository.save(comment);

                commentRepository.deleteById(commentForDelete.getId());
                Optional<Comment> deletedExtraFee = commentRepository.findById(commentForDelete.getId());
                assertFalse(deletedExtraFee.isPresent());
                assertThat(deletedExtraFee).isEmpty();
            }

        }

    }