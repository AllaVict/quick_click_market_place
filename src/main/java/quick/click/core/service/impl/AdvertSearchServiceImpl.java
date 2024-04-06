package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;

import java.security.Principal;
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

        final AdvertReadDto advertReadDto =  advertRepository.findById(advertId)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        LOGGER.debug("In findAdvertById find the Advert with id {}", advertReadDto.getId());

        return advertReadDto;
    }

    @Override
    public List<AdvertReadDto> findAllAdverts() {

        final List<AdvertReadDto> advertReadDtoList =  advertRepository.findAll()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAdvertById find all adverts");

        return advertReadDtoList;
    }

    @Override
    public List<AdvertReadDto> findAllAdvertsByUserId(Long userId) {

      final List<AdvertReadDto> advertReadDtoList =  advertRepository.findAllAdvertsByUserId(userId)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllAdvertsByUserId find all adverts for the user with id: {}", userId);

        return advertReadDtoList;
    }

    @Override
    public List<AdvertReadDto> findAllAdvertsByUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        final List<AdvertReadDto> advertReadDtoList =  advertRepository.findAllByUserOrderByCreatedDateDesc(user)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllAdvertsByUser find all adverts for the user with id: {}", user.getId());

        return advertReadDtoList;
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

}
