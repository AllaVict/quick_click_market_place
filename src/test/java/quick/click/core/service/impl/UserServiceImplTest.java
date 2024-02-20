
package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.dto.UserSignupDto;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static quick.click.config.factory.UserDtoFactory.*;
import static quick.click.config.factory.UserFactory.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImplTest")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TypeConverter<User, UserReadDto> typeConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final long USER_ID = 101L;

    private static final String EMAIL =  "test@example.com";

    private static final String PASSWORD = "password";

    private UserSignupDto userSignupDto;

    private UserReadDto userReadDto;

    private User user;

    @BeforeEach
    public void setUp() {
        user = createUser();
        userSignupDto = createUserSignupDto();
        userReadDto = createUserReadDto();
    }

    @Nested
    @DisplayName("When save a User")
    class SaveTests {
        @Test
        void testSave_shouldReturnsUserReadDto() {
            when(passwordEncoder.encode(userSignupDto.getPassword())).thenReturn(PASSWORD);
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(typeConverter.convert(user)).thenReturn(userReadDto);

            UserReadDto result = userService.save(userSignupDto);

            verify(userRepository).save(any(User.class));
            assertNotNull(result);
            assertEquals(userReadDto, result);
        }
    }

    @Nested
    @DisplayName("When Find a User By Id")
    class FindByIdTests {
        @Test
        void testFindById_shouldReturnsUserWithGivenId() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(typeConverter.convert(user)).thenReturn(userReadDto);

            UserReadDto result = userService.findById(USER_ID);

            verify(userRepository).findById(USER_ID);
            assertNotNull(result);
            assertEquals(userReadDto, result);
        }

        @Test
        void testFindById_shouldReturnsNoUserThrowsException () {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userService.findById(USER_ID));
        }
    }

    @Nested
    @DisplayName("When verify User existing By Email")
    class testExistsByEmail {

        @Test
        void testExistsByEmail_shouldReturnsTrueIfUserExits() {

            boolean expectedResult = true;
            when(userRepository.existsByEmail(EMAIL)).thenReturn(expectedResult);

            boolean result = userService.existsByEmail(EMAIL);

            verify(userRepository).existsByEmail(EMAIL);
            assertTrue(result);
            assertEquals(expectedResult, result);
        }
        @Test
        void testExistsByEmail_shouldReturnsFalseIfNoUser() {

            boolean expectedResult = false;
            when(userRepository.existsByEmail(EMAIL)).thenReturn(expectedResult);

            boolean result = userService.existsByEmail(EMAIL);

            verify(userRepository).existsByEmail(EMAIL);
            assertFalse(result);
            assertEquals(expectedResult, result);
        }

    }
}