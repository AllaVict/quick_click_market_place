package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.exeptions.AdvertRegistrationException;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertRegistrationService;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;

@CrossOrigin
@RestController
@RequestMapping(AdvertRegistrationController.BASE_URL)
@Tag(name = "Advert Registration Controller", description = "AdvertRegistration API")
public class AdvertRegistrationController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationController.class);

     public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

     public  final AdvertRegistrationService advertRegistrationService;

    public AdvertRegistrationController(final AdvertRegistrationService advertRegistrationService) {
        this.advertRegistrationService = advertRegistrationService;
    }

    /**
     * POST   http://localhost:8080/v1.0/adverts

     {
     "title": "Big dog",
     "description": "description a toy Big dog",
     "category": "TOYS",
     "status": "PUBLISHED",
     "phone": "+380507778855",
     "price": "100.00"
     "firstPriceDisplayed": "true"
     "currency": "EUR"
     "address": "Dania"
     "user_id": "1"
     */

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create an advert with a given request body")
    public ResponseEntity<?> registerAdvert(@RequestBody AdvertCreateDto advertCreateDto) {

        if (advertCreateDto == null || advertCreateDto.getTitle() == null || advertCreateDto.getDescription() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        try {

            AdvertReadDto advertReadDto = advertRegistrationService.registerAdvert(advertCreateDto);
            LOGGER.debug("In registerAdvert received POST advert register successfully with id {}", advertReadDto.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(advertReadDto);

        } catch (AdvertRegistrationException exception) {

            LOGGER.error("Error during advert registration: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during advert registration: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }
}

