package quick.click.security.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.User;
import quick.click.core.enums.Role;
import quick.click.core.repository.UserRepository;
import quick.click.utils.UserFactory;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserLoginServiceImplTest")
public class UserLoginServiceImplTest {

    private static final long USER_ID = 101L;
    private static final String EMAIL =  "test@example.com";

    private User user;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLoginServiceImpl userLoginService;

    @BeforeEach
    public void setUp() {
        user = UserFactory.createUserWithRole(Role.ROLE_USER);
    }

    @Nested
    @DisplayName("When loading a user by Username")
    class LoadUserByUsernameTests {

        @Test
        void testLoadUserByUsername_ExistingUser_ReturnsUserDetails() {
            // Arrange
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));
            // Act
            UserDetails userDetails = userLoginService.loadUserByUsername(EMAIL);
            // Assert
            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserByUsername_NonExistingUser_ThrowsException() {
            // Arrange
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
            // Act & The test expects an exception to be thrown, so nothing needs to be asserted explicitly.
            assertThrows(UsernameNotFoundException.class,
                    () ->  userLoginService.loadUserByUsername(EMAIL));
        }

    }

    @Nested
    @DisplayName("When loading a user by Id")
    class LoadUserByIdTests {
        @Test
        void testLoadUserById_ExistingUser_ReturnsUserDetails() {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            // Act
            UserDetails userDetails = userLoginService.loadUserById(USER_ID);
            // Assert
            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserById_NonExistingUser_ThrowsException() {
            // Arrange
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
            // Act & The test expects an exception to be thrown, so nothing needs to be asserted explicitly.
            assertThrows(ResourceNotFoundException.class,
                    () -> userLoginService.loadUserById(USER_ID));
        }

    }

}



