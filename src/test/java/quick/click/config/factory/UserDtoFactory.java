package quick.click.config.factory;

import quick.click.security.commons.model.dto.UserLoginDto;

import java.time.LocalDateTime;

public class UserDtoFactory {
    private static final long USER_ID = 101L;
    private static final String JHON = "Jhon";

    private static final String JHONSON = "Johnson";

    private static final String EMAIL =  "test@example.com";

    private static final String PASSWORD = "password";

    private static final LocalDateTime CREATED_DATE = LocalDateTime.of(2024, 10, 24, 20, 24);

    private UserDtoFactory() {
    }
    public static UserLoginDto createUserLoginDto() {
        final UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(EMAIL);
        userLoginDto.setPassword(PASSWORD);
        return userLoginDto;
    }

}
