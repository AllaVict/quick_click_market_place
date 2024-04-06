package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertRegistrationService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.Optional;

import static quick.click.core.enums.AdvertStatus.PUBLISHED;

@Service
public class AdvertRegistrationServiceImpl implements AdvertRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final UserRepository userRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;
    private final TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto;

    public AdvertRegistrationServiceImpl(final AdvertRepository advertRepository,
                                         final UserRepository userRepository,
                                         final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto,
                                         final TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
        this.typeConverterCreateDto = typeConverterCreateDto;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Override
    public AdvertReadDto registerAdvert(final AdvertCreateDto advertCreateDto,
                                        final AuthenticatedUser authenticatedUser) {
        final User user = getUserByAuthenticatedUser(authenticatedUser);

        AdvertReadDto advertReadDto = Optional.of(advertCreateDto)
                .map(this::setStatusPublished)
                .map(this::setCreatedDate)
                .map(typeConverterCreateDto::convert)
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();

        LOGGER.debug("Registering Advert with id {}", advertReadDto);

        return advertReadDto;
    }

    private AdvertCreateDto setStatusPublished(AdvertCreateDto advertCreateDto) {
        advertCreateDto.setStatus(PUBLISHED);
        return advertCreateDto;
    }

    private AdvertCreateDto setCreatedDate(AdvertCreateDto advertCreateDto) {
        advertCreateDto.setCreatedDate(LocalDateTime.now());
        return advertCreateDto;
    }

    private User getUserByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"));

    }
}
