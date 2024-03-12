package quick.click.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.service.CommentService;

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
     POST    http://localhost:8080/v1.0/comments/1
     {
     "message": "Das ist ein gutes Auto",
     }
     */
    @PostMapping("/{advertId}")
    public ResponseEntity<?> registerComment(@Valid @RequestBody CommentCreatingDto commentDTO,
                                                @PathVariable("advertId") Long advertId) {

        CommentReadDto commentReadDto = commentService.registerComment(advertId, commentDTO);

        LOGGER.debug("In registerComment received POST comment register successfully with id {} ", commentReadDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(commentReadDto);
    }
    /**
     GET    http://localhost:8080/v1.0/comments/1
     */
    @GetMapping("/{advertId}")
    public ResponseEntity<List<CommentReadDto>> getAllCommentsToAdvert(@PathVariable("advertId") Long advertId) {

        List<CommentReadDto> commentDTOList = commentService.getAllCommentsForAdvert(advertId);

        LOGGER.debug("In getAllCommentsToAdvert received GET get all comments successfully forAdvert with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body(commentDTOList);
    }
    /**
     Delete    http://localhost:8080/v1.0/comments/1
     */
    @DeleteMapping ("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);

        LOGGER.debug("In deleteComment received DELETE Comment delete successfully with id {} ", commentId);

       return ResponseEntity.status(HttpStatus.OK).body("The Comment has deleted successfully");
    }

}
