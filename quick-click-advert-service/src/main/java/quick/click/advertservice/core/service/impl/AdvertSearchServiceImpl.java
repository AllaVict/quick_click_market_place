package quick.click.advertservice.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.service.AdvertSearchService;

import java.util.List;

@Service
public class AdvertSearchServiceImpl implements AdvertSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    public AdvertSearchServiceImpl(AdvertRepository advertRepository,
                                   TypeConverter<Advert, AdvertReadDto> typeConverterReadDto) {
        this.advertRepository = advertRepository;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Override
    public AdvertReadDto findAdvertById(Long advertId) {

        AdvertReadDto advertReadDto =  advertRepository.findById(advertId)
                .map(typeConverterReadDto::convert).orElseThrow();

        LOGGER.debug("In findAdvertById find the Advert with id {}", advertReadDto.getId());

        return advertReadDto;
    }

    @Override
    public List<AdvertReadDto> findAllAdverts() {

        List<AdvertReadDto> advertReadDtoList =  advertRepository.findAll()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAdvertById find all Advert");

        return advertReadDtoList;
    }


}
