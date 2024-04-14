package quick.click.security.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service interface for performing login operations an user.
 *
 * @author Alla Borodina
 */
public interface UserLoginService extends UserDetailsService {

    /**
     * Loads the user details by user ID.
     *
     * @param id the ID of the user whose details are to be loaded.
     * @return the UserDetails of the specified user.
     */
    UserDetails loadUserById(final Long id);

}
