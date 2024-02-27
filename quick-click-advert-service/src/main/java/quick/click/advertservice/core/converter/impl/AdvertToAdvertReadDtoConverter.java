package quick.click.advertservice.core.converter.impl;

import org.springframework.stereotype.Component;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;
import quick.click.advertservice.core.domain.model.Advert;

@Component
public class AdvertToAdvertReadDtoConverter implements TypeConverter<Advert, AdvertReadDto> {

    @Override
    public Class<Advert> getSourceClass() {
        return Advert.class;
    }

    @Override
    public Class<AdvertReadDto> getTargetClass() {
        return AdvertReadDto.class;
    }

    @Override
    public AdvertReadDto convert(final Advert advert) {
        final AdvertReadDto advertReadDto = new AdvertReadDto();
        advertReadDto.setId(advert.getId());
        advertReadDto.setTitle(advert.getTitle());
        advertReadDto.setDescription(advert.getDescription());
        advertReadDto.setCategory(advert.getCategory());
        advertReadDto.setStatus(advert.getStatus());
        advertReadDto.setPhone(advert.getPhone());
        advertReadDto.setPrice(advert.getPrice());
        advertReadDto.setFirstPrice(advert.getFirstPrice());
        advertReadDto.setCurrency(advert.getCurrency());
        advertReadDto.setAddress(advert.getAddress());
       // advertReadDto.setImagId(advert.getImage().getId());
      // advertReadDto.setUserId(advert.getUser().getId());
        return advertReadDto;
    }
}
