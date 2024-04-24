package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.*;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.CommentRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.CommentService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for managing comments on advertisements.
 *
 * @author Alla Borodina
 */
@Service
public class CommentServiceImpl implements CommentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    private final TypeConverter<CommentCreatingDto, Comment> typeConverterCommentCreatingDto;

    private final TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto;

    /**
     * Constructs a new CommentServiceImpl with required repositories and converters.
     *
     * @param commentRepository               Repository for comment data access
     * @param advertRepository                Repository for advert data access
     * @param userRepository                  Repository for user data access
     * @param typeConverterCommentCreatingDto Converter from CommentCreatingDto to Comment
     * @param typeConverterCommentReadDto     Converter from Comment to CommentReadDto
     */
    public CommentServiceImpl(final CommentRepository commentRepository,
                              final AdvertRepository advertRepository,
                              final UserRepository userRepository,
                              final TypeConverter<CommentCreatingDto, Comment> typeConverterCommentCreatingDto,
                              final TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto) {
        this.commentRepository = commentRepository;
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
        this.typeConverterCommentCreatingDto = typeConverterCommentCreatingDto;
        this.typeConverterCommentReadDto = typeConverterCommentReadDto;
    }

    /**
     * Registers a comment for a specified advertisement.
     *
     * @param advertId           ID of the advertisement to which the comment is to be added
     * @param commentCreatingDto Data transfer object containing comment creation details
     * @param authenticatedUser  Authenticated user performing the operation
     * @return CommentReadDto object after successful registration
     * @throws ResourceNotFoundException if no advertisement is found for the given ID
     */
    @Override
    public CommentReadDto registerComment(final Long advertId,
                                          final CommentCreatingDto commentCreatingDto,
                                          final AuthenticatedUser authenticatedUser) {

       final CommentReadDto commentReadDto = Optional.of(commentCreatingDto)
                .map(commentDto -> settingsForCommentCreateDto(advertId, commentDto, authenticatedUser))
                .map(typeConverterCommentCreatingDto::convert)
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();

        LOGGER.debug("In saveComment register a comment for a Advert with id: {}", advertId);

        return commentReadDto;
    }

    /**
     * Edits an existing comment.
     *
     * @param commentId         ID of the comment to be updated
     * @param commentEditingDto Data transfer object containing new comment details
     * @param authenticatedUser Authenticated user performing the operation
     * @return CommentReadDto object after successful update
     * @throws ResourceNotFoundException if no comment is found for the given commentId and userId
     */
    @Override
    public CommentReadDto editComment(final Long commentId,
                                      final CommentEditingDto commentEditingDto,
                                      final AuthenticatedUser authenticatedUser) {
        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Optional<Comment> commentForUpdate = commentRepository.findByIdAndUserId(commentId, userId);

        if (commentForUpdate.isEmpty())
            commentForUpdate.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        LOGGER.debug("In editComment Updating success advert with id: {}", commentId);

        return commentForUpdate
                .map(comment -> this.updateCommentData(comment, commentEditingDto))
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();
    }

    /**
     * Finds all comments associated with a specified advert.
     *
     * @param advertId ID of the advert
     * @return List of CommentReadDto objects
     */
    @Override
    public List<CommentReadDto> findAllCommentsForAdvert(final Long advertId) {

        final List<CommentReadDto> comments = commentRepository.findAllByAdvertId(advertId)
                .stream().map(typeConverterCommentReadDto::convert)
                .collect(Collectors.toList());

        LOGGER.debug("In getAllCommentsForAdvert find all comments for Advert with id : {}", advertId);

        return comments;
    }

    /**
     * Finds a comment by its ID.
     *
     * @param commentId ID of the comment
     * @return CommentReadDto if the comment is found
     * @throws ResourceNotFoundException if the comment is not found
     */
    @Override
    public CommentReadDto findCommentById(final Long commentId) {
        final Comment foundComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        LOGGER.debug("In deleteComment Deleting success Comment with id: {}", commentId);
        return typeConverterCommentReadDto.convert(foundComment);
    }

    /**
     * Deletes a comment by its ID.
     *
     * @param commentId         ID of the comment to be deleted
     * @param authenticatedUser Authenticated user performing the operation
     * @throws ResourceNotFoundException if the comment is not found or if the user does not have permission to delete the comment
     */
    @Override
    public void deleteComment(final Long commentId,
                              final AuthenticatedUser authenticatedUser) {

        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Comment commentForDelete = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.delete(commentForDelete);

        LOGGER.debug("In deleteComment Deleting success Comment with id: {}", commentId);
    }

    private CommentCreatingDto settingsForCommentCreateDto(final Long advertId,
                                                           final CommentCreatingDto commentCreatingDto,
                                                           final AuthenticatedUser authenticatedUser) {
        commentCreatingDto.setCreatedDate(LocalDateTime.now());
        commentCreatingDto.setAdvertId(advertId);
        commentCreatingDto.setUserId(getUserByAuthenticatedUser(authenticatedUser));
        return commentCreatingDto;
    }

    private Long getUserIdByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        final String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"))
                .getId();

    }

    private Long getUserByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        final String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"))
                .getId();

    }

    private Comment updateCommentData(final Comment comment, final CommentEditingDto commentEditingDto) {
        comment.setId(commentEditingDto.getId());
        comment.setMessage(commentEditingDto.getMessage());
        comment.setAdvert(getAdvert(commentEditingDto.getAdvertId()));
        comment.setUserId(commentEditingDto.getUserId());
        comment.setUpdatedDate(commentEditingDto.getUpdatedDate());
        comment.setCreatedDate(commentEditingDto.getCreatedDate());
        comment.setUpdatedDate(LocalDateTime.now());
        return comment;
    }

    private Advert getAdvert(final Long advertId) {
        return Optional.ofNullable(advertId)
                .flatMap(advertRepository::findById)
                .orElse(null);
    }
}
