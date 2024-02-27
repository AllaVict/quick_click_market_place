package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.service.AdvertEditingService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdvertEditingServiceImpl implements AdvertEditingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertEditingServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;


    public AdvertEditingServiceImpl(final AdvertRepository advertRepository,
                                    final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto) {
        this.advertRepository = advertRepository;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    @Override
    public AdvertReadDto editAdvert(final Long id, final AdvertEditingDto advertEditingDto) {

        Optional<Advert> advertForUpdate = Optional.ofNullable(advertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", id)));

        LOGGER.debug("Updating success advert with id {}", id);

        return  advertForUpdate
                .map(advert -> updateAdvertData(advert, advertEditingDto))
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    @Override
    public AdvertReadDto changeNewPrice(Long advertId, Double newPrice) {
        Optional<Advert> advertForUpdate = Optional.ofNullable(advertRepository.findById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId)));

        LOGGER.debug("Updating price success for advert with id {}", advertId);

        return  advertForUpdate
                .map(advert -> updatePriceData(advert, newPrice))
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    private Advert updatePriceData(final Advert advert, final Double newPrice) {
        advert.setPrice(newPrice);
        advert.setUpdatedDate(LocalDateTime.now());
        return advert;
    }

    @Override
    public AdvertReadDto showFirstPrice(Long advertId) {
        Optional<Advert> advertForUpdate = Optional.ofNullable(advertRepository.findById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId)));

        LOGGER.debug("Updating price success for advert with id {}", advertId);

        return advertForUpdate
                .map(this::showFirstPrice)
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    @Override
    public void deleteAdvert(Long advertId) {

       Advert advertForDelete = advertRepository.findById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        advertRepository.delete(advertForDelete);

        LOGGER.debug("Deleting success advert with id {}", advertId);

    }

    private Advert updateAdvertData(final Advert advert, final AdvertEditingDto advertEditingDto) {
        advert.setTitle(advertEditingDto.getTitle());
        advert.setDescription(advertEditingDto.getDescription());
        advert.setCategory(advertEditingDto.getCategory());
        advert.setStatus(advertEditingDto.getStatus());
        advert.setPhone(advertEditingDto.getPhone());
        advert.setPrice(advertEditingDto.getPrice());
        advert.setFirstPriceDisplayed(advertEditingDto.getFirstPriceDisplayed());
        advert.setCurrency(advertEditingDto.getCurrency());
        advert.setAddress(advertEditingDto.getAddress());
        advert.setCreatedDate(advertEditingDto.getCreatedDate());
        advert.setUpdatedDate(LocalDateTime.now());
        return advert;
    }

    private Advert showFirstPrice(final Advert advert) {
        advert.setFirstPriceDisplayed(true);
        advert.setUpdatedDate(LocalDateTime.now());
        return advert;
    }

}

