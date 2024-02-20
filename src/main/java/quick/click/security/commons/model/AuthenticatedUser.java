package quick.click.security.commons.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import quick.click.core.domain.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthenticatedUser implements OAuth2User, UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;

    public AuthenticatedUser(final Long id,
                             final String email,
                             final String password,
                             final Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static AuthenticatedUser create(User user) {
        final List<GrantedAuthority> authorities = Stream.of(user.getRole())
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new AuthenticatedUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public static AuthenticatedUser create(User user, Map<String, Object> attributes) {
        AuthenticatedUser authenticatedUser = AuthenticatedUser.create(user);
        authenticatedUser.setAttributes(attributes);
        return authenticatedUser;
    }
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
