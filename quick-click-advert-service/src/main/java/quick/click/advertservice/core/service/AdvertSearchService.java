package quick.click.advertservice.core.service;


import quick.click.advertservice.core.domain.dto.AdvertReadDto;

import java.util.List;

public interface AdvertSearchService {

    AdvertReadDto findAdvertById(Long storyId);

    List<AdvertReadDto> findAllAdverts();

}
