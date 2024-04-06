package quick.click.core.service;

import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;

public interface AdvertEditingService {

    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto);

    void deleteAdvert(Long advertId);

}
