package quick.click.securityservice.core.service;

import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.securityservice.commons.model.dto.UserSignupDto;

public interface UserRegistrationService {

    UserReadDto save(UserSignupDto userSignupDto);


    UserReadDto findById(Long userId);

    boolean existsByEmail(String email);


}
