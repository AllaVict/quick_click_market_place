package quick.click.core.service;

import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

public interface CommentService {

    CommentReadDto registerComment(Long advertId, CommentCreatingDto commentDTO, AuthenticatedUser authenticatedUser);

    CommentReadDto editComment(Long advertId, CommentEditingDto commentDTO, AuthenticatedUser authenticatedUser);

    List<CommentReadDto> findAllCommentsForAdvert(Long advertId);

    CommentReadDto findCommentById(Long commentId);

    void deleteComment(Long commentId, AuthenticatedUser authenticatedUser);


}
