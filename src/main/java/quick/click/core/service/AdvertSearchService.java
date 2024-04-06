package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

public interface AdvertSearchService {

    AdvertReadDto findAdvertById(Long storyId);

    List<AdvertReadDto> findAllAdverts();

    List<AdvertReadDto> findAllAdvertsByUser(AuthenticatedUser authenticatedUser);

}
