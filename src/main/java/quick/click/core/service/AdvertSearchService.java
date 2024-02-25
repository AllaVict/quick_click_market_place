package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.enums.AdvertStatus;

public interface AdvertSearchService {

    AdvertReadDto findAdvertById(Long storyId);
    AdvertReadDto findAdvertByIdAndStatus(Long storyId, AdvertStatus status);

}
