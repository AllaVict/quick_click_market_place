package quick.click.security.commons.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import quick.click.security.commons.config.AppProperties;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.Date;

@Service
public class TokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;

    public TokenProvider(final AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Constructs a JWT token based on the authentication object provided.
     *
     * @param authentication the authentication object containing the principal from which the user ID is extracted.
     * @return a JWT token string that represents the user's authentication.
     */
    public String createToken(final Authentication authentication) {
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Long.toString(authenticatedUser.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    /**
     * Extracts the user ID from a JWT token.
     *
     * @param token the JWT token from which the user ID is to be extracted.
     * @return the user ID extracted from the token.
     */
     public Long getUserIdFromToken(final String token) {

        final var key = appProperties.getAuth().getTokenSecret();
        Claims claims = Jwts.parser()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validates a JWT token by checking its signature, structure, and expiration.
     *
     * @param authToken the JWT token to be validated.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(final String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret()).build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }

}
