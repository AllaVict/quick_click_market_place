package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.exeptions.RegistrationException;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertRegistrationService;
import quick.click.security.commons.model.AuthenticatedUser;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;

/**
 * Controller for handling advert registration requests.
 *
 * @author Alla Borodina
 */
@CrossOrigin
@RestController
@RequestMapping(AdvertRegistrationController.BASE_URL)
@Tag(name = "Advert Registration Controller", description = "AdvertRegistration API")
public class AdvertRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public final AdvertRegistrationService advertRegistrationService;

    public AdvertRegistrationController(final AdvertRegistrationService advertRegistrationService) {
        this.advertRegistrationService = advertRegistrationService;
    }

    /**
     * Handles the HTTP POST request to register a new advert. Validates the request body for necessary fields
     * and uses the AdvertRegistrationService to create the advert. Returns the created advert details on success.
     *
     * @param advertCreateDto   DTO containing the advert's creation data.
     * @param authenticatedUser The currently authenticated user trying to create the advert.
     * @return A ResponseEntity containing the created AdvertReadDto or an error message.
     * @throws AuthorizationException      If the authenticated user does not have permission to create an advert.
     * @throws RegistrationException If there are issues during the registration process.
     * @throws Exception                   For any other unexpected errors.
     */
    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create an advert with a given request body")
    public ResponseEntity<?> registerAdvert(@RequestBody final AdvertCreateDto advertCreateDto,
                                            @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        if (advertCreateDto == null || advertCreateDto.getTitle() == null || advertCreateDto.getDescription() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        try {

            final AdvertReadDto advertReadDto = advertRegistrationService.registerAdvert(advertCreateDto, authenticatedUser);
            LOGGER.debug("In registerAdvert received POST advert register successfully with id: {}, for user: {}",
                    advertReadDto.getId(), authenticatedUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(advertReadDto);

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (RegistrationException exception) {

            LOGGER.error("Error during advert registration: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during advert registration: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }
}

