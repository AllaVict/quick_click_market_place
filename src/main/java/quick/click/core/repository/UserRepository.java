package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.User;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on user.
 *
 * @author Alla Borodina
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves a user by their email.
     *
     * @param username the email of the user to be retrieved.
     * @return an Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findUserByEmail(String username);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check in the database.
     * @return true if a user exists with the given email, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to be retrieved.
     * @return an Optional containing the user if found, or an empty Optional otherwise.
     */
    Optional<User> findUserById(Long id);


}
