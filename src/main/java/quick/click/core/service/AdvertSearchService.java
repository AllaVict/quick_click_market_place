package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.AdvertReadWithoutAuthDto;
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

    AdvertReadWithoutAuthDto findAdvertById(Long storyId);

    List<AdvertReadWithoutAuthDto> findAllAdverts();

    List<AdvertReadDto> findAllAdvertsByUser(AuthenticatedUser authenticatedUser);

    List<AdvertReadDto> findAllByOrderByCreatedDateDesc();

    List<AdvertReadWithoutAuthDto> findByCategory(String category);

    List<AdvertReadWithoutAuthDto> findDiscounted();

    List<AdvertReadWithoutAuthDto> find10MaxViewed();

    List<AdvertReadWithoutAuthDto> findPromoted();

    Set<AdvertReadDto> findViewed(User user);

}
