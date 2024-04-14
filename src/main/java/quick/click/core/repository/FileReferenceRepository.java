package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.FileReference;
import quick.click.core.enums.FileType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on fileReference.
 *
 * @author Alla Borodina
 */
@Repository
public interface FileReferenceRepository extends JpaRepository<FileReference, Long> {

    /**
     * Finds the most recent file reference by its UUID.
     *
     * @param uuid the UUID of the file reference.
     * @return an Optional containing the most recent file reference if found, or an empty Optional otherwise.
     */
    Optional<FileReference> findTopByUuid(UUID uuid);

    /**
     * Finds all file references by their file type.
     *
     * @param type the type of files to find.
     * @return a list of file references matching the given type.
     */
    List<FileReference> findAllByFileType(FileType type);

    /**
     * Checks if a file reference exists by its UUID.
     *
     * @param uuid the UUID of the file reference to check.
     * @return true if a file reference exists with the given UUID, false otherwise.
     */
    boolean existsByUuid(UUID uuid);

    /**
     * Finds a file reference by its file URL.
     *
     * @param url the URL of the file to find.
     * @return an Optional containing the file reference if found, or an empty Optional otherwise.
     */
    Optional<FileReference> findByFileUrl(String url);

}