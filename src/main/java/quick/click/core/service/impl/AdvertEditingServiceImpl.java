package quick.click.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.converter.TypeConverter;
import quick.click.core.domain.dto.AdvertEditingDto;
import quick.click.core.domain.dto.AdvertReadDto;
import quick.click.core.domain.model.Advert;
import quick.click.core.repository.AdvertRepository;
import quick.click.core.repository.UserRepository;
import quick.click.core.service.AdvertEditingService;
import quick.click.security.commons.model.AuthenticatedUser;

import java.time.LocalDateTime;
import java.util.Optional;

import static quick.click.core.enums.AdvertStatus.ARCHIVED;
import static quick.click.core.enums.AdvertStatus.PUBLISHED;

/**
 * Service implementation for editing adverts.
 *
 * @author Alla Borodina
 */
@Service
public class AdvertEditingServiceImpl implements AdvertEditingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertEditingServiceImpl.class);

    private final AdvertRepository advertRepository;

    private final UserRepository userRepository;

    private final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto;

    public AdvertEditingServiceImpl(final AdvertRepository advertRepository,
                                    final UserRepository userRepository,
                                    final TypeConverter<Advert, AdvertReadDto> typeConverterReadDto) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
        this.typeConverterReadDto = typeConverterReadDto;

    }

    /**
     * Edits an existing advert based on the provided editing details and the authenticated user's information.
     * Only the owner of the advert can edit it.
     *
     * @param advertId          The ID of the advert to edit.
     * @param advertEditingDto  The data transfer object containing the editing details.
     * @param authenticatedUser The currently authenticated user attempting to edit the advert.
     * @return An {@link AdvertReadDto} containing the updated advert details.
     * @throws ResourceNotFoundException If the advert to be edited is not found.
     * @throws AuthorizationException    If the authenticated user does not own the advert or is unauthorized.
     */
    @Override
    public AdvertReadDto editAdvert(final Long advertId,
                                    final AdvertEditingDto advertEditingDto,
                                    final AuthenticatedUser authenticatedUser) {

        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);

        final Optional<Advert> advertForUpdate = advertRepository.findAdvertByIdAndUserId(advertId, userId);
        if (advertForUpdate.isEmpty())
            advertForUpdate.orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        advertEditingDto.setStatus(PUBLISHED);

        LOGGER.debug("In editAdvert advert has updated successfully with id {} ", advertId);

        return advertForUpdate
                .map(advert -> this.updateAdvertData(advert, advertEditingDto))
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    /**
     * Archives an existing advert based on the authenticated user's information.
     * Only the owner of the advert can archive it.
     *
     * @param advertId          The ID of the advert to archive.
     * @param authenticatedUser The currently authenticated user attempting to archive the advert.
     * @return An {@link AdvertReadDto} containing the archived advert details.
     * @throws ResourceNotFoundException If the advert to be archived is not found.
     * @throws AuthorizationException    If the authenticated user does not own the advert or is unauthorized.
     */
    @Override
    public AdvertReadDto archiveAdvert(final Long advertId,
                                       final AuthenticatedUser authenticatedUser) {

        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Optional<Advert> advertToArchive = advertRepository.findAdvertByIdAndUserId(advertId, userId);
        if (advertToArchive.isEmpty())
            advertToArchive.orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        LOGGER.debug("In archiveAdvert advert has archived successfully with id {} ", advertId);

        return advertToArchive
                .map(this::archive)
                .map(advertRepository::saveAndFlush)
                .map(typeConverterReadDto::convert)
                .orElseThrow();
    }

    /**
     * Deletes an existing advert based on the authenticated user's information.
     * Only the owner of the advert can delete it.
     *
     * @param advertId          The ID of the advert to delete.
     * @param authenticatedUser The currently authenticated user attempting to delete the advert.
     * @throws ResourceNotFoundException If the advert to be deleted is not found.
     * @throws AuthorizationException    If the authenticated user does not own the advert or is unauthorized.
     */
    @Override
    public void deleteAdvert(final Long advertId,
                             final AuthenticatedUser authenticatedUser) {

        final Long userId = getUserIdByAuthenticatedUser(authenticatedUser);
        final Optional<Advert> advertForDelete = advertRepository.findAdvertByIdAndUserId(advertId, userId);

        if (advertForDelete.isEmpty())
            advertForDelete.orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        advertRepository.delete(advertForDelete.orElseThrow());

        LOGGER.debug("Deleting success advert with id {}", advertId);

    }

    /**
     * Changes field 'favorite' of advert to true and save updated advert.
     *
     * @param advertId The ID of the advert to mark as favorite.
     * @return An AdvertReadDto containing the advert details.
     * @throws ResourceNotFoundException If the advert to be archived is not found.
     *
     */
    @Override
    public AdvertReadDto markAdvertAsFavorite(Long advertId) {
        Advert advertForMarkAsFavorite = advertRepository.findAdvertById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));
        advertForMarkAsFavorite.setFavorite(true);
        Advert markedAsFavorite = advertRepository.save(advertForMarkAsFavorite);
        return typeConverterReadDto.convert(markedAsFavorite);
    }

    protected Advert updateAdvertData(final Advert advert, final AdvertEditingDto advertEditingDto) {
        advert.setTitle(advertEditingDto.getTitle());
        advert.setDescription(advertEditingDto.getDescription());
        advert.setCategory(advertEditingDto.getCategory());
        advert.setStatus(advertEditingDto.getStatus());
        advert.setPhone(advertEditingDto.getPhone());
        advert.setPrice(advertEditingDto.getPrice());
        advert.setFirstPriceDisplayed(advertEditingDto.isFirstPriceDisplayed());
        advert.setCurrency(advertEditingDto.getCurrency());
        advert.setAddress(advertEditingDto.getAddress());
        advert.setCreatedDate(advertEditingDto.getCreatedDate());
        advert.setUpdatedDate(LocalDateTime.now());
        return advert;
    }

    private Advert archive(final Advert advert) {
        advert.setStatus(ARCHIVED);
        return advert;
    }

    private Long getUserIdByAuthenticatedUser(final AuthenticatedUser authenticatedUser) {
        String username = authenticatedUser.getEmail();
        return userRepository.findUserByEmail(username)
                .orElseThrow(() -> new AuthorizationException("Unauthorized access"))
                .getId();

    }

}

