package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertEditingService;
import quick.click.security.commons.model.AuthenticatedUser;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.core.controller.AdvertEditingController.BASE_URL;

@CrossOrigin
@RestController
@RequestMapping(BASE_URL)
@Tag(name = "Advert Editing Controller", description = "AdvertEditing API")
public class AdvertEditingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertEditingController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public final AdvertEditingService advertEditingService;

    public AdvertEditingController(final AdvertEditingService advertEditingService) {
        this.advertEditingService = advertEditingService;
    }

    /**
     * PUT   http://localhost:8081/v1.0/adverts/3
     * {
     * "title": "Big dog",
     * "description": "description a toy Big dog",
     * "category": "TOYS",
     * "status": "PUBLISHED",
     * "phone": "+380507778855",
     * "price": "100.00",
     * "firstPriceDisplayed": "true",
     * "currency": "EUR",
     * "address": "Dania",
     * "userId": "1"
     * }
     */
    @PutMapping("{id}")
    @Operation(summary = "Update an advert by id and a given request body")
    public ResponseEntity<?> editAdvert(@PathVariable("id") final Long advertId,
                                        @Valid @RequestBody final AdvertEditingDto advertEditingDto,
                                        @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        if (advertId == null || advertEditingDto == null || advertEditingDto.getTitle() == null || advertEditingDto.getDescription() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        try {
            final AdvertReadDto advertReadDto = advertEditingService.editAdvert(advertId, advertEditingDto, authenticatedUser);
            LOGGER.debug("In editAdvert received PUT advert edit successfully with id {}, for user: {}", advertId, authenticatedUser.getEmail());
            return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {
            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during editing the advert with id {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/archive/{id}")
    @Operation(summary = "Archive an advert by id")
    public ResponseEntity<?> archiveAdvert(@PathVariable("id") final Long advertId,
                                           @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        if (advertId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        try {

            final AdvertReadDto advertReadDto = advertEditingService.archiveAdvert(advertId, authenticatedUser);
            LOGGER.debug("In archiveAdvert received PUT advert has archived successfully with id {} ", advertId);
            return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during archiving the advert with id {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Delete    http://localhost:8080/v1.0/adverts/3
     */
    @DeleteMapping("{id}")
    @Operation(summary = "Delete an advert by id")
    public ResponseEntity<String> deleteAdvert(@PathVariable("id") final Long advertId,
                                               @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {

        if (advertId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        try {
            advertEditingService.deleteAdvert(advertId, authenticatedUser);
            LOGGER.debug("In editAdvert received DELETE advert delete successfully with id {} ", advertId);
            return ResponseEntity.status(HttpStatus.OK).body("The Advert has deleted successfully");

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser, exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {
            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during deleting the advert by id {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

}




