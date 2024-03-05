package quick.click.security.core.service;

import quick.click.core.domain.dto.UserReadDto;
import quick.click.security.commons.model.dto.UserSignupDto;

public interface UserRegistrationService {

    UserReadDto save(UserSignupDto userSignupDto);


    UserReadDto findById(Long userId);

    boolean existsByEmail(String email);


}
