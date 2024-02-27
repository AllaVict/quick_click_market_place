package quick.click.core.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quick.click.core.domain.dto.AdvertCreateDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.service.AdvertRegistrationService;
import quick.click.security.commons.model.dto.ApiResponse;
import quick.click.security.commons.model.dto.UserSignupDto;

import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.*;
import static quick.click.core.controller.AdvertRegistrationController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class AdvertRegistrationController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertRegistrationController.class);

     public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

     public  final AdvertRegistrationService advertRegistrationService;

    public AdvertRegistrationController(final AdvertRegistrationService advertRegistrationService) {
        this.advertRegistrationService = advertRegistrationService;
    }

    /**
     * POST   http://localhost:8080/v1.0/adverts
     @GetMapping("/adverts/{id}")
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
     }
     ????????????????
     private Long userId;
     images
     */
    @PostMapping(BASE_URL)
    public  ResponseEntity<AdvertReadDto> registerAdvert(@Valid @RequestBody AdvertCreateDto advertCreateDto) {

        final AdvertReadDto advertReadDto = advertRegistrationService.registerAdvert(advertCreateDto);

        LOGGER.debug("In registerAdvert received POST advert register successfully with id {} ", advertReadDto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(advertReadDto);

    }

}

