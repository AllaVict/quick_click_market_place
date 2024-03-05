package quick.click.advertservice.core.service;

import quick.click.advertservice.core.domain.dto.AdvertEditingDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;

public interface AdvertEditingService {
    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto);

    void deleteAdvert(Long advertId);

}
