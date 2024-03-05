package quick.click.security.core.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import quick.click.security.commons.model.dto.AuthResponse;
import quick.click.security.commons.model.dto.UserLoginDto;
import quick.click.security.commons.utils.TokenProvider;

import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.*;
import static quick.click.commons.constants.Constants.Tokens.UNAUTHENTICATED;
import static quick.click.security.core.controller.LoginController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public static final String BASE_URL = VERSION_1_0 + AUTH_URL;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    @Autowired
    public LoginController(final AuthenticationManager authenticationManager,
                           final TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<?> authenticateUser
            (@Valid @RequestBody final UserLoginDto userLoginDto) {

        String token = UNAUTHENTICATED;

        try {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword())
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = tokenProvider.createToken(authentication);
            LOGGER.debug("In authenticateUser received POST request user with username {} ",
                    userLoginDto.getEmail());
        } catch (BadCredentialsException e) {

            LOGGER.debug("In authenticateUser BadCredentialsException occurs during an attempt login " +
                    "with username {} ", userLoginDto.getEmail());
        }

        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);

    }

    @PostMapping(LOGOUT_URL)
    public ResponseEntity<String> logout(final HttpServletRequest request) throws ServletException {

        final String userName =  SecurityContextHolder.getContext().getAuthentication().getName();

        LOGGER.debug("In logout received POST request user logout successfully with username {}, ", userName);

        request.logout();

        return new ResponseEntity<>("User logout successfully with username " + userName, HttpStatus.OK);
    }

}

