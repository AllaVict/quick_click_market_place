package quick.click.security.core.service;

import quick.click.core.domain.dto.UserReadDto;
import quick.click.security.commons.model.dto.UserSignupDto;

/**
 * Service interface for registering an user.
 *
 * @author Alla Borodina
 */
public interface UserRegistrationService {

    /**
     * Saves a new user to the database.
     *
     * @param userSignupDto the data transfer object containing signup information.
     * @return a Data Transfer Object representing the read view of the user.
     */
    UserReadDto save(UserSignupDto userSignupDto);

    /**
     * Finds a user by their ID.
     *
     * @param userId the ID of the user to find.
     * @return a Data Transfer Object representing the read view of the user.
     */
    UserReadDto findById(Long userId);

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check.
     * @return true if a user exists with the given email, false otherwise.
     */
    boolean existsByEmail(String email);

}
