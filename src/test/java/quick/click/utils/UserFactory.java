package quick.click.utils;

import quick.click.core.domain.model.User;
import quick.click.core.enums.Role;
import quick.click.core.enums.Sex;

import java.time.LocalDateTime;

import static quick.click.core.enums.Sex.MALE;

public class UserFactory {

    private static final long USER_ID = 101L;
    private static final String JHON = "Jhon";

    private static final String JHONSON = "Johnson";

    private static final String EMAIL =  "test@example.com";

    private static final String PASSWORD = "password";

    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2024, 10, 24, 20, 24);

    private UserFactory() {
    }

    public static User createUser(
            final Long id,
            final String firstName,
            final String lastName,
            final Sex sex,
            final String email,
            final String password
    ) {
        final User user = new User(id, firstName, lastName, sex, email, password);
        user.setCreatedDate(CREATED_DATE);
        return user;
    }

    public static User createUser() {
        return createUserWithRole(Role.ROLE_USER);
    }

    public static User createUserWithRole(final Role role) {
        final User user = createUser(USER_ID, JHON, JHONSON, MALE, EMAIL, PASSWORD);
        user.setRole(role);

        return user;
    }
}
