package quick.click.security.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.commons.exceptions.ResourceNotFoundException;
import quick.click.security.commons.model.AuthenticatedUser;
import quick.click.security.core.service.UserLoginService;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserLoginServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {

        LOGGER.debug("In loadUserByUsername get UserDetails by email {}: ", email);

        final User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with email: " + email));

        return AuthenticatedUser.create(user);
    }

    @Override
    public UserDetails loadUserById(final Long id) {
        LOGGER.debug("In loadUserById get UserDetails by email {}: ", id);
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return AuthenticatedUser.create(user);
    }

}

