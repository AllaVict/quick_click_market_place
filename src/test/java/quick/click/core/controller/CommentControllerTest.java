package quick.click.core.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Comment;
import quick.click.core.service.CommentService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.AdvertFactory.createAdvertOne;
import static quick.click.config.factory.CommentDtoFactory.*;
import static quick.click.config.factory.CommentFactory.createCommentList;
import static quick.click.config.factory.CommentFactory.createCommentOne;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentController")
class CommentControllerTest {

        @Mock
        private CommentService commentService;
        @InjectMocks
        private CommentController commentController;

        private static final long COMMENT_ID = 101L;

        private static final long ADVERT_ID = 101L;

        private List<Comment> commentList;
        private List<CommentReadDto> commentReadDtoList;
        private Comment comment;
        private CommentReadDto commentReadDto;


    private CommentCreatingDto commentCreatingDto;

    private CommentEditingDto commentEditingDto;

        @BeforeEach
        public void setUp() {
            commentController = new CommentController(commentService);
            commentList = createCommentList();
            comment = createCommentOne();
            commentReadDto = createCommentReadDtoOne();
            commentReadDtoList = createCommentReadDtoList();
            commentEditingDto = createCommentEditingDto();
            commentCreatingDto = createCommentCreatingDto();
        }

        @Nested
        @DisplayName("When Create a Comment")
        class CreateCommentTests {
            @Test
            void testCreateComment_ShouldReturnComment() {
                when(commentService.registerComment(ADVERT_ID, commentCreatingDto)).thenReturn(commentReadDto);

                ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto);

                assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
                assertEquals(commentReadDto, responseEntity.getBody());
            }

            @Test
            void testCreateComment_ShouldReturn400Status_WhenInvalidRequest() {
                comment = new Comment();
                commentCreatingDto = new CommentCreatingDto();
                ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto);

                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
            }

        }

        @Nested
        @DisplayName("When Find All Comments")
        class FindAllCommentsTests {

            @Test
            void testFindAllComments_ShouldReturnAllComments() {
                when(commentService.findAllCommentsForAdvert(ADVERT_ID)).thenReturn(commentReadDtoList);

                ResponseEntity<?> responseEntity = commentController.findAllCommentsToAdvert(ADVERT_ID);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(commentReadDtoList, responseEntity.getBody());

            }

            @Test
            void testFindAllComments_ShouldReturn200Status_WhenReturnEmptyList() {
                commentReadDtoList = new ArrayList<>();
                when(commentService.findAllCommentsForAdvert(ADVERT_ID)).thenReturn(commentReadDtoList);

                ResponseEntity<?> responseEntity = commentController.findAllCommentsToAdvert(ADVERT_ID);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals(commentReadDtoList, responseEntity.getBody());
            }

        }

        @Nested
        @DisplayName("When Delete a Comment")
        class DeleteCommentTests {
            @Test
            void testDeleteComment_ShouldReturnComment() {
                doNothing().when(commentService).deleteComment(any(Long.class));

                ResponseEntity<?> responseEntity = commentController.deleteComment(COMMENT_ID);

                assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                assertEquals("The Comment has deleted successfully", responseEntity.getBody());
            }

            @Test
            void testDeleteComment_ShouldnReturn404Status_WhenCommentDoesNotExist() {
                doThrow(new ResourceNotFoundException("Comment", "id", COMMENT_ID))
                        .when(commentService).deleteComment(any(Long.class));

                ResponseEntity<?> responseEntity = commentController.deleteComment(COMMENT_ID);

                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            }

        }

    }
