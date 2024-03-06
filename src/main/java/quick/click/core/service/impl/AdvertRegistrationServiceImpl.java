package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.service.AdvertRegistrationService;

import java.time.LocalDateTime;
import java.util.Optional;

import static quick.click.core.enums.AdvertStatus.PUBLISHED;

@Service
public class AdvertRegistrationServiceImpl implements AdvertRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;
    private final TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto;

    public AdvertRegistrationServiceImpl(final AdvertRepository advertRepository,
                                         final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto,
                                         final TypeConverter<AdvertCreateDto, Advert>  typeConverterCreateDto) {
        this.advertRepository = advertRepository;
        this.typeConverterCreateDto = typeConverterCreateDto;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Override
    public AdvertReadDto registerAdvert(final AdvertCreateDto advertCreateDto) {

        advertCreateDto.setCreatedDate(LocalDateTime.now());
        advertCreateDto.setStatus(PUBLISHED);
        AdvertReadDto advertReadDto= Optional.of(advertCreateDto)
                .map(typeConverterCreateDto::convert)
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();

        LOGGER.debug("Registering Advert with id {}", advertReadDto);

        return advertReadDto;
    }

}
