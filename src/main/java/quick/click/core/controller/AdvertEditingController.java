package quick.click.core.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertEditingService;

import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.core.controller.AdvertEditingController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class AdvertEditingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertEditingController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public  final AdvertEditingService advertEditingService;

    public AdvertEditingController(final AdvertEditingService advertEditingService) {
        this.advertEditingService = advertEditingService;
    }

    /**
     PUT   http://localhost:8081/v1.0/adverts/3
     @PutMapping("/adverts/{id}")
     public ResponseEntity<AdvertReadDto> updateProduct(@PathVariable("id") Long id, @RequestBody AdvertEditingDto request)
     {
     "title": "Big dog",
     "description": "description a toy Big dog",
     "category": "TOYS",
     "status": "PUBLISHED",
     "phone": "+380507778855",
     "price": "100.00",
     "firstPriceDisplayed": "true",
     "currency": "EUR",
     "address": "Dania",
     "userId": "1"
     }
     ????????????????
     private Long userId;
     images
     */

    @PutMapping("{id}")
    public ResponseEntity<?> editAdvert(@PathVariable("id") final Long advertId,
                                                    @Valid @RequestBody final AdvertEditingDto advertEditingDto) {

        if (advertId == null || advertEditingDto==null || advertEditingDto.getTitle() == null || advertEditingDto.getDescription() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please fill all fields");

        final AdvertReadDto advertReadDto = advertEditingService.editAdvert(advertId, advertEditingDto);

        LOGGER.debug("In editAdvert received POST advert edit successfully with id {} ",advertId);

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);
    }

    /**
     Delete    http://localhost:8080/v1.0/adverts/3
     @DeleteMapping("/adverts/{id}")
     public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable("id") Long id)
     */
    @DeleteMapping ("{id}")
    public ResponseEntity<String> deleteAdvert(@PathVariable("id") final Long advertId) {

       advertEditingService.deleteAdvert(advertId);

       LOGGER.debug("In editAdvert received DELETE advert delete successfully with id {} ", advertId);

       return ResponseEntity.status(HttpStatus.OK).body("The Advert has deleted successfully");
    }

}




