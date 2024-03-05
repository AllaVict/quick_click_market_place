package quick.click.advertservice.factory;

import quick.click.advertservice.core.domain.dto.UserReadDto;

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

    public static UserReadDto createUserReadDto() {
        final UserReadDto userReadDto = new UserReadDto();
        userReadDto.setId(USER_ID);
        userReadDto.setFirstName(JOHN);
        userReadDto.setEmail(EMAIL);
        return userReadDto;
    }


}
