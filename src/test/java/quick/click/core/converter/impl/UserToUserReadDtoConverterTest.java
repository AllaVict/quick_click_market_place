package quick.click.core.converter.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import quick.click.config.factory.UserFactory;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("UserToUserReadDtoConverterTest")
class UserToUserReadDtoConverterTest {

    private UserToUserReadDtoConverter converter = new UserToUserReadDtoConverter();

    private User user;

    @BeforeEach
    public void setUp() {
        user = UserFactory.createUser();
    }

    @Test
    void shouldGetTargetClass() {
        assertEquals(UserReadDto.class, converter.getTargetClass());
    }

    @Test
    void shouldGetSourceClass() {
        assertEquals(User.class, converter.getSourceClass());
    }

    @Test
    void testConvert_shouldConvertUserToUserReadDto() {
        UserReadDto result = converter.convert(user);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
    }
}