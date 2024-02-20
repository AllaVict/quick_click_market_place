package quick.click.security.commons.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extensions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import quick.click.core.enums.Role;
import quick.click.security.commons.config.AppProperties;
import quick.click.security.commons.model.AuthenticatedUser;
import quick.click.config.factory.UserFactory;

import static org.junit.jupiter.api.Assertions.*;

@EnableConfigurationProperties
@ContextConfiguration(classes = {AppProperties.class })
@Extensions({
        @ExtendWith(SpringExtension.class),
        @ExtendWith(MockitoExtension.class)
})
@DisplayName("TokenProviderTest")
class TokenProviderTest {

    @Autowired
    private AppProperties appProperties;

    @InjectMocks
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticatedUser authenticatedUser;
    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private static String JWT_TOKEN;

    private static final String INVALID_JWT_TOKEN = "invalidJwtToken";

    private static final long USER_ID = 101L;

    @BeforeEach
    public void setUp() {
        tokenProvider = new TokenProvider(appProperties);
        authenticatedUser = getUserPrincipal();
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
        JWT_TOKEN = appProperties.getAuth().getTokenSecret();
    }
    private AuthenticatedUser getUserPrincipal() {
        return AuthenticatedUser.create(UserFactory.createUserWithRole(Role.ROLE_USER));
    }

    @Nested
    @DisplayName("When Create Token")
    class CreateTokenTests {
        @Test
        void testCreateToken_ReturnsToken() {

        }

    }

    @Nested
    @DisplayName("When get User Id From Token")
    class GetUserIdFromTokenTests {
        @Test
        void testGetUserIdFromToken_ReturnsUserId() {

        }
        @Test
        void testGetUserIdFromToken_ReturnsNoUserId() {

        }
    }

    @Nested
    @DisplayName("When Validate Token")
    class ValidateTokenTests {
        @Test
        void testValidateToken_ValidToken() {

            boolean isValid = tokenProvider.validateToken(JWT_TOKEN);

            assertFalse(isValid);
        }

        @Test
        void testValidateToken_InvalidToken() {

            boolean isInvalid = tokenProvider.validateToken(INVALID_JWT_TOKEN);

            assertFalse(isInvalid);
        }
    }
}