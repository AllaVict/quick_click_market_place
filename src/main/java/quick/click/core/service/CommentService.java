package quick.click.core.service;

import quick.click.core.domain.dto.CommentCreatingDto;
import quick.click.core.domain.dto.CommentEditingDto;
import quick.click.core.domain.dto.CommentReadDto;

import java.util.List;

public interface CommentService {

    public CommentReadDto registerComment(Long advertId, CommentCreatingDto commentDTO);

    public CommentReadDto editComment(Long advertId, CommentEditingDto commentDTO);

    public List<CommentReadDto> getAllCommentsForAdvert(Long advertId);

    public void deleteComment(Long commentId);


}
