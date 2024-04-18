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
import quick.click.commons.exeptions.RegistrationException;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Comment;
import quick.click.core.service.CommentService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

    private AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);

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
    @DisplayName("When register a comment")
    class RegisterCommentTests {
        @Test
        void testRegisterComment_ShouldReturnComment() {
            when(commentService.registerComment(ADVERT_ID, commentCreatingDto, authenticatedUser)).thenReturn(commentReadDto);

            ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto, authenticatedUser);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(commentReadDto, responseEntity.getBody());
        }

        @Test
        void testRegisterComment_ShouldReturn400Status_WhenInvalidRequest() {
            comment = new Comment();
            commentCreatingDto = new CommentCreatingDto();
            ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto, authenticatedUser);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void testRegisterComment_ShouldReturn400Status_WhenThrowsException() {
            when(commentService.registerComment(anyLong(), any(CommentCreatingDto.class), any(AuthenticatedUser.class)))
                    .thenThrow(new RegistrationException("Registration failed due to XYZ"));

            ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto, authenticatedUser);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
            assertTrue(responseEntity.getBody().toString().contains("Registration failed due to XYZ"));
        }

        @Test
        void testRegisterComment_UnauthorizedUser() {
            when(commentService.registerComment(anyLong(), any(CommentCreatingDto.class), any(AuthenticatedUser.class)))
                    .thenThrow(new AuthorizationException("Unauthorized access"));

            ResponseEntity<?> responseEntity = commentController.registerComment(ADVERT_ID, commentCreatingDto, authenticatedUser);

            assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

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
            doNothing().when(commentService).deleteComment(any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = commentController.deleteComment(COMMENT_ID, authenticatedUser);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("The Comment has deleted successfully", responseEntity.getBody());
        }

        @Test
        void testDeleteComment_ShouldnReturn404Status_WhenCommentDoesNotExist() {
            doThrow(new ResourceNotFoundException("Comment", "id", COMMENT_ID))
                    .when(commentService).deleteComment(any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = commentController.deleteComment(COMMENT_ID, authenticatedUser);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void testDeleteAdvert_UnauthorizedUser() {
            doThrow(new AuthorizationException("Unauthorized access"))
                    .when(commentService).deleteComment(any(Long.class), any(AuthenticatedUser.class));

            ResponseEntity<?> responseEntity = commentController.deleteComment(COMMENT_ID, authenticatedUser);

            assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        }

    }

}
