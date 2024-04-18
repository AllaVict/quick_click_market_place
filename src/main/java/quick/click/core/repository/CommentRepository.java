package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByAdvertId(Long advertId);

    Optional<Comment> findById(Long commentId);

    Optional<Comment>  findByIdAndUserId(Long commentId, Long userId);

}
