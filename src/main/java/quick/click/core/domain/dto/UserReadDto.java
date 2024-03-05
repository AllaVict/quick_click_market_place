package quick.click.core.domain.dto;

import quick.click.core.enums.AuthProvider;
import quick.click.core.enums.Role;
import quick.click.core.enums.UserStatus;

import java.util.Objects;

public class UserReadDto {

    private Long id;

    private String firstName;

    private String email;

    private Role role;

    private UserStatus status;

    private AuthProvider provider;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReadDto that = (UserReadDto) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(email, that.email) && role == that.role && status == that.status && provider == that.provider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, email, role, status, provider);
    }

    @Override
    public String toString() {
        return "UserReadDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", status=" + status +
                ", provider=" + provider +
                '}';
    }
}
