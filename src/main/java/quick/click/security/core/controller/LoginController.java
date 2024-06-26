package quick.click.security.core.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import quick.click.core.domain.dto.UserReadDto;
import quick.click.security.commons.model.dto.ApiResponse;
import quick.click.security.commons.model.dto.AuthResponse;
import quick.click.security.commons.model.dto.UserLoginDto;
import quick.click.security.commons.model.dto.UserSignupDto;
import quick.click.security.commons.utils.TokenProvider;
import quick.click.security.core.service.UserRegistrationService;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.*;
import static quick.click.commons.constants.Constants.Tokens.UNAUTHORIZED;
import static quick.click.security.core.controller.LoginController.BASE_URL;

@CrossOrigin
@RestController
@Tag(name = "Login Controller", description = "Login API")
@RequestMapping(BASE_URL)
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public static final String BASE_URL = VERSION_1_0 + AUTH_URL;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public LoginController(final AuthenticationManager authenticationManager,
                           final TokenProvider tokenProvider,
                           final UserRegistrationService userRegistrationService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRegistrationService = userRegistrationService;
    }

    /**
     * Authenticates a user based on their login credentials.
     *
     * @param userLoginDto the data transfer object containing the user's login credentials.
     * @return a ResponseEntity containing the authentication token if authentication is successful.
     */
    @PostMapping(LOGIN_URL)
    public ResponseEntity<?> authenticateUser
            (@Valid @RequestBody final UserLoginDto userLoginDto) {

        final String token;

        try {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(),
                            userLoginDto.getPassword())
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = tokenProvider.createToken(authentication);

            LOGGER.debug("In authenticateUser received POST request user with username {}",
                    userLoginDto.getEmail());

        } catch (BadCredentialsException e) {

            LOGGER.debug("In authenticateUser BadCredentialsException occurs during an attempt login " +
                    "with username {} ", userLoginDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User has bad credentials and not "
                    + UNAUTHORIZED);
        }

        return ResponseEntity.ok().body(new AuthResponse(token));
    }

    /**
     * Registers a new user in the system.
     *
     * @param userSignUpDto the data transfer object containing the user's signup information.
     * @return a ResponseEntity containing a message of success or failure.
     */
    @PostMapping(SIGNUP_URL)
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody UserSignupDto userSignUpDto) {

        if (userRegistrationService.existsByEmail(userSignUpDto.getEmail())) {

            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "Email is already exist!"));
        }

        final UserReadDto result = userRegistrationService.save(userSignUpDto);

        LOGGER.debug("In registerUser received POST user signup successfully with username {} ", result.getFirstName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    /**
     * Logs out the current user.
     *
     * @param request the HttpServletRequest object.
     * @return a ResponseEntity containing a message of success.
     * @throws ServletException if an error occurs during logout.
     */
    @PostMapping(LOGOUT_URL)
    public ResponseEntity<String> logout(final HttpServletRequest request) throws ServletException {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userName.equals("anonymousUser")) {
            LOGGER.debug("In logout user was not login with username {} ", userName);

            return ResponseEntity.status(HttpStatus.OK).body("User was not login");
        }

        LOGGER.debug("In logout received POST user logout request with username {} logout successfully, ", userName);

        request.logout();

        return ResponseEntity.ok().body("User logout successfully with username " + userName);

    }

}

