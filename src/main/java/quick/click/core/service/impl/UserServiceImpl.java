package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quick.click.commons.exceptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;
import quick.click.core.enums.AuthProvider;
import quick.click.core.enums.Role;
import quick.click.core.enums.UserStatus;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.dto.UserSignupDto;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;

    private final TypeConverter<User, UserReadDto> typeConverter;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(final UserRepository userRepository,
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

