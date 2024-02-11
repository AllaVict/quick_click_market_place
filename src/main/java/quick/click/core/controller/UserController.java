package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.config.ApiVersion;
import quick.click.core.domain.model.User;
import quick.click.core.service.UserService;

import java.util.List;

import static quick.click.commons.util.WebUtil.getFullRequestUri;

@RestController
@RequestMapping(path = UserController.BASE_URL)
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    static final String BASE_URL = ApiVersion.VERSION_1_0 + "/users";

    private static final String ALL_URL = "/all";

    private final UserService userService;

    public UserController(
            final UserService userDataService
    ) {
        this.userService = userDataService;
    }

    @GetMapping(ALL_URL)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all available users")
    public List<User> getAll() {
        LOGGER.debug("Received GET request to get all badges, request URI:[{}]", getFullRequestUri());
        final List<User> users = userService.findAll();
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user account settings by id")
    public User getUserAccountSettingsData(@PathVariable("id") final Long id) {
        LOGGER.debug("Received GET request for retrieve user account settings, request URI:[{}]", getFullRequestUri());
        final var user = userService.findById(id);
        return user;
    }

    @PutMapping
    @Operation(summary = "Update user account settings")
    public ResponseEntity<Void> updateUserAccountSettings(
            @RequestBody @Valid final User user,
            final BindingResult bindingResult
    ) {
        LOGGER.debug("Received POST request for edit user account settings, request URI:[{}]", getFullRequestUri());
        userService.update(user);
        return ResponseEntity.ok().build();
    }

}



