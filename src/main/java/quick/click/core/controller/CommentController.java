package quick.click.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.RegistrationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.service.CommentService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.COMMENTS_URL;
import static quick.click.core.controller.CommentController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    public static final String BASE_URL = VERSION_1_0 + COMMENTS_URL;

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * POST    http://localhost:8080/v1.0/comments/1
     * {
     * "message": "Das ist ein gutes Auto",
     * }
     */
    @PostMapping("/{advertId}")
    public ResponseEntity<?> registerComment(@PathVariable("advertId") final Long advertId,
                                             @Valid @RequestBody final CommentCreatingDto commentDTO,
                                             @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {
        try {

            if (commentDTO == null || commentDTO.getMessage() == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

            final CommentReadDto commentReadDto = commentService.registerComment(advertId, commentDTO, authenticatedUser);

            LOGGER.debug("In registerComment received POST comment register successfully with id: {}, for user: {}",
                    commentReadDto.getId(), authenticatedUser.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(commentReadDto);

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (RegistrationException exception) {

            LOGGER.error("Error during advert registration: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during a BaseFee creation: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * GET    http://localhost:8080/v1.0/comments/1
     */

    @GetMapping("/{advertId}")
    public ResponseEntity<?> findAllCommentsToAdvert(@PathVariable("advertId") Long advertId) {

        try {

            if (advertId == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please insert advertId");

            List<CommentReadDto> commentDTOList = commentService.findAllCommentsForAdvert(advertId);

            LOGGER.debug("In findAllCommentsToAdvert received GET get all comments successfully forAdvert with id {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body(commentDTOList);

        } catch (Exception exception) {

            LOGGER.error("Error finding all Comments : {} ", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }


    /**
     * Delete    http://localhost:8080/v1.0/comments/1
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        try {
            if (commentId == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please insert commentId");

            commentService.deleteComment(commentId, authenticatedUser);

            LOGGER.debug("In deleteComment received DELETE Comment delete successfully with id {} ", commentId);

            return ResponseEntity.status(HttpStatus.OK).body("The Comment has deleted successfully");

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Comment not found with id : '{}'", commentId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during deleting the comment with id {}", commentId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

}
