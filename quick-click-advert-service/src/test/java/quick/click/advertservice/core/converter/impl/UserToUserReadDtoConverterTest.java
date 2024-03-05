package quick.click.advertservice.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quick.click.advertservice.core.converter.TypeConverter;
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.factory.UserFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("UserToUserReadDtoConverterTest")
class UserToUserReadDtoConverterTest {

    private TypeConverter<User, UserReadDto> userToUserReadDtoConverter = new UserToUserReadDtoConverter();
    private User user;

    @BeforeEach
    public void setUp() {
        user = UserFactory.createUser();
    }

    @Test
    void shouldGetTargetClass() {
        assertEquals(UserReadDto.class, userToUserReadDtoConverter.getTargetClass());
    }

    @Test
    void shouldGetSourceClass() {
        assertEquals(User.class, userToUserReadDtoConverter.getSourceClass());
    }

    @Test
    void testConvert_shouldConvertUserToUserReadDto() {
        UserReadDto result = userToUserReadDtoConverter.convert(user);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
    }
}