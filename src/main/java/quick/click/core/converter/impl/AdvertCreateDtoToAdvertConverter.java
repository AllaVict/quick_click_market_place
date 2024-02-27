package quick.click.core.converter.impl;

import org.springframework.stereotype.Component;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.model.Advert;

    @Component
    public class AdvertCreateDtoToAdvertConverter implements TypeConverter<AdvertCreateDto,  Advert> {

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
            advert.setFirstPrice(advertCreateDto.getFirstPrice());
            advert.setCurrency(advertCreateDto.getCurrency());
            advert.setAddress(advertCreateDto.getAddress());
            // advert.setImagId(advert.getImage().getId());
            // advert.setUserId(advert.getUser().getId());
            return advert;
        }
    }
