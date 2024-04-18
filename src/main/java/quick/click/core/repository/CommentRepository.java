package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Comment} entities.
 *
 * @author Alla Borodina
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments associated with a specific advertisement.
     *
     * @param advertId The identifier of the advertisement for which comments are to be retrieved.
     * @return A list of {@link Comment} instances associated with the given advertisement ID.
     *         The list will be empty if no comments are found.
     */
    List<Comment> findAllByAdvertId(Long advertId);

    /**
     * Finds a comment by its unique identifier.
     *
     * @param commentId The unique identifier of the comment to be retrieved.
     * @return An {@link Optional} containing the found comment, or an empty {@link Optional}
     *         if no comment is found with the given ID.
     */
    Optional<Comment> findById(Long commentId);

    /**
     * Finds a comment by its ID and the user ID of the commenter.
     *
     * @param commentId The ID of the comment to find.
     * @param userId The user ID of the commenter.
     * @return An {@link Optional} containing the found comment if it exists under the given
     *         user ID, or an empty {@link Optional} if no such comment is found.
     */
    Optional<Comment>  findByIdAndUserId(Long commentId, Long userId);

}
