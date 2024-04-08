package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.ImageData;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {

    Optional<ImageData> findByName(String fileName);
    List<ImageData> findAllByAdvertId(Long advertId);

    Optional<ImageData> findByIdAndAdvertId(Long imageId, Long advertId);
}
