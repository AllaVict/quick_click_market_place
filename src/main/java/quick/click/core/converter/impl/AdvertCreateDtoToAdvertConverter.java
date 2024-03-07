package quick.click.core.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;

import java.util.Optional;

@Component
public class AdvertCreateDtoToAdvertConverter implements TypeConverter<AdvertCreateDto, Advert> {

    private final UserRepository userRepository;

    @Autowired
    public AdvertCreateDtoToAdvertConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Class<AdvertCreateDto> getSourceClass() {
        return AdvertCreateDto.class;
    }

    @Override
    public Class<Advert> getTargetClass() {
        return Advert.class;
    }

    @Override
    public Advert convert(final AdvertCreateDto advertCreateDto) {
        final Advert advert = new Advert();
        advert.setTitle(advertCreateDto.getTitle());
        advert.setDescription(advertCreateDto.getDescription());
        advert.setCategory(advertCreateDto.getCategory());
        advert.setStatus(advertCreateDto.getStatus());
        advert.setPhone(advertCreateDto.getPhone());
        advert.setPrice(advertCreateDto.getPrice());
        advert.setFirstPrice(advertCreateDto.getPrice());
        advert.setFirstPriceDisplayed(advertCreateDto.isFirstPriceDisplayed());
        advert.setCurrency(advertCreateDto.getCurrency());
        advert.setAddress(advertCreateDto.getAddress());
        advert.setFavorite(advert.isFavorite());
        advert.setUser(getUser(advertCreateDto.getUserId()));
        advert.setCreatedDate(advertCreateDto.getCreatedDate());
        // advert.setImagId(advert.getImage().getId());
        return advert;
    }

    private User getUser(Long userId) {
        return Optional.ofNullable(userId)
                .flatMap(userRepository::findById)
                .orElse(null);
    }

}
