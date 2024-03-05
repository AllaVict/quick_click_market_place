package quick.click.advertservice.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.advertservice.commons.exceptions.ResourceNotFoundException;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertEditingDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;
import quick.click.advertservice.core.repository.AdvertRepository;
import quick.click.advertservice.core.repository.FileReferenceRepository;
import quick.click.advertservice.core.service.AdvertEditingService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdvertEditingServiceImpl implements AdvertEditingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertEditingServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final FileReferenceRepository fileReferenceRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    public AdvertEditingServiceImpl(final AdvertRepository advertRepository,
                                    final FileReferenceRepository fileReferenceRepository,
                                    final TypeConverter<Advert, AdvertReadDto>  typeConverterReadDto) {
        this.advertRepository = advertRepository;
        this.fileReferenceRepository = fileReferenceRepository;
        this.typeConverterReadDto = typeConverterReadDto;

    }

    @Override
    public AdvertReadDto editAdvert(final Long advertId, final AdvertEditingDto advertEditingDto) {

        final Optional<Advert> advertForUpdate = advertRepository.findById(advertId);
        if(advertForUpdate.isEmpty())
            advertForUpdate.orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        LOGGER.debug("Updating success advert with id {}", advertId);

        return  advertForUpdate
                .map(advert -> this.updateAdvertData(advert, advertEditingDto))
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    @Override
    public void deleteAdvert(final Long advertId) {

       final Optional<Advert> advertForDelete = advertRepository.findById(advertId);

       if(advertForDelete.isEmpty())
            advertForDelete.orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

       final Long fileReferenceForDelete = advertForDelete.orElseThrow().getImage();
       LOGGER.debug("Deleting fileReference for advert with id {}", fileReferenceForDelete);
       if(fileReferenceForDelete!=null){
             fileReferenceRepository.deleteById(fileReferenceForDelete);
       } else {
           LOGGER.debug("Deleting fileReference for advert with id {}", fileReferenceForDelete);
       }

        advertRepository.delete(advertForDelete.orElseThrow());

        LOGGER.debug("Deleting success advert with id {}", advertId);

    }

    protected Advert updateAdvertData(final Advert advert, final AdvertEditingDto advertEditingDto) {
        advert.setTitle(advertEditingDto.getTitle());
        advert.setDescription(advertEditingDto.getDescription());
        advert.setCategory(advertEditingDto.getCategory());
        advert.setStatus(advertEditingDto.getStatus());
        advert.setPhone(advertEditingDto.getPhone());
        advert.setPrice(advertEditingDto.getPrice());
        advert.setFirstPriceDisplayed(advertEditingDto.isFirstPriceDisplayed());
        advert.setCurrency(advertEditingDto.getCurrency());
        advert.setAddress(advertEditingDto.getAddress());
        advert.setCreatedDate(advertEditingDto.getCreatedDate());
        advert.setUpdatedDate(LocalDateTime.now());
        return advert;
    }

}

