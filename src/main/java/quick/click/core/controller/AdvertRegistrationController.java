package quick.click.core.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertRegistrationService;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;

@RestController
@RequestMapping(AdvertRegistrationController.BASE_URL)
public class AdvertRegistrationController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationController.class);

     public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

     public  final AdvertRegistrationService advertRegistrationService;

    public AdvertRegistrationController(final AdvertRegistrationService advertRegistrationService) {
        this.advertRegistrationService = advertRegistrationService;
    }

    /**
     * POST   http://localhost:8080/v1.0/adverts
     @PostMapping("/adverts/{id}")
     ResponseEntity<AdvertReadDto> createProduct(@RequestBody AdvertCreateDto request)
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
     }
     */

    @PostMapping()
    public  ResponseEntity<?> registerAdvert(@Valid @RequestBody final AdvertCreateDto advertCreateDto) {

        if (advertCreateDto == null || advertCreateDto.getTitle() == null || advertCreateDto.getDescription() == null)
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

       final AdvertReadDto advertReadDto = advertRegistrationService.registerAdvert(advertCreateDto);

        LOGGER.debug("In registerAdvert received POST advert register successfully with id {} ", advertReadDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(advertReadDto);

    }

}

