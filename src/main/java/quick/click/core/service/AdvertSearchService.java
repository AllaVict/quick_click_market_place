package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.enums.Category;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;
import java.util.Set;

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

    List<AdvertReadDto> findByCategory(String category);

    List<AdvertReadDto> findDiscounted();

    List<AdvertReadDto> find10MaxViewed();

    List<AdvertReadDto> findPromoted();

    Set<AdvertReadDto> findViewed(User user);

}
