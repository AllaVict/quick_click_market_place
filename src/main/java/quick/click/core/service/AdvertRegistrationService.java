package quick.click.core.service;


import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;

public interface AdvertRegistrationService {

    AdvertReadDto registerAdvert(AdvertCreateDto advertCreateDto);

}
