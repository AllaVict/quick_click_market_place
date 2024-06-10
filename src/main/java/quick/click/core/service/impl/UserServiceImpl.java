package quick.click.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.AuthenticatedUser;

/**
 * Service implementation for handling search operations related to adverts.
 *
 * @author Elnur Kasimov
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired

    private final UserRepository userRepository;

    private final TypeConverter<User, UserReadDto> typeConverterReadDto;


    /**
     * Finds a user by its ID.
     *
     * @param userId The ID of the user to find.
     * @return A UserReadDto containing the user details if found.
     * @throws ResourceNotFoundException If no user is found with the given ID.
     */
    @Override
    public UserReadDto findUserById(final Long userId) {

        final UserReadDto userReadDto = userRepository.findById(userId)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        LOGGER.debug("In findUserById find the User with id: {}", userReadDto.getId());

        return userReadDto;

    }

    /**
     * Finds a user by its email.
     *
     * @param userEmail The email of the user to find.
     * @return A UserReadDto containing the user details if found.
     * @throws ResourceNotFoundException If no user is found with the given ID.
     */
    @Override
    public UserReadDto findUserByEmail(String userEmail) {

        final UserReadDto userReadDto = userRepository.findUserByEmail(userEmail)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        LOGGER.debug("In findUserByEmail find the User with email: {}", userReadDto.getEmail());

        return userReadDto;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }


    private User getUserByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"));

    }

}

