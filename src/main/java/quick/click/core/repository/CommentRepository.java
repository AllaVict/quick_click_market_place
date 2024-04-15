package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAdvertId(Long advertId);

    Comment findByIdAndUserId(Long commentId, Long userId);

}
