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
import quick.click.security.commons.model.UserPrincipal;

@RestController
public class UserAuthorizedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizedController.class);
    private final UserRepository userRepository;

    public UserAuthorizedController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('ADMIN')")
    public User getCurrentUser(@CurrentUser final UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @PostMapping("/authorized")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> forAuthorizedUsers() {

        LOGGER.debug("In forAuthorizedUsers - Received GET request to show authorized page");

        return new ResponseEntity<>("User in authorized page with username "
                + SecurityContextHolder.getContext().getAuthentication().getName(), HttpStatus.OK);
    }
}
