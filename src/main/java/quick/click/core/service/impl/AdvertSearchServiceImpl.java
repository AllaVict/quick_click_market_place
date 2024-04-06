package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

@Service
public class AdvertSearchServiceImpl implements AdvertSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final UserRepository userRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    public AdvertSearchServiceImpl(final AdvertRepository advertRepository,
                                   final UserRepository userRepository,
                                   final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Override
    public AdvertReadDto findAdvertById(final Long advertId) {

        final AdvertReadDto advertReadDto = advertRepository.findById(advertId)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        LOGGER.debug("In findAdvertById find the Advert with id {}", advertReadDto.getId());

        return advertReadDto;
    }

    @Override
    public List<AdvertReadDto> findAllAdverts() {

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAll()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAdvertById find all adverts");

        return advertReadDtoList;
    }

    @Override
    public List<AdvertReadDto> findAllAdvertsByUser(final AuthenticatedUser authenticatedUser) {

        final User user = getUserByAuthenticatedUser(authenticatedUser);

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAllByUserOrderByCreatedDateDesc(user)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllAdvertsByUser find all adverts for the user with id: {}, {}", user.getId(), advertReadDtoList);

        return advertReadDtoList;
    }

    private User getUserByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"));

    }

}
