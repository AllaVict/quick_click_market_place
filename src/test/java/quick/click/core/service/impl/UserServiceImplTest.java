
package quick.click.core.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.dto.UserSignupDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

    private User user;

    @BeforeEach
    public void setUp() {
        userSignupDto = new UserSignupDto();
        user = new User();
    }

    @Test
    void testSave() {
        when(passwordEncoder.encode(userSignupDto.getPassword())).thenReturn(PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(typeConverter.convert(user)).thenReturn(new UserReadDto());

        UserReadDto result = userService.save(userSignupDto);

        assertNotNull(result);
    }


    @Test
    void testFindById() {

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(typeConverter.convert(user)).thenReturn(new UserReadDto());

        UserReadDto result = userService.findById(USER_ID);

        assertNotNull(result);

    }

    @Test
    void testExistsByEmail() {

        boolean expectedResult = true;
        when(userRepository.existsByEmail(EMAIL)).thenReturn(expectedResult);

        boolean result = userService.existsByEmail(EMAIL);

        assertEquals(expectedResult, result);
    }
}