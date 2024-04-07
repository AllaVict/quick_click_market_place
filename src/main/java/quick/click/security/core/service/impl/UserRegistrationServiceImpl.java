package quick.click.security.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;
import quick.click.core.enums.AuthProvider;
import quick.click.core.enums.Role;
import quick.click.core.enums.UserStatus;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.dto.UserSignupDto;
import quick.click.security.core.service.UserRegistrationService;

import java.time.LocalDateTime;

/**
 * Service implementation for registering an user.
 *
 * @author Alla Borodina
 */
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
    private final UserRepository userRepository;

    private final TypeConverter<User, UserReadDto> typeConverter;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(final UserRepository userRepository,
                                       final TypeConverter<User, UserReadDto> typeConverter,
                                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.typeConverter = typeConverter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new user to the database.
     *
     * @param userSignupDto the data transfer object containing signup information.
     * @return a Data Transfer Object representing the read view of the user.
     */
    @Override
    public UserReadDto save(final UserSignupDto userSignupDto) {

        LOGGER.debug("In save() save User with email {} ", userSignupDto.getEmail());

        User user = new User(
                userSignupDto.getName(),
                passwordEncoder.encode(userSignupDto.getPassword()),
                userSignupDto.getEmail(),
                Role.ROLE_USER,
                UserStatus.ACTIVE,
                LocalDateTime.now(),
                AuthProvider.LOCAL);

        return typeConverter.convert(userRepository.save(user));
    }
    /**
     * Finds a user by their ID.
     *
     * @param userId the ID of the user to find.
     * @return a Data Transfer Object representing the read view of the user.
     */
    @Override
    public UserReadDto findById(final Long userId) {

        LOGGER.debug("In findById find User by id {} ", userId);

        return userRepository.findById(userId)
                .map(typeConverter::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email the email to check.
     * @return true if a user exists with the given email, false otherwise.
     */
    @Override
    public boolean existsByEmail(final String email) {

        LOGGER.debug("In existsByEmail verify does exist User by email {} ", email);

        return userRepository.existsByEmail(email);
    }

}

