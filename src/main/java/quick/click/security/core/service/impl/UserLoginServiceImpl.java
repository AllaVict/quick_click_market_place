package quick.click.security.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.AuthenticatedUser;
import quick.click.security.core.service.UserLoginService;

/**
 * Service implementation for performing login operations an user.
 *
 * @author Alla Borodina
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserLoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user by username (email) for authentication purposes.
     *
     * @param email the email of the user to load.
     * @return the UserDetails of the user.
     * @throws UsernameNotFoundException if the username is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        LOGGER.debug("In loadUserByUsername get UserDetails by email {}: ", email);

        final User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with email: " + email));

        return AuthenticatedUser.create(user);
    }

    /**
     * Loads the user by ID for authentication purposes.
     *
     * @param id the ID of the user to load.
     * @return the UserDetails of the user.
     */
    @Override
    public UserDetails loadUserById(final Long id) {
        LOGGER.debug("In loadUserById get UserDetails by email {}: ", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return AuthenticatedUser.create(user);
    }

}

