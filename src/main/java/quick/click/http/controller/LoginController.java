package quick.click.http.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.constants.Constants;
import quick.click.core.domain.dto.LoginDto;

@Controller
@RequestMapping()
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    static final String URL = "/login";

    private static final String TEMPLATE_LOGIN_PAGE = "login/login";

    private final AuthenticationManager authenticationManager;


    @Autowired
    public LoginController(final AuthenticationManager authenticationManager,
                           final MessageSource messageSource) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping(URL)
    @PatchMapping(value = URL)
    public String getLoginPage(@ModelAttribute(Constants.Attributes.LOGIN_DATA) final LoginDto loginDto
    ) {
        LOG.debug("In getLoginPage Received GET request to login page {} :", TEMPLATE_LOGIN_PAGE);

        try {

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
        return TEMPLATE_LOGIN_PAGE;
    }

}