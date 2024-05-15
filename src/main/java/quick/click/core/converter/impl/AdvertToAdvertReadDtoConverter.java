package quick.click.core.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;

@Component
public class AdvertToAdvertReadDtoConverter implements TypeConverter<Advert, AdvertReadDto> {

    private final TypeConverter<User, UserReadDto> typeConverterUserReadDto;

    @Autowired
    public AdvertToAdvertReadDtoConverter(final TypeConverter<User, UserReadDto> typeConverterUserReadDto) {
        this.typeConverterUserReadDto = typeConverterUserReadDto;
    }

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
        advertReadDto.setFirstPriceDisplayed(advert.isFirstPriceDisplayed());
        advertReadDto.setCurrency(advert.getCurrency());
        advertReadDto.setAddress(advert.getAddress());
        advertReadDto.setFavorite(advert.isFavorite());
        advertReadDto.setViewingQuantity(advert.getViewingQuantity());
        advertReadDto.setUser(typeConverterUserReadDto.convert(advert.getUser()));
        return advertReadDto;
    }
}
