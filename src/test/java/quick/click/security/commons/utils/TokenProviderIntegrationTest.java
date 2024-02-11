package quick.click.security.commons.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import quick.click.core.enums.Role;
import quick.click.security.commons.config.AppProperties;
import quick.click.security.commons.model.UserPrincipal;
import quick.click.utils.UserFactory;

@SpringBootTest
class TokenProviderIntegrationTest {

    @Autowired
    private TokenProvider tokenProvider;

    @Mock
    private AppProperties appProperties;

    @Mock
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateToken() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(getUserPrincipal(), null);
        // Act
        String token = tokenProvider.createToken(authentication);
        // Assert
        // Add your assertions here
    }

    @Test
    void testGetUserIdFromToken() {
        // Arrange
        String token = "example_token";
        // Act
        Long userId = tokenProvider.getUserIdFromToken(token);
        // Assert
        // Add your assertions here
    }

    @Test
    void testValidateToken() {
        // Arrange
        String authToken = "example_token";
        // Act
        boolean isValid = tokenProvider.validateToken(authToken);
        // Assert
        // Add your assertions here
    }

    private UserPrincipal getUserPrincipal() {
        return UserPrincipal.create(UserFactory.createUserWithRole(Role.ROLE_USER));
    }
}
