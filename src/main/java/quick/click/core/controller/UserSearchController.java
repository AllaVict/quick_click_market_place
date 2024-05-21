package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;
import quick.click.core.service.UserSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;
import java.util.Set;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.USERS_URL;
import static quick.click.core.controller.AdvertSearchController.BASE_URL;
import static quick.click.core.controller.UserSearchController.BASE_USER_URL;

/**
 * Controller for handling API requests related to searching users.
 *
 * @author Elnur Kasimov
 */
@CrossOrigin
@RestController
@RequestMapping(BASE_USER_URL)
@Tag(name = "User Search Controller", description = "UserSearch API")
public class UserSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSearchController.class);

    public static final String BASE_USER_URL = VERSION_1_0 + USERS_URL;

    public final UserSearchService userSearchService;

    public final AdvertSearchService advertSearchService;

    private final UserRepository userRepository;

    public UserSearchController(final UserSearchService userSearchService, final UserRepository userRepository,
                                AdvertSearchService advertSearchService) {
        this.userSearchService = userSearchService;
        this.userRepository = userRepository;
        this.advertSearchService = advertSearchService;
    }

    /**
     * Retrieves all advert which are viewed by user with ID and returns them.
     *
     * @param userId The ID of the user who viewed adverts to find.
     * @return A ResponseEntity containing the found advert or an error message.
     */
    @GetMapping("/{id}/viewed-adverts")
    @Operation(summary = "Find viewed adverts by user with id")
    public ResponseEntity<?> findViewedAdvertByUserId(@PathVariable("id") final Long userId) {

        try {

            User userFromDb = userRepository.findUserById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("User", "userId", userId)
            );
            Set<AdvertReadDto> viewedAdverts = advertSearchService.findViewed(userFromDb);

            LOGGER.debug("In findViewedAdvertByUserId received GET find the advert successfully with id {} ", userId);

            return ResponseEntity.status(HttpStatus.OK).body(viewedAdverts);

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("User with id '{}' not found ", userId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during finding the viewed adverts by userId {}", userId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }

    }



}



