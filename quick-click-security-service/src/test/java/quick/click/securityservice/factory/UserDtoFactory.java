package quick.click.securityservice.factory;

import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.securityservice.commons.model.dto.UserLoginDto;
import quick.click.securityservice.commons.model.dto.UserSignupDto;

import java.time.LocalDateTime;

public class UserDtoFactory {

    private static final long USER_ID = 101L;

    private static final String JOHN = "John";

    private static final String JOHNSON = "Johnson";

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
    public static UserSignupDto createUserSignupDto() {
        final UserSignupDto userSignupDto = new UserSignupDto();
        userSignupDto.setName(JOHN);
        userSignupDto.setEmail(EMAIL);
        userSignupDto.setPassword(PASSWORD);
        return userSignupDto;
    }

    public static UserReadDto createUserReadDto() {
        final UserReadDto userReadDto = new UserReadDto();
        userReadDto.setId(USER_ID);
        userReadDto.setFirstName(JOHN);
        userReadDto.setEmail(EMAIL);
        return userReadDto;
    }
}
