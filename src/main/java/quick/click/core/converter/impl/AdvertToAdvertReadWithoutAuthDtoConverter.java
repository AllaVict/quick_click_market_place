package quick.click.core.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.AdvertReadWithoutAuthDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;

@Component
public class AdvertToAdvertReadWithoutAuthDtoConverter implements TypeConverter<Advert, AdvertReadWithoutAuthDto> {

    private final TypeConverter<User, UserReadDto> typeConverterUserReadDto;

    @Autowired
    public AdvertToAdvertReadWithoutAuthDtoConverter(final TypeConverter<User, UserReadDto> typeConverterUserReadDto) {
        this.typeConverterUserReadDto = typeConverterUserReadDto;
    }

    @Override
    public Class<Advert> getSourceClass() {
        return Advert.class;
    }

    @Override
    public Class<AdvertReadWithoutAuthDto> getTargetClass() {
        return AdvertReadWithoutAuthDto.class;
    }

    @Override
    public AdvertReadWithoutAuthDto convert(final Advert advert) {
        final AdvertReadWithoutAuthDto advertReadWithoutAuthDto = new AdvertReadWithoutAuthDto();
        advertReadWithoutAuthDto.setId(advert.getId());
        advertReadWithoutAuthDto.setTitle(advert.getTitle());
        advertReadWithoutAuthDto.setDescription(advert.getDescription());
        advertReadWithoutAuthDto.setCategory(advert.getCategory());
        advertReadWithoutAuthDto.setStatus(advert.getStatus());
        advertReadWithoutAuthDto.setPhone(advert.getPhone());
        advertReadWithoutAuthDto.setPrice(advert.getPrice());
        advertReadWithoutAuthDto.setFirstPrice(advert.getFirstPrice());
        advertReadWithoutAuthDto.setFirstPriceDisplayed(advert.isFirstPriceDisplayed());
        advertReadWithoutAuthDto.setCurrency(advert.getCurrency());
        advertReadWithoutAuthDto.setAddress(advert.getAddress());
        advertReadWithoutAuthDto.setFavorite(advert.isFavorite());
        advertReadWithoutAuthDto.setViewingQuantity(advert.getViewingQuantity());
        advertReadWithoutAuthDto.setPromoted(advert.isPromoted());
        advertReadWithoutAuthDto.setUser(typeConverterUserReadDto.convert(advert.getUser()));
        return advertReadWithoutAuthDto;
    }
}
