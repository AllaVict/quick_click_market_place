package quick.click.advertservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.advertservice.core.domain.model.Advert;

import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    Optional<Advert> findAdvertById(Long id);
}
