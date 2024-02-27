package quick.click.advertservice.core.service;

import quick.click.advertservice.core.domain.dto.AdvertEditingDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;

public interface AdvertEditingService {
    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto);

    AdvertReadDto changeNewPrice(Long advertId, Double newPrice);

    public AdvertReadDto showFirstPrice(Long advertId);

    void deleteAdvert(Long advertId);

}
