package quick.click.security.commons.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import quick.click.security.core.service.UserLoginService;

import java.io.IOException;

import static quick.click.commons.constants.Constants.Headers.AUTHORIZATION_HEADER;
import static quick.click.commons.constants.Constants.Tokens.ACCESS_TOKEN_PREFIX;

/**
 * A filter that intercepts HTTP requests to authenticate users by validating
 * JWT tokens and setting authentication in the security context.
 *
 * @author Alla Borodina
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenProvider tokenProvider;

    private final UserLoginService userLoginService;

    @Autowired
    public TokenAuthenticationFilter(final TokenProvider tokenProvider,
                                     final UserLoginService userLoginService) {
        this.tokenProvider = tokenProvider;
        this.userLoginService = userLoginService;
    }

    /**
     * Processes an incoming request to authenticate the user by validating the JWT token.
     * If the token is valid, the user's authentication is set in the security context.
     *
     * @param request     the request to be processed.
     * @param response    the response to be filled during the processing of the request.
     * @param filterChain the filter chain to which the request and response should be passed for further processing.
     * @throws ServletException if an error occurs during the processing of the request.
     * @throws IOException      if an I/O error is encountered.
     */
    @Override
    public void doFilterInternal(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                final Long userId = tokenProvider.getUserIdFromToken(jwt);

                final UserDetails userDetails = userLoginService.loadUserById(userId);
                final UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            LOGGER.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(ACCESS_TOKEN_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
