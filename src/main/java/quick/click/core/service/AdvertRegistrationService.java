package quick.click.core.service;


import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.security.commons.model.AuthenticatedUser;

public interface AdvertRegistrationService {

    AdvertReadDto registerAdvert(AdvertCreateDto advertCreateDto, AuthenticatedUser authenticatedUser);

}
