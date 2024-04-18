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

@Service
public class CommentServiceImpl implements CommentService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final AdvertRepository advertRepository;
    private final UserRepository userRepository;

    private final TypeConverter<CommentCreatingDto, Comment> typeConverterCommentCreatingDto;

    private final TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto;

    public CommentServiceImpl(CommentRepository commentRepository,
                              AdvertRepository advertRepository,
                              UserRepository userRepository,
                              TypeConverter<CommentCreatingDto, Comment> typeConverterCommentCreatingDto,
                              TypeConverter<Comment, CommentReadDto> typeConverterCommentReadDto) {
        this.commentRepository = commentRepository;
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
        this.typeConverterCommentCreatingDto = typeConverterCommentCreatingDto;
        this.typeConverterCommentReadDto = typeConverterCommentReadDto;
    }


    @Override
    public CommentReadDto registerComment(final Long advertId,
                                          final CommentCreatingDto commentCreatingDto,
                                          final AuthenticatedUser authenticatedUser) {

        CommentReadDto commentReadDto = Optional.of(commentCreatingDto)
                .map(commentDto -> settingsForCommentCreateDto(advertId, commentDto, authenticatedUser))
                .map(typeConverterCommentCreatingDto::convert)
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();

        LOGGER.debug("In saveComment register a comment for a Advert with id: {}", advertId);

        return commentReadDto;
    }


    @Override
    public CommentReadDto editComment(final Long commentId,
                                      final CommentEditingDto commentEditingDto,
                                      final AuthenticatedUser authenticatedUser) {
        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Optional<Comment> commentForUpdate = commentRepository.findByIdAndUserId(commentId, userId);

        if(commentForUpdate.isEmpty())
            commentForUpdate.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        LOGGER.debug("In editComment Updating success advert with id {}", commentId);

        return  commentForUpdate
                .map(comment -> this.updateCommentData(comment, commentEditingDto))
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();
    }

    @Override
    public List<CommentReadDto> findAllCommentsForAdvert(final Long advertId) {

        final List<CommentReadDto> comments = commentRepository.findAllByAdvertId(advertId)
                .stream().map(typeConverterCommentReadDto::convert)
                .collect(Collectors.toList());

        LOGGER.debug("In getAllCommentsForAdvert find all comments for Advert with id : {}", advertId);

        return comments;
    }

    @Override
    public CommentReadDto findCommentById(final Long commentId) {
       final Comment foundComment = commentRepository.findById(commentId)
               .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        LOGGER.debug("In deleteComment Deleting success Comment with id {}", commentId);
        return typeConverterCommentReadDto.convert(foundComment);
    }

    @Override
    public void deleteComment(final Long commentId,
                              final AuthenticatedUser authenticatedUser) {

        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Comment commentForDelete = commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        commentRepository.delete(commentForDelete);

        LOGGER.debug("In deleteComment Deleting success Comment with id {}", commentId);
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
        String username = authenticatedUser.getEmail();
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
