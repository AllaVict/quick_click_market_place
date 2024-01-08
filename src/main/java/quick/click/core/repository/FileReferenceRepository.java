package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.FileReference;
import quick.click.core.enums.FileType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileReferenceRepository extends JpaRepository<FileReference, Long> {

    Optional<FileReference> findTopByUuid(UUID uuid);

    List<FileReference> findAllByFileType(FileType type);

    boolean existsByUuid(UUID uuid);

}