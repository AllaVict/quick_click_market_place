package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.enums.AdvertStatus;

import java.util.List;
import java.util.Optional;

public interface AdvertSearchService {

    AdvertReadDto findAdvertById(Long storyId);

    List<AdvertReadDto> findAllAdverts();

}
