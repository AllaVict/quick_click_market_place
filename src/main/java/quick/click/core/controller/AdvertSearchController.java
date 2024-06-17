package quick.click.core.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;
import quick.click.core.service.UserService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    private final AdvertRepository advertRepository;

    public final UserRepository userRepository;

    public AdvertSearchController(final AdvertSearchService advertSearchService, final AdvertRepository advertRepository,
                                  final UserRepository userRepository) {
        this.advertSearchService = advertSearchService;
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
    }

    /**
     * Finds an advert by its ID and returns it.
     *
     * @param advertId The ID of the advert to find.
     * @return A ResponseEntity containing the found advert or an error message.
     *
     *  GET   http://localhost:8080/v1.0/adverts/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find advert by id")
    public ResponseEntity<?> findAdvertById(@PathVariable("id") final Long advertId) {

        try {
            Advert fromDb = advertRepository.findAdvertById(advertId).orElseThrow(
                    () -> new ResourceNotFoundException("Advert", "advertId", advertId)
            );
            fromDb.setViewingQuantity(fromDb.getViewingQuantity() + 1);
            advertRepository.save(fromDb);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            if (username != null && !"anonymousUser".equals(username)) {
                User user = userRepository.findUserByEmail(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
                Set<Advert> viewedAdverts = user.getViewedAdverts();
                viewedAdverts.add(fromDb);
                fromDb.setViewer(user);
                userRepository.save(user);
            }

            final AdvertReadDto advertReadDto = advertSearchService.findAdvertById(advertId);

            LOGGER.debug("In findAdvertById received GET find the advert successfully with id: {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDto);

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during finding the advert with id: {}", advertId, exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred." + exception.getMessage());

        }

    }

    /**
     * Retrieves all adverts and returns them.
     *
     * @return A ResponseEntity containing a list of all adverts or an error message.
     *
     *  GET   http://localhost:8080/v1.0/adverts
     */
    @GetMapping()
    @Operation(summary = "Find all adverts")
    public ResponseEntity<?> findAllAdverts() {

        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAllAdverts();

            LOGGER.debug("In findAllAdvert received GET find all advert successfully");

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
     * @return A ResponseEntity containing a list of all adverts created by the authenticated user
     * or an error message.
     *
     *  GET   http://localhost:8080/v1.0/adverts/user
     *  Find all adverts by authorized user
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

            LOGGER.error("Unauthorized access attempt by user: {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (Exception exception) {

            LOGGER.error("Error finding adverts by user with name: {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Retrieves all adverts with certain category and returns them.
     *
     * @return A ResponseEntity containing a list of all adverts with certain category or an error message.
     */
    @GetMapping("/")
    @Operation(summary = "Find all adverts with certain category")
    public ResponseEntity<?> findAdvertsByCategory(@RequestParam("category") String category) {
        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findByCategory(category);

            LOGGER.debug("In findAdvertsByCategory received GET find all advert successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (IllegalArgumentException ex) {

            LOGGER.error("Error finding adverts with category {}", category, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Retrieves all adverts with discounted price and returns them.
     *
     * @return A ResponseEntity containing a list of all adverts with discounted price or an error message.
     */
    @GetMapping("/discounts")
    @Operation(summary = "Find all adverts with discounted price")
    public ResponseEntity<?> findDiscountedAdverts() {
        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findDiscounted();

            LOGGER.debug("In findDiscountedAdverts received GET find all advert successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding adverts with discounted price", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Retrieves 10 adverts with max viewing quantity.
     *
     * @return A ResponseEntity containing a list of 10 (or less) adverts with max viewing quantity or an error message.
     */
    @GetMapping("/max_viewed")
    @Operation(summary = "Find 10 adverts with max viewing quantity")
    public ResponseEntity<?> find10MaxViewedAdverts() {
        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.find10MaxViewed();

            LOGGER.debug("In find10MaxViewedAdverts received GET find all advert successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding adverts with max viewing quantity", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Retrieves all adverts that has 'true' in the field 'promoted' and returns them.
     *
     * @return A ResponseEntity containing a list of all adverts that has 'true' in the field 'promoted' or an error message.
     */
    @GetMapping("/promotions")
    @Operation(summary = "Find all adverts which are promoted")
    public ResponseEntity<?> findPromotedAdverts() {
        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findPromoted();

            LOGGER.debug("In findPromotedAdverts received GET find all advert successfully.");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding adverts which are promoted", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * Finds adverts that contains in their title certain word part and returns them.
     *
     * @param title The title part (case-insensitive) in advert title to find.
     * @return A ResponseEntity containing a list of all adverts that contains in their title word part or an error message.
     *
     *  GET   http://localhost:8080/v1.0/adverts/find
     */
    @GetMapping("/find")
    @Operation(summary = "Find adverts that contains in their title certain word part")
    public ResponseEntity<?> findAdvertsByTitle(@RequestParam("title") final String title) {

        try {
            final List<AdvertReadDto> advertReadDtoList = advertSearchService.findAdvertsByTitlePart(title.toLowerCase());

            LOGGER.debug("In findAdvertsByWordPart received GET find advert successfully");

            return ResponseEntity.status(HttpStatus.OK).body(advertReadDtoList);

        } catch (Exception ex) {

            LOGGER.error("Error finding adverts", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

}



