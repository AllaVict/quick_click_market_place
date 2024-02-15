package quick.click.security.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import quick.click.config.factory.UserDtoFactory;
import quick.click.config.factory.WithMockAuthenticatedUser;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.dto.UserLoginDto;
import quick.click.security.commons.model.dto.UserSignupDto;
import quick.click.security.commons.utils.TokenProvider;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static quick.click.commons.constants.Constants.Endpoints.*;
import static quick.click.config.factory.UserDtoFactory.createUserLoginDto;

import static quick.click.config.factory.UserDtoFactory.createUserSignupDto;
import static quick.click.security.core.controller.LoginController.BASE_URL;
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc()
@WithMockAuthenticatedUser
class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserService userService;

    private String token;

    private UserLoginDto userLoginDto;

    private UserSignupDto userSignupDto;

    private UserReadDto userReadDto;

    private static final String EMAIL =  "test@example.com";

    @BeforeEach
    public void setUp() {
        authentication = Mockito.mock(Authentication.class);
        token = tokenProvider.createToken(authentication);
        userLoginDto = createUserLoginDto();
        userSignupDto = createUserSignupDto();
        userReadDto = UserDtoFactory.createUserReadDto();
    }

    @Test
    void testAuthenticateUser() throws Exception {
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(SecurityContextHolder.getContext().getAuthentication());
        given(tokenProvider.createToken(any(Authentication.class)))
                .willReturn(token);

        mockMvc.perform(post(BASE_URL + LOGIN_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk());
   }

    @Test
    void testRegisterUser() throws Exception {
        given(userService.existsByEmail(anyString())).willReturn(false);
        given(userService.save(any(UserSignupDto.class))).willReturn(userReadDto);

        mockMvc.perform(post(BASE_URL + SIGNUP_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogout() throws Exception {
        given(SecurityContextHolder.getContext().getAuthentication().getName())
                .willReturn(EMAIL);

        mockMvc.perform(post(BASE_URL + LOGOUT_URL)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User logout successfully with username test@example.com")));
    }

}