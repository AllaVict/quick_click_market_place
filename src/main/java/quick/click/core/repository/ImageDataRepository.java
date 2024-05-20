package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.ImageData;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on ImageData.
 *
 * @author Alla Borodina
 */
@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

    /**
     * Retrieves a list of ImageData entities by the specified advert ID.
     *
     * @param advertId The ID of the advert to find associated images.
     * @return A list of ImageData objects associated with the given advert ID.
     */
    List<ImageData> findAllByAdvertId(Long advertId);

    /**
     * Finds an ImageData entity by image ID and advert ID.
     *
     * @param imageId The ID of the image to find.
     * @param advertId The ID of the advert associated with the image.
     * @return An Optional containing the found ImageData, if any.
     */
    Optional<ImageData> findByIdAndAdvertId(Long imageId, Long advertId);

}
