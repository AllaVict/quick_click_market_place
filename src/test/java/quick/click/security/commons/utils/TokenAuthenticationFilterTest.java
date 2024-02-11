package quick.click.security.commons.utils;

import io.jsonwebtoken.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;
import static quick.click.commons.constants.Constants.Headers.AUTHORIZATION_HEADER;
import static quick.click.commons.constants.Constants.Tokens.ACCESS_TOKEN_PREFIX;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import quick.click.security.core.service.UserLoginService;

class TokenAuthenticationFilterTest {

    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private UserLoginService userLoginService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.initMocks(this);
        tokenAuthenticationFilter = new TokenAuthenticationFilter(tokenProvider, userLoginService);
    }

    @Test
    void doFilterInternal_ValidJwtToken_AuthenticationSet() throws ServletException, IOException, java.io.IOException {
        String jwtToken = "validJwtToken";
        Long userId = 1234L;
        UserDetails userDetails = mock(UserDetails.class);
        UsernamePasswordAuthenticationToken authentication = mock(UsernamePasswordAuthenticationToken.class);
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(ACCESS_TOKEN_PREFIX + jwtToken);
        when(tokenProvider.validateToken(jwtToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(jwtToken)).thenReturn(userId);
        when(userLoginService.loadUserById(userId)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);
        when(authentication.getDetails()).thenReturn(null);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, times(1)).loadUserById(userId);
        verify(tokenProvider, times(1)).getUserIdFromToken(jwtToken);
        verify(authentication, times(1)).setDetails(any());
        verify(filterChain, times(1)).doFilter(request, response);
        verify(request, never()).getHeader("invalidHeader");
    }

    @Test
    void doFilterInternal_InvalidJwtToken_AuthenticationNotSet() throws ServletException, IOException, java.io.IOException {
        String jwtToken = "invalidJwtToken";
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(ACCESS_TOKEN_PREFIX + jwtToken);
        when(tokenProvider.validateToken(jwtToken)).thenReturn(false);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, never()).loadUserById(anyLong());
        verify(tokenProvider, times(1)).validateToken(jwtToken);
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
        String jwtToken = "invalidJwtToken";
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn("InvalidPrefix" + jwtToken);

        tokenAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userLoginService, never()).loadUserById(anyLong());
        verify(tokenProvider, never()).validateToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}