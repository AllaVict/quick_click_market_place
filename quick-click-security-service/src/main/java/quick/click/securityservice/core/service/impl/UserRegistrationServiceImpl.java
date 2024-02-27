package quick.click.securityservice.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quick.click.advertservice.commons.exeptions.ResourceNotFoundException;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.core.enums.AuthProvider;
import quick.click.advertservice.core.enums.Role;
import quick.click.advertservice.core.enums.UserStatus;
import quick.click.advertservice.core.repository.UserRepository;
import quick.click.securityservice.commons.model.dto.UserSignupDto;
import quick.click.securityservice.core.service.UserRegistrationService;

import java.time.LocalDateTime;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);
    private final UserRepository userRepository;

    private final TypeConverter<User, UserReadDto> typeConverter;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationServiceImpl(final UserRepository userRepository,
                                       final TypeConverter<User, UserReadDto>  typeConverter,
                                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.typeConverter = typeConverter;
        this.passwordEncoder = passwordEncoder;
    }

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

    @Override
    public UserReadDto findById(final Long userId) {

        LOGGER.debug("In findById find User by id {} ", userId);

        return userRepository.findById(userId)
                .map(typeConverter::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @Override
    public boolean existsByEmail(final String email) {

        LOGGER.debug("In existsByEmail verify does exist User by email {} ", email);

        return userRepository.existsByEmail(email);
    }

}

