package quick.click.security.core.controller;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import quick.click.security.commons.model.dto.AuthResponse;
import quick.click.security.commons.model.dto.UserLoginDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("LoginRestControllerIntegrationTest")
class LoginControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAuthenticateUser_ValidCredentials() {
        // Create login request
        UserLoginDto userLoginDto = null;
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("password123");

        // Send POST request to /auth/login
        ResponseEntity<AuthResponse> responseEntity = restTemplate.postForEntity("/auth/login", userLoginDto, AuthResponse.class);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getAccessToken());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        // Create login request
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail("test@example.com");
        userLoginDto.setPassword("invalidpassword");

        // Send POST request to /auth/login
        ResponseEntity<AuthResponse> responseEntity = restTemplate.postForEntity("/auth/login", userLoginDto, AuthResponse.class);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("unauthenticated", responseEntity.getBody().getAccessToken());
    }

    @Test
    void testLogout() throws ServletException {
        // Send POST request to /auth/logout
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/auth/logout", null, String.class);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User with username {} logout successfully", responseEntity.getBody());
    }
}