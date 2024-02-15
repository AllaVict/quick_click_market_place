package quick.click.security.core.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.dto.AuthResponse;
import quick.click.security.commons.model.dto.UserLoginDto;
import quick.click.security.commons.utils.TokenProvider;
import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static quick.click.commons.constants.Constants.Tokens.UNAUTHENTICATED;
import static quick.click.config.factory.UserDtoFactory.createUserLoginDto;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginControllerTest")
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private HttpServletRequest request;

    private static final String INVALID_PASSWORD ="invalidpassword";

    private static final String EMAIL =  "test@example.com";

    private UserLoginDto userLoginDto;

    private String token;

    @BeforeEach
    public void setUp() {
        authentication = Mockito.mock(Authentication.class);
        request = Mockito.mock(HttpServletRequest.class);
        securityContext = Mockito.mock(SecurityContext.class);
        token = tokenProvider.createToken(authentication);
        userLoginDto = createUserLoginDto();
    }

    @Nested
    @DisplayName("When authenticate a User")
    class tAuthenticateUserTests {

        @Test
        void testAuthenticateUser_ValidCredentials() {
            when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
            when(tokenProvider.createToken(Mockito.any())).thenReturn(token);

            ResponseEntity<?> responseEntity = loginController.authenticateUser(userLoginDto);

            assertEquals(new AuthResponse(token), responseEntity.getBody());
        }

        @Test
        void testAuthenticateUser_InvalidCredentials() {
            userLoginDto.setPassword(INVALID_PASSWORD);
            when(authenticationManager.authenticate(Mockito.any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            ResponseEntity<?> responseEntity = loginController.authenticateUser(userLoginDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(new AuthResponse(UNAUTHENTICATED), responseEntity.getBody());
        }

    }

    @Nested
    @DisplayName("When User Logout")
    class LogoutTests {
        @Test
        void testLogout_withResponseOk() throws ServletException {
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(authentication.getName()).thenReturn(EMAIL);

            ResponseEntity<String> responseEntity = loginController.logout(request);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(request, Mockito.times(1)).logout();
            assertEquals("User logout successfully with username test@example.com", responseEntity.getBody());
        }
    }

}

