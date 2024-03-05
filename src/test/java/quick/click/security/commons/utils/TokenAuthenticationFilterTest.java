package quick.click.security.commons.utils;

import io.jsonwebtoken.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;
import static quick.click.commons.constants.Constants.Headers.AUTHORIZATION_HEADER;
import static quick.click.commons.constants.Constants.Tokens.ACCESS_TOKEN_PREFIX;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import quick.click.config.annotation.IntegrationTest;
import quick.click.security.core.service.UserLoginService;

@IntegrationTest
@WebMvcTest(controllers = TokenAuthenticationFilterTest.class)
@DisplayName("TokenAuthenticationFilterTest")
class TokenAuthenticationFilterTest {

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private UserLoginService userLoginService;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @MockBean
    private FilterChain filterChain;

    private static final long USER_ID = 101L;

    private UserDetails userDetails;

    private UsernamePasswordAuthenticationToken authentication;

    private static final String JWT_TOKEN = "jwtToken";

    private static final String INVALID_JWT_TOKEN = "invalidJwtToken";

    private static final String INVALID_PREFIX = "InvalidPrefix";

    private static final String INVALID_HEADER = "invalidHeader";

    @BeforeEach
    void setUp() {
        tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenProvider, userLoginService);
        userDetails = mock(UserDetails.class);
        authentication = mock(UsernamePasswordAuthenticationToken.class);
    }

    @Test
    void doFilterInternal_ValidJwtToken_AuthenticationSet() throws ServletException, IOException, java.io.IOException {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(ACCESS_TOKEN_PREFIX + JWT_TOKEN);
        when(tokenProvider.validateToken(JWT_TOKEN)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(JWT_TOKEN)).thenReturn(USER_ID);
        when(userLoginService.loadUserById(USER_ID)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);
        when(authentication.getDetails()).thenReturn(null);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, times(1)).loadUserById(USER_ID);
        verify(tokenProvider, times(1)).getUserIdFromToken(JWT_TOKEN);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, never()).getHeader(INVALID_HEADER);
    }

    @Test
    void doFilterInternal_InvalidJwtToken_AuthenticationNotSet() throws ServletException, IOException, java.io.IOException {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(ACCESS_TOKEN_PREFIX + INVALID_JWT_TOKEN);
        when(tokenProvider.validateToken(INVALID_JWT_TOKEN)).thenReturn(false);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, never()).loadUserById(anyLong());
        verify(tokenProvider, times(1)).validateToken(INVALID_JWT_TOKEN);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_NoToken_AuthenticationNotSet() throws ServletException, IOException, java.io.IOException {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, never()).loadUserById(anyLong());
        verify(tokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidTokenPrefix_AuthenticationNotSet() throws ServletException, IOException, java.io.IOException {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(INVALID_PREFIX + INVALID_JWT_TOKEN);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, never()).loadUserById(anyLong());
        verify(tokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}