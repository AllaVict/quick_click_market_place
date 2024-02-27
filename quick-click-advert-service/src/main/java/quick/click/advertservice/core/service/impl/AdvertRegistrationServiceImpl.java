package quick.click.advertservice.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.service.AdvertRegistrationService;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdvertRegistrationServiceImpl implements AdvertRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    private final TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto;

    public AdvertRegistrationServiceImpl(final AdvertRepository advertRepository,
                                         final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto,
                                         final TypeConverter<AdvertCreateDto, Advert> typeConverterCreateDto) {
        this.advertRepository = advertRepository;
        this.typeConverterCreateDto = typeConverterCreateDto;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Transactional
    @Override
    public AdvertReadDto registerAdvert(final AdvertCreateDto advertCreateDto) {

        advertCreateDto.setCreatedDate(LocalDateTime.now());
        AdvertReadDto advertReadDto= Optional.of(advertCreateDto)
                .map(typeConverterCreateDto::convert)
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();

        LOGGER.debug("Registering Advert with id {}", advertReadDto.getId());

        return advertReadDto;
    }

}
