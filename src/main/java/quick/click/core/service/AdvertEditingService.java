package quick.click.core.service;

import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.security.commons.model.AuthenticatedUser;

public interface AdvertEditingService {
    AdvertReadDto editAdvert(Long advertId, AdvertEditingDto advertEditingDto, AuthenticatedUser authenticatedUser);

    AdvertReadDto archiveAdvert(Long advertId, AuthenticatedUser authenticatedUser);

    void deleteAdvert(Long advertId, AuthenticatedUser authenticatedUser);

}
