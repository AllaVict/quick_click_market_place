package quick.click.core.service;

import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;

import java.util.List;

public interface CommentService {

    CommentReadDto registerComment(Long advertId, CommentCreatingDto commentDTO);

    CommentReadDto editComment(Long advertId, CommentEditingDto commentDTO);

    List<CommentReadDto> findAllCommentsForAdvert(Long advertId);

    CommentReadDto findCommentById(Long commentId);

    void deleteComment(Long commentId);


}
