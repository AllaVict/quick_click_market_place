package quick.click.core.service;

import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;

public interface AdvertEditingService {
    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto);

    AdvertReadDto archiveAdvert(Long advertId);

    void deleteAdvert(Long advertId);

}
