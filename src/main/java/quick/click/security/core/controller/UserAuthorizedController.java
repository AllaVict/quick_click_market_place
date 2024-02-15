package quick.click.security.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.model.User;
import quick.click.core.repository.UserRepository;
import quick.click.security.commons.model.CurrentUser;
import quick.click.security.commons.model.AuthenticatedUser;

import static quick.click.commons.util.WebUtil.getFullRequestUri;

@RestController
public class UserAuthorizedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizedController.class);
    private final UserRepository userRepository;

    public UserAuthorizedController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ADMIN')")
    public User getCurrentUser(@CurrentUser final AuthenticatedUser authenticatedUser) {

        LOGGER.debug("In getCurrentUser received POST request to with username {} logout successfully, " +
                "request URI:[{}] ", authenticatedUser.getUsername(), getFullRequestUri());

        return userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authenticatedUser.getId()));
    }

    @PostMapping("/authorized")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> forAuthorizedUsers() {

        LOGGER.debug("In forAuthorizedUsers received POST request to show authorized page, " +
                "request URI:[{}]", getFullRequestUri());

        return new ResponseEntity<>("User in authorized page with username "
                + SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }
}
