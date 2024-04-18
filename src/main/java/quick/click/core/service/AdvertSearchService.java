package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

/**
 * Service interface for searching adverts.
 *
 * @author Alla Borodina
 */
public interface AdvertSearchService {

    AdvertReadDto findAdvertById(Long storyId);

    List<AdvertReadDto> findAllAdverts();

    List<AdvertReadDto> findAllAdvertsByUser(AuthenticatedUser authenticatedUser);

    List<AdvertReadDto> findAllByOrderByCreatedDateDesc();

}
