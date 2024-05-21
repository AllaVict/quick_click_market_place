package quick.click.core.service;

import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

/**
 * Service interface for searching users.
 *
 * @author Elnur Kasimov
 */
public interface UserSearchService {

    UserReadDto findUserById(Long userId);

    UserReadDto findUserByEmail(String userId);

    List<AdvertReadDto> findViewedAdvertsByUserId(long userId);



}
