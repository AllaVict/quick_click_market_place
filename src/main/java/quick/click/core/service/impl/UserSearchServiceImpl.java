package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.dto.UserReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;
import quick.click.core.enums.Category;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertSearchService;
import quick.click.core.service.UserSearchService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.util.List;

/**
 * Service implementation for handling search operations related to users.
 *
 * @author Elnur Kasimov
 */
@Service
public class UserSearchServiceImpl implements UserSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSearchServiceImpl.class);

    private final UserRepository userRepository;

    private final TypeConverter<User, UserReadDto> typeConverterReadDto;

    public UserSearchServiceImpl(final UserRepository userRepository,
                                 final TypeConverter<User, UserReadDto> typeConverterReadDto) {
        this.userRepository = userRepository;
        this.typeConverterReadDto = typeConverterReadDto;
    }

    /**
     * Finds a user by its ID.
     *
     * @param userId The ID of the user to find.
     * @return An UserReadDto containing the user details if found.
     * @throws ResourceNotFoundException If no user is found with the given ID.
     */
    @Override
    public UserReadDto findUserById(final Long userId) {

        final UserReadDto userReadDto = userRepository.findById(userId)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        LOGGER.debug("In findUserById find the User with id {}", userReadDto.getId());

        return userReadDto;
    }

    /**
     * Finds a user by its email.
     *
     * @param email The email of the user to find.
     * @return An UserReadDto containing the user details if found.
     * @throws ResourceNotFoundException If no user is found with the given email.
     */
    @Override
    public UserReadDto findUserByEmail(final String email) {

        final UserReadDto userReadDto = userRepository.findUserByEmail(email)
                .map(typeConverterReadDto::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        LOGGER.debug("In findUserByEmail find the User with email {}", userReadDto.getEmail());

        return userReadDto;
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
