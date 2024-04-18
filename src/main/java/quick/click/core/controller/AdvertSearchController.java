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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.core.controller.AdvertSearchController.BASE_URL;

/**
 * Controller for handling API requests related to searching adverts.
 *
 * @author Alla Borodina
 */
@CrossOrigin
@RestController
@RequestMapping(BASE_URL)
@Tag(name = "Advert Search Controller", description = "AdvertSearch API")
public class AdvertSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public final AdvertSearchService advertSearchService;

    public AdvertSearchController(final AdvertSearchService advertSearchService) {
        this.advertSearchService = advertSearchService;
    }

    /**
     * Finds an advert by its ID and returns it.
     *
     * @param advertId The ID of the advert to find.
     * @return A ResponseEntity containing the found advert or an error message.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find advert by id")
    public ResponseEntity<?> findAdvertById(@PathVariable("id") final Long advertId) {

        try {

            final AdvertReadDto advertReadDto = advertSearchService.findAdvertById(advertId);

            LOGGER.debug("In findAdvertById received GET find the advert successfully with id {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during finding the advert with id {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }

    }

    /**
     * Retrieves all adverts and returns them.
     *
     * @return A ResponseEntity containing a list of all adverts or an error message.
     */
    @GetMapping()
    @Operation(summary = "Find all adverts")
    public ResponseEntity<?> findAllAdverts() {

        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAllByOrderByCreatedDateDesc();

            LOGGER.debug("In findAllAdvert received GET find all advert successfully ");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding all adverts", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Retrieves all adverts created by the currently authenticated user.
     *
     * @param authenticatedUser The currently authenticated user.
     * @return A ResponseEntity containing a list of all adverts created by the authenticated user or an error message.
     */
    @GetMapping("/user")
    @Operation(summary = "Find all adverts by authorized user")
    public ResponseEntity<?> findAllAdvertsByUser(@AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        try {

            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAllAdvertsByUser(authenticatedUser);

            final String userName = authenticatedUser.getEmail();

            LOGGER.debug("In findAllAdvertsByUser find all adverts for the user with name: {}", userName);

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (Exception exception) {

            LOGGER.error("Error finding adverts by user with name {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }
}



