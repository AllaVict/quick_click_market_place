package quick.click.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import quick.click.core.domain.BaseEntity;
import quick.click.core.enums.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    @Size(max = 50)
    private String firstName;
    @Column(name = "last_name")
    @Size(max = 50)
    private String lastName;
    @Column
    @Size(max = 100)
    private String password;
    @Column(unique = true)
    @Size(max = 100)
    private String email;
    @Column(name = "email_confirmed")
    private boolean emailConfirmed;
    @Column
    @Size(max = 50)
    private String phone;
    @Column(name = "phone_displayed")
    private boolean phoneDisplayed;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column
    @Enumerated(EnumType.STRING)
    private LocaleType locale;
    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "avatar_id")
    protected FileReference avatar;
    @Column(name = "last_active_date")
    private LocalDateTime lastActiveDate;
    @Column
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    public User() {

    }

    public User(final String firstName,
                final String password,
                final String email,
                final Role role,
                final UserStatus status,
                final LocalDateTime lastActiveDate,
                final AuthProvider provider) {
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.status = status;
        this.lastActiveDate = lastActiveDate;
        this.provider = provider;
    }

    public User(
            final Long id,
            final String firstName,
            final String lastName,
            final Sex sex,
            final String email,
            final String password
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.email = email;
        this.password = password;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPhoneDisplayed() {
        return phoneDisplayed;
    }

    public void setPhoneDisplayed(boolean phoneDisplayed) {
        this.phoneDisplayed = phoneDisplayed;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocaleType getLocale() {
        return locale;
    }

    public void setLocale(LocaleType locale) {
        this.locale = locale;
    }

    public FileReference getAvatar() {
        return avatar;
    }

    public void setAvatar(FileReference avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDateTime lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }


    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, password, email);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", phoneDisplayed=" + phoneDisplayed +
                ", role=" + role +
                ", emailConfirmed=" + emailConfirmed +
                ", status=" + status +
                ", locale=" + locale +
                ", provider=" + provider +
                '}';
    }
}