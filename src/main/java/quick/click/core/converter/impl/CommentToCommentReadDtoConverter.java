package quick.click.core.converter.impl;

import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.core.domain.model.Comment;
@Component
public class CommentToCommentReadDtoConverter implements TypeConverter<Comment, CommentReadDto> {

    @Override
    public Class<Comment> getSourceClass() {
        return Comment.class;
    }

    @Override
    public Class<CommentReadDto> getTargetClass() {
        return CommentReadDto.class;
    }

    @Override
    public CommentReadDto convert(final Comment comment) {
        final CommentReadDto commentDto = new CommentReadDto();
        commentDto.setId(comment.getId());
        commentDto.setMessage(comment.getMessage());
        commentDto.setUsername(comment.getUsername());
        commentDto.setAdvertId(comment.getAdvert().getId());
        commentDto.setCreatedDate(comment.getCreatedDate());
        commentDto.setUpdatedDate(comment.getUpdatedDate());
        return commentDto;
    }
}
