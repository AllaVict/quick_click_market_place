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
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.service.AdvertSearchService;

import java.util.List;

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

        try {
            final AdvertReadDto advertReadDto = advertSearchService.findAdvertById(advertId);
            LOGGER.debug("In findAdvertById received GET find the advert successfully with id {} ", advertReadDto.getId());
            return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

        } catch (ResourceNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Advert not found with id: " + advertId, exception);
        } catch (Exception exception) {
            LOGGER.error("Error finding advert by id {}", advertId, exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", exception);
        }
    }

    /**
     GET    http://localhost:8081/v1.0/adverts
     */

    @GetMapping()
    @Operation(summary = "Find all adverts")
    public ResponseEntity<?> findAllAdverts() {

        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAllAdverts();
            if (advertReadDtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empty list");
            }

            LOGGER.debug("In findAllAdvert received GET find all advert successfully ");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding all adverts", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex);
        }
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Find all adverts by user id")
    public ResponseEntity<?> findAllAdvertsByUserId(@PathVariable("id") final Long userId) {

        try {

            final List<AdvertReadDto> advertReadDtoList =advertSearchService.findAllAdvertsByUserId(userId);
            if (advertReadDtoList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No adverts found for user id: " + userId);
            }
            LOGGER.debug("In findAllAdvertsByUserId find all adverts for the user with id: {}", userId);

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception exception) {

            LOGGER.error("Error finding adverts by user id {}", userId, exception);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", exception);
        }
    }
}



