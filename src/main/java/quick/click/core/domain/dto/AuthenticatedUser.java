package quick.click.core.domain.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import quick.click.core.enums.Role;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AuthenticatedUser implements UserDetails {

    private final Long userId;

    private final String email;

    private final String password;

    private final Role role;

     private final Collection<? extends GrantedAuthority> authorities; //security

    public AuthenticatedUser(
            final Long userId,
            final String password,
            final String email,
            final Role role,
            List<GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.role = role;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }


    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // user's email is username in our case
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

    private Collection<? extends GrantedAuthority> toAuthority(final Role role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AuthenticatedUser that = (AuthenticatedUser) o;

        return userId.equals(that.userId)
                && email.equals(that.email)
                && role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, role);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("email", email)
                .append("role", role)
                .toString();
    }

}