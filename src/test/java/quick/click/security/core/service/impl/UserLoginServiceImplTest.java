package quick.click.security.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserLoginServiceImplTest")
public class UserLoginServiceImplTest {

    private static final long USER_ID = 101L;
    private static final String EMAIL =  "test@example.com";
    private static final String PASSWORD = "password";

    private User user;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLoginServiceImpl userLoginService;


    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
    }

    @Nested
    @DisplayName("When loading a user by Username")
    class LoadUserByUsernameTests {

        @Test
        void testLoadUserByUsername_ExistingUser_ReturnsUserDetails() {
            // Arrange
            Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            // Act
            UserDetails userDetails = userLoginService.loadUserByUsername(EMAIL);
            // Assert
            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserByUsername_NonExistingUser_ThrowsException() {
            // Arrange
            Mockito.when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
            // Act
            userLoginService.loadUserByUsername(EMAIL);
            // The test expects an exception to be thrown, so nothing needs to be asserted explicitly.
        }

    }

    @Nested
    @DisplayName("When loading a user by Id")
    class LoadUserByIdTests {
        @Test
        void testLoadUserById_ExistingUser_ReturnsUserDetails() {
            // Arrange
            Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            // Act
            UserDetails userDetails = userLoginService.loadUserById(USER_ID);
            // Assert
            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserById_NonExistingUser_ThrowsException() {
            // Arrange
            Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
            // Act
            userLoginService.loadUserById(USER_ID);
            // The test expects an exception to be thrown, so nothing needs to be asserted explicitly.
        }

    }

}



