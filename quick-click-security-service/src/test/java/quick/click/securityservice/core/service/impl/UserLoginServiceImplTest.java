package quick.click.securityservice.core.service.impl;

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
import quick.click.advertservice.commons.exeptions.ResourceNotFoundException;
import quick.click.advertservice.core.domain.model.User;
import quick.click.advertservice.core.repository.UserRepository;
import quick.click.securityservice.factory.UserFactory;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        user = UserFactory.createUser();
    }

    @Nested
    @DisplayName("When loading a user by Username")
    class LoadUserByUsernameTests {

        @Test
        void testLoadUserByUsername_ExistingUser_ReturnsUserDetails() {
            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(user));

            UserDetails userDetails = userLoginService.loadUserByUsername(EMAIL);

            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserByUsername_NonExistingUser_ThrowsException() {

            when(userRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class,
                    () ->  userLoginService.loadUserByUsername(EMAIL));
        }

    }

    @Nested
    @DisplayName("When loading a user by Id")
    class LoadUserByIdTests {
        @Test
        void testLoadUserById_ExistingUser_ReturnsUserDetails() {
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

            UserDetails userDetails = userLoginService.loadUserById(USER_ID);

            assertNotNull(userDetails);
            assertEquals(user.getEmail(), userDetails.getUsername());
        }

        @Test
        void testLoadUserById_NonExistingUser_ThrowsException() {

            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> userLoginService.loadUserById(USER_ID));
        }

    }

}



