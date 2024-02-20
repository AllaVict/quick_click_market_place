package quick.click.config.factory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockAuthenticatedUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAuthenticatedUser> {

    @Override
    public SecurityContext createSecurityContext(final WithMockAuthenticatedUser mockAuthenticatedUser) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        final AuthenticatedUser authUser = new AuthenticatedUser(
                mockAuthenticatedUser.userId(),
                mockAuthenticatedUser.email(),
                mockAuthenticatedUser.password(),
                Stream.of(mockAuthenticatedUser.role())
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList())
                );

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                authUser,
                authUser.getPassword(),
                authUser.getAuthorities()
        );

        securityContext.setAuthentication(authentication);

        return securityContext;
    }
}
