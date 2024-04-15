package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.*;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.CommentRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.CommentService;

import java.security.Principal;
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
    public CommentReadDto registerComment(Long advertId, CommentCreatingDto commentCreatingDto) {
        commentCreatingDto.setCreatedDate(LocalDateTime.now());
        commentCreatingDto.setAdvertId(advertId);
        // User user = getUserByPrincipal(principal);
        commentCreatingDto.setUserId(advertRepository.findById(advertId).orElseThrow().getUser().getId());

        CommentReadDto commentDto = Optional.of(commentCreatingDto)
        .map(typeConverterCommentCreatingDto::convert)
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();

        LOGGER.debug("In saveComment Saving comment for Advert with id: {}", advertId);

        return commentDto;
    }

    @Override
    public List<CommentReadDto> getAllCommentsForAdvert(Long advertId) {

        List<CommentReadDto> comments = commentRepository.findAllByAdvertId(advertId)
                .stream().map(typeConverterCommentReadDto::convert)
                .collect(Collectors.toList());

        LOGGER.debug("In getAllCommentsForAdvert find all comments for Advert with id : {}", advertId);

        return comments;
    }

    @Override
    public CommentReadDto editComment(final Long commentId, final CommentEditingDto commentEditingDto) {

        final Optional<Comment> commentForUpdate = commentRepository.findById(commentId);
        if(commentForUpdate.isEmpty())
            commentForUpdate.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        LOGGER.debug("In editComment Updating success advert with id {}", commentId);

        commentEditingDto.setUpdatedDate(LocalDateTime.now());
        return  commentForUpdate
                .map(comment -> this.updateCommentData(comment, commentEditingDto))
                .map(commentRepository::saveAndFlush)
                .map(typeConverterCommentReadDto::convert)
                .orElseThrow();
    }

    @Override
    public void deleteComment(Long commentId) {
        Optional<Comment> commentForDelete = commentRepository.findById(commentId);
        if(commentForDelete.isEmpty())
            commentForDelete.orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

       // commentForDelete.ifPresent(commentRepository::delete);
        commentRepository.delete(commentForDelete.orElseThrow());
        LOGGER.debug("In deleteComment Deleting success Comment with id {}", commentId);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    protected Comment updateCommentData(final Comment comment, final CommentEditingDto commentEditingDto) {
        comment.setId(commentEditingDto.getId());
        comment.setMessage(commentEditingDto.getMessage());
        comment.setAdvert(getAdvert(commentEditingDto.getAdvertId()));
        comment.setUserId(commentEditingDto.getUserId());
        comment.setUpdatedDate(commentEditingDto.getUpdatedDate());
        comment.setCreatedDate(commentEditingDto.getCreatedDate());
        comment.setUpdatedDate(LocalDateTime.now());
        return comment;
    }

    private Advert getAdvert(Long advertId) {
        return Optional.ofNullable(advertId)
                .flatMap(advertRepository::findById)
                .orElse(null);
    }

}
