package quick.click.core.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;

import java.util.List;
import java.util.Optional;

import static quick.click.commons.config.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.core.controller.AdvertRegistrationController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class AdvertSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public  final AdvertSearchService advertSearchService;

    public AdvertSearchController(AdvertSearchService advertSearchService) {
        this.advertSearchService = advertSearchService;
    }

    /**
     * GET   http://localhost:8080/v1.0/adverts/1
     @GetMapping("/adverts/{id}")
     public AdvertReadDto findById(@PathVariable("id") Long id) {
     */
    @GetMapping(BASE_URL+"/{id}")
    public ResponseEntity<AdvertReadDto> findAdvertById(@PathVariable("id") Long advertId) {

      //  final AdvertReadDto advertReadDto = Optional.ofNullable(advertSearchService.findAdvertById(advertId))
      //          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final AdvertReadDto advertReadDto = advertSearchService.findAdvertById(advertId);
        LOGGER.debug("In findAdvertById received GET find the advert successfully with id {} ", advertReadDto.getId());

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

    }

    /**
     GET    http://localhost:8080/v1.0/adverts
     @GetMapping("/adverts")
     public ResponseEntity<List<AdvertReadDto>> findAll(){
     */
    @GetMapping(BASE_URL)
    public ResponseEntity<List<AdvertReadDto>> findAllAdverts() {

       // final List<AdvertReadDto> advertReadDtoList = Optional.ofNullable(advertSearchService.findAllAdverts())
       //         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAllAdverts();

        LOGGER.debug("In findAllAdvert received GET find all advert successfully ");

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

    }

}



