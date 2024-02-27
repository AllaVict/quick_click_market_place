package quick.click.securityservice.core.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserLoginService extends UserDetailsService {

    UserDetails loadUserById(final Long id);
}
