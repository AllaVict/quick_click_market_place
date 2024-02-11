package quick.click.security.core.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import quick.click.security.commons.model.dto.AuthResponse;
import quick.click.security.commons.model.dto.LoginRequest;
import quick.click.security.commons.utils.TokenProvider;

import static org.junit.jupiter.api.Assertions.*;
import static quick.click.commons.constants.Constants.Tokens.UNAUTHENTICATED;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginRestControllerTest")
class LoginControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private LoginController loginController;

    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest("test@example.com", "password123");
        // MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAuthenticateUser_ValidCredentials() {
        // Mock authentication and token
        Authentication authentication = Mockito.mock(Authentication.class);
        String token = tokenProvider.createToken(authentication);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(tokenProvider.createToken(Mockito.any())).thenReturn(token);
        // Execute the authenticateUser method
        ResponseEntity<?> responseEntity = loginController.authenticateUser(loginRequest);
        // Verify the response
        var expected = new AuthResponse(token);
        var actual = responseEntity.getBody();
        System.out.println("expected: " + expected + ", actual: " + actual);
        // assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new AuthResponse(token), responseEntity.getBody());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        loginRequest.setPassword("invalidpassword");
        // Mock BadCredentialsException
        Mockito.when(authenticationManager.authenticate(Mockito.any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        // Execute the authenticateUser method
        ResponseEntity<?> responseEntity = loginController.authenticateUser(loginRequest);
        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new AuthResponse(UNAUTHENTICATED), responseEntity.getBody());
    }

    @Test
    void testLogout() throws ServletException {
        // Mock HttpServletRequest and SecurityContextHolder
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        // Mock authenticated user name
        Mockito.when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("test@example.com");
        // Execute the logout method
        ResponseEntity<String> responseEntity = loginController.logout(request);
        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User with username {} logout successfullytest@example.com", responseEntity.getBody());
        Mockito.verify(request, Mockito.times(1)).logout();
    }
}