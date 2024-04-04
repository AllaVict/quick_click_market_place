package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;

import java.util.List;
import java.util.Optional;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.ADVERTS_URL;
import static quick.click.core.controller.AdvertSearchController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Tag(name = "Advert Search Controller", description = "AdvertSearch API")
public class AdvertSearchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchController.class);

    public static final String BASE_URL = VERSION_1_0 + ADVERTS_URL;

    public  final AdvertSearchService advertSearchService;

    public AdvertSearchController(final AdvertSearchService advertSearchService) {
        this.advertSearchService = advertSearchService;
    }

    /**
     * GET   http://localhost:8081/v1.0/adverts/1
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find advert by id")
    public ResponseEntity<AdvertReadDto> findAdvertById(@PathVariable("id") final Long advertId) {

          final AdvertReadDto advertReadDto = Optional.ofNullable(advertSearchService.findAdvertById(advertId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LOGGER.debug("In findAdvertById received GET find the advert successfully with id {} ", advertReadDto.getId());

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

    }

    /**
     GET    http://localhost:8081/v1.0/adverts
     */
    @GetMapping()
    @Operation(summary = "Find all adverts")
    public ResponseEntity<?> findAllAdverts() {

        final List<AdvertReadDto> advertReadDtoList =advertSearchService.findAllAdverts();

        if (advertReadDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list");
        }
        LOGGER.debug("In findAllAdvert received GET find all advert successfully ");

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Find all adverts by user id")
    public ResponseEntity<?> findAllAdvertsByUserId(@PathVariable("id") final Long userId) {

        final List<AdvertReadDto> advertReadDtoList =advertSearchService.findAllAdvertsByUserId(userId);

        if (advertReadDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list");
        }
        LOGGER.debug("In findAllAdvertsByUserId find all adverts for the user with id: {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

    }
}



