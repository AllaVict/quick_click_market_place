package quick.click.securityservice.core.controller;

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
import quick.click.advertservice.core.domain.dto.UserReadDto;
import quick.click.securityservice.commons.model.dto.ApiResponse;
import quick.click.securityservice.commons.model.dto.AuthResponse;
import quick.click.securityservice.commons.model.dto.UserLoginDto;
import quick.click.securityservice.commons.model.dto.UserSignupDto;
import quick.click.securityservice.commons.utils.TokenProvider;
import quick.click.securityservice.core.service.UserRegistrationService;
import quick.click.securityservice.factory.UserDtoFactory;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static quick.click.advertservice.commons.constants.Constants.Tokens.UNAUTHORIZED;


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

    @Mock
    private UserRegistrationService userService;

    private static final String INVALID_PASSWORD ="invalid-password";

    private static final String EMAIL =  "test@example.com";

    private UserLoginDto userLoginDto;

    private UserSignupDto userSignupDto;

    private UserReadDto userReadDto;

    private String token;

    @BeforeEach
    public void setUp() {
        authentication = Mockito.mock(Authentication.class);
        request = Mockito.mock(HttpServletRequest.class);
        securityContext = Mockito.mock(SecurityContext.class);
        token = tokenProvider.createToken(authentication);
        userLoginDto = UserDtoFactory.createUserLoginDto();
        userSignupDto = UserDtoFactory.createUserSignupDto();
        userReadDto = UserDtoFactory.createUserReadDto();
    }

    @Nested
    @DisplayName("When authenticate a User")
    class AuthenticateUserTests {

        @Test
        void testAuthenticateUser_ValidCredentials() {
            when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
            when(tokenProvider.createToken(Mockito.any())).thenReturn(token);

            ResponseEntity<?> responseEntity = loginController.authenticateUser(userLoginDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(new AuthResponse(token), responseEntity.getBody());
        }

        @Test
        void testAuthenticateUser_InvalidCredentials() {
            userLoginDto.setPassword(INVALID_PASSWORD);
            when(authenticationManager.authenticate(Mockito.any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            ResponseEntity<?> responseEntity = loginController.authenticateUser(userLoginDto);

            assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
            assertEquals("User has bad credentials and not "+UNAUTHORIZED, responseEntity.getBody());
        }

    }

    @Nested
    @DisplayName("When Register a User")
    class RegisterUserTests {
        @Test
        void testRegisterUser_validCredentials() {
            when(userService.existsByEmail(anyString())).thenReturn(false);
            when(userService.save(userSignupDto)).thenReturn(userReadDto);

            ResponseEntity<?> responseEntity = loginController.registerUser(userSignupDto);

            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(new ApiResponse(true, "User registered successfully!"),
                    responseEntity.getBody());
        }

        @Test
        void testRegisterUser_withExitingEmail() {
            when(userService.existsByEmail(anyString())).thenReturn(true);

            ResponseEntity<?> responseEntity = loginController.registerUser(userSignupDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
            assertEquals(new ApiResponse(false, "Email is already exist!"),
                    responseEntity.getBody());
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
        @Test
        void testLogout_noLoginWithResponseBadRequest() throws ServletException {
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(authentication.getName()).thenReturn("anonymousUser");

            ResponseEntity<String> responseEntity = loginController.logout(request);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(request, Mockito.times(0)).logout();
            assertEquals("User was not login", responseEntity.getBody());
        }

    }

}

