package quick.click.core.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.Comment;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;

import java.util.Optional;

@Component
public class CommentCreatingDtoToCommentConverter implements TypeConverter<CommentCreatingDto, Comment> {

    private final UserRepository userRepository;
    private final AdvertRepository advertRepository;

    @Autowired
    public CommentCreatingDtoToCommentConverter(final UserRepository userRepository,
                                                final AdvertRepository advertRepository) {
        this.userRepository = userRepository;
        this.advertRepository = advertRepository;
    }

    @Override
    public Class<CommentCreatingDto> getSourceClass() {
        return CommentCreatingDto.class;
    }

    @Override
    public Class<Comment> getTargetClass() {
        return Comment.class;
    }

    @Override
    public Comment convert(final CommentCreatingDto commentCreatingDto) {
        final Comment comment = new Comment();
        comment.setMessage(commentCreatingDto.getMessage());
        comment.setAdvert(getAdvert(commentCreatingDto.getAdvertId()));
        comment.setUserId(commentCreatingDto.getUserId());
        comment.setUsername(getUserNameFromUser(commentCreatingDto.getUserId()));
        comment.setCreatedDate(commentCreatingDto.getCreatedDate());
        return comment;
    }

    private Advert getAdvert(Long advertId) {
        return Optional.ofNullable(advertId)
                .flatMap(advertRepository::findById)
                .orElse(null);
    }
    private String getUserNameFromUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .map(user -> {
                    return user.getFirstName()+" "+user.getLastName();
                })
                .orElse(null);
    }

}
