package quick.click.http.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
import quick.click.commons.config.ApiVersion;
import quick.click.core.domain.dto.LoginDto;
import quick.click.core.service.UserLoginService;

import static quick.click.http.controller.LoginRestController.URL;

@RestController
@RequestMapping(URL)
public class LoginRestController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginRestController.class);

    public static final String URL = ApiVersion.VERSION_1_0;

    static final String URL_LOGIN = "/login";

    private final AuthenticationManager authenticationManager;

    private final UserLoginService userLoginService;

    @Autowired
    public LoginRestController(final AuthenticationManager authenticationManager,
                               final UserLoginService userLoginService) {
        this.authenticationManager = authenticationManager;
        this.userLoginService = userLoginService;
    }

    @PostMapping(URL_LOGIN)
    public ResponseEntity<String> authenticateUser
            (@RequestBody LoginDto loginDto) {

        LOG.debug("You try login with username {} and password {}", loginDto.getUsername(), loginDto.getPassword());

        try {

            userLoginService.loadUserByUsername(loginDto.getUsername());
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(), loginDto.getPassword()));
            LOG.debug("In getLoginPage user with username {} login successfully", loginDto.getUsername());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {

            LOG.debug("In authenticateUser BadCredentialsException occurs" +
                            "during your attempt login with username {} and password {}",
                    loginDto.getUsername(), loginDto.getPassword());
        }
        return new ResponseEntity<>("User with username {} login successfully!" +
                SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        LOG.debug("You logout successfully");
        request.logout();
        return new ResponseEntity<>("You logout successfully!.", HttpStatus.OK);
    }

    @GetMapping("/signed-user")
    public ResponseEntity<String> forSignedUsers() {

        LOG.debug("In forSignedUsers - Received GET request to show signed-user page");

        return new ResponseEntity<>("User with username {} in signed-user page!."
                + SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }

}

