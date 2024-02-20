package quick.click.security.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.CurrentUser;
import quick.click.security.commons.model.AuthenticatedUser;
import static quick.click.commons.util.WebUtil.getFullRequestUri;

@RestController
public class UserAuthorizedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizedController.class);
    private final UserService userService;

    public UserAuthorizedController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/user")
    @PreAuthorize("hasRole('USER')")
    public UserReadDto getCurrentUser(@CurrentUser final AuthenticatedUser authenticatedUser) {

        LOGGER.debug("In getCurrentUser received POST request to with username {}, " +
                "request URI:[{}] ", authenticatedUser.getUsername(), getFullRequestUri());

        return userService.findById(authenticatedUser.getId());
    }

    @GetMapping("/auth/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public UserReadDto getAdminUser(@CurrentUser final AuthenticatedUser authenticatedUser) {

        LOGGER.debug("In getAdminUser received POST request to with username {}, " +
                "request URI:[{}] ", authenticatedUser.getUsername(), getFullRequestUri());

        return userService.findById(authenticatedUser.getId());
    }
}
