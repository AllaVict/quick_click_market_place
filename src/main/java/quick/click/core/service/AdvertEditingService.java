package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.AdvertEditingDto;

public interface AdvertEditingService {
    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto);

    AdvertReadDto changeNewPrice(Long advertId, Double newPrice);

    public AdvertReadDto showFirstPrice(Long advertId);

    void deleteAdvert(Long advertId);

}
