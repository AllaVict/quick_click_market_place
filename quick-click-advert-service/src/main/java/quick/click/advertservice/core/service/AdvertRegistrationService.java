package quick.click.advertservice.core.service;


import quick.click.advertservice.core.domain.dto.AdvertCreateDto;
import quick.click.advertservice.core.domain.dto.AdvertReadDto;

public interface AdvertRegistrationService {

    AdvertReadDto registerAdvert(AdvertCreateDto advertCreateDto);

}
