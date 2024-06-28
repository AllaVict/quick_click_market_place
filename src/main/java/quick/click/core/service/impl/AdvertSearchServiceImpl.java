package quick.click.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.enums.Category;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for handling search operations related to adverts.
 *
 * @author Alla Borodina
 */
@Service
@RequiredArgsConstructor
public class AdvertSearchServiceImpl implements AdvertSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertSearchServiceImpl.class);

    @Autowired
    private final AdvertRepository advertRepository;

    private final UserRepository userRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;


    /**
     * Finds an advert by its ID.
     *
     * @param advertId The ID of the advert to find.
     * @return An AdvertReadDto containing the advert details if found.
     * @throws ResourceNotFoundException If no advert is found with the given ID.
     */
    @Override
    public AdvertReadDto findAdvertById(final Long advertId) {

        final AdvertReadDto advertReadDto = advertRepository.findById(advertId)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        LOGGER.debug("In findAdvertById find the Advert with id: {}", advertReadDto.getId());

        return advertReadDto;
    }

    /**
     * Retrieves all adverts.
     *
     * @return A list of AdvertReadDto containing details of all adverts.
     */
    @Override
    public List<AdvertReadDto> findAllAdverts() {

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAll()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllAdverts find all adverts");

        return advertReadDtoList;
    }

    /**
     * Retrieves all adverts ordered by creation date in descending order.
     *
     * @return A list of AdvertReadDto containing details of all adverts sorted by creation date.
     */
    @Override
    public List<AdvertReadDto> findAllByOrderByCreatedDateDesc() {

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAllByOrderByCreatedDateDesc()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllByOrderByCreatedDateDesc find all adverts sorted by createdDate desc");

        return advertReadDtoList;
    }

    /**
     * Finds all adverts created by a specific user.
     *
     * @param authenticatedUser The authenticated user whose adverts are to be found.
     * @return A list of AdvertReadDto containing details of the adverts created by the specified user.
     * @throws AuthorizationException If the authenticated user cannot be authorized.
     */
    @Override
    public List<AdvertReadDto> findAllAdvertsByUser(final AuthenticatedUser authenticatedUser) {

        final User user = getUserByAuthenticatedUser(authenticatedUser);

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAllByUserOrderByCreatedDateDesc(user)
                .stream()
                .filter(a -> Objects.equals(a.getUser().getId(), user.getId()))
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAllAdvertsByUser find all adverts for the user with id: {}, {}", user.getId(), advertReadDtoList);

        return advertReadDtoList;
    }

    /**
     * Retrieves all adverts with certain category.
     *
     * @param category The category by which all ads related to it are to be found.
     * @return A list of AdvertReadDto containing details of all adverts with certain category.
     * @throws IllegalArgumentException If the input category is out of the related enum range.
     */
    @Override
    public List<AdvertReadDto> findByCategory(final String category) throws IllegalArgumentException {
        Category categoryToSearch = findCategoryByString(category);
        final List<AdvertReadDto> advertReadDtoList = advertRepository.findByCategory(categoryToSearch)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();
        LOGGER.debug("In findByCategory find all adverts with category {}", category);

        return advertReadDtoList;
    }

    /**
     * Retrieves all adverts with discounted price.
     *
     * @return A list of AdvertReadDto containing details of all adverts with discounted price.
     */
    @Override
    public List<AdvertReadDto> findDiscounted() {
        final List<AdvertReadDto> advertReadDtoList = advertRepository.findDiscounted()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();
        LOGGER.debug("In findDiscounted find all adverts with discounted price");

        return advertReadDtoList;
    }

    /**
     * Retrieves 10 adverts with max viewing quantity.
     *
     * @return A list of AdvertReadDto containing details of 10 adverts with max viewing quantity.
     */
    @Override
    public List<AdvertReadDto> find10MaxViewed() {
        final List<AdvertReadDto> advertReadDtoList = advertRepository.find10MaxViewed()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();
        LOGGER.debug("In find10MaxViewed find 10 adverts with max viewing quantity");

        return advertReadDtoList;
    }

    /**
     * Retrieves all adverts which are promoted (i.e. participated in some promotion).
     *
     * @return A list of AdvertReadDto containing details of all adverts which are promoted.
     */
    @Override
    public List<AdvertReadDto> findPromoted() {
        final List<AdvertReadDto> advertReadDtoList = advertRepository.findPromoted()
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();
        LOGGER.debug("In findPromoted find all adverts which are promoted");

        return advertReadDtoList;
    }

    /**
     * Retrieves all adverts which are viewed by authorized user.
     *
     * @param user The user whose viewed adverts should be found.
     * @return A list of AdvertReadDto containing details of all adverts that are viewed by authorized user.
     */
    @Override
    public Set<AdvertReadDto> findViewed(User user) {
        return user.getViewedAdverts()
                .stream()
                .map(typeConverterReadDto::convert)
                .collect(Collectors.toSet());
    }



    /**
     * Retrieves adverts that contains in their title certain word part.
     *
     * @param titlePart The title (or part of it) of the adverts which should be found.
     * @return A list of AdvertReadDto that contain in title titlePart and containing details of these adverts.
     */
    @Override
    public List<AdvertReadDto> findAdvertsByTitlePart(String titlePart) {

        final List<AdvertReadDto> advertReadDtoList = advertRepository.findAdvertsByTitlePart(titlePart)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();

        LOGGER.debug("In findAdvertsByTitlePart find adverts");

        return advertReadDtoList;

    }

    /**
     * Retrieves all adverts which are favorite (i.e. viewer has viewed them and marked as favorite).
     *
     * @param viewerId The user's id who viewed the advert and mark it as favorite.
     * @return A list of AdvertReadDto containing details of all adverts which are favorite.
     */
    @Override
    public List<AdvertReadDto> findFavorite(long viewerId) {
        final List<AdvertReadDto> advertReadDtoList = advertRepository.findFavorite(viewerId)
                .stream()
                .map(typeConverterReadDto::convert)
                .toList();
        LOGGER.debug("In findFavorite find all adverts which are favorite");

        return advertReadDtoList;
    }



    private User getUserByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"));

    }

    private Category findCategoryByString(String category) {
        for (Category value : Category.values()) {
            if (value.name().equals(category.toUpperCase())) {
                return value;
            }
        }
        throw new IllegalArgumentException("There is no such category: " + category);
    }

}
