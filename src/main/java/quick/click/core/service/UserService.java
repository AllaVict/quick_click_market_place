package quick.click.core.service;

import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;

/**
 * Service interface for searching users.
 *
 * @author Elnur Kasimov
 */
public interface UserService {

    UserReadDto findUserById(Long userId);

    UserReadDto findUserByEmail(String userId);

    User save(User user);

//    List<AdvertReadDto> findViewedAdvertsByUserId(long userId);



}
