package quick.click.security.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.security.commons.model.AuthenticatedUser;
import quick.click.security.core.service.UserRegistrationService;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.AUTH_URL;
import static quick.click.commons.util.WebUtil.getFullRequestUri;
import static quick.click.security.core.controller.UserAuthorizedController.BASE_URL;

@CrossOrigin
@RestController
@RequestMapping(BASE_URL)
public class UserAuthorizedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizedController.class);

    public static final String BASE_URL = VERSION_1_0 + AUTH_URL;
    private final UserRegistrationService userRegistrationService;

    public UserAuthorizedController(final UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public UserReadDto getCurrentUser(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        LOGGER.debug("In getCurrentUser received POST request to with username {}, " +
                "request URI:[{}] ", authenticatedUser.getUsername(), getFullRequestUri());

        return userRegistrationService.findById(authenticatedUser.getId());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public UserReadDto getAdminUser(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        LOGGER.debug("In getAdminUser received POST request to with username {}, " +
                "request URI:[{}] ", authenticatedUser.getUsername(), getFullRequestUri());

        return userRegistrationService.findById(authenticatedUser.getId());
    }
}
