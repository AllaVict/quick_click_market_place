package quick.click.security.commons.utils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * A component that handles initial entry for authentication processes, responding
 * to unauthorized access attempts with appropriate HTTP error codes.
 *
 * @author Alla Borodina
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    /**
     * Commences an authentication scheme.
     * <p>
     * This method is called whenever an exception is thrown due to an unauthenticated user trying to access
     * a resource that requires authentication.
     *
     * @param HttpServletRequest  the request that resulted in an AuthenticationException.
     * @param httpServletResponse the response that's populated with the authentication entry point response.
     * @param e                   the exception that is thrown when the user is unauthorized.
     * @throws IOException      if an input or output exception occurs.
     * @throws ServletException if a servlet exception occurs.
     */
    @Override
    public void commence(final HttpServletRequest httpServletRequest,
                         final HttpServletResponse httpServletResponse,
                         final AuthenticationException e) throws IOException, ServletException {
        LOGGER.error("Responding with unauthorized error. Message - {} ", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage());
    }
}
