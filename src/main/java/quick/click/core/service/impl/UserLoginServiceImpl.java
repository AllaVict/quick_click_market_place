package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.LoginAttemptFailedException;
import quick.click.core.domain.dto.LoginDto;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.core.domain.dto.AuthenticatedUser;
import quick.click.core.service.UserLoginService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private static final Logger LOG = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    private final UserRepository userRepository;

    public UserLoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        LOG.debug("In loadUserByUsername get UserDetails by username {}:", username);
        final User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));
        return build(user);

    }


    private AuthenticatedUser build(final User user) {
        final List<GrantedAuthority> authorities = Stream.of(user.getRole())
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new AuthenticatedUser(
                user.getId(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                authorities
        );
    }

}
